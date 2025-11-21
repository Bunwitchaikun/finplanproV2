package com.finplanpro.finplanpro.dto;

import lombok.Data;

@Data
public class Step2LifeDTO {
    private String healthStatus; // Excellent / Average / Poor
    private Integer lifeExpectancy;
}
