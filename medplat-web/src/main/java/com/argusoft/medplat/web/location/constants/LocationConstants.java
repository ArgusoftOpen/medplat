/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.constants;

import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.infrastructure.dto.SchoolDataBean;
import com.argusoft.medplat.mobile.dto.HealthInfrastructureBean;
import com.argusoft.medplat.mobile.dto.LocationMasterDataBean;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Define constants for location.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
public class LocationConstants {

    private LocationConstants() {
        throw new IllegalStateException("Utility Class");
    }

    public static List<LocationMasterDataBean> allLocationMasterDataBeans = new LinkedList();
    public static List<HealthInfrastructureBean> allHealthInfrastructureForMobile = new LinkedList();

    public static List<SchoolDataBean> allSchoolsForMobile = new LinkedList();

    /**
     * <p>
     * Define constants for location types.
     * </p>
     *
     * @author kunjan
     * @since 26/08/20 10:19 AM
     */
    public static class LocationType {

        private LocationType() {
            throw new IllegalStateException("Utility Class");
        }

        public static final String STATE = "S";
        public static final String DISTRICT = "D";
        public static final String CORPORATION = "C";
        public static final String BLOCK = "B";
        public static final String ZONE = "Z";
        public static final String UHC = "U";
        public static final String PHC = "P";
        public static final String SUBCENTER = "SC";
        public static final String VILLAGE = "V";
        public static final String AREA = "A";
        public static final String ASHA_AREA = "AA";
        public static final String ANM_AREA = "ANM";
        public static final String ANGANWADI_AREA = "ANG";
    }

    public static int getLocationLevel(String type) {
        switch (type) {
            case FamilyHealthSurveyServiceConstants.LOCATION_STATE:
                return 0;
            case FamilyHealthSurveyServiceConstants.LOCATION_REGION:
                return 1;
            case FamilyHealthSurveyServiceConstants.LOCATION_DISTRICT:
            case FamilyHealthSurveyServiceConstants.LOCATION_CORPORATION:
                return 2;
            case FamilyHealthSurveyServiceConstants.LOCATION_BLOCK:
            case FamilyHealthSurveyServiceConstants.LOCATION_ZONE:
                return 3;
            case FamilyHealthSurveyServiceConstants.LOCATION_PHC:
            case FamilyHealthSurveyServiceConstants.LOCATION_UHC:
                return 4;
            case FamilyHealthSurveyServiceConstants.LOCATION_SUBCENTER:
            case FamilyHealthSurveyServiceConstants.LOCATION_ANM_AREA:
                return 5;
            case FamilyHealthSurveyServiceConstants.LOCATION_VILLAGE:
            case FamilyHealthSurveyServiceConstants.LOCATION_URBAN_AREA:
            case FamilyHealthSurveyServiceConstants.LOCATION_ANGANWADI_AREA:
                return 6;
            case FamilyHealthSurveyServiceConstants.LOCATION_AREA:
            case FamilyHealthSurveyServiceConstants.LOCATION_ASHA_AREA:
                return 7;
            default:
                return 0;
        }
    }
}
