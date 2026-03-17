package com.finplanpro.finplanpro.service.calculation;

import com.finplanpro.finplanpro.dto.DesignResultDTO;
import com.finplanpro.finplanpro.dto.ScenarioResultDTO;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * คลาสสำหรับจำลองสถานการณ์ (Stress Test)
 */
@Component
public class ScenarioSimulator {

    public List<ScenarioResultDTO> runScenarios(DesignResultDTO baseDesign) {
        List<ScenarioResultDTO> results = new ArrayList<>();

        // Scenario 1: Lower Growth
        results.add(ScenarioResultDTO.builder()
                .scenarioName("ผลตอบแทนต่ำกว่าคาด")
                .success(false) // Placeholder
                .description("หากผลตอบแทนลดลง 2%, เงินทุนของคุณจะขาดไป X บาท")
                .build());

        // Scenario 2: Higher Inflation
        results.add(ScenarioResultDTO.builder()
                .scenarioName("เงินเฟ้อสูงกว่าคาด")
                .success(false) // Placeholder
                .description("หากเงินเฟ้อสูงขึ้น 1%, คุณต้องมีเงินเพิ่มอีก Y บาท")
                .build());

        // Scenario 3: Retire Earlier
        results.add(ScenarioResultDTO.builder()
                .scenarioName("เกษียณเร็วกว่าแผน")
                .success(false) // Placeholder
                .description("หากเกษียณเร็วขึ้น 5 ปี, คุณต้องลงทุนเพิ่มเดือนละ Z บาท")
                .build());

        // Scenario 4: Live Longer
        results.add(ScenarioResultDTO.builder()
                .scenarioName("อายุยืนกว่าที่คาด")
                .success(false) // Placeholder
                .description("หากคุณอายุยืนขึ้น 5 ปี, เงินทุนของคุณจะหมดก่อนเวลา")
                .build());

        return results;
    }
}
