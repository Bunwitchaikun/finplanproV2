package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.RetirementAdvanced;
import java.util.List;
import java.util.Optional;

public interface RetirementAdvancedService {
    RetirementAdvanced save(RetirementAdvanced retirementAdvanced);
    RetirementAdvanced calculate(RetirementAdvanced retirementAdvanced);
    List<RetirementAdvanced> findPlansByUser();
    Optional<RetirementAdvanced> findById(Long id);
    void deleteById(Long id);
}
