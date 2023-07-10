/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.reportconfig.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.reportconfig.model.ReportMaster;

import java.util.List;
import java.util.UUID;

/**
 *
 * <p>
 * Define methods for report master.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
public interface ReportMasterDao extends GenericDao<ReportMaster, Integer> {

    /**
     * Retrieves report master details with report id and report code.
     * @param id Report id.
     * @param code Report code.
     * @return Returns report master details.
     */
    ReportMaster getReportMasterWithParamterByIdOrCode(Integer id, String code);

    /**
     * Retrieves report master details by UUId and report code.
     * @param uuid UUID.
     * @param code Report code.
     * @return Returns report master details.
     */
    ReportMaster getReportMasterWithParamterByUUIDOrCode(UUID uuid, String code);

    /**
     * Retrieves list of report master ids.
     * @return Returns list of report master ids.
     */
    List<Integer> getAllIds();

    /**
     * Retrieves list of active records by report name, parent group id, sub group id.
     * @param reportName Report name.
     * @param offset The number of data to skip before starting to fetch details.
     * @param limit The number of data need to fetch.
     * @param groupAssociated Parent group id.
     * @param subGroupAssociated Sub group id.
     * @param groupdId Group id.
     * @param subGroupId Sub group id.
     * @param userId User id.
     * @param menuType Type of menu.
     * @param sortBy Sort by field name.
     * @param sortOrder Sort on field name.
     * @return Returns list of reports details.
     */
    List<ReportMaster> getActiveReports(String reportName, Integer offset,
            Integer limit, Boolean groupAssociated, Boolean subGroupAssociated,
            Integer groupdId, Integer subGroupId, String userId, String menuType, String sortBy, String sortOrder);

    /**
     * Is report code available or not.
     * @param code Report code.
     * @param id Report id.
     * @return Returns true/false based on code available or not.
     */
    Boolean isCodeAvailable(String code, Integer id);

    /**
     * Is code available by UUID.
     * @param code Report code.
     * @param uuid UUID.
     * @return Returns true/false based on code available or not.
     */
    Boolean isCodeAvailableByUUID(String code, UUID uuid);

}
