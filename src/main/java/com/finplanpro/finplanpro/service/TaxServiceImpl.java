package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.TaxRecord;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.TaxRecordRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class TaxServiceImpl implements TaxService {

    private final TaxRecordRepository taxRecordRepository;
    private final UserRepository userRepository;

    public TaxServiceImpl(TaxRecordRepository taxRecordRepository, UserRepository userRepository) {
        this.taxRecordRepository = taxRecordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaxRecord calculateTax(TaxRecord record) {
        // 1. Calculate Total Income
        BigDecimal annualIncome = record.getMonthlyIncome().multiply(BigDecimal.valueOf(12)).add(record.getOtherAnnualIncome());
        record.setTotalIncome(annualIncome);

        // 2. Calculate Standard Expense (50% of income, max 100,000)
        BigDecimal standardExpense = annualIncome.multiply(BigDecimal.valueOf(0.5));
        if (standardExpense.compareTo(new BigDecimal("100000")) > 0) {
            standardExpense = new BigDecimal("100000");
        }

        // 3. Calculate Total Deductions
        BigDecimal totalDeduction = BigDecimal.ZERO;
        totalDeduction = totalDeduction.add(new BigDecimal("60000")); // Personal
        if (record.isHasSpouse()) {
            totalDeduction = totalDeduction.add(new BigDecimal("60000")); // Spouse
        }
        totalDeduction = totalDeduction.add(new BigDecimal("30000").multiply(BigDecimal.valueOf(record.getChildrenCount()))); // Children
        totalDeduction = totalDeduction.add(new BigDecimal("30000").multiply(BigDecimal.valueOf(record.getParentCount()))); // Parents
        totalDeduction = totalDeduction.add(new BigDecimal("60000").multiply(BigDecimal.valueOf(record.getDisabledCareCount()))); // Disabled Person Care

        // Insurance & Funds (with caps)
        totalDeduction = totalDeduction.add(record.getLifeInsurancePremium().min(new BigDecimal("100000")));
        totalDeduction = totalDeduction.add(record.getHealthInsurancePremium().min(new BigDecimal("25000")));
        totalDeduction = totalDeduction.add(record.getParentHealthInsurancePremium().min(new BigDecimal("15000")));
        totalDeduction = totalDeduction.add(record.getPensionInsurancePremium().min(new BigDecimal("200000")));
        totalDeduction = totalDeduction.add(record.getProvidentFund().min(new BigDecimal("500000")));
        totalDeduction = totalDeduction.add(record.getRmf().min(new BigDecimal("500000")));
        totalDeduction = totalDeduction.add(record.getSsf().min(new BigDecimal("200000")));
        totalDeduction = totalDeduction.add(record.getNac().min(new BigDecimal("13200")));
        
        record.setTotalDeduction(totalDeduction);

        // 4. Calculate Net Taxable Income
        BigDecimal netTaxableIncome = annualIncome.subtract(standardExpense).subtract(totalDeduction);
        if (netTaxableIncome.compareTo(BigDecimal.ZERO) < 0) {
            netTaxableIncome = BigDecimal.ZERO;
        }
        record.setNetTaxableIncome(netTaxableIncome);

        // 5. Calculate Tax Payable (Progressive Rates)
        record.setTaxPayable(calculateProgressiveTax(netTaxableIncome));

        return record;
    }

    @Override
    public TaxRecord save(TaxRecord taxRecord) {
        User user = getCurrentUser();
        taxRecord.setUser(user);
        return taxRecordRepository.save(taxRecord);
    }

    @Override
    public List<TaxRecord> findRecordsByUser() {
        return taxRecordRepository.findByUserOrderByTaxYearDesc(getCurrentUser());
    }

    private BigDecimal calculateProgressiveTax(BigDecimal income) {
        BigDecimal tax = BigDecimal.ZERO;
        
        if (income.compareTo(new BigDecimal("150000")) > 0) {
            tax = tax.add(income.subtract(new BigDecimal("150000")).min(new BigDecimal("150000")).multiply(new BigDecimal("0.05")));
        }
        if (income.compareTo(new BigDecimal("300000")) > 0) {
            tax = tax.add(income.subtract(new BigDecimal("300000")).min(new BigDecimal("200000")).multiply(new BigDecimal("0.10")));
        }
        if (income.compareTo(new BigDecimal("500000")) > 0) {
            tax = tax.add(income.subtract(new BigDecimal("500000")).min(new BigDecimal("250000")).multiply(new BigDecimal("0.15")));
        }
        if (income.compareTo(new BigDecimal("750000")) > 0) {
            tax = tax.add(income.subtract(new BigDecimal("750000")).min(new BigDecimal("250000")).multiply(new BigDecimal("0.20")));
        }
        if (income.compareTo(new BigDecimal("1000000")) > 0) {
            tax = tax.add(income.subtract(new BigDecimal("1000000")).min(new BigDecimal("1000000")).multiply(new BigDecimal("0.25")));
        }
        if (income.compareTo(new BigDecimal("2000000")) > 0) {
            tax = tax.add(income.subtract(new BigDecimal("2000000")).min(new BigDecimal("3000000")).multiply(new BigDecimal("0.30")));
        }
        if (income.compareTo(new BigDecimal("5000000")) > 0) {
            tax = tax.add(income.subtract(new BigDecimal("5000000")).multiply(new BigDecimal("0.35")));
        }
        
        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username);
        if (user == null) {
            user = userRepository.findByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
