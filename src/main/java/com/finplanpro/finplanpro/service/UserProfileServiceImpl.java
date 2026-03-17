package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.UserProfileDto;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.entity.UserProfile;
import com.finplanpro.finplanpro.repository.UserProfileRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserProfile findByUser() {
        User user = getCurrentUser();
        return userProfileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });
    }

    @Override
    public UserProfile save(UserProfileDto userProfileDto) {
        return save(userProfileDto, null);
    }

    @Override
    public UserProfile save(UserProfileDto userProfileDto, String profileImageUrl) {
        User user = getCurrentUser();
        UserProfile userProfile = userProfileRepository.findByUser(user)
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(user);
                    return newProfile;
                });

        userProfile.setFirstName(userProfileDto.getFirstName());
        userProfile.setLastName(userProfileDto.getLastName());
        userProfile.setDateOfBirth(userProfileDto.getDateOfBirth());
        userProfile.setGender(userProfileDto.getGender());

        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            userProfile.setProfileImageUrl(profileImageUrl);
        }

        return userProfileRepository.save(userProfile);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username);
        if (user == null) {
            user = userRepository.findByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
