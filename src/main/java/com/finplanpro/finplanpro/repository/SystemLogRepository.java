package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    Page<SystemLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
