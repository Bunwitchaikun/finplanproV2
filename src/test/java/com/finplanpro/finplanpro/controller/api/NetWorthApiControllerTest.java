package com.finplanpro.finplanpro.controller.api;

import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.service.NetWorthSnapshotService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NetWorthApiController.class)
class NetWorthApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NetWorthSnapshotService netWorthSnapshotService;

    @Test
    @WithMockUser
    @DisplayName("GET /api/networth/latest ควรคืนค่า JSON ของ Snapshot ล่าสุด")
    void testGetLatestNetWorth_Success() throws Exception {
        System.out.println("--- RUNNING: [ApiTest] testGetLatestNetWorth_Success ---");

        // 1. Arrange
        NetWorthSnapshot snapshot = new NetWorthSnapshot();
        snapshot.setId(1L);
        snapshot.setSnapshotName("Q4 2024");
        snapshot.setNetWorth(new BigDecimal("550000.00"));
        snapshot.setTotalAssets(new BigDecimal("900000.00"));
        snapshot.setTotalLiabilities(new BigDecimal("350000.00"));
        snapshot.setSnapshotDate(LocalDate.of(2024, 10, 25));

        when(netWorthSnapshotService.findSnapshotsByUser()).thenReturn(List.of(snapshot));

        System.out.println("INPUT: GET request to /api/networth/latest");

        // 2. Act & 3. Assert
        mockMvc.perform(get("/api/networth/latest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.snapshotName").value("Q4 2024"))
                .andExpect(jsonPath("$.netWorth").value(550000.00))
                .andExpect(jsonPath("$.totalAssets").value(900000.00))
                .andExpect(jsonPath("$.totalLiabilities").value(350000.00));

        System.out.println("✅ SUCCESS: Returned correct JSON payload.");
        System.out.println("--- FINISHED: [ApiTest] testGetLatestNetWorth_Success ---\n");
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/networth/latest ควรคืนค่า 204 No Content เมื่อไม่มีข้อมูล")
    void testGetLatestNetWorth_NoData() throws Exception {
        System.out.println("--- RUNNING: [ApiTest] testGetLatestNetWorth_NoData ---");

        // 1. Arrange
        when(netWorthSnapshotService.findSnapshotsByUser()).thenReturn(Collections.emptyList());

        System.out.println("INPUT: GET request to /api/networth/latest when no data exists");

        // 2. Act & 3. Assert
        mockMvc.perform(get("/api/networth/latest"))
                .andExpect(status().isNoContent());

        System.out.println("✅ SUCCESS: Returned 204 No Content as expected.");
        System.out.println("--- FINISHED: [ApiTest] testGetLatestNetWorth_NoData ---\n");
    }
}
