package dev.villanidev.sportstracker.dashboard.web;

import dev.villanidev.sportstracker.dashboard.sse.MatchEventsSseBridge;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/sse/matches")
public class MatchEventsSseController {

    private final MatchEventsSseBridge bridge;

    public MatchEventsSseController(MatchEventsSseBridge bridge) {
        this.bridge = bridge;
    }

    @GetMapping(path = "/{matchId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamMatchEvents(@PathVariable("matchId") UUID matchId) {
        return bridge.subscribeToMatch(matchId);
    }
}
