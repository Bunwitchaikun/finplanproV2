package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RetirementBasicRepository extends JpaRepository<RetirementBasic, Long> {
    List<RetirementBasic> findByUserId(Long userId);
}
