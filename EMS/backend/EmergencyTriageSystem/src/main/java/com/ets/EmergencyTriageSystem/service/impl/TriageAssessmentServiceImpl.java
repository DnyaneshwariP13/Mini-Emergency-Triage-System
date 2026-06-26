package com.ets.EmergencyTriageSystem.service.impl;

import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyRequestDTO;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyResponseDTO;
import com.ets.EmergencyTriageSystem.entity.Patient;
import com.ets.EmergencyTriageSystem.entity.TriageAssessment;
import com.ets.EmergencyTriageSystem.enums.Priority;
import com.ets.EmergencyTriageSystem.exceptions.BadRequestException;
import com.ets.EmergencyTriageSystem.exceptions.NotFoundException;
import com.ets.EmergencyTriageSystem.repo.PatientRepository;
import com.ets.EmergencyTriageSystem.repo.TriageAssessmentRepository;
import com.ets.EmergencyTriageSystem.service.TriageAssessmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TriageAssessmentServiceImpl implements TriageAssessmentService {

    private final TriageAssessmentRepository triageRepository;
    private final PatientRepository patientRepository;

    @Override
    @Transactional
    public Response<TriageOnlyResponseDTO> addTriageAssessment(TriageOnlyRequestDTO request) {
        log.info("Inside addTriageAssessment() for patient ID: {}", request.getPatientId());

            if (request.getPatientId() == null) {
        throw new BadRequestException("Patient ID is required");
    }

        // 1. Validate patient exists
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new NotFoundException("Patient not found with ID: " + request.getPatientId()));

        // 2. Enforce "one triage per patient"
        if (triageRepository.existsByPatient_PatientId(request.getPatientId())) {
            throw new BadRequestException("Patient already has a triage assessment. Use update endpoint if needed.");
        }

        // 3. Build entity
        TriageAssessment assessment = new TriageAssessment();
        assessment.setPatient(patient);
        assessment.setPriority(request.getPriority());
        assessment.setBloodPressure(request.getBloodPressure());
        assessment.setPulseRate(request.getPulseRate());
        assessment.setTemperature(request.getTemperature());
        assessment.setCreatedDate(LocalDateTime.now()); // @CreationTimestamp will override, but safe

        // 4. Save
        TriageAssessment saved = triageRepository.save(assessment);

        // 5. Convert to DTO
        TriageOnlyResponseDTO dto = convertToDTO(saved);

        return Response.<TriageOnlyResponseDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Triage assessment created successfully")
                .data(dto)
                .build();
    }

    @Override
    public Response<TriageOnlyResponseDTO> getAssessmentById(Long assessmentId) {
        log.info("Inside getAssessmentById() for ID: {}", assessmentId);

        TriageAssessment assessment = triageRepository.findByIdWithPatient(assessmentId)
                .orElseThrow(() -> new NotFoundException("Triage assessment not found with ID: " + assessmentId));

        return Response.<TriageOnlyResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Triage assessment retrieved successfully")
                .data(convertToDTO(assessment))
                .build();
    }

    @Override
    public Response<List<TriageOnlyResponseDTO>> getAssessmentsByPatientId(Long patientId) {
        log.info("Inside getAssessmentsByPatientId() for patient ID: {}", patientId);

        if (!patientRepository.existsById(patientId)) {
            throw new NotFoundException("Patient not found with ID: " + patientId);
        }

        List<TriageAssessment> assessments = triageRepository.findByPatientIdWithPatient(patientId);
        List<TriageOnlyResponseDTO> dtos = assessments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Response.<List<TriageOnlyResponseDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Triage assessments retrieved successfully")
                .data(dtos)
                .build();
    }



    // ---- helper ----
    private TriageOnlyResponseDTO convertToDTO(TriageAssessment assessment) {
        TriageOnlyResponseDTO dto = new TriageOnlyResponseDTO();
        dto.setAssessmentId(assessment.getAssessmentId());
        dto.setPatientId(assessment.getPatient().getPatientId());
        dto.setPatientName(assessment.getPatient().getPatientName());
        dto.setPriority(assessment.getPriority());
        dto.setBloodPressure(assessment.getBloodPressure());
        dto.setPulseRate(assessment.getPulseRate());
        dto.setTemperature(assessment.getTemperature());
        dto.setCreatedDate(assessment.getCreatedDate());
        return dto;
    }
}
