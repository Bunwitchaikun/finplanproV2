package com.finplanpro.finplanpro.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Step5HavesDTO {
    private BigDecimal providentFund;
    private BigDecimal rmf;
    private BigDecimal ssf;
    private BigDecimal annuity;
    private BigDecimal stocks;
    private BigDecimal gold;
    private BigDecimal otherAssets;
    private Double growthRate; // optional expected growth before retirement

    // computed
    private BigDecimal futureValueTotal;
}
