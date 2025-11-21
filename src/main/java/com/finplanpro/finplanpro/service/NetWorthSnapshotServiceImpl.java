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
    public NetWorthSnapshot save(NetWorthSnapshot snapshot) {
        User user = getCurrentUser();
        snapshot.setUser(user);
        // The @PrePersist/@PreUpdate in the entity will handle calculations
        return snapshotRepository.save(snapshot);
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
