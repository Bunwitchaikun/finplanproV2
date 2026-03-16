package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.OrgSettings;
import com.finplanpro.finplanpro.entity.Role;
import com.finplanpro.finplanpro.entity.SystemLog;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.OrgSettingsRepository;
import com.finplanpro.finplanpro.repository.RoleRepository;
import com.finplanpro.finplanpro.repository.SystemLogRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SystemLogRepository systemLogRepository;
    private final OrgSettingsRepository orgSettingsRepository;
    private final DataSource dataSource;

    public AdminController(UserRepository userRepository,
                           RoleRepository roleRepository,
                           SystemLogRepository systemLogRepository,
                           OrgSettingsRepository orgSettingsRepository,
                           DataSource dataSource) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.systemLogRepository = systemLogRepository;
        this.orgSettingsRepository = orgSettingsRepository;
        this.dataSource = dataSource;
    }

    // ─── Admin Dashboard ─────────────────────────────────────────────────────

    @GetMapping
    public String dashboard(Model model) {
        long totalUsers = userRepository.count();
        long totalLogs  = systemLogRepository.count();
        List<SystemLog> recentLogs = systemLogRepository
                .findAllByOrderByCreatedAtDesc(PageRequest.of(0, 5)).getContent();
        boolean dbOk = checkDatabase();

        // Secret codes — visible in template only to Owner via sec:authorize
        OrgSettings settings = orgSettingsRepository.findById(1).orElse(new OrgSettings());

        model.addAttribute("activeMenu", "admin");
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalLogs", totalLogs);
        model.addAttribute("recentLogs", recentLogs);
        model.addAttribute("dbStatus", dbOk ? "Online" : "Error");
        model.addAttribute("dbOk", dbOk);
        model.addAttribute("settings", settings);
        return "admin/dashboard";
    }

    // ─── Update Secret Codes (Owner only) ────────────────────────────────────

    @PostMapping("/settings/codes")
    public String updateCodes(@RequestParam String ownerCode,
                              @RequestParam String ceoCode,
                              @RequestParam String adminCode,
                              Authentication auth,
                              RedirectAttributes ra) {
        if (!hasRole(auth, "ROLE_OWNER")) {
            ra.addFlashAttribute("errorMsg", "ไม่มีสิทธิ์ดำเนินการนี้");
            return "redirect:/admin";
        }
        OrgSettings settings = orgSettingsRepository.findById(1).orElse(new OrgSettings());
        settings.setId(1);
        settings.setOwnerCode(ownerCode.isBlank() ? settings.getOwnerCode() : ownerCode);
        settings.setCeoCode(ceoCode.isBlank() ? settings.getCeoCode() : ceoCode);
        settings.setAdminCode(adminCode.isBlank() ? settings.getAdminCode() : adminCode);
        orgSettingsRepository.save(settings);

        systemLogRepository.save(new SystemLog(
                "CODE_UPDATED", auth.getName(), "อัปเดต Secret Codes", null));
        ra.addFlashAttribute("successMsg", "อัปเดต Secret Codes เรียบร้อยแล้ว");
        return "redirect:/admin";
    }

    // ─── View System Logs ─────────────────────────────────────────────────────

    @GetMapping("/logs")
    public String logs(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "20") int size,
                       Model model) {
        Page<SystemLog> logsPage = systemLogRepository
                .findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
        model.addAttribute("activeMenu", "admin");
        model.addAttribute("logsPage", logsPage);
        model.addAttribute("currentPage", page);
        return "admin/logs";
    }

    // ─── Manage User Accounts ────────────────────────────────────────────────

    @GetMapping("/users")
    public String users(Model model, Authentication auth) {
        List<User> users = userRepository.findAll();
        boolean isOwner = hasRole(auth, "ROLE_OWNER");
        boolean isCeo   = hasRole(auth, "ROLE_CEO");
        model.addAttribute("activeMenu", "admin");
        model.addAttribute("users", users);
        model.addAttribute("isOwner", isOwner);
        model.addAttribute("isCeo", isCeo);
        return "admin/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable Long id,
                             Authentication auth,
                             RedirectAttributes ra) {
        User current = userRepository.findByEmail(auth.getName());
        if (current != null && current.getId().equals(id)) {
            ra.addFlashAttribute("errorMsg", "ไม่สามารถปิดการใช้งานบัญชีของตัวเองได้");
            return "redirect:/admin/users";
        }
        userRepository.findById(id).ifPresent(user -> {
            boolean newState = !user.isEnabled();
            user.setEnabled(newState);
            userRepository.save(user);
            systemLogRepository.save(new SystemLog(
                    newState ? "USER_ENABLED" : "USER_DISABLED",
                    auth.getName(),
                    (newState ? "เปิดใช้งาน" : "ปิดใช้งาน") + " บัญชี: " + user.getEmail(),
                    null));
        });
        ra.addFlashAttribute("successMsg", "อัปเดตสถานะผู้ใช้เรียบร้อยแล้ว");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String changeRole(@PathVariable Long id,
                             @RequestParam String newRole,
                             Authentication auth,
                             RedirectAttributes ra) {
        User current = userRepository.findByEmail(auth.getName());

        // Cannot change own role
        if (current != null && current.getId().equals(id)) {
            ra.addFlashAttribute("errorMsg", "ไม่สามารถเปลี่ยน Role ของตัวเองได้");
            return "redirect:/admin/users";
        }

        // Only OWNER can assign CEO; CEO cannot assign roles
        boolean actorIsOwner = hasRole(auth, "ROLE_OWNER");
        boolean actorIsCeo   = hasRole(auth, "ROLE_CEO");

        if (!actorIsOwner && !actorIsCeo) {
            ra.addFlashAttribute("errorMsg", "ไม่มีสิทธิ์เปลี่ยน Role");
            return "redirect:/admin/users";
        }
        if (!actorIsOwner && newRole.equals("ROLE_CEO")) {
            ra.addFlashAttribute("errorMsg", "เฉพาะ Owner เท่านั้นที่สามารถกำหนด Role CEO ได้");
            return "redirect:/admin/users";
        }
        if (newRole.equals("ROLE_OWNER") && !actorIsOwner) {
            ra.addFlashAttribute("errorMsg", "เฉพาะ Owner เท่านั้นที่สามารถกำหนด Role Owner ได้");
            return "redirect:/admin/users";
        }

        userRepository.findById(id).ifPresent(user -> {
            // Cannot change an Owner's role
            boolean targetIsOwner = user.getRoles().stream()
                    .anyMatch(r -> r.getName().equals("ROLE_OWNER"));
            if (targetIsOwner) return;

            Role role = roleRepository.findByName(newRole);
            if (role != null) {
                user.setRoles(new HashSet<>(Set.of(role)));
                userRepository.save(user);
                systemLogRepository.save(new SystemLog(
                        "ROLE_CHANGED", auth.getName(),
                        "เปลี่ยน Role ของ " + user.getEmail() + " เป็น " + newRole, null));
            }
        });

        ra.addFlashAttribute("successMsg", "เปลี่ยน Role เรียบร้อยแล้ว");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id,
                             Authentication auth,
                             RedirectAttributes ra) {
        User current = userRepository.findByEmail(auth.getName());
        if (current != null && current.getId().equals(id)) {
            ra.addFlashAttribute("errorMsg", "ไม่สามารถลบบัญชีของตัวเองได้");
            return "redirect:/admin/users";
        }
        userRepository.findById(id).ifPresent(user -> {
            // Cannot delete an Owner
            boolean targetIsOwner = user.getRoles().stream()
                    .anyMatch(r -> r.getName().equals("ROLE_OWNER"));
            if (targetIsOwner) return;
            String email = user.getEmail();
            userRepository.delete(user);
            systemLogRepository.save(new SystemLog(
                    "USER_DELETED", auth.getName(), "ลบบัญชี: " + email, null));
        });
        ra.addFlashAttribute("successMsg", "ลบผู้ใช้เรียบร้อยแล้ว");
        return "redirect:/admin/users";
    }

    // ─── Monitor System Health ───────────────────────────────────────────────

    @GetMapping("/health")
    public String health(Model model) {
        Runtime runtime = Runtime.getRuntime();
        long totalMem = runtime.totalMemory();
        long freeMem  = runtime.freeMemory();
        long usedMem  = totalMem - freeMem;
        long maxMem   = runtime.maxMemory();
        int  memPct   = (int) (usedMem * 100 / maxMem);
        boolean dbOk  = checkDatabase();

        Map<String, String> info = new LinkedHashMap<>();
        info.put("Java Version",       System.getProperty("java.version"));
        info.put("OS",                 System.getProperty("os.name") + " " + System.getProperty("os.version"));
        info.put("CPU Cores",          String.valueOf(runtime.availableProcessors()));
        info.put("Server Time",        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        info.put("Max Memory",         formatBytes(maxMem));
        info.put("Used Memory",        formatBytes(usedMem));
        info.put("Free Memory",        formatBytes(freeMem));
        info.put("Database",           dbOk ? "✓ Connected" : "✗ Error");
        info.put("Total Users",        String.valueOf(userRepository.count()));
        info.put("Total Log Entries",  String.valueOf(systemLogRepository.count()));

        model.addAttribute("activeMenu", "admin");
        model.addAttribute("healthData", info);
        model.addAttribute("memPct", memPct);
        model.addAttribute("dbOk", dbOk);
        model.addAttribute("dbStatus", dbOk ? "Online" : "Error");
        model.addAttribute("usedMem", formatBytes(usedMem));
        model.addAttribute("maxMem", formatBytes(maxMem));
        return "admin/health";
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

    private boolean checkDatabase() {
        try (Connection conn = dataSource.getConnection()) {
            return conn.isValid(2);
        } catch (Exception e) {
            return false;
        }
    }

    private String formatBytes(long bytes) {
        if (bytes >= 1_073_741_824) return String.format("%.1f GB", bytes / 1_073_741_824.0);
        if (bytes >= 1_048_576)     return String.format("%.1f MB", bytes / 1_048_576.0);
        return String.format("%.1f KB", bytes / 1_024.0);
    }
}
