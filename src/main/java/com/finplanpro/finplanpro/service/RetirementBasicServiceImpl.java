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

        // --- Calculation Logic ---
        int yearsToRetirement = plan.getRetireAge() - plan.getCurrentAge();
        int yearsInRetirement = plan.getLifeExpectancy() - plan.getRetireAge();

        // 1. Future Value of monthly expense at retirement
        BigDecimal futureMonthlyExpense = plan.getMonthlyExpense()
                .multiply(BigDecimal.valueOf(Math.pow(1 + (plan.getInflationRate() / 100), yearsToRetirement)));

        // 2. Total funds needed at the start of retirement (using Present Value of an Annuity formula)
        double realReturnRate = ((1 + (plan.getPostRetireReturn() / 100)) / (1 + (plan.getInflationRate() / 100))) - 1;

        BigDecimal totalFunds;
        if (realReturnRate == 0) {
            totalFunds = futureMonthlyExpense.multiply(BigDecimal.valueOf(yearsInRetirement * 12L));
        } else {
            BigDecimal pvFactor = BigDecimal.valueOf(
                (1 - Math.pow(1 + realReturnRate, -yearsInRetirement * 12)) / realReturnRate
            );
            totalFunds = futureMonthlyExpense.multiply(pvFactor);
        }

        plan.setTotalFundsNeeded(totalFunds.setScale(2, RoundingMode.HALF_UP));
        // --- End Calculation Logic ---

        return retirementBasicRepository.save(plan);
    }

    @Override
    public List<RetirementBasic> findPlansByUser() {
        User user = getCurrentUser();
        return retirementBasicRepository.findByUser(user);
    }
    
    @Override
    public Optional<RetirementBasic> findById(Long id) {
        return retirementBasicRepository.findById(id);
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
}
