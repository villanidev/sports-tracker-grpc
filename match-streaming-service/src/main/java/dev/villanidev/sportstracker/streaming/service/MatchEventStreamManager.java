package dev.villanidev.sportstracker.streaming.service;

import dev.villanidev.sportstracker.grpc.MatchEventUpdate;
import dev.villanidev.sportstracker.grpc.MatchStreamingServiceGrpc;
import dev.villanidev.sportstracker.grpc.SubscribeMatchEventsRequest;
import dev.villanidev.sportstracker.model.Match;
import dev.villanidev.sportstracker.model.MatchEvent;
import dev.villanidev.sportstracker.streaming.repository.MatchEventStreamingRepository;
import dev.villanidev.sportstracker.streaming.repository.MatchStreamingRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@GrpcService
public class MatchEventStreamManager extends MatchStreamingServiceGrpc.MatchStreamingServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(MatchEventStreamManager.class);

    private final MatchStreamingRepository matchRepository;
    private final MatchEventStreamingRepository eventRepository;

    private final Map<UUID, List<StreamObserver<MatchEventUpdate>>> observersByMatch = new ConcurrentHashMap<>();

    private volatile Instant lastPollTime = Instant.EPOCH;

    public MatchEventStreamManager(MatchStreamingRepository matchRepository,
                                   MatchEventStreamingRepository eventRepository) {
        this.matchRepository = matchRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void subscribeMatchEvents(SubscribeMatchEventsRequest request,
                                     StreamObserver<MatchEventUpdate> responseObserver) {
        UUID matchId = UUID.fromString(request.getMatchId());
        log.info("New subscriber for match {}", matchId);

        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + matchId));

        if (request.getIncludeHistory()) {
            List<MatchEvent> history = eventRepository.findByMatchOrderByCreatedAtAsc(match);
            history.stream()
                    .map(e -> toGrpcUpdate(e, match))
                    .forEach(responseObserver::onNext);
        }

        observersByMatch
                .computeIfAbsent(matchId, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(responseObserver);
    }

    @Scheduled(fixedDelay = 2000L)
    @Transactional
    public void pollAndDispatchNewEvents() {
        Instant pollFrom = lastPollTime;
        Instant now = Instant.now();
        lastPollTime = now;

        List<MatchEvent> newEvents = eventRepository
                .findByProcessedForStreamingFalseAndCreatedAtAfterOrderByCreatedAtAsc(pollFrom);

        if (newEvents.isEmpty()) {
            return;
        }

        log.debug("Dispatching {} new events", newEvents.size());

        for (MatchEvent event : newEvents) {
            UUID matchId = event.getMatch().getId();
            List<StreamObserver<MatchEventUpdate>> observers = observersByMatch.get(matchId);
            if (observers == null || observers.isEmpty()) {
                continue;
            }

            Match match = event.getMatch();
            MatchEventUpdate update = toGrpcUpdate(event, match);

            synchronized (observers) {
                Iterator<StreamObserver<MatchEventUpdate>> it = observers.iterator();
                while (it.hasNext()) {
                    StreamObserver<MatchEventUpdate> observer = it.next();
                    try {
                        observer.onNext(update);
                    } catch (Exception ex) {
                        log.warn("Removing failed observer for match {}", matchId, ex);
                        it.remove();
                    }
                }
            }

            event.setProcessedForStreaming(Boolean.TRUE);
            eventRepository.save(event);
        }
    }

    private MatchEventUpdate toGrpcUpdate(MatchEvent event, Match match) {
        return MatchEventUpdate.newBuilder()
                .setEventId(event.getId().toString())
                .setMatchId(match.getId().toString())
                .setEventType(event.getEventType().name())
                .setPlayerId(event.getPlayer() != null ? event.getPlayer().getId().toString() : "")
                .setMinute(event.getMinute() != null ? event.getMinute() : 0)
                .setDetailsJson(event.getDetails() != null ? event.getDetails() : "{}")
                .setHomeScore(match.getHomeScore() != null ? match.getHomeScore() : 0)
                .setAwayScore(match.getAwayScore() != null ? match.getAwayScore() : 0)
                .setCreatedAtEpochMillis(event.getCreatedAt() != null ? event.getCreatedAt().toEpochMilli() : 0L)
                .build();
    }
}
