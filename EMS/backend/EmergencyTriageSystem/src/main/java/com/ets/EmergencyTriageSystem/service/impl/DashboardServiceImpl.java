package com.ets.EmergencyTriageSystem.service.impl;

import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.DashboardStatDTO;
import com.ets.EmergencyTriageSystem.dto.PriorityDistributionDTO;
import com.ets.EmergencyTriageSystem.enums.Priority;
import com.ets.EmergencyTriageSystem.enums.Status;
import com.ets.EmergencyTriageSystem.repo.PatientRepository;
import com.ets.EmergencyTriageSystem.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final PatientRepository patientRepository;

    @Override
    public Response<DashboardStatDTO> getDashboardStats() {
        log.info("Fetching dashboard statistics");

        long totalPatients = patientRepository.count();
        long redPriorityPatients = patientRepository.countByTriagePriority(Priority.RED);
        long waitingPatients = patientRepository.countByStatus(Status.WAITING);
        long dischargedPatients = patientRepository.countByStatus(Status.DISCHARGED);

        DashboardStatDTO stats = DashboardStatDTO.builder()
                .totalPatients(totalPatients)
                .redPriorityPatients(redPriorityPatients)
                .waitingPatients(waitingPatients)
                .dischargedPatients(dischargedPatients)
                .build();

        return Response.<DashboardStatDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Dashboard statistics retrieved successfully")
                .data(stats)
                .build();
    }


    @Override
    public Response<PriorityDistributionDTO> getPriorityDistribution() {
        log.info("Fetching priority distribution for chart");

        // Option 1: Using the group-by query (recommended)
        List<Object[]> results = patientRepository.countPatientsGroupedByPriority();

        long red = 0, yellow = 0, green = 0;
        for (Object[] row : results) {
            Priority priority = (Priority) row[0];
            Long count = (Long) row[1];
            switch (priority) {
                case RED -> red = count;
                case YELLOW -> yellow = count;
                case GREEN -> green = count;
            }
        }

        long totalPatients = patientRepository.count();
        long noTriage = totalPatients - (red + yellow + green);

        PriorityDistributionDTO dto = PriorityDistributionDTO.builder()
                .red(red)
                .yellow(yellow)
                .green(green)
                .noTriage(noTriage)
                .build();

        return Response.<PriorityDistributionDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Priority distribution retrieved successfully")
                .data(dto)
                .build();

        /*
        // Option 2: Using individual count methods (simpler but 4 queries)
        long red = patientRepository.countByTriagePriority(Priority.RED);
        long yellow = patientRepository.countByTriagePriority(Priority.YELLOW);
        long green = patientRepository.countByTriagePriority(Priority.GREEN);
        long total = patientRepository.count();
        long noTriage = total - (red + yellow + green);
        // Build DTO similarly...
        */
    }
}