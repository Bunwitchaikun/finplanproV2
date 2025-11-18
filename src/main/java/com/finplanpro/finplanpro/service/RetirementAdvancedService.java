package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.RetirementAdvanced;
import com.finplanpro.finplanpro.repository.RetirementAdvancedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RetirementAdvancedService {

    @Autowired
    private RetirementAdvancedRepository retirementAdvancedRepository;

    public RetirementAdvanced save(RetirementAdvanced retirementAdvanced) {
        return retirementAdvancedRepository.save(retirementAdvanced);
    }

    public Optional<RetirementAdvanced> findById(Long id) {
        return retirementAdvancedRepository.findById(id);
    }

    public List<RetirementAdvanced> findByUserId(Long userId) {
        return retirementAdvancedRepository.findByUserId(userId);
    }

    public void deleteById(Long id) {
        retirementAdvancedRepository.deleteById(id);
    }
}
