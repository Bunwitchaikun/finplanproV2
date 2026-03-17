package com.finplanpro.finplanpro.config;

import com.finplanpro.finplanpro.entity.UserProfile;
import com.finplanpro.finplanpro.service.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserProfileService userProfileService;

    public GlobalControllerAdvice(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @ModelAttribute("loggedInUserProfile")
    public UserProfile getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        try {
            return userProfileService.findByUser();
        } catch (Exception e) {
            return null;
        }
    }
}
