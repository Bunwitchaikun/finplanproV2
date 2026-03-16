package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.service.PasswordResetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ForgotPasswordController {

    private final PasswordResetService passwordResetService;

    public ForgotPasswordController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordSubmit(@RequestParam String email, RedirectAttributes ra) {
        passwordResetService.sendResetEmail(email);
        ra.addFlashAttribute("successMsg",
            "หากมี account ที่ใช้ email นี้ ระบบจะส่งลิงก์รีเซ็ตรหัสผ่านไปให้ภายใน 1-2 นาที");
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPasswordSubmit(@RequestParam String token,
                                      @RequestParam String newPassword,
                                      @RequestParam String confirmPassword,
                                      RedirectAttributes ra) {
        if (!newPassword.equals(confirmPassword)) {
            ra.addFlashAttribute("errorMsg", "รหัสผ่านไม่ตรงกัน");
            return "redirect:/reset-password?token=" + token;
        }

        String result = passwordResetService.resetPassword(token, newPassword);
        return switch (result) {
            case "success" -> {
                ra.addFlashAttribute("successMsg", "รีเซ็ตรหัสผ่านสำเร็จ กรุณาเข้าสู่ระบบด้วยรหัสผ่านใหม่");
                yield "redirect:/login";
            }
            case "expired" -> {
                ra.addFlashAttribute("errorMsg", "ลิงก์หมดอายุแล้ว กรุณาขอลิงก์ใหม่");
                yield "redirect:/forgot-password";
            }
            default -> {
                ra.addFlashAttribute("errorMsg", "ลิงก์ไม่ถูกต้องหรือใช้ไปแล้ว");
                yield "redirect:/forgot-password";
            }
        };
    }
}
