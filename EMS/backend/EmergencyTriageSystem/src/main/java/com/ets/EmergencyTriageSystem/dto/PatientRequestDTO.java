package com.ets.EmergencyTriageSystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

import com.ets.EmergencyTriageSystem.enums.Status;

@Data
public class PatientRequestDTO {


    private Long patientId;

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

    // Optional - if not provided, the system will auto-set to current time
    private LocalDateTime arrivalDateTime;
    private Status status;
}