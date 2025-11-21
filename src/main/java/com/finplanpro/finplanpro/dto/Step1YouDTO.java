package com.finplanpro.finplanpro.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Step1YouDTO {
    private LocalDate dob;
    private String gender;
    private Integer retirementAge;

    // computed
    private Integer currentAge;
    private Integer yearsToRetire;
}
