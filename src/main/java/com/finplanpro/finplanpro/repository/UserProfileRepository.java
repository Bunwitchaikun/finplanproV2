package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);
}
