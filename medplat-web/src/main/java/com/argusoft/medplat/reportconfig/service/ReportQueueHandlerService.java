package com.argusoft.medplat.reportconfig.service;

import com.argusoft.medplat.reportconfig.model.ReportOfflineDetails;

/**
 * <p>
 * Defines method for report queue handler service
 * </p>
 *
 * @author sneha
 * @since 12-01-2021 03:52
 */
public interface ReportQueueHandlerService {

    /**
     * Process report, generate pdf and upload it
     *
     * @param reportOfflineDetails A report to be process
     */
    void handleReportQueue(ReportOfflineDetails reportOfflineDetails);
}
