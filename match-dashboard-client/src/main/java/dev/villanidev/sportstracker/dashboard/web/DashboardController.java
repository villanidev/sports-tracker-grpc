package dev.villanidev.sportstracker.dashboard.web;

import dev.villanidev.sportstracker.dashboard.live.LiveMatchSummary;
import dev.villanidev.sportstracker.dashboard.live.LiveMatchesClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DashboardController {

    private final LiveMatchesClient liveMatchesClient;

    public DashboardController(LiveMatchesClient liveMatchesClient) {
        this.liveMatchesClient = liveMatchesClient;
    }

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(name = "matchId", required = false) String matchId,
                            Model model) {
        model.addAttribute("matchId", matchId);

        List<LiveMatchSummary> liveMatches = liveMatchesClient.getLiveMatches();
        model.addAttribute("liveMatches", liveMatches);

        LiveMatchSummary selectedMatch = null;
        if (matchId != null && !matchId.isBlank()) {
            for (LiveMatchSummary m : liveMatches) {
                if (matchId.equals(m.getId())) {
                    selectedMatch = m;
                    break;
                }
            }
        }
        model.addAttribute("selectedMatch", selectedMatch);

        return "dashboard";
    }
}

