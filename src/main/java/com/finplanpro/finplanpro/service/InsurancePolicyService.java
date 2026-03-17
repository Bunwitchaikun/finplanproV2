package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.InsuranceSummaryDto;
import com.finplanpro.finplanpro.entity.InsurancePolicy;

import java.util.List;
import java.util.Optional;

public interface InsurancePolicyService {
    InsurancePolicy save(InsurancePolicy policy);
    List<InsurancePolicy> findPoliciesByUser();
    Optional<InsurancePolicy> findById(Long id);
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    void deleteAllByUser();
    InsuranceSummaryDto getSummaryForCurrentUser();
    boolean isPolicyNumberDuplicate(InsurancePolicy policy);
}
