package com.finplanpro.finplanpro.service.calculation;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * คลาสสำหรับสูตรคำนวณทางการเงินพื้นฐาน
 */
@Component
public class FinancialCalculator {

    // เพิ่ม SCALE เพื่อความแม่นยำในการคำนวณ
    public static final int SCALE = 32; // เพิ่มจาก 16 เป็น 32
    public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    public static final MathContext MC = new MathContext(SCALE, ROUNDING_MODE);

    public BigDecimal calculateFV(BigDecimal presentValue, BigDecimal rate, int periods) {
        if (periods <= 0) return presentValue;
        return presentValue.multiply(BigDecimal.ONE.add(rate).pow(periods, MC)); // ใช้ MC ใน pow
    }

    public BigDecimal calculatePV(BigDecimal pmt, BigDecimal rate, int periods) {
        if (rate.compareTo(BigDecimal.ZERO) == 0) {
            return pmt.multiply(BigDecimal.valueOf(periods));
        }
        BigDecimal r = rate;
        BigDecimal n = BigDecimal.valueOf(periods);
        
        BigDecimal onePlusR = BigDecimal.ONE.add(r);
        BigDecimal pvFactorNumerator = BigDecimal.ONE.subtract(onePlusR.pow(-n.intValue(), MC));
        BigDecimal pvFactor = pvFactorNumerator.divide(r, SCALE, ROUNDING_MODE);
        
        return pmt.multiply(pvFactor);
    }

    public BigDecimal calculatePMT(BigDecimal futureValue, BigDecimal rate, int periods) {
        if (rate.compareTo(BigDecimal.ZERO) == 0) {
            return futureValue.divide(BigDecimal.valueOf(periods), SCALE, ROUNDING_MODE);
        }
        BigDecimal r = rate;
        BigDecimal n = BigDecimal.valueOf(periods);

        BigDecimal onePlusR = BigDecimal.ONE.add(r);
        BigDecimal denominator = onePlusR.pow(n.intValue(), MC).subtract(BigDecimal.ONE); // ใช้ MC ใน pow
        
        if (denominator.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return futureValue.multiply(r).divide(denominator, SCALE, ROUNDING_MODE);
    }

    /**
     * Calculates the periodic payment (PMT) required to reach a future value (FV)
     * given a present value (PV), interest rate, and number of periods.
     * Assumes payments are made at the end of each period.
     *
     * @param fv The future value desired.
     * @param pv The present value (initial investment).
     * @param annualRate The annual interest rate (e.g., 0.05 for 5%).
     * @param years The number of years.
     * @param paymentsPerYear The number of payments per year (e.g., 12 for monthly).
     * @return The periodic payment.
     */
    public BigDecimal calculatePMTWithPV(BigDecimal fv, BigDecimal pv, BigDecimal annualRate, int years, int paymentsPerYear) {
        if (paymentsPerYear <= 0) {
            throw new IllegalArgumentException("Payments per year must be greater than zero.");
        }

        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(paymentsPerYear), SCALE, ROUNDING_MODE);
        int totalPeriods = years * paymentsPerYear;

        if (totalPeriods <= 0) {
            return fv.compareTo(pv) > 0 ? fv.subtract(pv) : BigDecimal.ZERO;
        }

        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal remainingAmount = fv.subtract(pv);
            return remainingAmount.divide(BigDecimal.valueOf(totalPeriods), SCALE, ROUNDING_MODE);
        }

        // Calculate the future value of the present value
        BigDecimal onePlusMonthlyRate = BigDecimal.ONE.add(monthlyRate);
        BigDecimal fvOfPv = pv.multiply(onePlusMonthlyRate.pow(totalPeriods, MC)); // ใช้ MC ใน pow

        // The remaining future value that needs to be covered by periodic payments
        BigDecimal remainingFvToCover = fv.subtract(fvOfPv);

        // If remainingFvToCover is negative, it means PV already exceeds FV, so no PMT needed (or even withdrawal possible)
        if (remainingFvToCover.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        // Now calculate PMT for this remaining future value, starting from zero
        // This is equivalent to calling calculatePMT(remainingFvToCover, monthlyRate, totalPeriods)
        BigDecimal numerator = remainingFvToCover.multiply(monthlyRate);
        BigDecimal denominator = onePlusMonthlyRate.pow(totalPeriods, MC).subtract(BigDecimal.ONE); // ใช้ MC ใน pow

        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return numerator.divide(denominator, SCALE, ROUNDING_MODE);
    }
}
