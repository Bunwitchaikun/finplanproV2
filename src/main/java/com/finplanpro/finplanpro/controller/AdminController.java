package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.Role;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.entity.UserRole;
import com.finplanpro.finplanpro.repository.RoleRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import com.finplanpro.finplanpro.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;
    @Autowired private UserRoleRepository userRoleRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalUsers", userRepository.count());
        model.addAttribute("dbStatus", "Online");
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id,
                             @RequestParam String roleName,
                             Authentication auth,
                             RedirectAttributes ra) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            ra.addFlashAttribute("errorMsg", "User not found");
            return "redirect:/admin/users";
        }

        boolean actorIsOwner = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_OWNER"));

        if (roleName.equals("ROLE_OWNER") && !actorIsOwner) {
            ra.addFlashAttribute("errorMsg", "เฉพาะ Owner เท่านั้นที่สามารถกำหนด Role Owner ได้");
            return "redirect:/admin/users";
        }

        Role role = roleRepository.findByName(roleName).orElse(null);
        if (role == null) {
            ra.addFlashAttribute("errorMsg", "Role not found: " + roleName);
            return "redirect:/admin/users";
        }

        userRoleRepository.deleteAll(user.getRoles());
        UserRole newUserRole = UserRole.builder().user(user).role(role).build();
        user.setRoles(new HashSet<>(Set.of(newUserRole)));
        userRoleRepository.save(newUserRole);

        ra.addFlashAttribute("successMsg", "เปลี่ยน Role สำเร็จ");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "redirect:/admin/users";
        model.addAttribute("user", user);
        return "admin/user-edit";
    }

    @PostMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id,
                           @RequestParam String email,
                           @RequestParam(required = false) String newPassword,
                           RedirectAttributes ra) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "redirect:/admin/users";

        user.setEmail(email);
        if (newPassword != null && !newPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        userRepository.save(user);
        ra.addFlashAttribute("successMsg", "อัพเดทข้อมูลสำเร็จ");
        return "redirect:/admin/users";
    }
}
