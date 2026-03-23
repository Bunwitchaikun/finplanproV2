package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.NetWorthItem;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.NetWorthSnapshotRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NetWorthSnapshotServiceImpl implements NetWorthSnapshotService {

    private final NetWorthSnapshotRepository snapshotRepository;
    private final UserRepository userRepository;

    public NetWorthSnapshotServiceImpl(NetWorthSnapshotRepository snapshotRepository, UserRepository userRepository) {
        this.snapshotRepository = snapshotRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public NetWorthSnapshot save(NetWorthSnapshot snapshot) {
        User user = getCurrentUser();
        snapshot.setUser(user);

        // Ensure all items have proper snapshot reference
        if (snapshot.getItems() != null) {
            snapshot.getItems().forEach(item -> {
                if (item.getSnapshot() == null) {
                    item.setSnapshot(snapshot);
                }
            });
        }

        // The @PrePersist/@PreUpdate in the entity will handle calculations
        return snapshotRepository.save(snapshot);
    }

    @Override
    public void seedDefaultItems(NetWorthSnapshot snapshot) {
        // Assets
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "เงินฝากออมทรัพย์", "เงินฝากออมทรัพย์", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "พันธบัตรรัฐบาล", "พันธบัตรรัฐบาล", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "กองทุนลดหย่อนภาษี (SSF/RMF/ThaiESG)", "กองทุนลดหย่อนภาษี (SSF/RMF/ThaiESG)", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "กองทุนรวมหุ้น", "กองทุนรวมหุ้น", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "หุ้นไทย", "หุ้นไทย", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "ทองคำ/โลหะมีค่า", "ทองคำ/โลหะมีค่า", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "คริปโตเคอเรนซี/สินทรัพย์ดิจิทัล", "คริปโตเคอเรนซี/สินทรัพย์ดิจิทัล", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));

        // Liabilities
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "บัตรเครดิต", "บัตรเครดิต", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "สินเชื่อส่วนบุคคล", "สินเชื่อส่วนบุคคล", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "หนี้ยืมบุคคล", "หนี้ยืมบุคคล", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "สินเชื่อบ้าน", "สินเชื่อบ้าน", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem(
                "สินเชื่อรถยนต์", "สินเชื่อรถยนต์", com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
    }

    @Override
    public List<NetWorthSnapshot> findSnapshotsByUser() {
        User user = getCurrentUser();
        return snapshotRepository.findByUserAndDraftFalseOrderBySnapshotDateDesc(user);
    }

    @Override
    @Transactional
    public void saveDraft(List<Map<String, Object>> items) {
        User user = getCurrentUser();
        NetWorthSnapshot draft = snapshotRepository.findByUserAndDraftTrue(user)
                .orElseGet(() -> {
                    NetWorthSnapshot d = new NetWorthSnapshot();
                    d.setUser(user);
                    d.setDraft(true);
                    d.setSnapshotDate(LocalDate.now());
                    return d;
                });
        draft.getItems().clear();
        if (items != null) {
            for (Map<String, Object> m : items) {
                NetWorthItem it = new NetWorthItem();
                it.setName(String.valueOf(m.getOrDefault("name", "")));
                it.setCategory(String.valueOf(m.getOrDefault("category", "")));
                Object amt = m.get("amount");
                it.setAmount(amt != null ? new BigDecimal(amt.toString()) : BigDecimal.ZERO);
                Object cf = m.get("cashFlow");
                it.setMonthlyCashFlow(cf != null ? new BigDecimal(cf.toString()) : BigDecimal.ZERO);
                String typeStr = String.valueOf(m.getOrDefault("type", "ASSET"));
                it.setType("LIABILITY".equals(typeStr) ? NetWorthItem.ItemType.LIABILITY : NetWorthItem.ItemType.ASSET);
                draft.addItem(it);
            }
        }
        snapshotRepository.save(draft);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NetWorthItem> getDraftItems() {
        User user = getCurrentUser();
        return snapshotRepository.findByUserAndDraftTrue(user)
                .map(snap -> {
                    List<NetWorthItem> list = new java.util.ArrayList<>(snap.getItems());
                    return list;
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<NetWorthSnapshot> findById(Long id) {
        return snapshotRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = getCurrentUser();
        snapshotRepository.findById(id).ifPresent(snapshot -> {
            if (!snapshot.getUser().equals(user)) {
                throw new SecurityException("User not authorized to delete this snapshot.");
            }
            snapshotRepository.deleteById(id);
        });
    }

    @Override
    @Transactional
    public void deleteAllByUser() {
        User user = getCurrentUser();
        snapshotRepository.deleteByUser(user);
    }

    @Override
    @Transactional
    public void updateMeta(Long id, LocalDate date, String time, String name) {
        User user = getCurrentUser();
        NetWorthSnapshot snap = snapshotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Snapshot not found"));
        if (!snap.getUser().equals(user)) {
            throw new SecurityException("Unauthorized");
        }
        snap.setSnapshotDate(date);
        snap.setSnapshotTime(time);
        snap.setSnapshotName(name);
        snapshotRepository.save(snap);
    }

    @Override
    @Transactional
    public void deleteByIds(java.util.List<Long> ids) {
        User user = getCurrentUser();
        ids.forEach(id -> snapshotRepository.findById(id).ifPresent(snap -> {
            if (snap.getUser().equals(user)) {
                snapshotRepository.deleteById(id);
            }
        }));
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
