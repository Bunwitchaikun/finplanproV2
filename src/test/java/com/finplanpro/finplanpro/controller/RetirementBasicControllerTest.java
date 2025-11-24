package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.entity.RetirementBasic;
import com.finplanpro.finplanpro.service.RetirementBasicService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get; // เพิ่ม import นี้
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RetirementBasicController.class)
class RetirementBasicControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetirementBasicService retirementBasicService;

    // ... (เทสเดิม 2 อัน) ...
    @Test
    @WithMockUser
    @DisplayName("POST /retirement/basic ควรบันทึกข้อมูลสำเร็จและ Redirect พร้อม Success Message")
    void testCalculateAndSavePlan_Success() throws Exception {
        System.out.println("--- RUNNING: [IntegrationTest] testCalculateAndSavePlan_Success ---");

        // 1. Arrange
        RetirementBasic savedPlan = new RetirementBasic();
        savedPlan.setPlanName("My Test Plan");
        savedPlan.setTotalFundsNeeded(new BigDecimal("20000000.00"));
        savedPlan.setYearsToRetirement(30);
        savedPlan.setLifeExpectancy(90);
        savedPlan.setRetireAge(60);
        savedPlan.setRetirementMonthlyExpense(new BigDecimal("72817.87"));
        savedPlan.setAnnualExpenseAtRetirement(new BigDecimal("873814.44"));
        savedPlan.setRequiredMonthlyInvestment(new BigDecimal("15000.00"));

        when(retirementBasicService.calculateAndSave(any(RetirementBasic.class))).thenReturn(savedPlan);

        System.out.println("INPUT: POST to /retirement/basic with valid form data");

        // 2. Act & 3. Assert
        mockMvc.perform(post("/retirement/basic")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("planName", "My Test Plan")
                        .param("currentAge", "30")
                        .param("retireAge", "60")
                        .param("monthlyExpense", "30000")
                        .param("lifeExpectancy", "90")
                        .param("inflationRate", "3.0")
                        .param("preRetireReturn", "8.0")
                        .param("postRetireReturn", "3.0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/retirement/basic"))
                .andExpect(flash().attributeExists("successMessage"));

        System.out.println("✅ SUCCESS: Redirected to correct URL with success message.");
        System.out.println("--- FINISHED: [IntegrationTest] testCalculateAndSavePlan_Success ---\n");
    }

    @Test
    @WithMockUser
    @DisplayName("POST /retirement/basic ควร Redirect กลับพร้อม Error Message เมื่อข้อมูลไม่ถูกต้อง")
    void testCalculateAndSavePlan_ValidationError() throws Exception {
        System.out.println("--- RUNNING: [IntegrationTest] testCalculateAndSavePlan_ValidationError ---");

        System.out.println("INPUT: POST to /retirement/basic with invalid data (currentAge > retireAge)");

        // 2. Act & 3. Assert
        mockMvc.perform(post("/retirement/basic")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("planName", "Invalid Plan")
                        .param("currentAge", "65")
                        .param("retireAge", "60")
                        .param("monthlyExpense", "30000")
                        .param("lifeExpectancy", "90")
                        .param("inflationRate", "3.0")
                        .param("preRetireReturn", "8.0")
                        .param("postRetireReturn", "3.0")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/retirement/basic"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.newPlan"));

        System.out.println("✅ SUCCESS: Redirected back with validation errors as expected.");
        System.out.println("--- FINISHED: [IntegrationTest] testCalculateAndSavePlan_ValidationError ---\n");
    }

    // --- เทสใหม่ที่เพิ่มเข้ามา ---
    @Test
    @DisplayName("[Access Control] GET /retirement/basic โดยไม่ล็อกอิน ควรได้รับ 401 Unauthorized")
    void testShowRetirementPage_Unauthenticated() throws Exception {
        System.out.println("--- RUNNING: [SecurityTest] testShowRetirementPage_Unauthenticated ---");
        System.out.println("INPUT: GET request to /retirement/basic from unauthenticated user");

        mockMvc.perform(get("/retirement/basic"))
                .andExpect(status().isUnauthorized());

        System.out.println("✅ SUCCESS: Returned 401 Unauthorized as expected.");
        System.out.println("--- FINISHED: [SecurityTest] testShowRetirementPage_Unauthenticated ---\n");
    }

    @Test
    @DisplayName("[Access Control] POST /retirement/basic โดยไม่ล็อกอิน ควรได้รับ 401 Unauthorized")
    void testPostToRetirementPage_Unauthenticated() throws Exception {
        System.out.println("--- RUNNING: [SecurityTest] testPostToRetirementPage_Unauthenticated ---");
        System.out.println("INPUT: POST request to /retirement/basic from unauthenticated user");

        mockMvc.perform(post("/retirement/basic")
                        .with(csrf())) // แม้จะไม่ล็อกอิน ก็ยังต้องส่ง CSRF token ไปด้วย
                .andExpect(status().isUnauthorized());

        System.out.println("✅ SUCCESS: Returned 401 Unauthorized as expected.");
        System.out.println("--- FINISHED: [SecurityTest] testPostToRetirementPage_Unauthenticated ---\n");
    }
}
