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
        return policyRepository.getSummaryByUser(getCurrentUser());
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
