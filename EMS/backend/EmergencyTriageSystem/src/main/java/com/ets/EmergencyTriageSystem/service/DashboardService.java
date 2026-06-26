package com.ets.EmergencyTriageSystem.service;

import com.ets.EmergencyTriageSystem.dto.Response;
import com.ets.EmergencyTriageSystem.dto.DashboardStatDTO;
import com.ets.EmergencyTriageSystem.dto.PriorityDistributionDTO;

public interface DashboardService {
    Response<DashboardStatDTO> getDashboardStats();
     Response<PriorityDistributionDTO> getPriorityDistribution();
}