package com.ets.EmergencyTriageSystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatDTO {
    private long totalPatients;
    private long redPriorityPatients;
    private long waitingPatients;
    private long dischargedPatients;
}