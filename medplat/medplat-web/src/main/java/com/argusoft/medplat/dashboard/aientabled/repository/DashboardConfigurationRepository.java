package com.argusoft.medplat.dashboard.aientabled.repository;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface DashboardConfigurationRepository extends JpaRepository<DashboardConfiguration, Long> {
    List<DashboardConfiguration> findByUserId(Long userId);
    List<DashboardConfiguration> findByUserIdAndIsActive(Long userId, Boolean isActive);
    Optional<DashboardConfiguration> findByUserIdAndIsDefault(Long userId, Boolean isDefault);
    List<DashboardConfiguration> findByDashboardType(String dashboardType);
    @Query("SELECT d FROM DashboardConfiguration d WHERE d.isPublic = true AND d.isActive = true")
    List<DashboardConfiguration> findAllPublicDashboards();
    @Query("SELECT d FROM DashboardConfiguration d WHERE d.userId = :userId OR :role LIKE CONCAT('%', d.roleBasedAccess, '%')")
    List<DashboardConfiguration> findAccessibleDashboards(@Param("userId") Long userId, @Param("role") String role);
    List<DashboardConfiguration> findByAiEnabledAndIsActive(Boolean aiEnabled, Boolean isActive);
}
