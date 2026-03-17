package com.finplanpro.finplanpro.service;

import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.entity.User;
import com.finplanpro.finplanpro.repository.NetWorthSnapshotRepository;
import com.finplanpro.finplanpro.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("เงินสด",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("ตราสารหนี้/พันธบัตร/หุ้นกู้",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("กองทุนสำรองเลี้ยงชีพ RMF/LTF",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("กองทุนหุ้น",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("หุ้นรายตัว",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("ทองคำ",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("บิตคอย",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.ASSET));

        // Liabilities
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("บัตรเครดิต",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("บัตรกดเงินสด",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("สินเชื่อส่วนบุคคล",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("ผ่อนสินค้า",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("หนี้นอกระบบ",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("กู้ซื้อบ้าน",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
        snapshot.addItem(new com.finplanpro.finplanpro.entity.NetWorthItem("กู้ซื้อรถ",
                com.finplanpro.finplanpro.entity.NetWorthItem.ItemType.LIABILITY));
    }

    @Override
    public List<NetWorthSnapshot> findSnapshotsByUser() {
        User user = getCurrentUser();
        return snapshotRepository.findByUserOrderBySnapshotDateDesc(user);
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
