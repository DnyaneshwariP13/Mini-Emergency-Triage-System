package com.ets.EmergencyTriageSystem.dto;

import java.time.LocalDateTime;

import com.ets.EmergencyTriageSystem.enums.Priority;
import com.ets.EmergencyTriageSystem.enums.Status;
import lombok.Data;


@Data
public class PatientListResponseDTO {
    private Long patientId;
    private String patientName;
    private Integer age;
    private String mobileNumber;
    private Priority priority;  // Flattened from TriageAssessment
    private Status status;
    private LocalDateTime arrivalDateTime;
    

    
}
