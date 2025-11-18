package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import java.util.List;
import java.util.Optional;

public interface RetirementBasicService {
    RetirementBasic calculateAndSave(RetirementBasic retirementBasic);
    List<RetirementBasic> findPlansByUser();
    Optional<RetirementBasic> findById(Long id);
    void deleteById(Long id);
}
