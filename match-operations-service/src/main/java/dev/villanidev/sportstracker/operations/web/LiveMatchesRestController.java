package dev.villanidev.sportstracker.operations.web;

import dev.villanidev.sportstracker.model.Match;
import dev.villanidev.sportstracker.operations.service.MatchOperationsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matches")
public class LiveMatchesRestController {

    private final MatchOperationsService operationsService;

    public LiveMatchesRestController(MatchOperationsService operationsService) {
        this.operationsService = operationsService;
    }

    @GetMapping("/live")
    public List<MatchSummaryDto> getLiveMatches() {
        List<Match> matches = operationsService.findLiveMatches();
        return matches.stream()
                .map(MatchSummaryDto::fromMatch)
                .collect(Collectors.toList());
    }

    public static class MatchSummaryDto {
        public String id;
        public String homeTeamName;
        public String awayTeamName;
        public String status;
        public int homeScore;
        public int awayScore;
        public Integer currentMinute;

        static MatchSummaryDto fromMatch(Match match) {
            MatchSummaryDto dto = new MatchSummaryDto();
            dto.id = match.getId().toString();
            dto.homeTeamName = match.getHomeTeam().getName();
            dto.awayTeamName = match.getAwayTeam().getName();
            dto.status = match.getStatus() != null ? match.getStatus().name() : null;
            dto.homeScore = match.getHomeScore() != null ? match.getHomeScore() : 0;
            dto.awayScore = match.getAwayScore() != null ? match.getAwayScore() : 0;
            dto.currentMinute = match.getCurrentMinute();
            return dto;
        }
    }
}
