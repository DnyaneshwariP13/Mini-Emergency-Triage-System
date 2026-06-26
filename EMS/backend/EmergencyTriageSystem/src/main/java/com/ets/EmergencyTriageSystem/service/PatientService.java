package com.ets.EmergencyTriageSystem.service;

import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyRequestDTO;
import com.ets.EmergencyTriageSystem.dto.TriageOnlyResponseDTO;
import com.ets.EmergencyTriageSystem.dto.PatientRequestDTO;
import com.ets.EmergencyTriageSystem.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {
    Response<Patient> addPatient(PatientRequestDTO patientRequest);
    Response<List<Patient>> getPatientList();
    Response<Patient> getPatientById(Long id);
    Response<Patient> updatePatient(PatientRequestDTO patientRequest);
    Response<List<Patient>> getPatientByName(String patientName);
    Response<List<Patient>> getPatientByPriority(String priority);
    Response<Patient> updatePatientStatus(Long patientId, String status);

       // ********** NEW TRIAGE-RELATED METHODS **********
    Response<TriageOnlyResponseDTO> addTriageForPatient(Long patientId, TriageOnlyRequestDTO request);
    Response<TriageOnlyResponseDTO> getTriageForPatient(Long patientId);
    //Page<Patient> getPatientList(String search, String priority, Pageable pageable);

}
