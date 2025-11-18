package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.UserProfile;
import com.finplanpro.finplanpro.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String editProfile(@PathVariable Long id,
                              @Valid @ModelAttribute("userProfile") UserProfile formProfile,
                              BindingResult bindingResult) {

        Optional<UserProfile> existingProfileOpt = profileService.findById(id);
        if (existingProfileOpt.isEmpty()) {
            return "redirect:/";
        }

        UserProfile existingProfile = existingProfileOpt.get();

        if (bindingResult.hasErrors()) {
            formProfile.setUser(existingProfile.getUser());
            return "profile/edit-profile";
        }

        existingProfile.setFirstName(formProfile.getFirstName());
        existingProfile.setLastName(formProfile.getLastName());
        existingProfile.setDateOfBirth(formProfile.getDateOfBirth());
        existingProfile.setGender(formProfile.getGender());

        profileService.save(existingProfile);

        return "redirect:/profile/" + id;
    }
}
