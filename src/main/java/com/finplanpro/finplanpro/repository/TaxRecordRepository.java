package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.TaxRecord;
import com.finplanpro.finplanpro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxRecordRepository extends JpaRepository<TaxRecord, Long> {
    List<TaxRecord> findByUserOrderByTaxYearDesc(User user);
    void deleteByUser(User user);
}
