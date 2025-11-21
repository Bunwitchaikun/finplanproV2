package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.TaxRecord;

import java.util.List;

public interface TaxService {
    TaxRecord calculateTax(TaxRecord taxRecord);
    TaxRecord save(TaxRecord taxRecord);
    List<TaxRecord> findRecordsByUser();
}
