package com.ets.EmergencyTriageSystem.dto;

import com.ets.EmergencyTriageSystem.enums.Priority;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class TriageOnlyRequestDTO {
    //@NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Triage Priority is required")
    private Priority priority;

    @NotBlank(message = "Blood Pressure is required")
    private String bloodPressure;

    @NotNull(message = "Pulse Rate is required")
    @Min(value = 1, message = "Pulse rate must be greater than 0")
    private Integer pulseRate;

    @NotNull(message = "Temperature is required")
    private Double temperature;

    private LocalDateTime createdDate;
}
/* 
@Data
public class TriageAssessmentRequestDTO {

   // ----- PATIENT FIELDS (From your entity) -----
    @NotBlank(message = "Patient Name is required")
    private String patientName;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age should be greater than 0")
    private Integer age;

    @NotBlank(message = "Gender is required")
    private String gender;

    @NotBlank(message = "Mobile Number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile Number should be a valid 10-digit number")
    private String mobileNumber;

    @NotBlank(message = "Chief Complaint is required")
    private String chiefComplaint;

    // Optional - if not provided, we set it to now in the Service/Entity
    private LocalDateTime arrivalDateTime;

    // ----- TRIAGE FIELDS (From TriageAssessment entity) -----
    @NotNull(message = "Triage Priority is required")
    private Priority priority; // RED, YELLOW, GREEN

    @NotBlank(message = "Blood Pressure is required")
    private String bloodPressure;

    @NotNull(message = "Pulse Rate is required")
    @Min(value = 1, message = "Pulse rate must be greater than 0")
    private Integer pulseRate;

    @NotNull(message = "Temperature is required")
    private Double temperature;
}
*/
