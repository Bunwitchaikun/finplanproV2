package com.finplanpro.finplanpro.controller;

import com.finplanpro.finplanpro.dto.InsuranceSummaryDto;
import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.entity.TaxRecord;
import com.finplanpro.finplanpro.service.InsurancePolicyService;
import com.finplanpro.finplanpro.service.NetWorthSnapshotService;
import com.finplanpro.finplanpro.service.RetirementAdvancedService;
import com.finplanpro.finplanpro.service.TaxRecordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NetWorthSnapshotService netWorthSnapshotService;

    @MockBean
    private RetirementAdvancedService retirementAdvancedService;

    @MockBean
    private InsurancePolicyService insurancePolicyService;

    @MockBean
    private TaxRecordService taxRecordService;

    @Test
    @DisplayName("GET /dashboard ควรแสดงหน้า Dashboard พร้อมข้อมูลสรุปสำหรับผู้ใช้ที่ล็อกอิน")
    @WithMockUser(username = "testuser")
    void testShowDashboard_Authenticated() throws Exception {
        System.out.println("--- RUNNING: [IntegrationTest] testShowDashboard_Authenticated ---");

        // 1. Arrange - กำหนดพฤติกรรมของ Mock Services
        NetWorthSnapshot snapshot = new NetWorthSnapshot();
        snapshot.setNetWorth(new BigDecimal("123456.78"));
        snapshot.setSnapshotDate(LocalDate.now());
        when(netWorthSnapshotService.findSnapshotsByUser()).thenReturn(List.of(snapshot));

        when(retirementAdvancedService.findAllPlansByUser("testuser")).thenReturn(Collections.emptyList());

        // สร้าง DTO พร้อมค่าเริ่มต้นทั้งหมดเพื่อป้องกัน NullPointerException
        InsuranceSummaryDto insuranceSummary = new InsuranceSummaryDto(
                BigDecimal.valueOf(1000000), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.valueOf(20000), BigDecimal.valueOf(5000),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
        );
        when(insurancePolicyService.getSummaryForCurrentUser()).thenReturn(insuranceSummary);

        TaxRecord taxRecord = new TaxRecord();
        taxRecord.setTaxPayable(new BigDecimal("5050.00"));
        when(taxRecordService.findRecordsByUser()).thenReturn(List.of(taxRecord));

        System.out.println("INPUT: GET request to /dashboard from authenticated user 'testuser'");

        // 2. Act & 3. Assert
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists(
                        "latestNetWorth",
                        "retirementFundsNeeded",
                        "insuranceSummary",
                        "latestTaxPayable",
                        "netWorthLabels",
                        "netWorthData"
                ))
                .andExpect(model().attribute("latestNetWorth", new BigDecimal("123456.78")))
                .andExpect(model().attribute("latestTaxPayable", new BigDecimal("5050.00")));

        System.out.println("✅ SUCCESS: Correct view and model attributes are returned.");
        System.out.println("--- FINISHED: [IntegrationTest] testShowDashboard_Authenticated ---\n");
    }

    @Test
    @DisplayName("[Access Control] GET /dashboard โดยไม่ล็อกอิน ควรได้รับ 401 Unauthorized")
    void testShowDashboard_Unauthenticated() throws Exception {
        System.out.println("--- RUNNING: [SecurityTest] testShowDashboard_Unauthenticated ---");
        System.out.println("INPUT: GET request to /dashboard from unauthenticated user");

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isUnauthorized()); // ตรวจสอบว่าได้ HTTP 401 Unauthorized

        System.out.println("✅ SUCCESS: Returned 401 Unauthorized as expected.");
        System.out.println("--- FINISHED: [SecurityTest] testShowDashboard_Unauthenticated ---\n");
    }
}
