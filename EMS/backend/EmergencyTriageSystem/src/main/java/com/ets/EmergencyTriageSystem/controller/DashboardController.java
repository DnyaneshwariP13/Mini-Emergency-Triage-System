package com.ets.EmergencyTriageSystem.controller;

import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.DashboardStatDTO;
import com.ets.EmergencyTriageSystem.dto.PriorityDistributionDTO;
import com.ets.EmergencyTriageSystem.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<Response<DashboardStatDTO>> getDashboardStats() {
        log.info("Received request for dashboard statistics");
        Response<DashboardStatDTO> response = dashboardService.getDashboardStats();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/priority-distribution")
    public ResponseEntity<Response<PriorityDistributionDTO>> getPriorityDistribution() {
        log.info("Received request for priority distribution chart data");
        return ResponseEntity.ok(dashboardService.getPriorityDistribution());
    }
}