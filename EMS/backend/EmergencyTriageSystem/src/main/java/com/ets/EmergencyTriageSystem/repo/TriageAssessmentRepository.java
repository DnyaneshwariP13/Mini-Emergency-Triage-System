package com.ets.EmergencyTriageSystem.repo;

import com.ets.EmergencyTriageSystem.entity.TriageAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TriageAssessmentRepository extends JpaRepository<TriageAssessment, Long> {

    /**
     * Find triage assessment by ID with patient loaded eagerly (prevents LazyInitializationException)
     */
    @Query("SELECT t FROM TriageAssessment t LEFT JOIN FETCH t.patient WHERE t.assessmentId = :id")
    Optional<TriageAssessment> findByIdWithPatient(@Param("id") Long id);

    /**
     * Get all triage assessments for a specific patient (though we enforce one per patient)
     */
    List<TriageAssessment> findByPatient_PatientId(Long patientId);

    /**
     * Get all triage assessments for a patient with patient data loaded
     */
    @Query("SELECT t FROM TriageAssessment t LEFT JOIN FETCH t.patient WHERE t.patient.patientId = :patientId")
    List<TriageAssessment> findByPatientIdWithPatient(@Param("patientId") Long patientId);

    /**
     * Check if a patient already has a triage assessment (enforces one triage per patient)
     */
    boolean existsByPatient_PatientId(Long patientId);
}