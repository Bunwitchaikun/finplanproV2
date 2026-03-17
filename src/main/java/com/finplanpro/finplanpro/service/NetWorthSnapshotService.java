package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.NetWorthSnapshot;

import java.util.List;
import java.util.Optional;

public interface NetWorthSnapshotService {
    NetWorthSnapshot save(NetWorthSnapshot snapshot);

    List<NetWorthSnapshot> findSnapshotsByUser();

    Optional<NetWorthSnapshot> findById(Long id);

    void deleteById(Long id);

    void deleteAllByUser();

    void seedDefaultItems(NetWorthSnapshot snapshot);
}
