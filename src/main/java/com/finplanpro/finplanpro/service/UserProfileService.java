package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.UserProfileDto;
import com.finplanpro.finplanpro.entity.UserProfile;

public interface UserProfileService {
    UserProfile findByUser();

    UserProfile save(UserProfileDto userProfileDto);

    UserProfile save(UserProfileDto userProfileDto, String profileImageUrl);
}
