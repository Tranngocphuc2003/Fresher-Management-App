package com.springboot.spring_boot_project.controller;

import com.springboot.spring_boot_project.service.FresherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {
    @Autowired
    private FresherService fresherService;

    // Dashboard for Fresher count by center

    @GetMapping("/dashboard/center")
    public String getFresherCountByCenter(Model model) {
        model.addAttribute("centerData", fresherService.getFresherCountByCenter());
        return "dashboard-center";
    }

    // Dashboard for Fresher count by score

    @GetMapping("/dashboard/score")
    public String getFresherCountByScore(Model model) {
        model.addAttribute("scoreData", fresherService.getFresherCountByScore());
        return "dashboard-score";
    }
}
