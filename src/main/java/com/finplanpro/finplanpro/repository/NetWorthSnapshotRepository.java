package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NetWorthSnapshotRepository extends JpaRepository<NetWorthSnapshot, Long> {
    List<NetWorthSnapshot> findByUserAndDraftFalseOrderBySnapshotDateDesc(User user);
    Optional<NetWorthSnapshot> findByUserAndDraftTrue(User user);
    void deleteByUser(User user);
}
