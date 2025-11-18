package com.finplanpro.finplanpro.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_roles")
public class UserRole {

    @EmbeddedId
    private UserRoleKey id;

}
