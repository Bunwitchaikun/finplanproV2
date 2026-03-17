package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.service.NetWorthSnapshotService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/assets")
public class NetWorthController {

    private final NetWorthSnapshotService snapshotService;

    public NetWorthController(NetWorthSnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    @GetMapping
    public String showForm(Model model) {
        if (!model.containsAttribute("snapshot")) {
            NetWorthSnapshot snapshot = new NetWorthSnapshot();
            // Seed default items for the view
            snapshotService.seedDefaultItems(snapshot);
            model.addAttribute("snapshot", snapshot);
        }
        model.addAttribute("activeMenu", "assets");
        return "assets/form";
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute("snapshot") NetWorthSnapshot snapshot, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "assets/form";
        }
        snapshotService.save(snapshot);
        redirectAttributes.addFlashAttribute("successMessage",
                "Snapshot '" + snapshot.getSnapshotName() + "' saved successfully!");
        return "redirect:/assets/list";
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
        model.addAttribute("activeMenu", "assets");
        return "assets/form";
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
        return "redirect:/assets/list";
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
        return "redirect:/assets/list";
    }
}
