package com.ets.EmergencyTriageSystem.controller;

import com.ets.EmergencyTriageSystem.dto.PatientRequestDTO;
import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyRequestDTO;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyResponseDTO;
import com.ets.EmergencyTriageSystem.entity.Patient;
import com.ets.EmergencyTriageSystem.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;



import java.util.List;

//@CrossOrigin(origins = "http://localhost:4200", allowedMethods = {"PATCH", "OPTIONS", "GET", "POST", "PUT", "DELETE"})
@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    /**
     * 1. Register a new patient
     * POST /api/patients
     */
    @PostMapping
    public ResponseEntity<Response<Patient>> registerPatient(
            @Valid @RequestBody PatientRequestDTO request) {
        log.info("Received request to register patient: {}", request.getPatientName());
        Response<Patient> response = patientService.addPatient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 2. Get all patients (for the patient list screen)
     * GET /api/patients
     */
    
    @GetMapping
    public ResponseEntity<Response<List<Patient>>> getAllPatients() {
        log.info("Received request to get all patients");
        Response<List<Patient>> response = patientService.getPatientList();
        return ResponseEntity.ok(response);
    }
  


    /**
     * 3. Get patient by ID
     * GET /api/patients/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<Patient>> getPatientById(
            @PathVariable Long id) {
        log.info("Received request to get patient by ID: {}", id);
        Response<Patient> response = patientService.getPatientById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 4. Update patient details
     * PUT /api/patients
     */
    @PutMapping("/{id}") 
    public ResponseEntity<Response<Patient>> updatePatient(
         @PathVariable Long id,
        @Valid @RequestBody PatientRequestDTO request) {
    log.info("Received update request for patient ID: {}", id);
    // Set the ID from the path into the DTO
    request.setPatientId(id);
    Response<Patient> response = patientService.updatePatient(request);
    return ResponseEntity.ok(response);
}

    /**
     * 5. Search patients by name (for the search bar)
     * GET /api/patients/search?name=John
     */
    @GetMapping("/search")
    public ResponseEntity<Response<List<Patient>>> searchPatientsByName(
            @RequestParam(required = false) String name) {
        log.info("Received request to search patients by name: {}", name);
        Response<List<Patient>> response = patientService.getPatientByName(name);
        return ResponseEntity.ok(response);
    }

    /**
     * 6. Filter patients by priority (for the filter dropdown)
     * GET /api/patients/filter?priority=RED
     */
    @GetMapping("/filter")
    public ResponseEntity<Response<List<Patient>>> filterPatientsByPriority(
            @RequestParam String priority) {
        log.info("Received request to filter patients by priority: {}", priority);
        Response<List<Patient>> response = patientService.getPatientByPriority(priority);
        return ResponseEntity.ok(response);
    }

   
  /**
     * Update patient status – use PATCH (RESTful) or POST (if client doesn't support PATCH)
     * Method 1: PATCH – recommended
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Response<Patient>> updateStatusPatch(
            @PathVariable Long id,
            @RequestParam String status) {
        log.info("PATCH request to update status for patient ID: {}", id);
        Response<Patient> response = patientService.updatePatientStatus(id, status);
        return ResponseEntity.ok(response);
    }


      /**
     * Add triage assessment for an existing patient
     * POST /api/patients/{patientId}/triage
     */
    @PostMapping("/{patientId}/triage")
    public ResponseEntity<Response<TriageOnlyResponseDTO>> addTriageForPatient(
            @PathVariable Long patientId,
            @Valid @RequestBody TriageOnlyRequestDTO request) {
        log.info("Received request to add triage for patient ID: {}", patientId);
        request.setPatientId(patientId);  // Set it from the path
        Response<TriageOnlyResponseDTO> response = patientService.addTriageForPatient(patientId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get triage assessment for a patient
     * GET /api/patients/{patientId}/triage
     */
    @GetMapping("/{patientId}/triage")
    public ResponseEntity<Response<TriageOnlyResponseDTO>> getTriageForPatient(
            @PathVariable Long patientId) {
        log.info("Received request to get triage for patient ID: {}", patientId);
        Response<TriageOnlyResponseDTO> response = patientService.getTriageForPatient(patientId);
        return ResponseEntity.ok(response);
    }
}

