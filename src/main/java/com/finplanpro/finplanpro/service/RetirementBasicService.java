package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import com.finplanpro.finplanpro.repository.RetirementBasicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RetirementBasicService {

    @Autowired
    private RetirementBasicRepository retirementBasicRepository;

    public RetirementBasic save(RetirementBasic retirementBasic) {
        return retirementBasicRepository.save(retirementBasic);
    }

    public Optional<RetirementBasic> findById(Long id) {
        return retirementBasicRepository.findById(id);
    }

    public List<RetirementBasic> findByUserId(Long userId) {
        return retirementBasicRepository.findByUserId(userId);
    }

    public void deleteById(Long id) {
        retirementBasicRepository.deleteById(id);
    }

    public double calculateTotalRetirementFund(RetirementBasic retirementBasic) {
        int yearsToRetirement = retirementBasic.getRetireAge() - retirementBasic.getCurrentAge();
        double futureMonthlyExpense = retirementBasic.getMonthlyExpense() * Math.pow(1 + retirementBasic.getInflationRate() / 100, yearsToRetirement);

        int retirementYears = retirementBasic.getLifeExpectancy() - retirementBasic.getRetireAge();
        double totalRetirementFund = 0;

        for (int i = 0; i < retirementYears * 12; i++) {
            totalRetirementFund += futureMonthlyExpense * Math.pow(1 + retirementBasic.getPostRetireReturn() / 100 / 12, -i);
        }

        return totalRetirementFund;
    }
}
