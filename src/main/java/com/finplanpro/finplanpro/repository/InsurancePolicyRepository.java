package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.dto.InsuranceSummaryDto;
import com.finplanpro.finplanpro.entity.InsurancePolicy;
import com.finplanpro.finplanpro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {
    List<InsurancePolicy> findByUser(User user);
    boolean existsByPolicyNumberAndUser(String policyNumber, User user);
    boolean existsByPolicyNumberAndUserAndIdNot(String policyNumber, User user, Long id);
    
    @Query("SELECT new com.finplanpro.finplanpro.dto.InsuranceSummaryDto(" +
           "COALESCE(SUM(i.lifeCoverage), 0), " +
           "COALESCE(SUM(i.disabilityCoverage), 0), " +
           "COALESCE(SUM(i.healthCareRoom), 0), " +
           "COALESCE(SUM(i.healthCarePerVisit), 0), " +
           "COALESCE(SUM(i.opdPerVisit), 0), " +
           "COALESCE(SUM(i.compensationPerDay), 0), " +
           "COALESCE(SUM(i.mainPremium), 0), " +
           "COALESCE(SUM(i.riderPremium), 0), " +
           "COALESCE(SUM(i.accidentCoverage), 0), " +
           "COALESCE(SUM(i.savingsReturn), 0), " +
           "COALESCE(SUM(i.pension), 0), " +
           "COALESCE(SUM(i.unitLinkedBenefits), 0), " +
           "COALESCE(SUM(i.earlyMidCriticalIllness), 0), " +
           "COALESCE(SUM(i.severeCriticalIllness), 0), " +
           "COALESCE(SUM(i.partialAccidentCompensation), 0)) " +
           "FROM InsurancePolicy i WHERE i.user = :user")
    InsuranceSummaryDto getSummaryByUser(User user);
}
