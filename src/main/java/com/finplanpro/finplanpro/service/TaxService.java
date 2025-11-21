package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.TaxRequestDTO;
import com.finplanpro.finplanpro.entity.TaxRecord;

import java.util.List;

public interface TaxService {
    TaxRecord calculateTax(TaxRequestDTO taxRequest); // Changed parameter to DTO
    TaxRecord save(TaxRecord taxRecord);
    List<TaxRecord> findRecordsByUser();
}
