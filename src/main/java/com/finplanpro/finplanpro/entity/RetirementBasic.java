package com.finplanpro.finplanpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "retirement_basic")
public class RetirementBasic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private int currentAge;
    private int retireAge;
    private double monthlyExpense;
    private double inflationRate;
    private int lifeExpectancy;
    private double preRetireReturn;
    private double postRetireReturn;
}
