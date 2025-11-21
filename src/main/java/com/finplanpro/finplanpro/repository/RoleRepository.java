package com.finplanpro.finplanpro.repository;

import com.finplanpro.finplanpro.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
