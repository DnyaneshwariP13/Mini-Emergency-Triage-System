package com.ets.EmergencyTriageSystem.entity;

import java.time.LocalDateTime;

import com.ets.EmergencyTriageSystem.enums.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Patient")
@Data
@NoArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PatientId")
    private Long patientId;

    // 1. Patient Name - Required
    @Column(name = "PatientName", nullable = false, length = 100)
    @NotBlank(message = "Patient Name is required")
    private String patientName;

    // 2. Age - Required & > 0
    @Column(name = "Age", nullable = false)
    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age should be greater than 0")
    private Integer age;

    // 3. Gender - Required (captured from form)
    @Column(name = "Gender", nullable = false, length = 10)
    @NotBlank(message = "Gender is required")
    private String gender;

    // 4. Mobile Number - Required & Valid (10 digits for Indian standard)
    @Column(name = "MobileNumber", nullable = false, length = 15,unique = true)
    @NotBlank(message = "Mobile Number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile Number should be a valid 10-digit number")
    private String mobileNumber;

    // 5. Chief Complaint - Required
    @Column(name = "ChiefComplaint", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Chief Complaint is required")
    private String chiefComplaint;

    // 6. Arrival Date & Time - Required (Auto-set if not provided)
    @Column(name = "ArrivalDateTime", nullable = false, updatable = false)
    @NotNull(message = "Arrival Date & Time is required")
    private LocalDateTime arrivalDateTime;

    // 7. Status - Defaults to "WAITING" (no input from form needed)
    @Column(name = "Status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.WAITING;

    // Relationship with Triage Assessment
    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private TriageAssessment triageAssessment;

    // Helper method to set arrival time automatically if not provided
    public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
        this.arrivalDateTime = (arrivalDateTime != null) ? arrivalDateTime : LocalDateTime.now();
    }

}
