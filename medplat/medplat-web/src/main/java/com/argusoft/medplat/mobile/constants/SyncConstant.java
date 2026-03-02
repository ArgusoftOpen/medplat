package com.argusoft.medplat.mobile.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Add an understandable class description here
 * </p>
 *
 * @author rahul
 * @since 21/05/21 5:08 PM
 */
public class SyncConstant {
    private SyncConstant() {
        throw new IllegalStateException("Utility Class");
    }

    public static final String HEALTH_INFRASTRUCTURE_BEAN = "HealthInfrastructureBean";
    public static final String LABEL_BEAN = "LabelBean";
    public static final String LOCATION_MASTER_BEAN = "LocationMasterBean";
    public static final String LOCATION_TYPE_BEAN = "LocationTypeBean";
    public static final String MENU_BEAN = "MenuBean";
    public static final String SCHOOL_BEAN = "SchoolBean";

    public static final String ANNOUNCEMENT_BEAN = "AnnouncementBean";
    public static final String COVID_TRAVELLERS_INFO_BEAN = "CovidTravellersInfoBean";
    public static final String DATA_QUALITY_BEAN = "DataQualityBean";
    public static final String FAMILY_BEAN = "FamilyBean";
    public static final String FHW_SERVICE_DETAIL_BEAN = "FHWServiceDetailBean";
    public static final String LIBRARY_BEAN = "LibraryBean";
    public static final String LIST_VALUE_BEAN = "ListValueBean";
    public static final String MEMBER_CBAC_DETAIL_BEAN = "MemberCbacDetailBean";
    public static final String MIGRATED_FAMILY_BEAN = "MigratedFamilyBean";
    public static final String MIGRATED_MEMBERS_BEAN = "MigratedMembersBean";
    public static final String NOTIFICATION_BEAN = "NotificationBean";
    public static final String COURSE_BEAN = "CourseBean";
    public static final String LMS_USER_METADATA_BEAN = "LmsUserMetadataBean";
    public static final String CHARDHAM_TOURIST_BEAN = "ChardhamTouristsBean";
    public static final String MO_CONFIRMED_BEAN = "MoConfirmedBean";
    public static final String DRUG_INVENTORY_BEAN = "DrugInventoryBean";
    public static final String USER_HEALTH_INFRA_BEAN = "UserHealthInfraBean";
    public static final String FAMILY_AVAILABILITY_BEAN = "FamilyAvailabilityBean";

    public static final Map<String, Boolean> SYSTEM_BEANS = new HashMap<>();

    static {
        SYSTEM_BEANS.put(HEALTH_INFRASTRUCTURE_BEAN, true);
        SYSTEM_BEANS.put(LABEL_BEAN, true);
        SYSTEM_BEANS.put(LOCATION_MASTER_BEAN, true);
        SYSTEM_BEANS.put(LOCATION_TYPE_BEAN, true);
        SYSTEM_BEANS.put(MENU_BEAN, true);
        SYSTEM_BEANS.put(SCHOOL_BEAN, true);
        SYSTEM_BEANS.put(LIST_VALUE_BEAN, true);
    }
}
