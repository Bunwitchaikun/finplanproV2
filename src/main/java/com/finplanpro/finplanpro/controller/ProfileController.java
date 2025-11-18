package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.dto.UserProfileDto;
import com.finplanpro.finplanpro.entity.UserProfile;
import com.finplanpro.finplanpro.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserProfileService userProfileService;

    public ProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model) {
        UserProfile userProfile = userProfileService.findByUser();
        model.addAttribute("userProfile", userProfile);
        model.addAttribute("activeMenu", "profile"); // Add this line
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("userProfile") UserProfileDto userProfileDto,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "profile";
        }
        userProfileService.save(userProfileDto);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/profile";
    }
}
