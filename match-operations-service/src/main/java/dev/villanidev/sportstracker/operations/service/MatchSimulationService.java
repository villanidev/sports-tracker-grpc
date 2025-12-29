package dev.villanidev.sportstracker.operations.service;

import dev.villanidev.sportstracker.model.Match;
import dev.villanidev.sportstracker.model.MatchEvent;
import dev.villanidev.sportstracker.model.MatchStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchSimulationService {

    private static final Logger log = LoggerFactory.getLogger(MatchSimulationService.class);

    private final MatchOperationsService operationsService;

    public MatchSimulationService(MatchOperationsService operationsService) {
        this.operationsService = operationsService;
    }

    // Every 30 seconds, generate a plausible event for a random live match
    @Scheduled(fixedDelay = 30000L, initialDelay = 10000L)
    public void simulateRandomMatchEvent() {
        // Demo engine: act only on live matches with demo mode enabled
        List<Match> liveMatches = operationsService.findLiveMatches();
        if (liveMatches.isEmpty()) {
            return;
        }

        List<Match> demoMatches = liveMatches.stream()
                .filter(m -> Boolean.TRUE.equals(m.getDemoModeEnabled()))
                .toList();

        if (demoMatches.isEmpty()) {
            return;
        }

        MatchEvent event = operationsService.logRandomEventForRandomLiveMatch();

        if (event != null && event.getMatch().getStatus() == MatchStatus.LIVE
                && Boolean.TRUE.equals(event.getMatch().getDemoModeEnabled())) {
            log.info("[DEMO] Simulated event: {} for match {} at minute {}", event.getEventType(),
                    event.getMatch().getId(), event.getMinute());
        }
    }
}
