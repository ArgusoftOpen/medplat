package com.argusoft.medplat.dashboard.aientabled.controller;
import com.argusoft.medplat.dashboard.aientabled.dto.DashboardConfigurationDto;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardConfiguration;
import com.argusoft.medplat.dashboard.aientabled.service.DashboardConfigurationService;
import com.argusoft.medplat.dashboard.aientabled.service.DashboardPersonalizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/dashboard")
@Slf4j
@CrossOrigin(origins = "*")
public class DashboardController {
    @Autowired
    private DashboardConfigurationService dashboardService;
    @Autowired
    private DashboardPersonalizationService personalizationService;
    @PostMapping("/create")
    public ResponseEntity<?> createDashboard(@RequestBody DashboardConfigurationDto configDto) {
        try {
            log.info("Creating dashboard: {}", configDto.getDashboardName());
            DashboardConfiguration dashboard = dashboardService.createDashboard(configDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Dashboard created successfully");
            response.put("dashboardId", dashboard.getId());
            response.put("dashboard", dashboard);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error creating dashboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to create dashboard: " + e.getMessage()));
        }
    }
    @GetMapping("/{dashboardId}")
    public ResponseEntity<?> getDashboard(@PathVariable Long dashboardId) {
        try {
            Optional<DashboardConfiguration> dashboard = dashboardService.getDashboardById(dashboardId);
            if (dashboard.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Dashboard not found"));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", dashboard.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching dashboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to fetch dashboard: " + e.getMessage()));
        }
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserDashboards(@PathVariable Long userId) {
        try {
            List<DashboardConfiguration> dashboards = dashboardService.getUserDashboards(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", dashboards.size());
            response.put("dashboards", dashboards);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching user dashboards", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to fetch dashboards: " + e.getMessage()));
        }
    }
    @GetMapping("/type/{dashboardType}")
    public ResponseEntity<?> getDashboardsByType(@PathVariable String dashboardType) {
        try {
            List<DashboardConfiguration> dashboards = dashboardService.getDashboardsByType(dashboardType);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", dashboards.size());
            response.put("dashboards", dashboards);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching dashboards by type", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to fetch dashboards: " + e.getMessage()));
        }
    }
    @PutMapping("/{dashboardId}")
    public ResponseEntity<?> updateDashboard(@PathVariable Long dashboardId, 
                                             @RequestBody DashboardConfigurationDto configDto) {
        try {
            DashboardConfiguration updated = dashboardService.updateDashboard(dashboardId, configDto);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Dashboard updated successfully");
            response.put("dashboard", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating dashboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to update dashboard: " + e.getMessage()));
        }
    }
    @DeleteMapping("/{dashboardId}")
    public ResponseEntity<?> deleteDashboard(@PathVariable Long dashboardId) {
        try {
            dashboardService.deleteDashboard(dashboardId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Dashboard deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error deleting dashboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to delete dashboard: " + e.getMessage()));
        }
    }
    @PutMapping("/{dashboardId}/set-default/{userId}")
    public ResponseEntity<?> setDefaultDashboard(@PathVariable Long dashboardId, @PathVariable Long userId) {
        try {
            dashboardService.setDefaultDashboard(userId, dashboardId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Default dashboard set successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error setting default dashboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to set default dashboard: " + e.getMessage()));
        }
    }
    @PostMapping("/{dashboardId}/clone/{userId}")
    public ResponseEntity<?> cloneDashboard(@PathVariable Long dashboardId, 
                                            @PathVariable Long userId,
                                            @RequestParam(required = false) String newName) {
        try {
            DashboardConfiguration cloned = dashboardService.cloneDashboard(dashboardId, userId, newName);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Dashboard cloned successfully");
            response.put("newDashboardId", cloned.getId());
            response.put("dashboard", cloned);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error cloning dashboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to clone dashboard: " + e.getMessage()));
        }
    }
    @GetMapping("/{dashboardId}/export")
    public ResponseEntity<?> exportDashboard(@PathVariable Long dashboardId) {
        try {
            String jsonData = dashboardService.exportDashboardAsJson(dashboardId);
            if (jsonData == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Dashboard not found"));
            }
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=dashboard-" + dashboardId + ".json")
                .body(jsonData);
        } catch (Exception e) {
            log.error("Error exporting dashboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to export dashboard: " + e.getMessage()));
        }
    }
    @PostMapping("/import/{userId}")
    public ResponseEntity<?> importDashboard(@PathVariable Long userId, @RequestBody String jsonData) {
        try {
            DashboardConfiguration imported = dashboardService.importDashboardFromJson(jsonData, userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Dashboard imported successfully");
            response.put("dashboardId", imported.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error importing dashboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to import dashboard: " + e.getMessage()));
        }
    }
    @GetMapping("/public/all")
    public ResponseEntity<?> getPublicDashboards() {
        try {
            List<DashboardConfiguration> dashboards = dashboardService.getPublicDashboards();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", dashboards.size());
            response.put("dashboards", dashboards);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching public dashboards", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to fetch public dashboards: " + e.getMessage()));
        }
    }
    @GetMapping("/recommendations/{userId}/{dashboardId}")
    public ResponseEntity<?> getRecommendations(@PathVariable Long userId, @PathVariable Long dashboardId) {
        try {
            List<Map<String, Object>> recommendations = 
                personalizationService.getPersonalizedRecommendations(userId, dashboardId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("recommendations", recommendations);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching recommendations", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to fetch recommendations: " + e.getMessage()));
        }
    }
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}
