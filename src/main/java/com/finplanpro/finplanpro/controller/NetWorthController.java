package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.service.NetWorthSnapshotService;
import com.finplanpro.finplanpro.service.RetirementBasicService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/assets")
public class NetWorthController {

    private final NetWorthSnapshotService snapshotService;
    private final RetirementBasicService retirementService;

    public NetWorthController(NetWorthSnapshotService snapshotService,
                              RetirementBasicService retirementService) {
        this.snapshotService = snapshotService;
        this.retirementService = retirementService;
    }

    @GetMapping
    public String showForm(Model model) {
        if (!model.containsAttribute("snapshot")) {
            model.addAttribute("snapshot", new NetWorthSnapshot());
        }
        model.addAttribute("userSnapshots", snapshotService.findSnapshotsByUser());
        model.addAttribute("retirementPlans", retirementService.findPlansByUser());
        model.addAttribute("activeMenu", "assets");
        return "assets/form";
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute("snapshot") NetWorthSnapshot snapshot, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "assets/form";
        }
        NetWorthSnapshot saved = snapshotService.save(snapshot);
        redirectAttributes.addFlashAttribute("successMessage", "บันทึก Snapshot สำเร็จ!");
        return "redirect:/assets/edit/" + saved.getId();
    }

    @GetMapping("/list")
    public String showList(Model model) {
        model.addAttribute("snapshots", snapshotService.findSnapshotsByUser());
        model.addAttribute("activeMenu", "assets");
        return "assets/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<NetWorthSnapshot> snapshotOpt = snapshotService.findById(id);
        if (snapshotOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Snapshot not found.");
            return "redirect:/assets/list";
        }
        model.addAttribute("snapshot", snapshotOpt.get());
        model.addAttribute("userSnapshots", snapshotService.findSnapshotsByUser());
        model.addAttribute("retirementPlans", retirementService.findPlansByUser());
        model.addAttribute("activeMenu", "assets");
        return "assets/form";
    }

    @PostMapping("/edit/{id}/meta")
    public String updateMeta(@PathVariable Long id,
                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate snapshotDate,
                             @RequestParam(required = false) String snapshotTime,
                             @RequestParam(required = false) String snapshotName,
                             RedirectAttributes redirectAttributes) {
        try {
            snapshotService.updateMeta(id, snapshotDate, snapshotTime, snapshotName);
            redirectAttributes.addFlashAttribute("successMessage", "อัปเดต Snapshot สำเร็จ!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "เกิดข้อผิดพลาด: " + e.getMessage());
        }
        return "redirect:/assets";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        System.out.println("=== DELETE METHOD CALLED FOR ID: " + id + " ===");
        try {
            snapshotService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Snapshot deleted successfully.");
            System.out.println("=== DELETE SUCCESSFUL ===");
        } catch (Exception e) {
            System.out.println("=== DELETE FAILED: " + e.getMessage() + " ===");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting snapshot.");
        }
        return "redirect:/assets";
    }

    @PostMapping("/delete/bulk")
    public String deleteBulk(@RequestParam List<Long> ids, RedirectAttributes redirectAttributes) {
        try {
            snapshotService.deleteByIds(ids);
            redirectAttributes.addFlashAttribute("successMessage", "ลบ " + ids.size() + " Snapshot สำเร็จ!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "เกิดข้อผิดพลาด: " + e.getMessage());
        }
        return "redirect:/assets";
    }

    @PostMapping("/delete/all")
    public String deleteAll(RedirectAttributes redirectAttributes) {
        System.out.println("=== DELETE ALL METHOD CALLED ===");
        try {
            snapshotService.deleteAllByUser();
            redirectAttributes.addFlashAttribute("successMessage", "All snapshots have been deleted.");
            System.out.println("=== DELETE ALL SUCCESSFUL ===");
        } catch (Exception e) {
            System.out.println("=== DELETE ALL FAILED: " + e.getMessage() + " ===");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting all snapshots.");
        }
        return "redirect:/assets";
    }
}
