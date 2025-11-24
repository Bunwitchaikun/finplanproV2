package com.finplanpro.finplanpro.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * End-to-End UI Test for the Login page using Selenium.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // รันแอปพลิเคชันบนพอร์ตว่าง
class LoginUITest {

    @LocalServerPort
    private int port; // พอร์ตที่แอปพลิเคชันรันอยู่

    private static WebDriver driver;
    private String baseUrl;

    @BeforeAll
    static void setupClass() {
        // ตั้งค่า ChromeDriver ให้โดยอัตโนมัติ
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        // สร้าง URL พื้นฐาน
        baseUrl = "http://localhost:" + port;

        // ตั้งค่าให้ Chrome รันแบบไม่แสดงหน้าจอ (headless)
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        // สร้าง Instance ของ WebDriver
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit(); // ปิดเบราว์เซอร์หลังแต่ละเทส
        }
    }

    @Test
    @DisplayName("[UI Test] ควรเปิดหน้า Login และมี Title ที่ถูกต้อง")
    void testLoginPageTitle() {
        System.out.println("--- RUNNING: [UITest] testLoginPageTitle ---");

        // 1. Arrange & Act
        String loginUrl = baseUrl + "/login";
        System.out.println("Navigating to: " + loginUrl);
        driver.get(loginUrl);

        // 2. Assert
        String expectedTitle = "FinPlanPro - Login";
        String actualTitle = driver.getTitle();
        System.out.println("Actual Page Title: " + actualTitle);

        assertEquals(expectedTitle, actualTitle, "Page title should be correct.");
        System.out.println("✅ SUCCESS: Page title is correct.");
        System.out.println("--- FINISHED: [UITest] testLoginPageTitle ---\n");
    }
}
