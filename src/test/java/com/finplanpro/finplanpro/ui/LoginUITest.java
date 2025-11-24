package com.finplanpro.finplanpro.ui;

import com.finplanpro.finplanpro.AbstractIntegrationTest; // เพิ่ม import นี้
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginUITest extends AbstractIntegrationTest { // เพิ่ม extends

    // ... โค้ดที่เหลือเหมือนเดิม ...
    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private String baseUrl;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        baseUrl = "http://localhost:" + port;
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("[UI Test] ควรเปิดหน้า Login และมี Title ที่ถูกต้อง")
    void testLoginPageTitle() {
        System.out.println("--- RUNNING: [UITest] testLoginPageTitle ---");
        String loginUrl = baseUrl + "/login";
        System.out.println("Navigating to: " + loginUrl);
        driver.get(loginUrl);
        String expectedTitle = "FinPlanPro - Login";
        String actualTitle = driver.getTitle();
        System.out.println("Actual Page Title: " + actualTitle);
        assertEquals(expectedTitle, actualTitle, "Page title should be correct.");
        System.out.println("✅ SUCCESS: Page title is correct.");
        System.out.println("--- FINISHED: [UITest] testLoginPageTitle ---\n");
    }
}
