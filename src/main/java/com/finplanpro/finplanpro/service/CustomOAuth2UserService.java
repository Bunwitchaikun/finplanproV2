package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.Role;
import com.finplanpro.finplanpro.entity.SystemLog;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.RoleRepository;
import com.finplanpro.finplanpro.repository.SystemLogRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SystemLogRepository systemLogRepository;

    public CustomOAuth2UserService(UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   SystemLogRepository systemLogRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.systemLogRepository = systemLogRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String googleId = (String) attributes.get("sub");
        String email    = (String) attributes.get("email");
        String name     = (String) attributes.get("name");

        User user = userRepository.findByGoogleId(googleId);

        if (user == null) {
            // ลอง link กับ account เดิมถ้า email ตรงกัน
            user = userRepository.findByEmail(email);
            if (user != null) {
                user.setGoogleId(googleId);
                userRepository.save(user);
            } else {
                // สร้าง user ใหม่
                user = new User();
                user.setGoogleId(googleId);
                user.setEmail(email);
                user.setUsername(email.split("@")[0]);
                user.setPassword(null);
                user.setEnabled(true);

                Role userRole = roleRepository.findByName("ROLE_USER");
                user.setRoles(new HashSet<>(Set.of(userRole)));
                userRepository.save(user);

                systemLogRepository.save(new SystemLog(
                        "GOOGLE_REGISTER", email, "สมัครสมาชิกผ่าน Google: " + name, null));
            }
        }

        // ตรวจสอบ enabled
        if (!user.isEnabled()) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("account_disabled"),
                    "บัญชีนี้ถูกระงับการใช้งาน กรุณาติดต่อผู้ดูแลระบบ");
        }

        systemLogRepository.save(new SystemLog(
                "LOGIN_SUCCESS", email, "เข้าสู่ระบบสำเร็จผ่าน Google", null));

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}
