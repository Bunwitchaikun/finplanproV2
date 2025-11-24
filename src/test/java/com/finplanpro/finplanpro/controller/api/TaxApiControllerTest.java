package com.finplanpro.finplanpro.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finplanpro.finplanpro.dto.TaxRequestDTO;
import com.finplanpro.finplanpro.dto.TaxResultDTO;
import com.finplanpro.finplanpro.service.TaxCalculationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaxApiController.class)
class TaxApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // ใช้สำหรับแปลง Object เป็น JSON

    @MockBean
    private TaxCalculationService taxCalculationService;

    @Test
    @WithMockUser
    @DisplayName("POST /api/tax/calculate ควรคำนวณและคืนค่าภาษีเป็น JSON")
    void testCalculateTaxApi() throws Exception {
        System.out.println("--- RUNNING: [ApiTest] testCalculateTaxApi ---");

        // 1. Arrange
        // สร้าง Request DTO
        TaxRequestDTO request = new TaxRequestDTO();
        request.setMonthlyIncome(100000.0);
        request.setSpouse(true);
        request.setChildren(2);
        request.setParents(2);
        request.setLifeInsurance(true);
        request.setSsf(true);
        request.setRmf(true);
        request.setProvidentFund(true);
        request.setSocialSecurity(true);

        // สร้าง Result DTO ที่คาดหวังว่าจะได้จาก Service
        TaxResultDTO mockResult = new TaxResultDTO();
        mockResult.setNetIncome(new BigDecimal("251000.00"));
        mockResult.setTaxAmount(new BigDecimal("5050.00"));

        // กำหนดให้เมื่อ service ถูกเรียก ให้ return mockResult
        when(taxCalculationService.calculateTax(any(TaxRequestDTO.class))).thenReturn(mockResult);

        System.out.println("INPUT (JSON): " + objectMapper.writeValueAsString(request));

        // 2. Act & 3. Assert
        mockMvc.perform(post("/api/tax/calculate")
                        .with(csrf()) // ต้องมี CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // แปลง Object เป็น JSON String
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.netIncome").value(251000.00))
                .andExpect(jsonPath("$.taxAmount").value(5050.00));

        System.out.println("✅ SUCCESS: API returned correct tax calculation JSON.");
        System.out.println("--- FINISHED: [ApiTest] testCalculateTaxApi ---\n");
    }
}
