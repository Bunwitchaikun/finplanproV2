package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.InsuranceSummaryDto;
import com.finplanpro.finplanpro.entity.InsurancePolicy;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.InsurancePolicyRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class InsurancePolicyServiceImpl implements InsurancePolicyService {

    private final InsurancePolicyRepository policyRepository;
    private final UserRepository userRepository;

    public InsurancePolicyServiceImpl(InsurancePolicyRepository policyRepository, UserRepository userRepository) {
        this.policyRepository = policyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public InsurancePolicy save(InsurancePolicy policy) {
        User user = getCurrentUser();
        policy.setUser(user);
        return policyRepository.save(policy);
    }

    @Override
    public List<InsurancePolicy> findPoliciesByUser() {
        return policyRepository.findByUser(getCurrentUser());
    }

    @Override
    public Optional<InsurancePolicy> findById(Long id) {
        return policyRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = getCurrentUser();
        policyRepository.findById(id).ifPresent(policy -> {
            if (!policy.getUser().equals(user)) {
                throw new SecurityException("User not authorized to delete this policy.");
            }
            policyRepository.deleteById(id);
        });
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        User user = getCurrentUser();
        List<InsurancePolicy> policiesToDelete = policyRepository.findAllById(ids);
        policiesToDelete.forEach(policy -> {
            if (!policy.getUser().equals(user)) {
                throw new SecurityException("User not authorized to delete one or more selected policies.");
            }
        });
        policyRepository.deleteAllById(ids);
    }

    @Override
    @Transactional
    public void deleteAllByUser() {
        policyRepository.deleteAll(findPoliciesByUser());
    }

    @Override
    public InsuranceSummaryDto getSummaryForCurrentUser() {
        List<InsurancePolicy> policies = findPoliciesByUser();
        
        // Initialize all fields with BigDecimal.ZERO
        BigDecimal lifeCoverage = BigDecimal.ZERO;
        BigDecimal disabilityCoverage = BigDecimal.ZERO;
        BigDecimal accidentCoverage = BigDecimal.ZERO;
        BigDecimal savingsReturn = BigDecimal.ZERO;
        BigDecimal pension = BigDecimal.ZERO;
        BigDecimal unitLinkedBenefits = BigDecimal.ZERO;
        BigDecimal healthCareRoom = BigDecimal.ZERO;
        BigDecimal healthCarePerVisit = BigDecimal.ZERO;
        BigDecimal opdPerVisit = BigDecimal.ZERO;
        BigDecimal compensationPerDay = BigDecimal.ZERO;
        BigDecimal earlyMidCriticalIllness = BigDecimal.ZERO;
        BigDecimal severeCriticalIllness = BigDecimal.ZERO;
        BigDecimal partialAccidentCompensation = BigDecimal.ZERO;
        BigDecimal mainPremium = BigDecimal.ZERO;
        BigDecimal riderPremium = BigDecimal.ZERO;

        // Loop through each policy and add its values to the totals
        for (InsurancePolicy policy : policies) {
            lifeCoverage = lifeCoverage.add(Optional.ofNullable(policy.getLifeCoverage()).orElse(BigDecimal.ZERO));
            disabilityCoverage = disabilityCoverage.add(Optional.ofNullable(policy.getDisabilityCoverage()).orElse(BigDecimal.ZERO));
            accidentCoverage = accidentCoverage.add(Optional.ofNullable(policy.getAccidentCoverage()).orElse(BigDecimal.ZERO));
            savingsReturn = savingsReturn.add(Optional.ofNullable(policy.getSavingsReturn()).orElse(BigDecimal.ZERO));
            pension = pension.add(Optional.ofNullable(policy.getPension()).orElse(BigDecimal.ZERO));
            unitLinkedBenefits = unitLinkedBenefits.add(Optional.ofNullable(policy.getUnitLinkedBenefits()).orElse(BigDecimal.ZERO));
            healthCareRoom = healthCareRoom.add(Optional.ofNullable(policy.getHealthCareRoom()).orElse(BigDecimal.ZERO));
            healthCarePerVisit = healthCarePerVisit.add(Optional.ofNullable(policy.getHealthCarePerVisit()).orElse(BigDecimal.ZERO));
            opdPerVisit = opdPerVisit.add(Optional.ofNullable(policy.getOpdPerVisit()).orElse(BigDecimal.ZERO));
            compensationPerDay = compensationPerDay.add(Optional.ofNullable(policy.getCompensationPerDay()).orElse(BigDecimal.ZERO));
            earlyMidCriticalIllness = earlyMidCriticalIllness.add(Optional.ofNullable(policy.getEarlyMidCriticalIllness()).orElse(BigDecimal.ZERO));
            severeCriticalIllness = severeCriticalIllness.add(Optional.ofNullable(policy.getSevereCriticalIllness()).orElse(BigDecimal.ZERO));
            partialAccidentCompensation = partialAccidentCompensation.add(Optional.ofNullable(policy.getPartialAccidentCompensation()).orElse(BigDecimal.ZERO));
            mainPremium = mainPremium.add(Optional.ofNullable(policy.getMainPremium()).orElse(BigDecimal.ZERO));
            riderPremium = riderPremium.add(Optional.ofNullable(policy.getRiderPremium()).orElse(BigDecimal.ZERO));
        }

        // Create and return the DTO with the calculated totals
        return new InsuranceSummaryDto(
            lifeCoverage, disabilityCoverage, healthCareRoom, healthCarePerVisit, opdPerVisit,
            compensationPerDay, mainPremium, riderPremium, accidentCoverage, savingsReturn,
            pension, unitLinkedBenefits, earlyMidCriticalIllness, severeCriticalIllness, partialAccidentCompensation
        );
    }
    
    @Override
    public boolean isPolicyNumberDuplicate(InsurancePolicy policy) {
        User user = getCurrentUser();
        if (policy.getId() == null) { // New policy
            return policyRepository.existsByPolicyNumberAndUser(policy.getPolicyNumber(), user);
        } else { // Existing policy
            return policyRepository.existsByPolicyNumberAndUserAndIdNot(policy.getPolicyNumber(), user, policy.getId());
        }
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
