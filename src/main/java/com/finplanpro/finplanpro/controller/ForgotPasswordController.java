package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ForgotPasswordController {

    @Autowired
    private PasswordResetService passwordResetService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String email, RedirectAttributes ra) {
        passwordResetService.sendResetEmail(email);
        ra.addFlashAttribute("successMsg",
                "ถ้า email นี้มีในระบบ จะได้รับลิงก์รีเซ็ตรหัสผ่านใน inbox ภายในไม่กี่นาที");
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes ra) {

        if (!password.equals(confirmPassword)) {
            ra.addFlashAttribute("errorMsg", "รหัสผ่านไม่ตรงกัน");
            return "redirect:/reset-password?token=" + token;
        }

        boolean success = passwordResetService.resetPassword(token, password);
        if (success) {
            ra.addFlashAttribute("successMsg", "รีเซ็ตรหัสผ่านสำเร็จ กรุณาเข้าสู่ระบบ");
            return "redirect:/login";
        } else {
            ra.addFlashAttribute("errorMsg", "ลิงก์หมดอายุหรือไม่ถูกต้อง กรุณาขอลิงก์ใหม่");
            return "redirect:/forgot-password";
        }
    }
}
