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

        // Map Entity to DTO
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setFirstName(userProfile.getFirstName());
        userProfileDto.setLastName(userProfile.getLastName());
        userProfileDto.setDateOfBirth(userProfile.getDateOfBirth());
        userProfileDto.setGender(userProfile.getGender());
        userProfileDto.setProfileImageUrl(userProfile.getProfileImageUrl());

        if (userProfile.getUser() != null) {
            userProfileDto.setEmail(userProfile.getUser().getEmail());
            userProfileDto.setUsername(userProfile.getUser().getUsername());
        }

        model.addAttribute("userProfile", userProfileDto);
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

        try {
            // Handle Profile Image Upload
            if (userProfileDto.getProfileImage() != null && !userProfileDto.getProfileImage().isEmpty()) {
                String uploadDir = "src/main/resources/static/uploads/profile/";
                java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);
                if (!java.nio.file.Files.exists(uploadPath)) {
                    java.nio.file.Files.createDirectories(uploadPath);
                }

                String fileName = java.util.UUID.randomUUID().toString() + "_"
                        + userProfileDto.getProfileImage().getOriginalFilename();
                java.nio.file.Path filePath = uploadPath.resolve(fileName);
                java.nio.file.Files.copy(userProfileDto.getProfileImage().getInputStream(), filePath,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Set the URL path for specific user profile logic to handle (or set directly
                // if DTO to Entity mapping handles it)
                // Since we need to persist this, typically we'd set it on the DTO or handle it
                // in the service.
                // For now, let's assume the service handles mapping or we pass it explicitly.
                // However, UserProfileDto doesn't have profileImageUrl, it has the file.
                // We likely need to pass the URL to the service.

                // Let's modify the service call to accept the image URL or handle the DTO
                // differently.
                // OR, simpler: Modify DTO to have a hidden profileImageUrl field to pass to
                // service?
                // Better: Update service to handle DTO + image URL.
                // But Dto has the file. Let's start by modifying the DTO to hold the URL string
                // as well, or update service.

                // WAIT: The service `save(UserProfileDto)` probably maps DTO to Entity.
                // I should assume I need to pass the URL to the service.
                // Let's check UserProfileService.java first to see how it saves.

                userProfileService.save(userProfileDto, "/uploads/profile/" + fileName);
            } else {
                userProfileService.save(userProfileDto, null);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error uploading image: " + e.getMessage());
            return "redirect:/profile";
        }

        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/profile";
    }
}
