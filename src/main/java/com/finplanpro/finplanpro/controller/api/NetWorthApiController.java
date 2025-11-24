package com.finplanpro.finplanpro.controller.api;

import com.finplanpro.finplanpro.entity.NetWorthSnapshot;
import com.finplanpro.finplanpro.service.NetWorthSnapshotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/networth")
public class NetWorthApiController {

    private final NetWorthSnapshotService netWorthSnapshotService;

    public NetWorthApiController(NetWorthSnapshotService netWorthSnapshotService) {
        this.netWorthSnapshotService = netWorthSnapshotService;
    }

    @GetMapping("/latest")
    public ResponseEntity<NetWorthSnapshot> getLatestNetWorth() {
        List<NetWorthSnapshot> snapshots = netWorthSnapshotService.findSnapshotsByUser();
        if (snapshots.isEmpty()) {
            return ResponseEntity.noContent().build(); // คืนค่า 204 No Content ถ้าไม่มีข้อมูล
        }
        return ResponseEntity.ok(snapshots.get(0)); // คืนค่า Snapshot ล่าสุดพร้อม Status 200 OK
    }
}
