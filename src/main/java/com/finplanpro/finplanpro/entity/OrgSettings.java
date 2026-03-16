package com.finplanpro.finplanpro.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "org_settings")
@Getter
@Setter
public class OrgSettings {

    @Id
    private Integer id = 1;

    @Column(name = "owner_code", nullable = false)
    private String ownerCode;

    @Column(name = "ceo_code", nullable = false)
    private String ceoCode;

    @Column(name = "admin_code", nullable = false)
    private String adminCode;
}
