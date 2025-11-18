package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.UserProfile;
import com.finplanpro.finplanpro.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public Optional<UserProfile> findById(Long id) {
        return profileRepository.findById(id);
    }

    public UserProfile save(UserProfile userProfile) {
        return profileRepository.save(userProfile);
    }
}
