/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.dashboard.fhs.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Constants related to family health survey service</p>
 *
 * @author prateek
 * @since 27/08/20 12:00 AM
 */
public class FamilyHealthSurveyServiceConstants {

    private FamilyHealthSurveyServiceConstants() {
        throw new IllegalStateException("Utility Class");
    }

    private static final String VERIFIED = "Verified";
    private static final String REVERIFICATION = "Reverification";

    //FHS FAMILY STATES
    public static final String FHS_FAMILY_STATE_ARCHIVED = "com.argusoft.imtecho.family.state.archived";
    public static final String FHS_FAMILY_STATE_ARCHIVED_FHSR_REVERIFICATION = "com.argusoft.imtecho.family.state.archived.fhsr.reverification";
    public static final String FHS_FAMILY_STATE_ARCHIVED_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.archived.fhw.reverified";
    public static final String FHS_FAMILY_STATE_ARCHIVED_FHSR_VERIFIED = "com.argusoft.imtecho.family.state.archived.fhsr.verified";
    public static final String FHS_FAMILY_STATE_ARCHIVED_MO_REVERIFICATION = "com.argusoft.imtecho.family.state.archived.mo.reverification";
    public static final String FHS_FAMILY_STATE_ARCHIVED_MO_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.archived.mo.fhw.reverified";
    public static final String FHS_FAMILY_STATE_ARCHIVED_MO_VERIFIED = "com.argusoft.imtecho.family.state.archived.mo.verified";
    public static final String FHS_FAMILY_STATE_ARCHIVED_EMRI_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.archived.emri.fhw.reverified";

    public static final String FHS_FAMILY_STATE_MERGED = "com.argusoft.imtecho.family.state.merged";
    public static final String FHS_FAMILY_STATE_UNVERIFIED = "com.argusoft.imtecho.family.state.unverified";
    public static final String FHS_FAMILY_STATE_TEMPORARY = "com.argusoft.imtecho.family.state.temporary";
    public static final String FHS_FAMILY_STATE_MIGRATED = "com.argusoft.imtecho.family.state.migrated";
    public static final String FHS_FAMILY_STATE_MIGRATED_OUT_OF_STATE = "com.argusoft.imtecho.family.state.migrated.outofstate";
    public static final String FHS_FAMILY_STATE_MIGRATED_LFU = "com.argusoft.imtecho.family.state.migrated.lfu";

    public static final String FHS_FAMILY_STATE_NEW = "com.argusoft.imtecho.family.state.new";
    public static final String FHS_FAMILY_STATE_NEW_FHSR_VERIFIED = "com.argusoft.imtecho.family.state.new.fhsr.verified";
    public static final String FHS_FAMILY_STATE_NEW_FHSR_REVERIFICATION = "com.argusoft.imtecho.family.state.new.fhsr.reverification";
    public static final String FHS_FAMILY_STATE_NEW_MO_VERIFIED = "com.argusoft.imtecho.family.state.new.mo.verified";
    public static final String FHS_FAMILY_STATE_NEW_MO_REVERIFICATION = "com.argusoft.imtecho.family.state.new.mo.reverification";
    public static final String FHS_FAMILY_STATE_NEW_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.new.fhw.reverified";
    public static final String FHS_FAMILY_STATE_NEW_ARCHIVED_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.new.archived.fhw.reverified";
    public static final String FHS_FAMILY_STATE_NEW_ARCHIVED_MO_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.new.archived.mo.fhw.reverified";
    public static final String FHS_FAMILY_STATE_NEW_MO_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.new.mo.fhw.reverified";

    public static final String FHS_FAMILY_STATE_VERIFIED = "com.argusoft.imtecho.family.state.verified";
    public static final String FHS_FAMILY_STATE_PROCESSING = "com.argusoft.imtecho.family.state.processing";
    public static final String FHS_FAMILY_STATE_EMRI_VERIFIED_OK = "com.argusoft.imtecho.family.state.emri.verified.ok";
    public static final String FHS_FAMILY_STATE_EMRI_REVERIFICATION = "com.argusoft.imtecho.family.state.emri.fhw.reverification";
    public static final String FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.emri.fhw.reverified";
    public static final String FHS_FAMILY_STATE_ORPHAN = "com.argusoft.imtecho.family.state.orphan";
    public static final String FHS_FAMILY_STATE_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.fhw.reverified";
    public static final String FHS_FAMILY_STATE_MO_REVERIFICATION = "com.argusoft.imtecho.family.state.mo.reverification";
    public static final String FHS_FAMILY_STATE_MO_REVERIFIED = "com.argusoft.imtecho.family.state.mo.reverified";
    public static final String FHS_FAMILY_STATE_MO_VERIFIED = "com.argusoft.imtecho.family.state.mo.verified";
    public static final String FHS_FAMILY_STATE_MO_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.mo.fhw.reverified";

    //CFHC FAMILY STATES
    public static final String CFHC_FAMILY_STATE_UNVERIFIED = "CFHC_FU";                    //UNVERIFIED

    public static final String CFHC_FAMILY_STATE_VERIFIED = "CFHC_FV";                      //VERIFIED (When existing family verified from mobile)
    public static final String CFHC_FAMILY_STATE_NEW = "CFHC_FN";                           //VERIFIED (When a family added from mobile)
    public static final String CFHC_FAMILY_STATE_IN_REVERIFICATION = "CFHC_FIR";            //IN REVERIFICATION (When sent for reverification by MO)
    public static final String CFHC_FAMILY_STATE_GVK_IN_REVERIFICATION = "CFHC_GVK_FIR";    //IN REVERIFICATION (When sent for reverification by GVK)
    public static final String CFHC_FAMILY_STATE_GVK_VERIFIED = "CFHC_GVK_FV";              //VERIFIED (When verified by GVK)
    public static final String CFHC_FAMILY_STATE_MO_VERIFIED = "CFHC_MO_FV";                //VERIFIED (When verified by MO)
    public static final String CFHC_FAMILY_STATE_GVK_REVERIFIED = "CFHC_GVK_FRV";           //VERIFIED (When reverified by MOBILE after GVK sent for reverification)
    public static final String CFHC_FAMILY_STATE_MO_REVERIFIED = "CFHC_MO_FRV";             //VERIFIED (When reverified by MOBILE after MO sent for reverification)
    public static final String CFHC_FAMILY_STATE_GVK_REVERIFICATION_POOL = "CFHC_GVK_FRVP"; //VERIFIED (When in a pool for reverification by gvk)
    public static final String CFHC_FAMILY_STATE_MO_REVERIFICATION_POOL = "CFHC_MO_FRVP";   //VERIFIED (When in a pool for reverification by MO)

    //IDSP FAMILY AND MEMBER STATES
    public static final String IDSP_FAMILY_STATE_IDSP_TEMP = "IDSP_TEMP";
    public static final String IDSP_MEMBER_STATE_IDSP_TEMP = "IDSP_TEMP";

    //FHS MEMBER STATES
    public static final String FHS_MEMBER_STATE_MIGRATED = "com.argusoft.imtecho.member.state.migrated";
    public static final String FHS_MEMBER_STATE_MIGRATED_OUT_OF_STATE = "com.argusoft.imtecho.member.state.migrated.outofstate";
    public static final String FHS_MEMBER_STATE_MIGRATED_LFU = "com.argusoft.imtecho.member.state.migrated.lfu";

    public static final String FHS_MEMBER_STATE_ARCHIVED = "com.argusoft.imtecho.member.state.archived";
    public static final String FHS_MEMBER_STATE_ARCHIVED_FHSR_REVERIFICATION = "com.argusoft.imtecho.member.state.archived.fhsr.reverification";
    public static final String FHS_MEMBER_STATE_ARCHIVED_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.archived.fhw.reverified";
    public static final String FHS_MEMBER_STATE_ARCHIVED_FHSR_VERIFIED = "com.argusoft.imtecho.member.state.archived.fhsr.verified";
    public static final String FHS_MEMBER_STATE_ARCHIVED_MO_REVERIFICATION = "com.argusoft.imtecho.member.state.archived.mo.reverification";
    public static final String FHS_MEMBER_STATE_ARCHIVED_MO_VERIFIED = "com.argusoft.imtecho.member.state.archived.mo.verified";
    public static final String FHS_MEMBER_STATE_ARCHIVED_MO_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.archived.fhw.reverified";
    public static final String FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL = "com.argusoft.imtecho.family.state.emri.verification.pool";

    public static final String FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_VERIFIED = "com.argusoft.imtecho.family.state.emri.verification.pool.verified";
    public static final String FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_ARCHIVED = "com.argusoft.imtecho.family.state.emri.verification.pool.archived";
    public static final String FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_DEAD = "com.argusoft.imtecho.family.state.emri.verification.pool.dead";
    public static final String FHS_FAMILY_STATE_EMRI_VERIFIED_OK_VERIFIED = "com.argusoft.imtecho.family.state.emri.verified.ok.verified";
    public static final String FHS_FAMILY_STATE_EMRI_VERIFIED_OK_DEAD = "com.argusoft.imtecho.family.state.emri.verified.ok.dead";
    public static final String FHS_FAMILY_STATE_EMRI_VERIFIED_OK_ARCHIVED = "com.argusoft.imtecho.family.state.emri.verified.ok.archived";
    public static final String FHS_FAMILY_STATE_EMRI_REVERIFICATION_VERIFIED = "com.argusoft.imtecho.family.state.emri.fhw.reverification.verified";
    public static final String FHS_FAMILY_STATE_EMRI_REVERIFICATION_DEAD = "com.argusoft.imtecho.family.state.emri.fhw.reverification.dead";
    public static final String FHS_FAMILY_STATE_EMRI_REVERIFICATION_ARCHIVED = "com.argusoft.imtecho.family.state.emri.fhw.reverification.archived";
    public static final String FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_VERIFIED = "com.argusoft.imtecho.family.state.emri.fhw.reverified.verified";
    public static final String FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_DEAD = "com.argusoft.imtecho.family.state.emri.fhw.reverified.dead";
    public static final String FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_ARCHIVED = "com.argusoft.imtecho.family.state.emri.fhw.reverified.archived";

    public static final String FHS_MEMBER_STATE_DEAD = "com.argusoft.imtecho.member.state.dead";
    public static final String FHS_MEMBER_STATE_DEAD_FHSR_REVERIFICATION = "com.argusoft.imtecho.member.state.dead.fhsr.reverification";
    public static final String FHS_MEMBER_STATE_DEAD_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.dead.fhw.reverified";
    public static final String FHS_MEMBER_STATE_DEAD_FHSR_VERIFIED = "com.argusoft.imtecho.member.state.dead.fhsr.verified";
    public static final String FHS_MEMBER_STATE_DEAD_MO_REVERIFICATION = "com.argusoft.imtecho.member.state.dead.mo.reverification";
    public static final String FHS_MEMBER_STATE_DEAD_MO_VERIFIED = "com.argusoft.imtecho.member.state.dead.mo.verified";
    public static final String FHS_MEMBER_STATE_DEAD_MO_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.dead.mo.fhw.reverified";

    public static final String FHS_MEMBER_STATE_UNVERIFIED = "com.argusoft.imtecho.member.state.unverified";
    public static final String FHS_MEMBER_STATE_VERIFIED = "com.argusoft.imtecho.member.state.verified";
    public static final String FHS_MEMBER_STATE_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.fhw.reverified";
    public static final String FHS_MEMBER_STATE_MO_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.mo.fhw.reverified";
    public static final String FHS_MEMBER_STATE_NEW_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.new.fhw.reverified";
    public static final String FHS_MEMBER_STATE_NEW = "com.argusoft.imtecho.member.state.new";
    public static final String FHS_MEMBER_STATE_TEMPORARY = "com.argusoft.imtecho.member.state.temporary";
    public static final String FHS_MEMBER_STATE_ORPHAN = "com.argusoft.imtecho.member.state.orphan";
    public static final String FHS_MEMBER_STATE_NEW_ARCHIVED_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.new.archived.fhw.reverified";

    //CFHC MEMBER STATES
    public static final String CFHC_MEMBER_STATE_UNVERIFIED = "CFHC_MU";
    public static final String CFHC_MEMBER_STATE_VERIFIED = "CFHC_MV";
    public static final String CFHC_MEMBER_STATE_NEW = "CFHC_MN";
    public static final String CFHC_MEMBER_STATE_IN_REVERIFICATION = "CFHC_MIR";
    public static final String CFHC_MEMBER_STATE_MO_VERIFIED = "CFHC_MMOV";
    public static final String CFHC_MEMBER_STATE_DEAD = "CFHC_MD";

    // NCD MEMBER STATES
    public static final String NCD_MEMBER_STATE_NEW = "NCD_MN";
    public static final String NCD_FAMILY_STATE_NEW = "NCD_FN";

    //MEMBER BASIC STATES
    public static final String FHS_MEMBER_BASIC_STATE_VERIFIED = VERIFIED;
    public static final String FHS_MEMBER_BASIC_STATE_NEW = "NEW";

    //Family Health Service Type
    public static final String FHS_MEMBER_TYPE = "com.argusoft.imtecho.member.type";

    public static final String FHS_VERIFIED_MEMBER_COUNT = "VERIFIED_MEMBERS";
    public static final String FHS_VERIFIED_FAMILY_COUNT = "VERIFIED_FAMILIES";
    public static final String FHS_UNVERIFIED_MEMBER_COUNT = "UNVERIFIED_MEMBERS";
    public static final String FHS_UNVERIFIED_FAMILY_COUNT = "UNVERIFIED_FAMILIES";

    public static final double EMRI_VERIFIED_PERCENTAGE = 1d;
    public static final double EMRI_ARCHIVED_PERCENTAGE = 1d;
    public static final double EMRI_MIGRATED_PERCENTAGE = 1d;

    public static final List<String> FHS_TO_BE_PROCESSED_CRITERIA_FAMILY_STATES = new ArrayList<>();

    public static final List<String> VALID_MEMBERS_BASIC_STATES = new ArrayList<>();

    //FHS FAMILY CRITERIA STATE LIST
    public static final List<String> FHS_VERIFIED_CRITERIA_FAMILY_STATES = new ArrayList<>();
    public static final List<String> FHS_ARCHIVED_CRITERIA_FAMILY_STATES = new ArrayList<>();
    public static final List<String> FHS_NEW_CRITERIA_FAMILY_STATES = new ArrayList<>();
    public static final List<String> FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES = new ArrayList<>();
    public static final List<String> FHS_MIGRATED_CRITERIA_FAMILY_STATES = new ArrayList<>();
    public static final List<String> FHS_TOTAL_MEMBERS_CRITERIA_FAMILY_STATES = new ArrayList<>();
    public static final List<String> FHS_TOTAL_VERIFIED_FAMILY_CRITERIA_FAMILY_STATES = new ArrayList<>();

    //FHS MEMBER CRITERIA STATE LIST
    public static final List<String> FHS_IN_REVERIFICATION_MEMBER_STATES = new ArrayList<>();
    public static final List<String> FHS_DEAD_CRITERIA_MEMBER_STATES = new ArrayList<>();
    public static final List<String> FHS_ARCHIVED_CRITERIA_MEMBER_STATES = new ArrayList<>();
    public static final List<String> FHS_MIGRATED_CRITERIA_MEMBER_STATES = new ArrayList<>();
    public static final List<String> FHS_TOTAL_MEMBERS_CRITERIA_MEMBER_STATES = new ArrayList<>();
    public static final List<String> FHS_INACTIVE_CRITERIA_MEMBER_STATES = new ArrayList<>();

    // FHS FAMILY STATES MAP
    public static final Map<String, String> FHS_FAMILY_VERIFICATION_DISPLAY_STATES_ARCHIVE = new HashMap<>();
    public static final Map<String, String> FHS_FAMILY_VERIFICATION_DISPLAY_STATES_NEW = new HashMap<>();
    public static final Map<String, Map<String, String>> FHS_FAMILY_VERIFICATION_STATES = new HashMap<>();

    // FHS MEMBER STATES MAP
    public static final Map<String, String> FHS_MEMBER_VERIFICATION_DISPLAY_STATES_ARCHIVE = new HashMap<>();
    public static final Map<String, String> FHS_MEMBER_VERIFICATION_DISPLAY_STATES_DEAD = new HashMap<>();
    public static final Map<String, Map<String, String>> FHS_MEMBER_VERIFICATION_STATES = new HashMap<>();

    //MO VERIFICATION STATES MAP
    public static final Map<String, String> MO_FAMILY_VERIFICATION_DISPLAY_STATES_ARCHIVE = new HashMap<>();
    public static final Map<String, String> MO_FAMILY_VERIFICATION_DISPLAY_STATES_NEW = new HashMap<>();
    public static final Map<String, Map<String, String>> MO_FAMILY_VERIFICATION_STATES = new HashMap<>();
    public static final Map<String, String> MO_MEMBER_VERIFICATION_DISPLAY_STATES_ARCHIVE = new HashMap<>();
    public static final Map<String, String> MO_MEMBER_VERIFICATION_DISPLAY_STATES_DEAD = new HashMap<>();
    public static final Map<String, Map<String, String>> MO_MEMBER_VERIFICATION_STATES = new HashMap<>();

    //FHS MEMBER VERIFICATION STATES LIST
    public static final List<String> FHS_MEMBER_VERIFICATION_STATE_ARCHIVED = new ArrayList<>();
    public static final List<String> FHS_MEMBER_VERIFICATION_STATE_UNVERIFIED = new ArrayList<>();
    public static final List<String> FHS_MEMBER_VERIFICATION_STATE_VERIFIED = new ArrayList<>();
    public static final List<String> FHS_MEMBER_VERIFICATION_STATE_REVERIFICATION = new ArrayList<>();
    public static final List<String> FHS_MEMBER_VERIFICATION_STATE_NEW = new ArrayList<>();
    public static final List<String> FHS_MEMBER_VERIFICATION_STATE_DEAD = new ArrayList<>();

    public static final String LOCATION_STATE = "S";
    public static final String LOCATION_REGION = "R";
    public static final String LOCATION_DISTRICT = "D";
    public static final String LOCATION_CORPORATION = "C";
    public static final String LOCATION_BLOCK = "B";
    public static final String LOCATION_ZONE = "Z";
    public static final String LOCATION_UHC = "U";
    public static final String LOCATION_PHC = "P";
    public static final String LOCATION_SUBCENTER = "SC";
    public static final String LOCATION_URBAN_AREA = "UA";
    public static final String LOCATION_VILLAGE = "V";
    public static final String LOCATION_AREA = "A";
    public static final String LOCATION_ANGANWADI_AREA = "ANG";
    public static final String LOCATION_ANM_AREA = "ANM";
    public static final String LOCATION_ASHA_AREA = "AA";

    public static final Map<String, Integer> LOCATION_TYPES_AND_LEVELS = new HashMap<>();

    public static final String FHS_FAMILY_ROLE_FHW = "FHW";

    //Anganwadi states
    public static final String ANGANWADI_STATE_ARCHIVED = "com.argusoft.imtecho.anganwadi.state.archived";
    public static final String ANGANWADI_STATE_ACTIVE = "com.argusoft.imtecho.anganwadi.state.active";

    static {
        //FHS_MEMBER_VERIFICATION_STATE_ARCHIVED
        FHS_MEMBER_VERIFICATION_STATE_ARCHIVED.add(FHS_MEMBER_STATE_ARCHIVED_FHSR_VERIFIED);
        FHS_MEMBER_VERIFICATION_STATE_ARCHIVED.add(FHS_MEMBER_STATE_ARCHIVED_FHW_REVERIFIED);
        FHS_MEMBER_VERIFICATION_STATE_ARCHIVED.add(FHS_MEMBER_STATE_ARCHIVED);
        FHS_MEMBER_VERIFICATION_STATE_ARCHIVED.add(FHS_MEMBER_STATE_ARCHIVED_MO_VERIFIED);

        //FHS_MEMBER_VERIFICATION_STATE_UNVERIFIED
        FHS_MEMBER_VERIFICATION_STATE_UNVERIFIED.add(FHS_MEMBER_STATE_UNVERIFIED);

        //FHS_MEMBER_VERIFICATION_STATE_VERIFIED
        FHS_MEMBER_VERIFICATION_STATE_VERIFIED.add(FHS_MEMBER_STATE_VERIFIED);
        FHS_MEMBER_VERIFICATION_STATE_VERIFIED.add(FHS_MEMBER_STATE_FHW_REVERIFIED);
        FHS_MEMBER_VERIFICATION_STATE_VERIFIED.add(FHS_MEMBER_STATE_MO_FHW_REVERIFIED);

        //FHS_MEMBER_VERIFICATION_STATE_REVERIFICATION
        FHS_MEMBER_VERIFICATION_STATE_REVERIFICATION.add(FHS_MEMBER_STATE_DEAD_FHSR_REVERIFICATION);
        FHS_MEMBER_VERIFICATION_STATE_REVERIFICATION.add(FHS_MEMBER_STATE_DEAD_MO_REVERIFICATION);
        FHS_MEMBER_VERIFICATION_STATE_REVERIFICATION.add(FHS_MEMBER_STATE_ARCHIVED_FHSR_REVERIFICATION);
        FHS_MEMBER_VERIFICATION_STATE_REVERIFICATION.add(FHS_MEMBER_STATE_ARCHIVED_MO_REVERIFICATION);

        //FHS_MEMBER_VERIFICATION_STATE_NEW
        FHS_MEMBER_VERIFICATION_STATE_NEW.add(FHS_MEMBER_STATE_NEW);
        FHS_MEMBER_VERIFICATION_STATE_NEW.add(FHS_MEMBER_STATE_NEW_FHW_REVERIFIED);

        //FHS_MEMBER_VERIFICATION_STATE_DEAD
        FHS_MEMBER_VERIFICATION_STATE_DEAD.add(FHS_MEMBER_STATE_DEAD_FHW_REVERIFIED);
        FHS_MEMBER_VERIFICATION_STATE_DEAD.add(FHS_MEMBER_STATE_DEAD_MO_VERIFIED);
        FHS_MEMBER_VERIFICATION_STATE_DEAD.add(FHS_MEMBER_STATE_DEAD_FHSR_VERIFIED);
        FHS_MEMBER_VERIFICATION_STATE_DEAD.add(FHS_MEMBER_STATE_DEAD_MO_FHW_REVERIFIED);
        FHS_MEMBER_VERIFICATION_STATE_DEAD.add(FHS_MEMBER_STATE_DEAD);

        //FHS_TO_BE_PROCESSED_CRITERIA_FAMILY_STATES
        FHS_TO_BE_PROCESSED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_UNVERIFIED);
        FHS_TO_BE_PROCESSED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_ORPHAN);

        //VALID_MEMBERS_BASIC_STATES
        VALID_MEMBERS_BASIC_STATES.add(FHS_MEMBER_BASIC_STATE_VERIFIED);
        VALID_MEMBERS_BASIC_STATES.add(FHS_MEMBER_BASIC_STATE_NEW);

        //FHS_VERIFIED_CRITERIA_FAMILY_STATES
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_VERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_MERGED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_FHW_REVERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_VERIFIED_OK);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_MO_REVERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_MO_FHW_REVERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_DEAD);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_VERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_VERIFIED_OK_DEAD);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_VERIFIED_OK_ARCHIVED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_DEAD);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_VERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_VERIFIED_OK_VERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_ARCHIVED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_ARCHIVED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_VERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_NEW);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_VERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_IN_REVERIFICATION);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_GVK_IN_REVERIFICATION);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_GVK_VERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_MO_VERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_GVK_REVERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_MO_REVERIFIED);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_GVK_REVERIFICATION_POOL);
        FHS_VERIFIED_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_MO_REVERIFICATION_POOL);

        //FHS_ARCHIVED_CRITERIA_FAMILY_STATES
        FHS_ARCHIVED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_ARCHIVED);
        FHS_ARCHIVED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_ARCHIVED_FHW_REVERIFIED);
        FHS_ARCHIVED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_ARCHIVED_FHSR_VERIFIED);
        FHS_ARCHIVED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_ARCHIVED_MO_VERIFIED);
        FHS_ARCHIVED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_ARCHIVED_MO_FHW_REVERIFIED);
        FHS_ARCHIVED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_NEW_ARCHIVED_MO_FHW_REVERIFIED);
        FHS_ARCHIVED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_NEW_ARCHIVED_FHW_REVERIFIED);
        FHS_ARCHIVED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_ARCHIVED_EMRI_FHW_REVERIFIED);

        //FHS_NEW_CRITERIA_FAMILY_STATES
        FHS_NEW_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_NEW);
        FHS_NEW_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_NEW_FHSR_VERIFIED);
        FHS_NEW_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_NEW_FHW_REVERIFIED);
        FHS_NEW_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_NEW_MO_VERIFIED);
        FHS_NEW_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_NEW_MO_FHW_REVERIFIED);
        FHS_NEW_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_NEW);

        //FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES
        FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_ARCHIVED_FHSR_REVERIFICATION);
        FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_ARCHIVED_MO_REVERIFICATION);
        FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_MO_REVERIFICATION);
        FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_NEW_FHSR_REVERIFICATION);
        FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_NEW_MO_REVERIFICATION);
        FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_REVERIFICATION);
        FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_REVERIFICATION_DEAD);
        FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_REVERIFICATION_ARCHIVED);
        FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_EMRI_REVERIFICATION_VERIFIED);
        FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_IN_REVERIFICATION);
        FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES.add(CFHC_FAMILY_STATE_GVK_IN_REVERIFICATION);

        //FHS_IN_REVERIFICATION_MEMBER_STATES
        FHS_IN_REVERIFICATION_MEMBER_STATES.add(FHS_MEMBER_STATE_DEAD_FHSR_REVERIFICATION);
        FHS_IN_REVERIFICATION_MEMBER_STATES.add(FHS_MEMBER_STATE_DEAD_MO_REVERIFICATION);
        FHS_IN_REVERIFICATION_MEMBER_STATES.add(FHS_MEMBER_STATE_ARCHIVED_FHSR_REVERIFICATION);
        FHS_IN_REVERIFICATION_MEMBER_STATES.add(FHS_MEMBER_STATE_ARCHIVED_MO_REVERIFICATION);
        FHS_IN_REVERIFICATION_MEMBER_STATES.add(CFHC_MEMBER_STATE_IN_REVERIFICATION);

        //FHS_TOTAL_MEMBERS_CRITERIA_FAMILY_STATES
        FHS_TOTAL_MEMBERS_CRITERIA_FAMILY_STATES.addAll(FHS_NEW_CRITERIA_FAMILY_STATES);
        FHS_TOTAL_MEMBERS_CRITERIA_FAMILY_STATES.addAll(FHS_VERIFIED_CRITERIA_FAMILY_STATES);

        //FHS_TOTAL_VERIFIED_FAMILY_CRITERIA_FAMILY_STATES
        FHS_TOTAL_VERIFIED_FAMILY_CRITERIA_FAMILY_STATES.addAll(FHS_NEW_CRITERIA_FAMILY_STATES);
        FHS_TOTAL_VERIFIED_FAMILY_CRITERIA_FAMILY_STATES.addAll(FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        FHS_TOTAL_VERIFIED_FAMILY_CRITERIA_FAMILY_STATES.addAll(FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES);
        FHS_TOTAL_VERIFIED_FAMILY_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_TEMPORARY);

        //FHS_TOTAL_MEMBERS_CRITERIA_MEMBER_STATES
        FHS_TOTAL_MEMBERS_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_FHW_REVERIFIED);
        FHS_TOTAL_MEMBERS_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_VERIFIED);
        FHS_TOTAL_MEMBERS_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_NEW);

        //FHS_MIGRATED_CRITERIA_MEMBER_STATES
        FHS_MIGRATED_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_MIGRATED);
        FHS_MIGRATED_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_MIGRATED_OUT_OF_STATE);
        FHS_MIGRATED_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_MIGRATED_LFU);

        //FHS_MIGRATED_CRITERIA_FAMILY_STATES
        FHS_MIGRATED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_MIGRATED);
        FHS_MIGRATED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_MIGRATED_OUT_OF_STATE);
        FHS_MIGRATED_CRITERIA_FAMILY_STATES.add(FHS_FAMILY_STATE_MIGRATED_LFU);

        //FHS_FAMILY_VERIFICATION_DISPLAY_STATES_ARCHIVE
        FHS_FAMILY_VERIFICATION_DISPLAY_STATES_ARCHIVE.put(FHS_FAMILY_STATE_ARCHIVED_FHSR_VERIFIED, VERIFIED);
        FHS_FAMILY_VERIFICATION_DISPLAY_STATES_ARCHIVE.put(FHS_FAMILY_STATE_ARCHIVED_FHSR_REVERIFICATION, REVERIFICATION);

        //FHS_FAMILY_VERIFICATION_DISPLAY_STATES_NEW
        FHS_FAMILY_VERIFICATION_DISPLAY_STATES_NEW.put(FHS_FAMILY_STATE_NEW_FHSR_VERIFIED, VERIFIED);
        FHS_FAMILY_VERIFICATION_DISPLAY_STATES_NEW.put(FHS_FAMILY_STATE_NEW_FHSR_REVERIFICATION, REVERIFICATION);

        //FHS_FAMILY_VERIFICATION_STATES
        FHS_FAMILY_VERIFICATION_STATES.put(FHS_FAMILY_STATE_ARCHIVED, FHS_FAMILY_VERIFICATION_DISPLAY_STATES_ARCHIVE);
        FHS_FAMILY_VERIFICATION_STATES.put(FHS_FAMILY_STATE_NEW, FHS_FAMILY_VERIFICATION_DISPLAY_STATES_NEW);

        //FHS_MEMBER_VERIFICATION_DISPLAY_STATES_ARCHIVE
        FHS_MEMBER_VERIFICATION_DISPLAY_STATES_ARCHIVE.put(FHS_MEMBER_STATE_ARCHIVED_FHSR_VERIFIED, VERIFIED);
        FHS_MEMBER_VERIFICATION_DISPLAY_STATES_ARCHIVE.put(FHS_MEMBER_STATE_ARCHIVED_FHSR_REVERIFICATION, REVERIFICATION);

        //FHS_MEMBER_VERIFICATION_DISPLAY_STATES_DEAD
        FHS_MEMBER_VERIFICATION_DISPLAY_STATES_DEAD.put(FHS_MEMBER_STATE_DEAD_FHSR_VERIFIED, VERIFIED);
        FHS_MEMBER_VERIFICATION_DISPLAY_STATES_DEAD.put(FHS_MEMBER_STATE_DEAD_FHSR_REVERIFICATION, REVERIFICATION);

        //FHS_MEMBER_VERIFICATION_STATES
        FHS_MEMBER_VERIFICATION_STATES.put(FHS_MEMBER_STATE_ARCHIVED, FHS_MEMBER_VERIFICATION_DISPLAY_STATES_ARCHIVE);
        FHS_MEMBER_VERIFICATION_STATES.put(FHS_MEMBER_STATE_DEAD, FHS_MEMBER_VERIFICATION_DISPLAY_STATES_DEAD);

        //MO_FAMILY_VERIFICATION_DISPLAY_STATES_ARCHIVE
        MO_FAMILY_VERIFICATION_DISPLAY_STATES_ARCHIVE.put(FHS_FAMILY_STATE_ARCHIVED_MO_VERIFIED, VERIFIED);
        MO_FAMILY_VERIFICATION_DISPLAY_STATES_ARCHIVE.put(FHS_FAMILY_STATE_ARCHIVED_MO_REVERIFICATION, REVERIFICATION);

        //MO_FAMILY_VERIFICATION_DISPLAY_STATES_NEW
        MO_FAMILY_VERIFICATION_DISPLAY_STATES_NEW.put(FHS_FAMILY_STATE_NEW_MO_VERIFIED, VERIFIED);
        MO_FAMILY_VERIFICATION_DISPLAY_STATES_NEW.put(FHS_FAMILY_STATE_NEW_MO_REVERIFICATION, REVERIFICATION);

        //MO_FAMILY_VERIFICATION_STATES
        MO_FAMILY_VERIFICATION_STATES.put(FHS_FAMILY_STATE_ARCHIVED_FHW_REVERIFIED, MO_FAMILY_VERIFICATION_DISPLAY_STATES_ARCHIVE);
        MO_FAMILY_VERIFICATION_STATES.put(FHS_FAMILY_STATE_NEW_FHW_REVERIFIED, MO_FAMILY_VERIFICATION_DISPLAY_STATES_NEW);

        //MO_MEMBER_VERIFICATION_DISPLAY_STATES_ARCHIVE
        MO_MEMBER_VERIFICATION_DISPLAY_STATES_ARCHIVE.put(FHS_MEMBER_STATE_ARCHIVED_MO_VERIFIED, VERIFIED);
        MO_MEMBER_VERIFICATION_DISPLAY_STATES_ARCHIVE.put(FHS_MEMBER_STATE_ARCHIVED_MO_REVERIFICATION, REVERIFICATION);

        //MO_MEMBER_VERIFICATION_DISPLAY_STATES_DEAD
        MO_MEMBER_VERIFICATION_DISPLAY_STATES_DEAD.put(FHS_MEMBER_STATE_DEAD_MO_VERIFIED, VERIFIED);
        MO_MEMBER_VERIFICATION_DISPLAY_STATES_DEAD.put(FHS_MEMBER_STATE_DEAD_MO_REVERIFICATION, REVERIFICATION);

        //MO_MEMBER_VERIFICATION_STATES
        MO_MEMBER_VERIFICATION_STATES.put(FHS_MEMBER_STATE_ARCHIVED_FHW_REVERIFIED, MO_MEMBER_VERIFICATION_DISPLAY_STATES_ARCHIVE);
        MO_MEMBER_VERIFICATION_STATES.put(FHS_MEMBER_STATE_DEAD_FHW_REVERIFIED, MO_MEMBER_VERIFICATION_DISPLAY_STATES_DEAD);

        FHS_DEAD_CRITERIA_MEMBER_STATES.add(CFHC_MEMBER_STATE_DEAD);
        FHS_DEAD_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_DEAD);
        FHS_DEAD_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_DEAD_FHSR_VERIFIED);
        FHS_DEAD_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_DEAD_FHW_REVERIFIED);
        FHS_DEAD_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_DEAD_MO_FHW_REVERIFIED);
        FHS_DEAD_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_DEAD_MO_VERIFIED);

        FHS_ARCHIVED_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_ARCHIVED);
        FHS_ARCHIVED_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_ARCHIVED_FHW_REVERIFIED);
        FHS_ARCHIVED_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_ARCHIVED_FHSR_VERIFIED);
        FHS_ARCHIVED_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_ARCHIVED_MO_VERIFIED);
        FHS_ARCHIVED_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_ARCHIVED_MO_FHW_REVERIFIED);

        FHS_INACTIVE_CRITERIA_MEMBER_STATES.add(FHS_MEMBER_STATE_MIGRATED);
        FHS_INACTIVE_CRITERIA_MEMBER_STATES.add(IDSP_MEMBER_STATE_IDSP_TEMP);
        FHS_INACTIVE_CRITERIA_MEMBER_STATES.addAll(FHS_DEAD_CRITERIA_MEMBER_STATES);
        FHS_INACTIVE_CRITERIA_MEMBER_STATES.addAll(FHS_ARCHIVED_CRITERIA_MEMBER_STATES);

        //LOCATION_TYPES_AND_LEVELS
        LOCATION_TYPES_AND_LEVELS.put(LOCATION_STATE, 1);
        LOCATION_TYPES_AND_LEVELS.put(LOCATION_REGION, 2);
        LOCATION_TYPES_AND_LEVELS.put(LOCATION_DISTRICT, 3);
        LOCATION_TYPES_AND_LEVELS.put(LOCATION_CORPORATION, 3);
        LOCATION_TYPES_AND_LEVELS.put(LOCATION_BLOCK, 4);
        LOCATION_TYPES_AND_LEVELS.put(LOCATION_ZONE, 4);
        LOCATION_TYPES_AND_LEVELS.put(LOCATION_PHC, 5);
        LOCATION_TYPES_AND_LEVELS.put(LOCATION_UHC, 5);
        LOCATION_TYPES_AND_LEVELS.put(LOCATION_SUBCENTER, 6);
        LOCATION_TYPES_AND_LEVELS.put(LOCATION_URBAN_AREA, 6);
        LOCATION_TYPES_AND_LEVELS.put(LOCATION_VILLAGE, 7);
    }
}
