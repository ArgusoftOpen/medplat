package com.argusoft.medplat.dashboard.aientabled.repository;
import com.argusoft.medplat.dashboard.aientabled.model.DashboardDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface DashboardDataSourceRepository extends JpaRepository<DashboardDataSource, Long> {
    List<DashboardDataSource> findBySourceType(String sourceType);
    Optional<DashboardDataSource> findBySourceName(String sourceName);
    List<DashboardDataSource> findByIsActive(Boolean isActive);
    List<DashboardDataSource> findByRealTimeEnabled(Boolean realTimeEnabled);
    List<DashboardDataSource> findByCreatedBy(Long createdBy);
}
