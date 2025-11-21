package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.RetirementAdvanced;
import com.finplanpro.finplanpro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetirementAdvancedRepository extends JpaRepository<RetirementAdvanced, Long> {
    List<RetirementAdvanced> findByUser(User user);
}
