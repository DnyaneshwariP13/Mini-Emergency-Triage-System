package com.ets.EmergencyTriageSystem.repo;

import com.ets.EmergencyTriageSystem.entity.Patient;
import com.ets.EmergencyTriageSystem.enums.Priority;
import com.ets.EmergencyTriageSystem.enums.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {


 // 1. Get all patients with triage data loaded (replaces findAll) no sorting
    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.triageAssessment")
    List<Patient> findAllWithTriage();

    // 2. Get all patients with triage data, sorted
    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.triageAssessment")
    List<Patient> findAllWithTriage(Sort sort);

    // 3. Search by name with triage data (case-insensitive)
    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.triageAssessment WHERE LOWER(p.patientName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Patient> findByNameWithTriage(@Param("name") String name, org.springframework.data.domain.Sort sort);

    // 4. Filter by priority with triage data
    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.triageAssessment t WHERE t.priority = :priority")
    List<Patient> findByPriorityWithTriage(@Param("priority") Priority priority, org.springframework.data.domain.Sort sort);

    // 5. Get single patient by ID with triage data (optional, for detail view)
    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.triageAssessment WHERE p.patientId = :id")
    Optional<Patient> findByIdWithTriage(@Param("id") Long id);

    // Added this method to ensure each patient has a unique mobile number, which is important for identification and contact purposes.
   boolean existsByMobileNumber(String mobileNumber);
   
       /**
     * Count patients by status (WAITING, UNDER_TREATMENT, DISCHARGED)
     */
    long countByStatus(Status status);

    /**
     * Count patients with a specific triage priority (RED, YELLOW, GREEN)
     */
    @Query("SELECT COUNT(p) FROM Patient p JOIN p.triageAssessment t WHERE t.priority = :priority")
    long countByTriagePriority(@Param("priority") Priority priority);


        // Returns a list of Object[] where each array is [priority, count]
    @Query("SELECT t.priority, COUNT(p) FROM Patient p JOIN p.triageAssessment t GROUP BY t.priority")
    List<Object[]> countPatientsGroupedByPriority();

    


}