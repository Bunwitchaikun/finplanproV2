package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.InsurancePolicy;
import com.finplanpro.finplanpro.service.InsurancePolicyService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/insurance")
public class InsuranceController {

    private final InsurancePolicyService policyService;

    public InsuranceController(InsurancePolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    public String showForm(Model model) {
        if (!model.containsAttribute("policy")) {
            model.addAttribute("policy", new InsurancePolicy());
        }
        model.addAttribute("summary", policyService.getSummaryForCurrentUser());
        model.addAttribute("activeMenu", "insurance");
        return "insurance/form";
    }

    @PostMapping
    public String saveOrUpdate(@Valid @ModelAttribute("policy") InsurancePolicy policy, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (policyService.isPolicyNumberDuplicate(policy)) {
            result.rejectValue("policyNumber", "duplicate.policyNumber", "This policy number already exists.");
        }
        if (result.hasErrors()) {
            model.addAttribute("summary", policyService.getSummaryForCurrentUser());
            model.addAttribute("activeMenu", "insurance");
            return "insurance/form";
        }
        policyService.save(policy);
        redirectAttributes.addFlashAttribute("successMessage", "Policy '" + policy.getPolicyNumber() + "' saved successfully!");
        return "redirect:/insurance";
    }

    @GetMapping("/list")
    public String showList(Model model) {
        model.addAttribute("policies", policyService.findPoliciesByUser());
        model.addAttribute("activeMenu", "insurance");
        return "insurance/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<InsurancePolicy> policyOpt = policyService.findById(id);
        if (policyOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Policy not found.");
            return "redirect:/insurance/list";
        }
        redirectAttributes.addFlashAttribute("policy", policyOpt.get());
        return "redirect:/insurance";
    }

    @PostMapping("/delete-selected")
    public String deleteSelected(@RequestParam(value = "selectedIds", required = false) List<Long> selectedIds, RedirectAttributes redirectAttributes) {
        if (selectedIds == null || selectedIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select at least one policy to delete.");
            return "redirect:/insurance/list";
        }
        try {
            policyService.deleteByIds(selectedIds);
            redirectAttributes.addFlashAttribute("successMessage", "Selected policies deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting selected policies.");
        }
        return "redirect:/insurance/list";
    }

    @PostMapping("/delete/all")
    public String deleteAll(RedirectAttributes redirectAttributes) {
        try {
            policyService.deleteAllByUser();
            redirectAttributes.addFlashAttribute("successMessage", "All policies have been deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting all policies.");
        }
        return "redirect:/insurance/list";
    }
}
