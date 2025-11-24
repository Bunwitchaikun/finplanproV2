package com.finplanpro.finplanpro.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NetWorthSnapshotTest {

    private NetWorthItem createItem(String name, BigDecimal amount, NetWorthItem.ItemType type) {
        NetWorthItem item = new NetWorthItem(name, type);
        item.setAmount(amount);
        return item;
    }

    @Test
    @DisplayName("คำนวณ Total Assets, Liabilities และ Net Worth ได้อย่างถูกต้อง")
    void testCalculateTotals_HappyPath() {
        System.out.println("--- RUNNING: [D5] testCalculateTotals_HappyPath ---");
        // 1. Arrange
        NetWorthSnapshot snapshot = new NetWorthSnapshot();
        snapshot.addItem(createItem("เงินสด", new BigDecimal("150000"), NetWorthItem.ItemType.ASSET));
        snapshot.addItem(createItem("กองทุนหุ้น", new BigDecimal("500000"), NetWorthItem.ItemType.ASSET));
        snapshot.addItem(createItem("บิตคอย", new BigDecimal("250000"), NetWorthItem.ItemType.ASSET));
        snapshot.addItem(createItem("บัตรเครดิต", new BigDecimal("50000"), NetWorthItem.ItemType.LIABILITY));
        snapshot.addItem(createItem("กู้ซื้อรถ", new BigDecimal("300000"), NetWorthItem.ItemType.LIABILITY));
        System.out.println("INPUT: 3 Assets (150k, 500k, 250k), 2 Liabilities (50k, 300k)");

        // 2. Act
        snapshot.calculateTotals();
        System.out.println("ACTUAL_RESULT: Assets=" + snapshot.getTotalAssets() + ", Liabilities=" + snapshot.getTotalLiabilities() + ", NetWorth=" + snapshot.getNetWorth());

        // 3. Assert
        BigDecimal expectedAssets = new BigDecimal("900000");
        BigDecimal expectedLiabilities = new BigDecimal("350000");
        BigDecimal expectedNetWorth = new BigDecimal("550000");
        System.out.println("EXPECTED_RESULT: Assets=" + expectedAssets + ", Liabilities=" + expectedLiabilities + ", NetWorth=" + expectedNetWorth);

        assertEquals(0, expectedAssets.compareTo(snapshot.getTotalAssets()), "Total Assets should be calculated correctly.");
        assertEquals(0, expectedLiabilities.compareTo(snapshot.getTotalLiabilities()), "Total Liabilities should be calculated correctly.");
        assertEquals(0, expectedNetWorth.compareTo(snapshot.getNetWorth()), "Net Worth should be calculated correctly.");
        System.out.println("✅ SUCCESS: All calculations are correct.");
        System.out.println("--- FINISHED: [D5] testCalculateTotals_HappyPath ---\n");
    }

    @Test
    @DisplayName("คำนวณ Net Worth เมื่อไม่มีรายการทรัพย์สิน")
    void testCalculateTotals_NoAssets() {
        System.out.println("--- RUNNING: [D5] testCalculateTotals_NoAssets ---");
        // 1. Arrange
        NetWorthSnapshot snapshot = new NetWorthSnapshot();
        snapshot.addItem(createItem("บัตรเครดิต", new BigDecimal("50000"), NetWorthItem.ItemType.LIABILITY));
        System.out.println("INPUT: 0 Assets, 1 Liability (50k)");

        // 2. Act
        snapshot.calculateTotals();
        System.out.println("ACTUAL_RESULT: Assets=" + snapshot.getTotalAssets() + ", Liabilities=" + snapshot.getTotalLiabilities() + ", NetWorth=" + snapshot.getNetWorth());

        // 3. Assert
        BigDecimal expectedAssets = BigDecimal.ZERO;
        BigDecimal expectedLiabilities = new BigDecimal("50000");
        BigDecimal expectedNetWorth = new BigDecimal("-50000");
        System.out.println("EXPECTED_RESULT: Assets=" + expectedAssets + ", Liabilities=" + expectedLiabilities + ", NetWorth=" + expectedNetWorth);

        assertEquals(0, expectedAssets.compareTo(snapshot.getTotalAssets()), "Total Assets should be zero.");
        assertEquals(0, expectedLiabilities.compareTo(snapshot.getTotalLiabilities()), "Total Liabilities should be correct.");
        assertEquals(0, expectedNetWorth.compareTo(snapshot.getNetWorth()), "Net Worth should be negative.");
        System.out.println("✅ SUCCESS: All calculations are correct for no-asset case.");
        System.out.println("--- FINISHED: [D5] testCalculateTotals_NoAssets ---\n");
    }

    @Test
    @DisplayName("คำนวณ Net Worth เมื่อมีรายการเป็น null")
    void testCalculateTotals_WithNullAmount() {
        System.out.println("--- RUNNING: [D5] testCalculateTotals_WithNullAmount ---");
        // 1. Arrange
        NetWorthSnapshot snapshot = new NetWorthSnapshot();
        snapshot.addItem(createItem("เงินสด", new BigDecimal("100000"), NetWorthItem.ItemType.ASSET));
        snapshot.addItem(createItem("ทองคำ", null, NetWorthItem.ItemType.ASSET)); // รายการที่มีค่าเป็น null
        snapshot.addItem(createItem("บัตรเครดิต", new BigDecimal("20000"), NetWorthItem.ItemType.LIABILITY));
        System.out.println("INPUT: 2 Assets (100k, null), 1 Liability (20k)");

        // 2. Act
        snapshot.calculateTotals();
        System.out.println("ACTUAL_RESULT: Assets=" + snapshot.getTotalAssets() + ", Liabilities=" + snapshot.getTotalLiabilities() + ", NetWorth=" + snapshot.getNetWorth());

        // 3. Assert
        BigDecimal expectedAssets = new BigDecimal("100000");
        BigDecimal expectedLiabilities = new BigDecimal("20000");
        BigDecimal expectedNetWorth = new BigDecimal("80000");
        System.out.println("EXPECTED_RESULT: Assets=" + expectedAssets + ", Liabilities=" + expectedLiabilities + ", NetWorth=" + expectedNetWorth);

        assertEquals(0, expectedAssets.compareTo(snapshot.getTotalAssets()), "Total Assets should ignore null values.");
        assertEquals(0, expectedLiabilities.compareTo(snapshot.getTotalLiabilities()), "Total Liabilities should be correct.");
        assertEquals(0, expectedNetWorth.compareTo(snapshot.getNetWorth()), "Net Worth should be correct.");
        System.out.println("✅ SUCCESS: Calculation correctly ignores null amounts.");
        System.out.println("--- FINISHED: [D5] testCalculateTotals_WithNullAmount ---\n");
    }
}