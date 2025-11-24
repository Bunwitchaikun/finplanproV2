package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.UserDto;
import com.finplanpro.finplanpro.entity.Role;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.entity.UserProfile;
import com.finplanpro.finplanpro.repository.RoleRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ... (เมธอด saveUser, findUserByUsername, findUserByEmail เหมือนเดิม) ...

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Validates a user's credentials.
     * @param username the username to validate
     * @param rawPassword the raw password to check
     * @return true if the credentials are valid, false otherwise
     */
    @Override
    public boolean validateUser(String username, String rawPassword) {
        User user = findUserByUsername(username);
        if (user == null) {
            return false; // ไม่พบผู้ใช้
        }
        // ตรวจสอบรหัสผ่านที่เข้ารหัสแล้ว
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    @Override
    @Transactional
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // Encode the password
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Create and associate UserProfile
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(userDto.getFirstName());
        userProfile.setLastName(userDto.getLastName());
        userProfile.setUser(user); // Link profile to user
        user.setUserProfile(userProfile); // Link user to profile

        // Assign Role
        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoles(Set.of(role));

        // Save the user, and the profile will be saved automatically due to CascadeType.ALL
        userRepository.save(user);
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }
}
