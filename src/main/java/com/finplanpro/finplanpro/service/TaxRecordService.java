package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.TaxRequestDTO;
import com.finplanpro.finplanpro.dto.TaxResultDTO;
import com.finplanpro.finplanpro.entity.TaxRecord;

import java.util.List;

public interface TaxRecordService {
    void save(TaxRequestDTO request, TaxResultDTO result);
    List<TaxRecord> findRecordsByUser();
}
