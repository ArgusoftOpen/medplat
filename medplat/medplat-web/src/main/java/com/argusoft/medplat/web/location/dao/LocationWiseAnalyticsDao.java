/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.web.location.model.LocationWiseAnalytics;

/**
 *
 * <p>
 * Define methods for location wise analytics.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
public interface LocationWiseAnalyticsDao extends GenericDao<LocationWiseAnalytics, Integer> {

    /**
     * Update FHS details.
     */
    void updateFHSDetail();

    /**
     * Update FHS details for report.
     */
    void updateFHSDataForReport();

    /**
     * Update sickle cell details for report.
     */
    void updateSickleCellDataForReport();

    /**
     * Update disease details for report.
     */
    void updateDiseaseDataForReport();

    /**
     * Update women survey details.
     */
    void updateWomenSurveyData();

    /**
     * Update family and member details.
     */
    void updateFamilyAndMemberData();

    /**
     * Update child data for report.
     */
    void updateChildDataForReport();

    /**
     * Insert FHS progress details.
     */
    void insertFHSProgressData();

}
