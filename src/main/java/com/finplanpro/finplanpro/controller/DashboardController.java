package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.dto.InsuranceSummaryDto;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.entity.RetirementAdvanced;
import com.finplanpro.finplanpro.entity.TaxRecord;
import com.finplanpro.finplanpro.service.InsurancePolicyService;
import com.finplanpro.finplanpro.service.NetWorthSnapshotService;
import com.finplanpro.finplanpro.service.RetirementAdvancedService;
import com.finplanpro.finplanpro.service.TaxRecordService;
import org.springframework.security.core.Authentication;
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
        private final RetirementAdvancedService retirementAdvancedService;
        private final InsurancePolicyService insurancePolicyService;
        private final TaxRecordService taxRecordService;

        public DashboardController(NetWorthSnapshotService netWorthSnapshotService,
                        RetirementAdvancedService retirementAdvancedService,
                        InsurancePolicyService insurancePolicyService,
                        TaxRecordService taxRecordService) {
                this.netWorthSnapshotService = netWorthSnapshotService;
                this.retirementAdvancedService = retirementAdvancedService;
                this.insurancePolicyService = insurancePolicyService;
                this.taxRecordService = taxRecordService;
        }

        @GetMapping("/dashboard")
        public String showDashboard(Model model, Authentication authentication) {
                model.addAttribute("activeMenu", "dashboard");

                // --- Summary Cards Data ---
                List<NetWorthSnapshot> snapshots = netWorthSnapshotService.findSnapshotsByUser();
                BigDecimal latestNetWorth = snapshots.isEmpty() ? BigDecimal.ZERO : snapshots.get(0).getNetWorth();
                BigDecimal latestTotalAssets = snapshots.isEmpty() ? BigDecimal.ZERO
                                : snapshots.get(0).getTotalAssets();
                BigDecimal latestTotalLiabilities = snapshots.isEmpty() ? BigDecimal.ZERO
                                : snapshots.get(0).getTotalLiabilities();
                model.addAttribute("latestNetWorth", latestNetWorth);
                model.addAttribute("latestTotalAssets", latestTotalAssets);
                model.addAttribute("latestTotalLiabilities", latestTotalLiabilities);

                // Get retirement goal from Advanced planner (latest plan)
                BigDecimal retirementFundsNeeded = BigDecimal.ZERO;
                if (authentication != null) {
                        List<RetirementAdvanced> advancedPlans = retirementAdvancedService
                                        .findAllPlansByUser(authentication.getName());
                        if (!advancedPlans.isEmpty()) {
                                retirementFundsNeeded = advancedPlans.get(0).getTotalFundsNeeded();
                                if (retirementFundsNeeded == null) {
                                        retirementFundsNeeded = BigDecimal.ZERO;
                                }
                        }
                }
                model.addAttribute("retirementFundsNeeded", retirementFundsNeeded);

                InsuranceSummaryDto insuranceSummary = insurancePolicyService.getSummaryForCurrentUser();
                model.addAttribute("insuranceSummary", insuranceSummary);

                List<TaxRecord> taxRecords = taxRecordService.findRecordsByUser();
                BigDecimal latestTaxPayable = taxRecords.isEmpty() ? BigDecimal.ZERO
                                : taxRecords.get(0).getTaxPayable();
                BigDecimal latestNetIncome = taxRecords.isEmpty() ? BigDecimal.ZERO : taxRecords.get(0).getNetIncome();
                model.addAttribute("latestTaxPayable", latestTaxPayable);
                model.addAttribute("latestNetIncome", latestNetIncome);

                // --- Charts Data ---

                // Net Worth Trend Chart
                Collections.reverse(snapshots); // Reverse to show oldest first for chart
                List<String> netWorthLabels = snapshots.stream()
                                .map(s -> s.getSnapshotDate().format(DateTimeFormatter.ofPattern("MMM yyyy")))
                                .collect(Collectors.toList());
                List<BigDecimal> netWorthData = snapshots.stream()
                                .map(NetWorthSnapshot::getNetWorth)
                                .collect(Collectors.toList());
                // เพิ่มข้อมูล Assets และ Liabilities สำหรับกราฟ
                List<BigDecimal> totalAssetsData = snapshots.stream()
                                .map(NetWorthSnapshot::getTotalAssets)
                                .collect(Collectors.toList());
                List<BigDecimal> totalLiabilitiesData = snapshots.stream()
                                .map(NetWorthSnapshot::getTotalLiabilities)
                                .collect(Collectors.toList());

                model.addAttribute("netWorthLabels", netWorthLabels);
                model.addAttribute("netWorthData", netWorthData);
                model.addAttribute("totalAssetsData", totalAssetsData); // เพิ่ม
                model.addAttribute("totalLiabilitiesData", totalLiabilitiesData); // เพิ่ม

                // Insurance Coverage Chart
                if (insuranceSummary != null) {
                        model.addAttribute("insuranceLabels",
                                        List.of("Life", "Disability", "Critical Illness", "Health", "Accident"));
                        model.addAttribute("insuranceData", List.of(
                                        insuranceSummary.getTotalLifeCoverage(),
                                        insuranceSummary.getTotalDisabilityCoverage(),
                                        insuranceSummary.getTotalCriticalIllnessCoverage(),
                                        insuranceSummary.getTotalHealthCareRoom()
                                                        .add(insuranceSummary.getTotalHealthCarePerVisit()), // Combine
                                                                                                             // health
                                        insuranceSummary.getAccidentCoverage()));
                }

                return "dashboard";
        }
}
