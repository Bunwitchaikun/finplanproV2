package com.finplanpro.finplanpro.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class Step6DesignDTO {

    // --- Key Inputs ---
    private BigDecimal totalExpensesFv;
    private BigDecimal totalHavesFv;
    private BigDecimal totalExtraIncome;
    private BigDecimal presentValue;
    private int yearsToRetirement;

    // --- Main Calculation Results ---
    private BigDecimal fundingGap;
    private BigDecimal pmtWorstCase;
    private BigDecimal pmtBaseCase;
    private BigDecimal pmtBestCase;

    // --- Chart Data ---
    private List<String> chartLabels; // Years (e.g., "Year 0", "Year 1")
    private List<ChartDataPoint> worstCaseGrowth;
    private List<ChartDataPoint> baseCaseGrowth;
    private List<ChartDataPoint> bestCaseGrowth;

    @Data
    @Builder
    public static class ChartDataPoint {
        private int year;
        private BigDecimal amount;
    }
}
