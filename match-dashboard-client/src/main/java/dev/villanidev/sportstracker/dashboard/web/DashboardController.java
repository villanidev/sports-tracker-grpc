package dev.villanidev.sportstracker.dashboard.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(name = "matchId", required = false) String matchId,
                            Model model) {
        model.addAttribute("matchId", matchId);
        return "dashboard";
    }
}
