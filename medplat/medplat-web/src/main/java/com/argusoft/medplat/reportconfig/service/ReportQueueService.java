package com.argusoft.medplat.reportconfig.service;


/**
 * <p>
 * Defined methods for report queue service
 * </p>
 *
 * @author sneha
 * @since 11-01-2021 02:15
 */
public interface ReportQueueService {

    /**
     * Starts report thread from given tenant
     *
     * @param tenantMaster An instance of AppTenantMaster
     */
//    void startReportThread(AppTenantMaster tenantMaster);
    void startReportThread();

    /**
     * Updates status of report queue from processed to new
     */
    void updateStatusFromProcessedToNewOnServerStartup();

    /**
     * Deletes old reports based on given number of days
     * @param numberOfDays A number of days from system configuration
     */
    void deleteOldReports(Integer numberOfDays);
}
