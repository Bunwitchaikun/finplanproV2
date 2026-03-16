package com.finplanpro.finplanpro.config;

import com.finplanpro.finplanpro.entity.SystemLog;
import com.finplanpro.finplanpro.repository.SystemLogRepository;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityEventListener {

    private final SystemLogRepository systemLogRepository;

    public SecurityEventListener(SystemLogRepository systemLogRepository) {
        this.systemLogRepository = systemLogRepository;
    }

    @EventListener
    public void onLoginSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        String ip = extractIp(event.getAuthentication().getDetails());
        systemLogRepository.save(new SystemLog(
                "LOGIN_SUCCESS",
                username,
                "เข้าสู่ระบบสำเร็จ",
                ip
        ));
    }

    @EventListener
    public void onLoginFailure(AbstractAuthenticationFailureEvent event) {
        String username = String.valueOf(event.getAuthentication().getPrincipal());
        String ip = extractIp(event.getAuthentication().getDetails());
        systemLogRepository.save(new SystemLog(
                "LOGIN_FAILED",
                username,
                "เข้าสู่ระบบล้มเหลว: " + event.getException().getMessage(),
                ip
        ));
    }

    private String extractIp(Object details) {
        if (details instanceof WebAuthenticationDetails webDetails) {
            return webDetails.getRemoteAddress();
        }
        return "unknown";
    }
}
