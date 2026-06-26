package com.ets.EmergencyTriageSystem.dto;

import com.ets.EmergencyTriageSystem.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatientResponseDTO {

    private Long patientId;
    private String patientName;
    private Integer age;
    private String gender;
    private String mobileNumber;
    private String chiefComplaint;
    private LocalDateTime arrivalDateTime;
    private Status status; // WAITING, UNDER_TREATMENT, DISCHARGED

    // For the Patient List grid, you might also want the Priority here.
    // We will fetch that via a join query in the service.
}
