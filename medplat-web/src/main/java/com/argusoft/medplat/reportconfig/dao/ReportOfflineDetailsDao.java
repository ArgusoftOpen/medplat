package com.argusoft.medplat.reportconfig.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.reportconfig.model.ReportOfflineDetails;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Defines database method for report offline feature
 * </p>
 *
 * @author sneha
 * @since 11-01-2021 01:50
 */
public interface ReportOfflineDetailsDao extends GenericDao<ReportOfflineDetails, Integer> {

    /**
     * Returns a list of ReportOfflineDetails from given date
     *
     * @param toTime A date time to fetch sms
     * @param limit  A limit value
     * @return A list of ReportOfflineDetails
     */
    List<ReportOfflineDetails> retrieveOfflineReport(Date toTime, Integer limit);

    /**
     * Returns a list of ReportOfflineDetails of given user id
     *
     * @param userId A user id
     * @return A list of ReportOfflineDetails
     */
    List<ReportOfflineDetails> retrieveByUserId(Integer userId);

    /**
     * Update status of report offline from new to processed of given id
     *
     * @param id A report offline id
     */
    void markAsProcessed(Integer id);

    /**
     * Update status of  report offline from processed to ready for download and set the file id of given id
     *
     * @param id     A report offline id
     * @param fileId A file id
     */
    void markAsReadyForDownload(Integer id, Long fileId);

    /**
     * Update status of  report offline from processed to error and set the error string
     *
     * @param id     A report offline id
     * @param error Error string
     */
    void markAsError(Integer id, String error);

    /**
     * Updates status of report offline from processed to new
     */
    void updateStatusFromProcessedToNewOnServerStartup();

    /**
     * Returns a list of ReportOfflineDetails from given date
     *
     * @param toTime A date time to fetch sms
     * @return A list of ReportOfflineDetails
     */
    List<ReportOfflineDetails> retrieveOfflineReportByDate(Date toTime);
}
