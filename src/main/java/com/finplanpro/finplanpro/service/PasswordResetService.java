package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.PasswordResetToken;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.PasswordResetTokenRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public PasswordResetService(UserRepository userRepository,
                                PasswordResetTokenRepository tokenRepository,
                                JavaMailSender mailSender,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendResetEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) return; // ไม่แจ้ง error เพื่อความปลอดภัย

        // ลบ token เก่าของ user นี้
        tokenRepository.deleteByUser(user);

        // สร้าง token ใหม่
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(UUID.randomUUID().toString());
        resetToken.setUser(user);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(resetToken);

        // ส่ง email
        String resetLink = baseUrl + "/reset-password?token=" + resetToken.getToken();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("FinPlanPro — Reset your password");
        message.setText(
            "สวัสดีครับ " + user.getUsername() + ",\n\n" +
            "คุณได้ขอรีเซ็ตรหัสผ่านสำหรับบัญชี FinPlanPro\n\n" +
            "กดลิงก์ด้านล่างเพื่อตั้งรหัสผ่านใหม่ (ลิงก์หมดอายุใน 30 นาที):\n\n" +
            resetLink + "\n\n" +
            "หากคุณไม่ได้ขอรีเซ็ตรหัสผ่าน กรุณาเพิกเฉยต่อ email นี้\n\n" +
            "ขอบคุณครับ,\nทีม FinPlanPro"
        );
        mailSender.send(message);
    }

    public String resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);

        if (resetToken == null) return "invalid";
        if (resetToken.isUsed()) return "used";
        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) return "expired";

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return "success";
    }
}
