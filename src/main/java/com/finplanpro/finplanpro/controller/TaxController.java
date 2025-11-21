package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.TaxRecord;
import com.finplanpro.finplanpro.service.TaxService;
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

    private final TaxService taxService;

    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @GetMapping
    public String showTaxForm(Model model) {
        if (!model.containsAttribute("taxRecord")) {
            model.addAttribute("taxRecord", new TaxRecord());
        }
        model.addAttribute("activeMenu", "tax");
        return "tax/form";
    }

    @PostMapping("/calculate")
    public String calculateTax(@ModelAttribute TaxRecord taxRecord, RedirectAttributes redirectAttributes) {
        TaxRecord calculatedRecord = taxService.calculateTax(taxRecord);
        redirectAttributes.addFlashAttribute("taxRecord", calculatedRecord);
        redirectAttributes.addFlashAttribute("showResult", true);
        return "redirect:/tax";
    }

    @PostMapping("/save")
    public String saveTaxRecord(@ModelAttribute TaxRecord taxRecord, RedirectAttributes redirectAttributes) {
        // Recalculate before saving to ensure data integrity
        TaxRecord finalRecord = taxService.calculateTax(taxRecord);
        taxService.save(finalRecord);
        redirectAttributes.addFlashAttribute("successMessage", "Tax record for year " + finalRecord.getTaxYear() + " has been saved.");
        return "redirect:/tax"; // Or redirect to a list view if you create one
    }
}
