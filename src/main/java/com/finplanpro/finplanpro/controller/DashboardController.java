package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.dto.InsuranceSummaryDto;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.entity.RetirementBasic;
import com.finplanpro.finplanpro.entity.TaxRecord;
import com.finplanpro.finplanpro.service.InsurancePolicyService;
import com.finplanpro.finplanpro.service.NetWorthSnapshotService;
import com.finplanpro.finplanpro.service.RetirementBasicService;
import com.finplanpro.finplanpro.service.TaxRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final NetWorthSnapshotService netWorthSnapshotService;
    private final RetirementBasicService retirementBasicService;
    private final InsurancePolicyService insurancePolicyService;
    private final TaxRecordService taxRecordService;

    public DashboardController(NetWorthSnapshotService netWorthSnapshotService,
                               RetirementBasicService retirementBasicService,
                               InsurancePolicyService insurancePolicyService,
                               TaxRecordService taxRecordService) {
        this.netWorthSnapshotService = netWorthSnapshotService;
        this.retirementBasicService = retirementBasicService;
        this.insurancePolicyService = insurancePolicyService;
        this.taxRecordService = taxRecordService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("activeMenu", "dashboard");

        // --- Summary Cards Data ---
        List<NetWorthSnapshot> snapshots = netWorthSnapshotService.findSnapshotsByUser();
        BigDecimal latestNetWorth = snapshots.isEmpty() ? BigDecimal.ZERO : snapshots.get(0).getNetWorth();
        model.addAttribute("latestNetWorth", latestNetWorth);

        List<RetirementBasic> retirementPlans = retirementBasicService.findPlansByUser();
        BigDecimal retirementFundsNeeded = retirementPlans.isEmpty() ? BigDecimal.ZERO : retirementPlans.get(0).getTotalFundsNeeded();
        model.addAttribute("retirementFundsNeeded", retirementFundsNeeded);

        InsuranceSummaryDto insuranceSummary = insurancePolicyService.getSummaryForCurrentUser();
        model.addAttribute("insuranceSummary", insuranceSummary);

        List<TaxRecord> taxRecords = taxRecordService.findRecordsByUser();
        BigDecimal latestTaxPayable = taxRecords.isEmpty() ? BigDecimal.ZERO : taxRecords.get(0).getTaxPayable();
        model.addAttribute("latestTaxPayable", latestTaxPayable);

        // --- Charts Data ---

        // Net Worth Trend Chart
        Collections.reverse(snapshots); 
        List<String> netWorthLabels = snapshots.stream()
                .map(s -> s.getSnapshotDate().format(DateTimeFormatter.ofPattern("MMM yyyy")))
                .collect(Collectors.toList());
        List<BigDecimal> netWorthData = snapshots.stream()
                .map(NetWorthSnapshot::getNetWorth)
                .collect(Collectors.toList());
        model.addAttribute("netWorthLabels", netWorthLabels);
        model.addAttribute("netWorthData", netWorthData);

        // Insurance Coverage Chart
        if (insuranceSummary != null) {
            model.addAttribute("insuranceLabels", List.of("Life", "Disability", "Critical Illness", "Health", "Accident"));
            model.addAttribute("insuranceData", List.of(
                insuranceSummary.getTotalLifeCoverage(),
                insuranceSummary.getTotalDisabilityCoverage(),
                insuranceSummary.getTotalCriticalIllnessCoverage(),
                insuranceSummary.getTotalHealthCareRoom().add(insuranceSummary.getTotalHealthCarePerVisit()), // Combine health
                insuranceSummary.getAccidentCoverage()
            ));
        }

        return "dashboard";
    }
}
