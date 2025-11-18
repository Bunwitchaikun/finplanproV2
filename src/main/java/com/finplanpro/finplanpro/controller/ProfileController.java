package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.UserProfile;
import com.finplanpro.finplanpro.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/{id}")
    public String viewProfile(@PathVariable Long id, Model model) {
        Optional<UserProfile> userProfile = profileService.findById(id);
        if (userProfile.isPresent()) {
            model.addAttribute("userProfile", userProfile.get());
            return "profile/profile";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/{id}/edit")
    public String editProfileForm(@PathVariable Long id, Model model) {
        Optional<UserProfile> userProfile = profileService.findById(id);
        if (userProfile.isPresent()) {
            model.addAttribute("userProfile", userProfile.get());
            return "profile/edit-profile";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/{id}/edit")
    public String editProfile(UserProfile userProfile) {
        profileService.save(userProfile);
        return "redirect:/profile/" + userProfile.getId();
    }
}
