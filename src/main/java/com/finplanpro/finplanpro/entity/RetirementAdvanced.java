package com.finplanpro.finplanpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "retirement_advanced")
public class RetirementAdvanced {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate dateOfBirth;
    private String gender;
    private int retireAge;
    private String lifestyle;
    private double monthlyCost;
    private double basicCost;
    private double specialCost;
    private double assets;
    private double rmf;
    private double pension;
    private double annuity;
}
