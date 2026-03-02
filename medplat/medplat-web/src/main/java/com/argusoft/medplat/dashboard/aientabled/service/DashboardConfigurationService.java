package com.argusoft.medplat.dashboard.aientabled.service;
import com.argusoft.medplat.dashboard.aientabled.dto.DashboardConfigurationDto;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardConfiguration;
import java.util.List;
import java.util.Optional;
public interface DashboardConfigurationService {
    DashboardConfiguration createDashboard(DashboardConfigurationDto configDto);
    DashboardConfiguration updateDashboard(Long dashboardId, DashboardConfigurationDto configDto);
    Optional<DashboardConfiguration> getDashboardById(Long dashboardId);
    List<DashboardConfiguration> getUserDashboards(Long userId);
    List<DashboardConfiguration> getAccessibleDashboards(Long userId, String userRole);
    List<DashboardConfiguration> getPublicDashboards();
    Optional<DashboardConfiguration> getDefaultDashboard(Long userId);
    void setDefaultDashboard(Long userId, Long dashboardId);
    void deleteDashboard(Long dashboardId);
    DashboardConfiguration cloneDashboard(Long sourceDashboardId, Long userId, String newName);
    String exportDashboardAsJson(Long dashboardId);
    DashboardConfiguration importDashboardFromJson(String json, Long userId);
    List<DashboardConfiguration> getDashboardsByType(String dashboardType);
}
