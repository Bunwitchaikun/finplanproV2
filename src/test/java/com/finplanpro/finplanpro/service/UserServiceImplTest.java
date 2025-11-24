package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("ทดสอบ validateUser เมื่อ User และ Password ถูกต้อง")
    void testValidateUser_Success() {
        System.out.println("--- RUNNING: [Auth] testValidateUser_Success ---");
        // 1. Arrange
        String username = "testuser";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(encodedPassword);

        // กำหนดให้เมื่อ userRepository.findByUsername ถูกเรียกด้วย "testuser" ให้ return mockUser
        when(userRepository.findByUsername(username)).thenReturn(mockUser);
        // กำหนดให้เมื่อ passwordEncoder.matches ถูกเรียกด้วยรหัสผ่านที่ถูกต้อง ให้ return true
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        System.out.println("INPUT: username=" + username + ", password=" + rawPassword);

        // 2. Act
        boolean isValid = userService.validateUser(username, rawPassword);
        System.out.println("ACTUAL_RESULT: " + isValid);

        // 3. Assert
        assertTrue(isValid, "User should be valid when credentials are correct.");
        System.out.println("✅ SUCCESS: User validation successful.");
        System.out.println("--- FINISHED: [Auth] testValidateUser_Success ---\n");
    }

    @Test
    @DisplayName("ทดสอบ validateUser เมื่อ Password ไม่ถูกต้อง")
    void testValidateUser_WrongPassword() {
        System.out.println("--- RUNNING: [Auth] testValidateUser_WrongPassword ---");
        // 1. Arrange
        String username = "testuser";
        String wrongPassword = "wrongpassword";
        String encodedPassword = "encodedPassword123";

        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setPassword(encodedPassword);

        when(userRepository.findByUsername(username)).thenReturn(mockUser);
        // กำหนดให้เมื่อ passwordEncoder.matches ถูกเรียกด้วยรหัสผ่านที่ผิด ให้ return false
        when(passwordEncoder.matches(wrongPassword, encodedPassword)).thenReturn(false);

        System.out.println("INPUT: username=" + username + ", password=" + wrongPassword);

        // 2. Act
        boolean isValid = userService.validateUser(username, wrongPassword);
        System.out.println("ACTUAL_RESULT: " + isValid);

        // 3. Assert
        assertFalse(isValid, "User should be invalid when password is wrong.");
        System.out.println("✅ SUCCESS: User validation failed as expected.");
        System.out.println("--- FINISHED: [Auth] testValidateUser_WrongPassword ---\n");
    }

    @Test
    @DisplayName("ทดสอบ validateUser เมื่อไม่พบ User ในระบบ")
    void testValidateUser_UserNotFound() {
        System.out.println("--- RUNNING: [Auth] testValidateUser_UserNotFound ---");
        // 1. Arrange
        String username = "nonexistentuser";
        String password = "password123";

        // กำหนดให้เมื่อ userRepository.findByUsername ถูกเรียกด้วยชื่อที่ไม่มีในระบบ ให้ return null
        when(userRepository.findByUsername(username)).thenReturn(null);

        System.out.println("INPUT: username=" + username + ", password=" + password);

        // 2. Act
        boolean isValid = userService.validateUser(username, password);
        System.out.println("ACTUAL_RESULT: " + isValid);

        // 3. Assert
        assertFalse(isValid, "User should be invalid when user does not exist.");
        System.out.println("✅ SUCCESS: User validation failed as expected for non-existent user.");
        System.out.println("--- FINISHED: [Auth] testValidateUser_UserNotFound ---\n");
    }
}
