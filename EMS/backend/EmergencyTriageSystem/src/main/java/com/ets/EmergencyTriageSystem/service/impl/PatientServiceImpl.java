package com.ets.EmergencyTriageSystem.service.impl;

import com.ets.EmergencyTriageSystem.dto.PatientRequestDTO;
import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyRequestDTO;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyResponseDTO;
import com.ets.EmergencyTriageSystem.entity.Patient;
import com.ets.EmergencyTriageSystem.enums.Priority;
import com.ets.EmergencyTriageSystem.enums.Status;
import com.ets.EmergencyTriageSystem.exceptions.BadRequestException;
import com.ets.EmergencyTriageSystem.exceptions.NotFoundException;
import com.ets.EmergencyTriageSystem.repo.PatientRepository;
import com.ets.EmergencyTriageSystem.service.PatientService;
import com.ets.EmergencyTriageSystem.service.TriageAssessmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;




@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final TriageAssessmentService triageAssessmentService;

    @Override
    @Transactional
    public Response<Patient> addPatient (PatientRequestDTO request) {
        log.info("Inside addPatient()");

            // 1.  CHECK FOR DUPLICATE PATIENT BY MOBILE NUMBER
        if (patientRepository.existsByMobileNumber(request.getMobileNumber())) {
        throw new BadRequestException("Patient with mobile number " + request.getMobileNumber() + " already exists in the system.");
        }

        // Convert DTO to Entity
        Patient patient = new Patient();
        patient.setPatientName(request.getPatientName());
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setMobileNumber(request.getMobileNumber());
        patient.setChiefComplaint(request.getChiefComplaint());
        // Set arrival time; if null, default to now
        patient.setArrivalDateTime(request.getArrivalDateTime() != null
                ? request.getArrivalDateTime()
                : LocalDateTime.now());
        // Status defaults to WAITING (already set in entity)

        Patient savedPatient = patientRepository.save(patient);

        return Response.<Patient>builder()
                .statusCode(HttpStatus.CREATED.value())  // 201 Created
                .message("Patient registered successfully")
                .data(savedPatient)
                .build();
    }
    
    @Override
    public Response<List<Patient>> getPatientList() {
        log.info("Inside getPatientList()");
        
        // Used JOIN FETCH method with sorting
        List<Patient> patients = patientRepository.findAllWithTriage(
                Sort.by(Sort.Direction.DESC, "patientId")
        );
        
        return Response.<List<Patient>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Patients retrieved successfully")
                .data(patients)
                .build();
    }

      
    
    @Override
    public Response<Patient> getPatientById(Long id) {
        log.info("Inside getPatientById() for id: {}", id);

        Patient patient = patientRepository.findByIdWithTriage(id)
                .orElseThrow(() -> new NotFoundException("Patient not found with ID: " + id));

        return Response.<Patient>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Patient retrieved successfully")
                .data(patient)
                .build();
    }

    @Override
    @Transactional
    public Response<Patient> updatePatient(PatientRequestDTO request) {
        log.info("Inside updatePatient()");

        if (request.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required for update");
        }

        Patient existingPatient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new NotFoundException("Patient not found with ID: " + request.getPatientId()));

        // Update only non-null fields
        if (request.getPatientName() != null) {
            existingPatient.setPatientName(request.getPatientName());
        }
        if (request.getAge() != null) {
            existingPatient.setAge(request.getAge());
        }
        if (request.getGender() != null) {
            existingPatient.setGender(request.getGender());
        }
        if (request.getMobileNumber() != null) {
            existingPatient.setMobileNumber(request.getMobileNumber());
        }
        if (request.getChiefComplaint() != null) {
            existingPatient.setChiefComplaint(request.getChiefComplaint());
        }
        if (request.getStatus() != null) {
        existingPatient.setStatus(request.getStatus());
}

        Patient updatedPatient = patientRepository.save(existingPatient);

        return Response.<Patient>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Patient updated successfully")
                .data(updatedPatient)
                .build();
    }


    @Override
    public Response<List<Patient>> getPatientByName(String patientName) {
        log.info("Inside getPatientByName() with name: {}", patientName);

        if (patientName == null || patientName.trim().isEmpty()) {
            return getPatientList(); // Reuse the same method
        }

        // Use JOIN FETCH method for search
        List<Patient> patients = patientRepository.findByNameWithTriage(
                patientName.trim(),
                Sort.by(Sort.Direction.DESC, "patientId")
        );

        return Response.<List<Patient>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Patients matching name retrieved successfully")
                .data(patients)
                .build();
    }

    @Override
    public Response<List<Patient>> getPatientByPriority(String priority) {
        log.info("Inside getPatientByPriority() with priority: {}", priority);

        Priority priorityEnum;
        try {
            priorityEnum = Priority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid priority value: {}. Returning empty list.", priority);
            return Response.<List<Patient>>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Invalid priority filter. Returning empty list.")
                    .data(List.of())
                    .build();
        }

        // Use JOIN FETCH method for priority filter
        List<Patient> patients = patientRepository.findByPriorityWithTriage(
                priorityEnum,
                Sort.by(Sort.Direction.DESC, "patientId")
        );

        return Response.<List<Patient>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Patients filtered by priority retrieved successfully")
                .data(patients)
                .build();
    }


    @Override
    @Transactional
    public Response<Patient> updatePatientStatus(Long patientId, String status) {
        log.info("Inside updatePatientStatus() for patient ID: {} with status: {}", patientId, status);

        // 1. Fetch patient
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new NotFoundException("Patient not found with ID: " + patientId));

        // 2. Validate and convert status
        Status newStatus;
        try {
            newStatus = Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status + ". Allowed values: WAITING, UNDER_TREATMENT, DISCHARGED");
        }

        // 3. Update only the status
        patient.setStatus(newStatus);

        // 4. Save
        Patient updatedPatient = patientRepository.save(patient);

        return Response.<Patient>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Patient status updated successfully")
                .data(updatedPatient)
                .build();
    }



    @Override
    @Transactional
    public Response<TriageOnlyResponseDTO> addTriageForPatient(Long patientId, TriageOnlyRequestDTO request) {
        log.info("Inside addTriageForPatient() for patient ID: {}", patientId);
        if (!patientRepository.existsById(patientId)) {
            throw new NotFoundException("Patient not found with ID: " + patientId);
        }
        request.setPatientId(patientId);
        return triageAssessmentService.addTriageAssessment(request);
    }

    @Override
    public Response<TriageOnlyResponseDTO> getTriageForPatient(Long patientId) {
        log.info("Inside getTriageForPatient() for patient ID: {}", patientId);
        if (!patientRepository.existsById(patientId)) {
            throw new NotFoundException("Patient not found with ID: " + patientId);
        }
        var responseList = triageAssessmentService.getAssessmentsByPatientId(patientId);
        List<TriageOnlyResponseDTO> assessments = responseList.getData();
        if (assessments == null || assessments.isEmpty()) {
            throw new NotFoundException("No triage assessment found for patient ID: " + patientId);
        }
        return Response.<TriageOnlyResponseDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Triage assessment retrieved successfully")
                .data(assessments.get(0))
                .build();
    }




   
}