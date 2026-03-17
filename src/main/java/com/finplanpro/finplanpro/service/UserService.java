package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.Role;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.entity.UserRole;
import com.finplanpro.finplanpro.repository.RoleRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import com.finplanpro.finplanpro.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRoleRepository userRoleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(String username, String email, String rawPassword, String inviteCode) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already registered");
        }

        String roleName = resolveRole(inviteCode);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .enabled(true)
                .build();
        userRepository.save(user);

        UserRole userRole = UserRole.builder().user(user).role(role).build();
        userRoleRepository.save(userRole);
    }

    private String resolveRole(String inviteCode) {
        if (inviteCode == null || inviteCode.isBlank()) return "ROLE_USER";
        return switch (inviteCode.trim()) {
            case "OWNER@FinPlan"  -> "ROLE_OWNER";
            case "CEO@FinPlan"    -> "ROLE_CEO";
            case "ADMIN@FinPlan"  -> "ROLE_ADMIN";
            default               -> "ROLE_USER";
        };
    }
}
