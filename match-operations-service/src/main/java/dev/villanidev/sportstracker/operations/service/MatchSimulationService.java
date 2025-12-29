package dev.villanidev.sportstracker.operations.service;

import dev.villanidev.sportstracker.model.MatchEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
        MatchEvent event = operationsService.logRandomEventForRandomLiveMatch();
        if (event != null) {
            log.info("Simulated event: {} for match {} at minute {}", event.getEventType(),
                    event.getMatch().getId(), event.getMinute());
        }
    }
}
