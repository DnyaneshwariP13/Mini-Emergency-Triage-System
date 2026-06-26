package com.ets.EmergencyTriageSystem.service;

import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyRequestDTO;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyResponseDTO;

import java.util.List;

public interface TriageAssessmentService {

    /**
     * Add a new triage assessment for an existing patient.
     * Throws exception if patient not found or already has a triage.
     */
    Response<TriageOnlyResponseDTO> addTriageAssessment(TriageOnlyRequestDTO request);

    /**
     * Get a specific triage assessment by its ID.
     */
    Response<TriageOnlyResponseDTO> getAssessmentById(Long assessmentId);

    /**
     * Get all triage assessments for a given patient (though we enforce one per patient).
     */
    Response<List<TriageOnlyResponseDTO>> getAssessmentsByPatientId(Long patientId);
}
