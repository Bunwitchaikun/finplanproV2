package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.dto.TaxRequestDTO;
import com.finplanpro.finplanpro.dto.TaxResultDTO;
import com.finplanpro.finplanpro.service.TaxCalculationService;
import com.finplanpro.finplanpro.service.TaxRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tax")
public class TaxController {

    private final TaxCalculationService calculationService;
    private final TaxRecordService recordService;

    public TaxController(TaxCalculationService calculationService, TaxRecordService recordService) {
        this.calculationService = calculationService;
        this.recordService = recordService;
    }

    @GetMapping
    public String showTaxForm(Model model) {
        if (!model.containsAttribute("taxRequest")) {
            model.addAttribute("taxRequest", new TaxRequestDTO());
        }
        model.addAttribute("activeMenu", "tax");
        return "tax/form";
    }

    @PostMapping("/calculate")
    public String calculateTax(@ModelAttribute("taxRequest") TaxRequestDTO taxRequest, RedirectAttributes redirectAttributes) {
        TaxResultDTO result = calculationService.calculateTax(taxRequest);
        
        redirectAttributes.addFlashAttribute("taxRequest", taxRequest);
        redirectAttributes.addFlashAttribute("taxResult", result);
        redirectAttributes.addFlashAttribute("showResult", true);
        
        return "redirect:/tax";
    }

    @PostMapping("/save")
    public String saveTaxRecord(@ModelAttribute("taxRequest") TaxRequestDTO taxRequest, RedirectAttributes redirectAttributes) {
        
        // Recalculate to ensure data integrity before saving
        TaxResultDTO finalResult = calculationService.calculateTax(taxRequest);
        recordService.save(taxRequest, finalResult);
        
        redirectAttributes.addFlashAttribute("successMessage", "Tax calculation for year " + taxRequest.getTaxYear() + " has been saved.");
        return "redirect:/tax/list";
    }

    @GetMapping("/list")
    public String showListPage(Model model) {
        model.addAttribute("records", recordService.findRecordsByUser());
        model.addAttribute("activeMenu", "tax");
        return "tax/list";
    }

    @PostMapping("/delete-all")
    public String deleteAllRecords(RedirectAttributes redirectAttributes) {
        recordService.deleteAllForCurrentUser();
        redirectAttributes.addFlashAttribute("successMessage", "All tax records have been deleted.");
        return "redirect:/tax/list";
    }
}
