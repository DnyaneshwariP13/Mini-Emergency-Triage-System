package com.ets.EmergencyTriageSystem.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.ets.EmergencyTriageSystem.enums.Priority;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TriageAssessment")
@Data
@NoArgsConstructor

public class TriageAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AssessmentId")
    private Long assessmentId;

    // Owning side of the relationship (FK to Patient)
    @OneToOne
    @JoinColumn(name = "PatientId", referencedColumnName = "PatientId", nullable = false)
    @NotNull(message = "Patient association is required")
    @JsonIgnore 
    private Patient patient;

     // 1. Priority - Required (RED, YELLOW, GREEN)
    @Column(name = "Priority", nullable = false)
    @NotNull(message = "Triage Priority is required")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    // 2. Blood Pressure - Required
    @Column(name = "BloodPressure", nullable = false, length = 20)
    @NotBlank(message = "Blood Pressure is required")
    private String bloodPressure; // e.g., "120/80"

    // 3. Pulse Rate - Required & must be positive
    @Column(name = "PulseRate", nullable = false)
    @NotNull(message = "Pulse Rate is required")
    @Min(value = 1, message = "Pulse rate must be greater than 0")
    private Integer pulseRate;

    // 4. Temperature - Required
    @Column(name = "Temperature", nullable = false)
    @NotNull(message = "Temperature is required")
    private Double temperature; // e.g., 98.6

    

    // 5. Created Date - Auto-generated
    @Column(name = "CreatedDate", nullable = false, updatable = false)
    @CreationTimestamp // Automatically sets the current timestamp when saved
    private LocalDateTime createdDate;
}
