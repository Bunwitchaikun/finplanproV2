package com.finplanpro.finplanpro.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        if (auth != null) {
            model.addAttribute("username", auth.getName());
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                            || a.getAuthority().equals("ROLE_CEO")
                            || a.getAuthority().equals("ROLE_OWNER"));
            model.addAttribute("isAdmin", isAdmin);
        }
        return "dashboard";
    }
}
