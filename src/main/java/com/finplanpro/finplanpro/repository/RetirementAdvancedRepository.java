package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.RetirementAdvanced;
import com.finplanpro.finplanpro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RetirementAdvancedRepository extends JpaRepository<RetirementAdvanced, UUID> {
    List<RetirementAdvanced> findByUser(User user);

    List<RetirementAdvanced> findByUserOrderByIdDesc(User user);

    void deleteByIdInAndUser(List<UUID> ids, User user);

    void deleteByUser(User user);
}
