package com.finplanpro.finplanpro.dto;

import lombok.Data;
import java.time.Year;

@Data
public class TaxRequestDTO {
    private int taxYear = Year.now().getValue();
    private Double monthlyIncome; // Use Double to allow null
    private Double customExpense; // Use Double to allow null
    private String incomeType = "เงินได้จากเงินเดือน โบนัส ค่าล่วงเวลา (Salary)";
    private int children = 0;
    private int childbirth = 0;
    private int parents = 2;
    private boolean spouse = false;
    private boolean disability = false;
    private boolean lifeInsurance = false;
    private boolean healthInsuranceSelf = false;
    private boolean healthInsuranceParents = false;
    private boolean pension = false;
    private boolean providentFund = false;
    private boolean rmf = false;
    private boolean ssf = false;
    private boolean socialSecurity = false; // กอช.
}
