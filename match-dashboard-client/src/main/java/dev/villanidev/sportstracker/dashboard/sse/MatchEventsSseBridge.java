package dev.villanidev.sportstracker.dashboard.sse;

import dev.villanidev.sportstracker.grpc.MatchEventUpdate;
import dev.villanidev.sportstracker.grpc.MatchStreamingServiceGrpc;
import dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MatchEventsSseBridge {

    private static final Logger log = LoggerFactory.getLogger(MatchEventsSseBridge.class);

    @GrpcClient("match-streaming-service")
    private MatchStreamingServiceGrpc.MatchStreamingServiceStub streamingStub;

    // Track active SSE emitters per match; simple fan-out in dashboard client
    private final Map<UUID, Map<Long, SseEmitter>> emittersByMatch = new ConcurrentHashMap<>();

    public SseEmitter subscribeToMatch(UUID matchId) {
        SseEmitter emitter = new SseEmitter(0L); // no timeout
        long emitterId = System.nanoTime();

        emittersByMatch
                .computeIfAbsent(matchId, id -> new ConcurrentHashMap<>())
                .put(emitterId, emitter);

        emitter.onCompletion(() -> removeEmitter(matchId, emitterId));
        emitter.onTimeout(() -> removeEmitter(matchId, emitterId));
        emitter.onError(e -> removeEmitter(matchId, emitterId));

        // For simplicity, one gRPC stream per SSE subscription.
        // Could be optimized later to share a single stream per match.
        SubscribeMatchEventsRequest request = SubscribeMatchEventsRequest.newBuilder()
                .setMatchId(matchId.toString())
                .setIncludeHistory(true)
                .build();

        streamingStub.subscribeMatchEvents(request, new StreamObserver<MatchEventUpdate>() {
            @Override
            public void onNext(MatchEventUpdate value) {
                try {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .name("match-event")
                            .data(value.toString());
                    emitter.send(event);
                } catch (IOException e) {
                    log.warn("Error sending SSE event for match {}. Closing emitter.", matchId, e);
                    emitter.completeWithError(e);
                    removeEmitter(matchId, emitterId);
                }
            }

            @Override
            public void onError(Throwable t) {
                log.warn("gRPC stream error for match {}", matchId, t);
                emitter.completeWithError(t);
                removeEmitter(matchId, emitterId);
            }

            @Override
            public void onCompleted() {
                log.info("gRPC stream completed for match {}", matchId);
                emitter.complete();
                removeEmitter(matchId, emitterId);
            }
        });

        return emitter;
    }

    private void removeEmitter(UUID matchId, long emitterId) {
        Map<Long, SseEmitter> emitters = emittersByMatch.get(matchId);
        if (emitters != null) {
            emitters.remove(emitterId);
        }
    }
}
