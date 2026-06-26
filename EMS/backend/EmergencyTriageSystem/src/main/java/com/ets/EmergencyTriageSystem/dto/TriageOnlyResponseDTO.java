package com.ets.EmergencyTriageSystem.dto;
import com.ets.EmergencyTriageSystem.enums.Priority;
import java.time.LocalDateTime;
import lombok.Data;


@Data
public class TriageOnlyResponseDTO {
    private Long assessmentId;
    private Long patientId;
    private String patientName;      // optional, for convenience
    private Priority priority;
    private String bloodPressure;
    private Integer pulseRate;
    private Double temperature;
    private LocalDateTime createdDate;
}
