package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import com.finplanpro.finplanpro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetirementBasicRepository extends JpaRepository<RetirementBasic, Long> {
    List<RetirementBasic> findByUser(User user);
}
