package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.UserDto;
import com.finplanpro.finplanpro.entity.OrgSettings;
import com.finplanpro.finplanpro.entity.Role;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.entity.UserProfile;
import com.finplanpro.finplanpro.repository.OrgSettingsRepository;
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
    private final OrgSettingsRepository orgSettingsRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           OrgSettingsRepository orgSettingsRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.orgSettingsRepository = orgSettingsRepository;
    }

    @Override
    @Transactional
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Create and associate UserProfile
        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName(userDto.getFirstName());
        userProfile.setLastName(userDto.getLastName());
        userProfile.setUser(user);
        user.setUserProfile(userProfile);

        // Determine role from secret code
        String roleName = resolveRoleFromCode(userDto.getSecretCode());
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = new Role();
            role.setName(roleName);
            role = roleRepository.save(role);
        }
        user.setRoles(Set.of(role));

        userRepository.save(user);
    }

    private String resolveRoleFromCode(String code) {
        if (code == null || code.isBlank()) return "ROLE_USER";
        OrgSettings settings = orgSettingsRepository.findById(1).orElse(null);
        if (settings == null) return "ROLE_USER";
        if (code.equals(settings.getOwnerCode())) return "ROLE_OWNER";
        if (code.equals(settings.getCeoCode()))   return "ROLE_CEO";
        if (code.equals(settings.getAdminCode())) return "ROLE_ADMIN";
        return "ROLE_USER";
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
