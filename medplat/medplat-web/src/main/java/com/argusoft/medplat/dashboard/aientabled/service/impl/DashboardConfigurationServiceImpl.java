package com.argusoft.medplat.dashboard.aientabled.service.impl;
import com.argusoft.medplat.dashboard.aientabled.dto.DashboardConfigurationDto;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardConfiguration;
import com.argusoft.medplat.dashboard.aientabled.repository.DashboardConfigurationRepository;
import com.argusoft.medplat.dashboard.aientabled.service.DashboardConfigurationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@Slf4j
@Transactional
public class DashboardConfigurationServiceImpl implements DashboardConfigurationService {
    @Autowired
    private DashboardConfigurationRepository dashboardConfigRepository;
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public DashboardConfiguration createDashboard(DashboardConfigurationDto configDto) {
        log.info("Creating new dashboard: {}", configDto.getDashboardName());
        DashboardConfiguration config = new DashboardConfiguration();
        config.setUserId(configDto.getUserId());
        config.setDashboardName(configDto.getDashboardName());
        config.setDescription(configDto.getDescription());
        config.setDashboardType(configDto.getDashboardType());
        config.setIsDefault(configDto.getIsDefault() != null ? configDto.getIsDefault() : false);
        config.setIsPublic(configDto.getIsPublic() != null ? configDto.getIsPublic() : false);
        config.setIsActive(true);
        config.setRoleBasedAccess(configDto.getRoleBasedAccess());
        config.setRefreshIntervalSeconds(configDto.getRefreshIntervalSeconds() != null ? configDto.getRefreshIntervalSeconds() : 300);
        config.setAiEnabled(configDto.getAiEnabled() != null ? configDto.getAiEnabled() : true);
        config.setNlpEnabled(configDto.getNlpEnabled() != null ? configDto.getNlpEnabled() : true);
        config.setAnomalyDetectionEnabled(configDto.getAnomalyDetectionEnabled() != null ? configDto.getAnomalyDetectionEnabled() : false);
        config.setPredictiveAnalyticsEnabled(configDto.getPredictiveAnalyticsEnabled() != null ? configDto.getPredictiveAnalyticsEnabled() : false);
        config.setRecommendationEnabled(configDto.getRecommendationEnabled() != null ? configDto.getRecommendationEnabled() : true);
        config.setCreatedDate(new Date());
        config.setModifiedDate(new Date());
        try {
            config.setConfiguration(objectMapper.writeValueAsString(configDto));
        } catch (Exception e) {
            log.error("Error serializing dashboard configuration", e);
        }
        return dashboardConfigRepository.save(config);
    }
    @Override
    public DashboardConfiguration updateDashboard(Long dashboardId, DashboardConfigurationDto configDto) {
        log.info("Updating dashboard: {}", dashboardId);
        Optional<DashboardConfiguration> existing = dashboardConfigRepository.findById(dashboardId);
        if (existing.isEmpty()) {
            throw new RuntimeException("Dashboard not found: " + dashboardId);
        }
        DashboardConfiguration config = existing.get();
        config.setDashboardName(configDto.getDashboardName());
        config.setDescription(configDto.getDescription());
        config.setDashboardType(configDto.getDashboardType());
        config.setIsPublic(configDto.getIsPublic());
        config.setRoleBasedAccess(configDto.getRoleBasedAccess());
        config.setRefreshIntervalSeconds(configDto.getRefreshIntervalSeconds());
        config.setAiEnabled(configDto.getAiEnabled());
        config.setNlpEnabled(configDto.getNlpEnabled());
        config.setAnomalyDetectionEnabled(configDto.getAnomalyDetectionEnabled());
        config.setPredictiveAnalyticsEnabled(configDto.getPredictiveAnalyticsEnabled());
        config.setRecommendationEnabled(configDto.getRecommendationEnabled());
        config.setModifiedDate(new Date());
        try {
            config.setConfiguration(objectMapper.writeValueAsString(configDto));
        } catch (Exception e) {
            log.error("Error serializing dashboard configuration", e);
        }
        return dashboardConfigRepository.save(config);
    }
    @Override
    public Optional<DashboardConfiguration> getDashboardById(Long dashboardId) {
        return dashboardConfigRepository.findById(dashboardId);
    }
    @Override
    public List<DashboardConfiguration> getUserDashboards(Long userId) {
        return dashboardConfigRepository.findByUserIdAndIsActive(userId, true);
    }
    @Override
    public List<DashboardConfiguration> getAccessibleDashboards(Long userId, String userRole) {
        return dashboardConfigRepository.findAccessibleDashboards(userId, userRole);
    }
    @Override
    public List<DashboardConfiguration> getPublicDashboards() {
        return dashboardConfigRepository.findAllPublicDashboards();
    }
    @Override
    public Optional<DashboardConfiguration> getDefaultDashboard(Long userId) {
        return dashboardConfigRepository.findByUserIdAndIsDefault(userId, true);
    }
    @Override
    public void setDefaultDashboard(Long userId, Long dashboardId) {
        log.info("Setting default dashboard for user: {} -> {}", userId, dashboardId);
        List<DashboardConfiguration> allDashboards = dashboardConfigRepository.findByUserId(userId);
        allDashboards.forEach(d -> d.setIsDefault(false));
        dashboardConfigRepository.saveAll(allDashboards);
        Optional<DashboardConfiguration> dashboard = dashboardConfigRepository.findById(dashboardId);
        if (dashboard.isPresent()) {
            dashboard.get().setIsDefault(true);
            dashboardConfigRepository.save(dashboard.get());
        }
    }
    @Override
    public void deleteDashboard(Long dashboardId) {
        log.info("Soft deleting dashboard: {}", dashboardId);
        Optional<DashboardConfiguration> dashboard = dashboardConfigRepository.findById(dashboardId);
        if (dashboard.isPresent()) {
            dashboard.get().setIsActive(false);
            dashboardConfigRepository.save(dashboard.get());
        }
    }
    @Override
    public DashboardConfiguration cloneDashboard(Long sourceDashboardId, Long userId, String newName) {
        log.info("Cloning dashboard: {} for user: {}", sourceDashboardId, userId);
        Optional<DashboardConfiguration> sourceOpt = dashboardConfigRepository.findById(sourceDashboardId);
        if (sourceOpt.isEmpty()) {
            throw new RuntimeException("Source dashboard not found");
        }
        DashboardConfiguration source = sourceOpt.get();
        DashboardConfiguration clone = new DashboardConfiguration();
        clone.setUserId(userId);
        clone.setDashboardName(newName != null ? newName : source.getDashboardName() + " (Copy)");
        clone.setDescription(source.getDescription());
        clone.setDashboardType(source.getDashboardType());
        clone.setConfiguration(source.getConfiguration());
        clone.setIsDefault(false);
        clone.setIsPublic(false);
        clone.setIsActive(true);
        clone.setAiEnabled(source.getAiEnabled());
        clone.setNlpEnabled(source.getNlpEnabled());
        clone.setAnomalyDetectionEnabled(source.getAnomalyDetectionEnabled());
        clone.setPredictiveAnalyticsEnabled(source.getPredictiveAnalyticsEnabled());
        clone.setRecommendationEnabled(source.getRecommendationEnabled());
        clone.setCreatedDate(new Date());
        clone.setModifiedDate(new Date());
        clone.setCreatedBy(userId);
        return dashboardConfigRepository.save(clone);
    }
    @Override
    public String exportDashboardAsJson(Long dashboardId) {
        try {
            Optional<DashboardConfiguration> dashboard = dashboardConfigRepository.findById(dashboardId);
            if (dashboard.isPresent()) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dashboard.get());
            }
        } catch (Exception e) {
            log.error("Error exporting dashboard", e);
        }
        return null;
    }
    @Override
    public DashboardConfiguration importDashboardFromJson(String json, Long userId) {
        try {
            DashboardConfiguration config = objectMapper.readValue(json, DashboardConfiguration.class);
            config.setId(null); 
            config.setUserId(userId);
            config.setCreatedDate(new Date());
            config.setModifiedDate(new Date());
            config.setCreatedBy(userId);
            return dashboardConfigRepository.save(config);
        } catch (Exception e) {
            log.error("Error importing dashboard", e);
            throw new RuntimeException("Failed to import dashboard", e);
        }
    }
    @Override
    public List<DashboardConfiguration> getDashboardsByType(String dashboardType) {
        return dashboardConfigRepository.findByDashboardType(dashboardType);
    }
}
