package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.RetirementBasicRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class RetirementBasicServiceImpl implements RetirementBasicService {

    private final RetirementBasicRepository retirementBasicRepository;
    private final UserRepository userRepository;

    public RetirementBasicServiceImpl(RetirementBasicRepository retirementBasicRepository, UserRepository userRepository) {
        this.retirementBasicRepository = retirementBasicRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RetirementBasic calculateAndSave(RetirementBasic plan) {
        User user = getCurrentUser();
        plan.setUser(user);

        RetirementBasic calculated = performCalculation(plan);
        return retirementBasicRepository.save(calculated);
    }

    @Override
    public List<RetirementBasic> findPlansByUser() {
        User user = getCurrentUser();
        List<RetirementBasic> plans = retirementBasicRepository.findByUser(user);
        plans.forEach(this::populateDerivedFields);
        return plans;
    }
    
    @Override
    public Optional<RetirementBasic> findById(Long id) {
        return retirementBasicRepository.findById(id)
                .map(this::populateDerivedFields);
    }

    @Override
    public void deleteById(Long id) {
        User user = getCurrentUser();
        retirementBasicRepository.findById(id).ifPresent(plan -> {
            if (plan.getUser().equals(user)) {
                retirementBasicRepository.deleteById(id);
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

    private RetirementBasic performCalculation(RetirementBasic plan) {
        if (plan.getMonthlyExpense() == null) {
            throw new IllegalArgumentException("กรุณากรอกค่าใช้จ่ายรายเดือน");
        }
        if (plan.getCurrentAge() == null || plan.getRetireAge() == null || plan.getLifeExpectancy() == null) {
            throw new IllegalArgumentException("กรุณากรอกอายุปัจจุบัน อายุเกษียณ และอายุขัย");
        }
        int yearsToRetirement = plan.getRetireAge() - plan.getCurrentAge();
        int yearsInRetirement = plan.getLifeExpectancy() - plan.getRetireAge();

        if (yearsToRetirement <= 0) {
            throw new IllegalArgumentException("อายุเกษียณต้องมากกว่าอายุปัจจุบัน");
        }
        if (yearsInRetirement <= 0) {
            throw new IllegalArgumentException("อายุขัยต้องมากกว่าอายุเกษียณ");
        }

        double inflationRate = plan.getInflationRate() / 100.0;
        double postReturn = plan.getPostRetireReturn() / 100.0;
        double preReturnMonthly = plan.getPreRetireReturn() / 100.0 / 12.0;

        double inflationFactor = Math.pow(1 + inflationRate, yearsToRetirement);
        BigDecimal retirementMonthlyExpense = plan.getMonthlyExpense()
                .multiply(BigDecimal.valueOf(inflationFactor));
        BigDecimal annualExpenseAtRetirement = retirementMonthlyExpense.multiply(BigDecimal.valueOf(12));

        double realReturnRate = ((1 + postReturn) / (1 + inflationRate)) - 1;

        BigDecimal totalFundsNeeded;
        if (Math.abs(realReturnRate) < 1e-9) {
            totalFundsNeeded = annualExpenseAtRetirement.multiply(BigDecimal.valueOf(yearsInRetirement));
        } else {
            double pvFactor = (1 - Math.pow(1 + realReturnRate, -yearsInRetirement)) / realReturnRate;
            totalFundsNeeded = annualExpenseAtRetirement.multiply(BigDecimal.valueOf(pvFactor));
        }

        int monthsToRetirement = yearsToRetirement * 12;
        BigDecimal requiredMonthlyInvestment;
        if (monthsToRetirement <= 0) {
            requiredMonthlyInvestment = BigDecimal.ZERO;
        } else if (Math.abs(preReturnMonthly) < 1e-9) {
            requiredMonthlyInvestment = totalFundsNeeded.divide(BigDecimal.valueOf(monthsToRetirement), 2, RoundingMode.HALF_UP);
        } else {
            double factor = Math.pow(1 + preReturnMonthly, monthsToRetirement) - 1;
            if (factor <= 0) {
                requiredMonthlyInvestment = totalFundsNeeded.divide(BigDecimal.valueOf(monthsToRetirement), 2, RoundingMode.HALF_UP);
            } else {
                double pmt = totalFundsNeeded.doubleValue() * preReturnMonthly / factor;
                requiredMonthlyInvestment = BigDecimal.valueOf(pmt);
            }
        }

        plan.setRetirementMonthlyExpense(retirementMonthlyExpense.setScale(2, RoundingMode.HALF_UP));
        plan.setAnnualExpenseAtRetirement(annualExpenseAtRetirement.setScale(2, RoundingMode.HALF_UP));
        plan.setRequiredMonthlyInvestment(requiredMonthlyInvestment.setScale(2, RoundingMode.HALF_UP));
        plan.setTotalFundsNeeded(totalFundsNeeded.setScale(2, RoundingMode.HALF_UP));
        plan.setYearsToRetirement(yearsToRetirement);
        return plan;
    }

    private RetirementBasic populateDerivedFields(RetirementBasic plan) {
        try {
            return performCalculation(plan);
        } catch (IllegalArgumentException ex) {
            // หากข้อมูลไม่ครบ/ไม่สมเหตุสมผล ให้คงค่าเดิมไว้
            return plan;
        }
    }
}
