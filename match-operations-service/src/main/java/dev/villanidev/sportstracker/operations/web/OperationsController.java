package dev.villanidev.sportstracker.operations.web;

import dev.villanidev.sportstracker.operations.service.MatchOperationsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OperationsController {

    private final MatchOperationsService operationsService;

    public OperationsController(MatchOperationsService operationsService) {
        this.operationsService = operationsService;
    }

    @GetMapping("/operations")
    public String operationsDashboard(Model model) {
        model.addAttribute("liveMatches", operationsService.findLiveMatches());
        model.addAttribute("latestEvents", operationsService.findLatestEvents());
        return "operations/index";
    }
}
