package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<UserProfile, Long> {
}
