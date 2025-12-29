package dev.villanidev.sportstracker.operations.web;

import dev.villanidev.sportstracker.model.MatchEventType;
import dev.villanidev.sportstracker.operations.service.MatchOperationsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class OperationsController {

    private final MatchOperationsService operationsService;

    public OperationsController(MatchOperationsService operationsService) {
        this.operationsService = operationsService;
    }

    @GetMapping("/operations")
    public String operationsDashboard(Model model) {
        populateDashboard(model);
        return "operations/index";
    }

    private void populateDashboard(Model model) {
        model.addAttribute("allMatches", operationsService.findAllMatches());
        model.addAttribute("latestEvents", operationsService.findLatestEvents());
        model.addAttribute("teams", operationsService.findAllTeams());
    }

    @GetMapping("/operations/teams")
    public String teamsPage(Model model) {
        model.addAttribute("teams", operationsService.findAllTeams());
        return "operations/teams";
    }

    @PostMapping("/operations/teams/create")
    public String createTeam(@RequestParam("name") String name,
                             Model model) {
        operationsService.createTeam(name);
        model.addAttribute("teams", operationsService.findAllTeams());
        return "operations/teams :: teamsRoot";
    }

    @PostMapping("/operations/teams/delete")
    public String deleteTeam(@RequestParam("teamId") UUID teamId, Model model) {
        operationsService.deleteTeam(teamId);
        model.addAttribute("teams", operationsService.findAllTeams());
        return "operations/teams :: teamsRoot";
    }

    @PostMapping("/operations/teams/rename")
    public String renameTeam(@RequestParam("teamId") UUID teamId,
                             @RequestParam("name") String name,
                             Model model) {
        if (name != null && !name.isBlank()) {
            operationsService.renameTeam(teamId, name.trim());
        }
        model.addAttribute("teams", operationsService.findAllTeams());
        return "operations/teams :: teamsRoot";
    }

    @PostMapping("/operations/match/create")
    public String createMatch(@RequestParam("homeTeamId") UUID homeTeamId, @RequestParam("awayTeamId") UUID awayTeamId,
                              Model model) {
        operationsService.createMatch(homeTeamId, awayTeamId);
        populateDashboard(model);
        return "operations/index :: operationsRoot";
    }

    @PostMapping("/operations/match/delete")
    public String deleteMatch(@RequestParam("matchId") UUID matchId, Model model) {
        operationsService.deleteMatch(matchId);
        populateDashboard(model);
        return "operations/index :: operationsRoot";
    }

    @PostMapping("/operations/match/start")
    public String startMatch(@RequestParam("matchId") UUID matchId, Model model) {
        operationsService.startMatch(matchId);
        populateDashboard(model);
        return "operations/index :: operationsRoot";
    }

    @PostMapping("/operations/match/pause")
    public String pauseMatch(@RequestParam("matchId") UUID matchId, Model model) {
        operationsService.pauseMatch(matchId);
        populateDashboard(model);
        return "operations/index :: operationsRoot";
    }

    @PostMapping("/operations/match/resume")
    public String resumeMatch(@RequestParam("matchId") UUID matchId, Model model) {
        operationsService.resumeMatch(matchId);
        populateDashboard(model);
        return "operations/index :: operationsRoot";
    }

    @PostMapping("/operations/match/half-time")
    public String halfTime(@RequestParam("matchId") UUID matchId, Model model) {
        operationsService.halfTime(matchId);
        populateDashboard(model);
        return "operations/index :: operationsRoot";
    }

    @PostMapping("/operations/match/finish")
    public String finishMatch(@RequestParam("matchId") UUID matchId, Model model) {
        operationsService.finishMatch(matchId);
        populateDashboard(model);
        return "operations/index :: operationsRoot";
    }

    @PostMapping("/operations/match/demo")
    public String toggleDemo(@RequestParam("matchId") UUID matchId,
                             @RequestParam("enabled") boolean enabled,
                             Model model) {
        operationsService.toggleDemoMode(matchId, enabled);
        populateDashboard(model);
        return "operations/index :: operationsRoot";
    }

    @PostMapping("/operations/match/event")
    public String createEvent(@RequestParam("matchId") UUID matchId,
                              @RequestParam("teamSide") String teamSide,
                              @RequestParam("eventType") String eventType,
                              Model model) {
        MatchEventType type = MatchEventType.valueOf(eventType);
        operationsService.logManualEvent(matchId, teamSide, type);
        populateDashboard(model);
        return "operations/index :: operationsRoot";
    }
}
