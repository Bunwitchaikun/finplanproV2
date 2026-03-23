package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.NetWorthItem;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface NetWorthSnapshotService {
    NetWorthSnapshot save(NetWorthSnapshot snapshot);

    List<NetWorthSnapshot> findSnapshotsByUser();

    Optional<NetWorthSnapshot> findById(Long id);

    void deleteById(Long id);

    void deleteAllByUser();

    void seedDefaultItems(NetWorthSnapshot snapshot);

    void updateMeta(Long id, LocalDate date, String time, String name);

    void deleteByIds(List<Long> ids);

    /** Save current working items to the user's draft snapshot (DB-persisted across logins). */
    void saveDraft(List<Map<String, Object>> items);

    /** Return items from the user's draft snapshot, or empty list if none. */
    List<NetWorthItem> getDraftItems();
}
