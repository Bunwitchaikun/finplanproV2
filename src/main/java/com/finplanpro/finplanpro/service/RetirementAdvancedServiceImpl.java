package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.RetirementAdvanced;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.RetirementAdvancedRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class RetirementAdvancedServiceImpl implements RetirementAdvancedService {

    private final RetirementAdvancedRepository retirementAdvancedRepository;
    private final UserRepository userRepository;

    // Assume average inflation and investment returns for calculation
    private static final double AVG_INFLATION_RATE = 3.0;
    private static final double POST_RETIRE_RETURN_RATE = 5.0;

    public RetirementAdvancedServiceImpl(RetirementAdvancedRepository retirementAdvancedRepository, UserRepository userRepository) {
        this.retirementAdvancedRepository = retirementAdvancedRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RetirementAdvanced save(RetirementAdvanced plan) {
        User user = getCurrentUser();
        plan.setUser(user);
        return retirementAdvancedRepository.save(plan);
    }

    @Override
    public RetirementAdvanced calculate(RetirementAdvanced plan) {
        // --- Calculation Logic from Step 1 to 7 ---
        long yearsToRetirement = ChronoUnit.YEARS.between(LocalDate.now(), plan.getDateOfBirth().plusYears(plan.getRetireAge()));
        long yearsInRetirement = plan.getLifeExpectancy() - plan.getRetireAge();

        // 1. Future Value of monthly expense at retirement
        BigDecimal futureMonthlyExpense = plan.getDesiredMonthlyExpense()
                .multiply(BigDecimal.valueOf(Math.pow(1 + (AVG_INFLATION_RATE / 100), yearsToRetirement)));

        // 2. Total funds needed for living expenses (using PV of Annuity)
        double realReturnRate = ((1 + (POST_RETIRE_RETURN_RATE / 100)) / (1 + (AVG_INFLATION_RATE / 100))) - 1;
        
        BigDecimal livingExpensesTotal;
        if (realReturnRate == 0) {
            livingExpensesTotal = futureMonthlyExpense.multiply(BigDecimal.valueOf(yearsInRetirement * 12L));
        } else {
             BigDecimal pvFactor = BigDecimal.valueOf(
                (1 - Math.pow(1 + realReturnRate, -yearsInRetirement * 12)) / realReturnRate
            );
            livingExpensesTotal = futureMonthlyExpense.multiply(pvFactor);
        }

        // 3. Add Future Value of special one-time expense
        BigDecimal futureSpecialExpense = plan.getSpecialExpense()
                .multiply(BigDecimal.valueOf(Math.pow(1 + (AVG_INFLATION_RATE / 100), yearsToRetirement)));

        BigDecimal totalFundsNeeded = livingExpensesTotal.add(futureSpecialExpense);
        plan.setTotalFundsNeeded(totalFundsNeeded.setScale(2, RoundingMode.HALF_UP));

        // 4. Calculate total current assets available for retirement
        BigDecimal totalHaves = plan.getCurrentAssets()
                .add(plan.getRmfSsf())
                .add(plan.getPension())
                .add(plan.getAnnuity());

        // 5. Calculate the gap
        BigDecimal fundGap = totalHaves.subtract(totalFundsNeeded);
        plan.setFundGap(fundGap.setScale(2, RoundingMode.HALF_UP));

        return plan;
    }

    @Override
    public List<RetirementAdvanced> findPlansByUser() {
        User user = getCurrentUser();
        return retirementAdvancedRepository.findByUser(user);
    }

    @Override
    public Optional<RetirementAdvanced> findById(Long id) {
        return retirementAdvancedRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        User user = getCurrentUser();
        retirementAdvancedRepository.findById(id).ifPresent(plan -> {
            if (plan.getUser().equals(user)) {
                retirementAdvancedRepository.deleteById(id);
            } else {
                throw new SecurityException("User not authorized to delete this plan");
            }
        });
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
