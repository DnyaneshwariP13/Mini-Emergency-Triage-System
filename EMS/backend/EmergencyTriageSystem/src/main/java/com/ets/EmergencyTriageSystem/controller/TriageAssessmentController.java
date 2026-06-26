package com.ets.EmergencyTriageSystem.controller;

import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyRequestDTO;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyResponseDTO;
import com.ets.EmergencyTriageSystem.service.TriageAssessmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/triage")
@RequiredArgsConstructor
@Slf4j
public class TriageAssessmentController {

    private final TriageAssessmentService triageService;

    // ---- create ----
    @PostMapping
    public ResponseEntity<Response<TriageOnlyResponseDTO>> addTriage(
            @Valid @RequestBody TriageOnlyRequestDTO request) {
        log.info("POST /api/triage – add triage for patient {}", request.getPatientId());
        Response<TriageOnlyResponseDTO> response = triageService.addTriageAssessment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ---- get by ID ----
    @GetMapping("/{assessmentId}")
    public ResponseEntity<Response<TriageOnlyResponseDTO>> getTriageById(
            @PathVariable Long assessmentId) {
        log.info("GET /api/triage/{}", assessmentId);
        return ResponseEntity.ok(triageService.getAssessmentById(assessmentId));
    }

    // ---- get all for a patient ----
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Response<List<TriageOnlyResponseDTO>>> getTriageForPatient(
            @PathVariable Long patientId) {
        log.info("GET /api/triage/patient/{}", patientId);
        return ResponseEntity.ok(triageService.getAssessmentsByPatientId(patientId));
    }
}