package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.PasswordResetToken;
import com.finplanpro.finplanpro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    @Transactional
    void deleteByUser(User user);
}
