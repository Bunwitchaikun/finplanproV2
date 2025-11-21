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

    private static final int SCALE = 16;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private static final MathContext MC = new MathContext(SCALE, ROUNDING_MODE);

    public BigDecimal calculateFV(BigDecimal presentValue, BigDecimal rate, int periods) {
        if (periods <= 0) return presentValue;
        return presentValue.multiply(BigDecimal.ONE.add(rate).pow(periods));
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
        BigDecimal denominator = onePlusR.pow(n.intValue()).subtract(BigDecimal.ONE);
        
        if (denominator.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;

        return futureValue.multiply(r).divide(denominator, SCALE, ROUNDING_MODE);
    }
}
