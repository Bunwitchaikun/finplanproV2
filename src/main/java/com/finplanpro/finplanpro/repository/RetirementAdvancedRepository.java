package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.RetirementAdvanced;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetirementAdvancedRepository extends JpaRepository<RetirementAdvanced, Long> {
    List<RetirementAdvanced> findByUserId(Long userId);
}
