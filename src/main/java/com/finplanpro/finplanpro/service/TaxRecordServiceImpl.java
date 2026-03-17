package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.dto.TaxRequestDTO;
import com.finplanpro.finplanpro.dto.TaxResultDTO;
import com.finplanpro.finplanpro.entity.TaxRecord;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.TaxRecordRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaxRecordServiceImpl implements TaxRecordService {

    private final TaxRecordRepository taxRecordRepository;
    private final UserRepository userRepository;

    public TaxRecordServiceImpl(TaxRecordRepository taxRecordRepository, UserRepository userRepository) {
        this.taxRecordRepository = taxRecordRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void save(TaxRequestDTO request, TaxResultDTO result) {
        User user = getCurrentUser();

        TaxRecord record = new TaxRecord();
        record.setUser(user);
        record.setTaxYear(request.getTaxYear());
        record.setAnnualIncome(result.getAnnualIncome());
        record.setExpenseDeduction(result.getTotalExpenseDeduction());
        record.setTotalAllowance(result.getTotalAllowance());
        record.setTotalDeduction(result.getTotalExpenseDeduction().add(result.getTotalAllowance()));
        record.setNetIncome(result.getNetIncome());
        record.setTaxPayable(result.getTaxAmount());

        taxRecordRepository.save(record);
    }

    @Override
    public List<TaxRecord> findRecordsByUser() {
        return taxRecordRepository.findByUserOrderByTaxYearDesc(getCurrentUser());
    }

    @Override
    @Transactional
    public void deleteAllForCurrentUser() {
        taxRecordRepository.deleteByUser(getCurrentUser());
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        // Ensure that the records belong to the current user before deleting
        User currentUser = getCurrentUser();
        List<TaxRecord> records = taxRecordRepository.findAllById(ids);
        for (TaxRecord record : records) {
            if (record.getUser().getId().equals(currentUser.getId())) {
                taxRecordRepository.delete(record);
            }
        }
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username);
        if (user == null) {
            user = userRepository.findByUsername(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
