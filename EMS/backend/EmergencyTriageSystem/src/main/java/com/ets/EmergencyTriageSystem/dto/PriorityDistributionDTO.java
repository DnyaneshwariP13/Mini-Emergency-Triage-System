package com.ets.EmergencyTriageSystem.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriorityDistributionDTO {
    private long red;
    private long yellow;
    private long green;
    private long noTriage;   // Patients without any triage assessment
}
