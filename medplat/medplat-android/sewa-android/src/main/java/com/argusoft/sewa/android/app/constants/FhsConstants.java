package com.argusoft.sewa.android.app.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FhsConstants {

    private FhsConstants() {
        throw new IllegalStateException("Utility Class");
    }

    private static List<String> stringList;

    //Relation With Head Of Family
    public static final String RELATION_FATHER = "FATHER";
    public static final String RELATION_MOTHER = "MOTHER";
    public static final String RELATION_SON = "SON";
    public static final String RELATION_DAUGHTER = "DAUGHTER";
    public static final String RELATION_BROTHER = "BROTHER";
    public static final String RELATION_SISTER = "SISTER";
    public static final String RELATION_GRANDFATHER = "GRANDFATHER";
    public static final String RELATION_GRANDMOTHER = "GRANDMOTHER";
    public static final String RELATION_NEPHEW = "NEPHEW";
    public static final String RELATION_NIECE = "NIECE";
    public static final String RELATION_SON_IN_LAW = "SON-IN-LAW";
    public static final String RELATION_DAUGHTER_IN_LAW = "DAUGHTER-IN-LAW";
    public static final String RELATION_GRANDSON = "GRANDSON";
    public static final String RELATION_GRANDDAUGHTER = "GRANDDAUGHTER";
    public static final String RELATION_HUSBAND = "HUSBAND";
    public static final String RELATION_WIFE = "WIFE";

    // NCD SCREENING
    public static final String COMPLETED_STATE = "COMPLETED_STATE";
    public static final String INCOMPLETE_STATE = "INCOMPLETE_STATE";
    public static final String STARTED_STATE = "STARTED_STATE";
    public static final String NOT_STARTED_STATE = "NOT_STARTED_STATE";

    public static final List<String> MALE_RELATION = Collections.unmodifiableList(getMaleRelation());
    public static final List<String> FEMALE_RELATION = Collections.unmodifiableList(getFemaleRelation());

    private static List<String> getMaleRelation() {
        stringList = new ArrayList<>();
        stringList.add(RELATION_FATHER);
        stringList.add(RELATION_SON);
        stringList.add(RELATION_HUSBAND);
        stringList.add(RELATION_BROTHER);
        stringList.add(RELATION_GRANDFATHER);
        stringList.add(RELATION_NEPHEW);
        stringList.add(RELATION_SON_IN_LAW);
        stringList.add(RELATION_GRANDSON);
        return stringList;
    }

    private static List<String> getFemaleRelation() {
        stringList = new ArrayList<>();
        stringList.add(RELATION_MOTHER);
        stringList.add(RELATION_DAUGHTER);
        stringList.add(RELATION_WIFE);
        stringList.add(RELATION_SISTER);
        stringList.add(RELATION_GRANDMOTHER);
        stringList.add(RELATION_NIECE);
        stringList.add(RELATION_DAUGHTER_IN_LAW);
        stringList.add(RELATION_GRANDDAUGHTER);
        return stringList;
    }

    //FHS Family States
    public static final String FHS_FAMILY_STATE_ARCHIVED = "com.argusoft.imtecho.family.state.archived";
    public static final String FHS_FAMILY_STATE_UNVERIFIED = "com.argusoft.imtecho.family.state.unverified";
    public static final String FHS_FAMILY_STATE_VERIFIED = "com.argusoft.imtecho.family.state.verified";
    public static final String FHS_FAMILY_STATE_NEW = "com.argusoft.imtecho.family.state.new";
    public static final String FHS_FAMILY_STATE_ORPHAN = "com.argusoft.imtecho.family.state.orphan";
    public static final String FHS_FAMILY_STATE_MERGED = "com.argusoft.imtecho.family.state.merged";
    public static final String FHS_FAMILY_STATE_TEMPORARY = "com.argusoft.imtecho.family.state.temporary";
    public static final String FHS_FAMILY_STATE_MIGRATED = "com.argusoft.imtecho.family.state.migrated";
    public static final String FHS_FAMILY_STATE_MIGRATED_OUT_OF_STATE = "com.argusoft.imtecho.family.state.migrated.outofstate";
    public static final String FHS_FAMILY_STATE_MIGRATED_LFU = "com.argusoft.imtecho.family.state.migrated.lfu";
    public static final String FHS_FAMILY_STATE_ARCHIVED_FHSR_VERIFIED = "com.argusoft.imtecho.family.state.archived.fhsr.verified";
    public static final String FHS_FAMILY_STATE_ARCHIVED_FHSR_REVERIFICATION = "com.argusoft.imtecho.family.state.archived.fhsr.reverification";
    public static final String FHS_FAMILY_STATE_ARCHIVED_MO_VERIFIED = "com.argusoft.imtecho.family.state.archived.mo.verified";
    public static final String FHS_FAMILY_STATE_ARCHIVED_MO_REVERIFICATION = "com.argusoft.imtecho.family.state.archived.mo.reverification";
    public static final String FHS_FAMILY_STATE_ARCHIVED_MO_REVERIFIED = "com.argusoft.imtecho.family.state.archived.mo.reverified";
    public static final String FHS_FAMILY_STATE_ARCHIVED_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.archived.fhw.reverified";
    public static final String FHS_FAMILY_STATE_ARCHIVED_MO_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.archived.mo.fhw.reverified";
    public static final String FHS_FAMILY_STATE_ARCHIVED_EMRI_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.archived.emri.fhw.reverified";
    public static final String FHS_FAMILY_STATE_NEW_FHSR_VERIFIED = "com.argusoft.imtecho.family.state.new.fhsr.verified";
    public static final String FHS_FAMILY_STATE_NEW_MO_VERIFIED = "com.argusoft.imtecho.family.state.new.mo.verified";
    public static final String FHS_FAMILY_STATE_NEW_FHSR_REVERIFICATION = "com.argusoft.imtecho.family.state.new.fhsr.reverification";
    public static final String FHS_FAMILY_STATE_NEW_MO_REVERIFICATION = "com.argusoft.imtecho.family.state.new.mo.reverification";
    public static final String FHS_FAMILY_STATE_NEW_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.new.fhw.reverified";
    public static final String FHS_FAMILY_STATE_NEW_MO_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.new.mo.fhw.reverified";
    public static final String FHS_FAMILY_STATE_NEW_ARCHIVED_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.new.archived.fhw.reverified";
    public static final String FHS_FAMILY_STATE_NEW_ARCHIVED_MO_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.new.archived.mo.fhw.reverified";
    public static final String FHS_FAMILY_STATE_EMRI_VERIFIED_OK = "com.argusoft.imtecho.family.state.emri.verified.ok";
    public static final String FHS_FAMILY_STATE_EMRI_REVERIFICATION = "com.argusoft.imtecho.family.state.emri.fhw.reverification";
    public static final String FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.emri.fhw.reverified";
    public static final String FHS_FAMILY_STATE_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.fhw.reverified";
    public static final String FHS_FAMILY_STATE_MO_REVERIFICATION = "com.argusoft.imtecho.family.state.mo.reverification";
    public static final String FHS_FAMILY_STATE_MO_REVERIFIED = "com.argusoft.imtecho.family.state.mo.reverified";
    public static final String FHS_FAMILY_STATE_MO_FHW_REVERIFIED = "com.argusoft.imtecho.family.state.mo.fhw.reverified";
    public static final String FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_VERIFIED = "com.argusoft.imtecho.family.state.emri.verification.pool.verified";
    public static final String FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_ARCHIVED = "com.argusoft.imtecho.family.state.emri.verification.pool.archived";
    public static final String FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_DEAD = "com.argusoft.imtecho.family.state.emri.verification.pool.dead";
    public static final String FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL = "com.argusoft.imtecho.family.state.emri.verification.pool";
    public static final String FHS_FAMILY_STATE_EMRI_VERIFIED_OK_VERIFIED = "com.argusoft.imtecho.family.state.emri.verified.ok.verified";
    public static final String FHS_FAMILY_STATE_EMRI_VERIFIED_OK_DEAD = "com.argusoft.imtecho.family.state.emri.verified.ok.dead";
    public static final String FHS_FAMILY_STATE_EMRI_VERIFIED_OK_ARCHIVED = "com.argusoft.imtecho.family.state.emri.verified.ok.archived";
    public static final String FHS_FAMILY_STATE_EMRI_REVERIFICATION_VERIFIED = "com.argusoft.imtecho.family.state.emri.fhw.reverification.verified";
    public static final String FHS_FAMILY_STATE_EMRI_REVERIFICATION_DEAD = "com.argusoft.imtecho.family.state.emri.fhw.reverification.dead";
    public static final String FHS_FAMILY_STATE_EMRI_REVERIFICATION_ARCHIVED = "com.argusoft.imtecho.family.state.emri.fhw.reverification.archived";
    public static final String FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_VERIFIED = "com.argusoft.imtecho.family.state.emri.fhw.reverified.verified";
    public static final String FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_DEAD = "com.argusoft.imtecho.family.state.emri.fhw.reverified.dead";
    public static final String FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_ARCHIVED = "com.argusoft.imtecho.family.state.emri.fhw.reverified.archived";
    public static final String CFHC_FAMILY_STATE_VERIFIED_LOCAL = "CFHC_FV_LOCAL";          //VERIFIED (When existing family verified from mobile)
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

    //IDSP - Only used in IDSP
    public static final String IDSP_FAMILY_STATE_IDSP_TEMP = "IDSP_TEMP";
    public static final String IDSP_MEMBER_STATE_IDSP_TEMP = "IDSP_TEMP";

    //FHS Member States
    public static final String CFHC_MEMBER_STATE_VERIFIED = "CFHC_MV";
    public static final String CFHC_MEMBER_STATE_NEW = "CFHC_MN";
    public static final String CFHC_MEMBER_STATE_IN_REVERIFICATION = "CFHC_MIR";
    public static final String CFHC_MEMBER_STATE_MO_VERIFIED = "CFHC_MMOV";
    public static final String CFHC_MEMBER_STATE_DEAD = "CFHC_MD";
    public static final String FHS_MEMBER_STATE_DEAD_FHSR_REVERIFICATION = "com.argusoft.imtecho.member.state.dead.fhsr.reverification";
    public static final String FHS_MEMBER_STATE_DEAD_MO_REVERIFICATION = "com.argusoft.imtecho.member.state.dead.mo.reverification";
    public static final String FHS_MEMBER_STATE_DEAD = "com.argusoft.imtecho.member.state.dead";
    public static final String FHS_MEMBER_STATE_DEAD_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.dead.fhw.reverified";
    public static final String FHS_MEMBER_STATE_DEAD_FHSR_VERIFIED = "com.argusoft.imtecho.member.state.dead.fhsr.verified";
    public static final String FHS_MEMBER_STATE_DEAD_MO_VERIFIED = "com.argusoft.imtecho.member.state.dead.mo.verified";
    public static final String FHS_MEMBER_STATE_DEAD_MO_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.dead.mo.fhw.reverified";
    public static final String FHS_MEMBER_STATE_ARCHIVED = "com.argusoft.imtecho.member.state.archived";
    public static final String FHS_MEMBER_STATE_ARCHIVED_FHSR_REVERIFICATION = "com.argusoft.imtecho.member.state.archived.fhsr.reverification";
    public static final String FHS_MEMBER_STATE_ARCHIVED_MO_REVERIFICATION = "com.argusoft.imtecho.member.state.archived.mo.reverification";
    public static final String FHS_MEMBER_STATE_ARCHIVED_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.archived.fhw.reverified";
    public static final String FHS_MEMBER_STATE_ARCHIVED_FHSR_VERIFIED = "com.argusoft.imtecho.member.state.archived.fhsr.verified";
    public static final String FHS_MEMBER_STATE_ARCHIVED_MO_VERIFIED = "com.argusoft.imtecho.member.state.archived.mo.verified";
    public static final String FHS_MEMBER_STATE_ARCHIVED_MO_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.archived.fhw.reverified";
    public static final String FHS_MEMBER_STATE_VERIFIED = "com.argusoft.imtecho.member.state.verified";
    public static final String FHS_MEMBER_STATE_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.fhw.reverified";
    public static final String FHS_MEMBER_STATE_MO_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.mo.fhw.reverified";
    public static final String FHS_MEMBER_STATE_NEW = "com.argusoft.imtecho.member.state.new";
    public static final String FHS_MEMBER_STATE_NEW_FHW_REVERIFIED = "com.argusoft.imtecho.member.state.new.fhw.reverified";
    public static final String FHS_MEMBER_STATE_MIGRATED = "com.argusoft.imtecho.member.state.migrated";
    public static final String FHS_MEMBER_STATE_MIGRATED_OUT_OF_STATE = "com.argusoft.imtecho.member.state.migrated.outofstate";
    public static final String FHS_MEMBER_STATE_MIGRATED_LFU = "com.argusoft.imtecho.member.state.migrated.lfu";
    public static final String FHS_MEMBER_STATE_UNVERIFIED = "com.argusoft.imtecho.member.state.unverified";

    //Family States List
    public static final List<String> FHS_VERIFIED_CRITERIA_FAMILY_STATES = Collections.unmodifiableList(getFhsVerifiedCriteriaFamilyStates());
    public static final List<String> FHS_NEW_CRITERIA_FAMILY_STATES = Collections.unmodifiableList(getFhsNewCriteriaFamilyStates());
    public static final List<String> FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES = Collections.unmodifiableList(getFhsInReverificationCriteriaFamilyStates());
    public static final List<String> FHS_ARCHIVED_CRITERIA_FAMILY_STATES = Collections.unmodifiableList(getFhsArchivedCriteriaFamilyStates());
    public static final List<String> FHS_MIGRATED_CRITERIA_FAMILY_STATES = Collections.unmodifiableList(getFhsMigratedCriteriaFamilyStates());
    public static final List<String> CFHC_FAMILY_STATES = Collections.unmodifiableList(getCfhcFamilyStates());
    public static final List<String> CFHC_VERIFIED_FAMILY_STATES = Collections.unmodifiableList(getCfhcVerifiedFamilyStates());
    public static final List<String> CFHC_IN_REVERIFICATION_FAMILY_STATES = Collections.unmodifiableList(getCfhcInReverificationFamilyStates());
    public static final List<String> FHS_ACTIVE_CRITERIA_FAMILY_STATES = Collections.unmodifiableList(getFhsActiveCriteriaFamilyStates());
    public static final List<String> FHS_INACTIVE_CRITERIA_FAMILY_STATES = Collections.unmodifiableList(getFhsInactiveCriteriaFamilyStates());

    //Member States List
    public static final List<String> FHS_ARCHIVED_CRITERIA_MEMBER_STATES = Collections.unmodifiableList(getFhsArchivedCriteriaMemberStates());
    public static final List<String> FHS_DEAD_CRITERIA_MEMBER_STATES = Collections.unmodifiableList(getFhsDeadCriteriaMemberStates());
    public static final List<String> FHS_IN_REVERIFICATION_MEMBER_STATES = Collections.unmodifiableList(getFhsInReverificationMemberStates());
    public static final List<String> FHS_VERIFIED_CRITERIA_MEMBER_STATES = Collections.unmodifiableList(getFhsVerifiedCriteriaMemberStates());
    public static final List<String> FHS_NEW_CRITERIA_MEMBER_STATES = Collections.unmodifiableList(getFhsNewCriteriaMemberStates());
    public static final List<String> FHS_MIGRATED_CRITERIA_MEMBER_STATES = Collections.unmodifiableList(getFhsMigratedCriteriaMemberStates());
    public static final List<String> FHS_ACTIVE_CRITERIA_MEMBER_STATES = Collections.unmodifiableList(getFhsActiveCriteriaMemberStates());
    public static final List<String> FHS_INACTIVE_CRITERIA_MEMBER_STATES = Collections.unmodifiableList(getFhsInactiveCriteriaMemberStates());

    private static List<String> getFhsVerifiedCriteriaFamilyStates() {
        stringList = new ArrayList<>();
        stringList.add(CFHC_FAMILY_STATE_VERIFIED);
        stringList.add(CFHC_FAMILY_STATE_MO_VERIFIED);
        stringList.add(CFHC_FAMILY_STATE_MO_REVERIFIED);
        stringList.add(CFHC_FAMILY_STATE_GVK_VERIFIED);
        stringList.add(CFHC_FAMILY_STATE_GVK_REVERIFIED);
        stringList.add(CFHC_FAMILY_STATE_GVK_REVERIFICATION_POOL);
        stringList.add(CFHC_FAMILY_STATE_MO_REVERIFICATION_POOL);
        stringList.add(FHS_FAMILY_STATE_VERIFIED);
        stringList.add(FHS_FAMILY_STATE_FHW_REVERIFIED);
        stringList.add(FHS_FAMILY_STATE_EMRI_VERIFIED_OK);
        stringList.add(FHS_FAMILY_STATE_MO_REVERIFIED);
        stringList.add(FHS_FAMILY_STATE_MO_FHW_REVERIFIED);
        stringList.add(FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED);
        stringList.add(FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_DEAD);
        stringList.add(FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_VERIFIED);
        stringList.add(FHS_FAMILY_STATE_EMRI_VERIFIED_OK_DEAD);
        stringList.add(FHS_FAMILY_STATE_EMRI_VERIFIED_OK_ARCHIVED);
        stringList.add(FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_DEAD);
        stringList.add(FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL);
        stringList.add(FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_VERIFIED);
        stringList.add(FHS_FAMILY_STATE_EMRI_VERIFIED_OK_VERIFIED);
        stringList.add(FHS_FAMILY_STATE_EMRI_IN_VERIFICATION_POOL_ARCHIVED);
        stringList.add(FHS_FAMILY_STATE_EMRI_FHW_REVERIFIED_ARCHIVED);
        return stringList;
    }

    private static List<String> getFhsNewCriteriaFamilyStates() {
        stringList = new ArrayList<>();
        stringList.add(CFHC_FAMILY_STATE_NEW);
        stringList.add(FHS_FAMILY_STATE_NEW);
        stringList.add(FHS_FAMILY_STATE_NEW_FHSR_VERIFIED);
        stringList.add(FHS_FAMILY_STATE_NEW_FHW_REVERIFIED);
        stringList.add(FHS_FAMILY_STATE_NEW_MO_VERIFIED);
        stringList.add(FHS_FAMILY_STATE_NEW_MO_FHW_REVERIFIED);
        return stringList;
    }

    private static List<String> getFhsInReverificationCriteriaFamilyStates() {
        stringList = new ArrayList<>();
        stringList.add(CFHC_FAMILY_STATE_IN_REVERIFICATION);
        stringList.add(CFHC_FAMILY_STATE_GVK_IN_REVERIFICATION);
        stringList.add(FHS_FAMILY_STATE_ARCHIVED_FHSR_REVERIFICATION);
        stringList.add(FHS_FAMILY_STATE_EMRI_REVERIFICATION);
        stringList.add(FHS_FAMILY_STATE_MO_REVERIFICATION);
        stringList.add(FHS_FAMILY_STATE_NEW_FHSR_REVERIFICATION);
        stringList.add(FHS_FAMILY_STATE_ARCHIVED_MO_REVERIFICATION);
        stringList.add(FHS_FAMILY_STATE_EMRI_REVERIFICATION_DEAD);
        stringList.add(FHS_FAMILY_STATE_EMRI_REVERIFICATION_ARCHIVED);
        stringList.add(FHS_FAMILY_STATE_EMRI_REVERIFICATION_VERIFIED);
        return stringList;
    }

    private static List<String> getFhsArchivedCriteriaFamilyStates() {
        stringList = new ArrayList<>();
        stringList.add(FHS_FAMILY_STATE_ARCHIVED);
        stringList.add(FHS_FAMILY_STATE_ARCHIVED_FHW_REVERIFIED);
        stringList.add(FHS_FAMILY_STATE_ARCHIVED_FHSR_VERIFIED);
        stringList.add(FHS_FAMILY_STATE_ARCHIVED_MO_VERIFIED);
        stringList.add(FHS_FAMILY_STATE_ARCHIVED_MO_FHW_REVERIFIED);
        stringList.add(FHS_FAMILY_STATE_NEW_ARCHIVED_MO_FHW_REVERIFIED);
        stringList.add(FHS_FAMILY_STATE_NEW_ARCHIVED_FHW_REVERIFIED);
        stringList.add(FHS_FAMILY_STATE_ARCHIVED_EMRI_FHW_REVERIFIED);
        stringList.add(FHS_FAMILY_STATE_ARCHIVED_MO_REVERIFIED);
        return stringList;
    }

    private static List<String> getFhsMigratedCriteriaFamilyStates() {
        stringList = new ArrayList<>();
        stringList.add(FHS_FAMILY_STATE_MIGRATED);
        stringList.add(FHS_FAMILY_STATE_MIGRATED_OUT_OF_STATE);
        stringList.add(FHS_FAMILY_STATE_MIGRATED_LFU);
        return stringList;
    }

    private static List<String> getCfhcFamilyStates() {
        stringList = new ArrayList<>();
        //Add all the CFHC States here
        stringList.add(CFHC_FAMILY_STATE_VERIFIED);
        stringList.add(CFHC_FAMILY_STATE_NEW);
        stringList.add(CFHC_FAMILY_STATE_GVK_VERIFIED);
        stringList.add(CFHC_FAMILY_STATE_GVK_REVERIFIED);
        stringList.add(CFHC_FAMILY_STATE_MO_VERIFIED);
        stringList.add(CFHC_FAMILY_STATE_MO_REVERIFIED);
        stringList.add(CFHC_FAMILY_STATE_IN_REVERIFICATION);
        stringList.add(CFHC_FAMILY_STATE_GVK_IN_REVERIFICATION);
        stringList.add(CFHC_FAMILY_STATE_GVK_REVERIFICATION_POOL);
        stringList.add(CFHC_FAMILY_STATE_MO_REVERIFICATION_POOL);
        return stringList;
    }

    private static List<String> getCfhcVerifiedFamilyStates() {
        stringList = new ArrayList<>();
        //Verified Families CFHC States
        stringList.add(CFHC_FAMILY_STATE_VERIFIED);
        stringList.add(CFHC_FAMILY_STATE_NEW);
        stringList.add(CFHC_FAMILY_STATE_GVK_REVERIFIED);
        stringList.add(CFHC_FAMILY_STATE_GVK_VERIFIED);
        stringList.add(CFHC_FAMILY_STATE_MO_VERIFIED);
        stringList.add(CFHC_FAMILY_STATE_MO_REVERIFIED);
        stringList.add(CFHC_FAMILY_STATE_GVK_REVERIFICATION_POOL);
        stringList.add(CFHC_FAMILY_STATE_MO_REVERIFICATION_POOL);
        return stringList;
    }

    private static List<String> getCfhcInReverificationFamilyStates() {
        stringList = new ArrayList<>();
        stringList.add(CFHC_FAMILY_STATE_IN_REVERIFICATION);
        stringList.add(CFHC_FAMILY_STATE_GVK_IN_REVERIFICATION);
        return stringList;
    }

    private static List<String> getFhsActiveCriteriaFamilyStates() {
        stringList = new ArrayList<>();
        stringList.add(FHS_FAMILY_STATE_TEMPORARY);
        stringList.addAll(FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        stringList.addAll(FHS_NEW_CRITERIA_FAMILY_STATES);
        return stringList;
    }

    private static List<String> getFhsInactiveCriteriaFamilyStates() {
        stringList = new ArrayList<>();
        stringList.addAll(FHS_ARCHIVED_CRITERIA_FAMILY_STATES);
        stringList.addAll(FHS_MIGRATED_CRITERIA_FAMILY_STATES);
        stringList.add(FHS_FAMILY_STATE_MERGED);
        stringList.add(FHS_FAMILY_STATE_UNVERIFIED);
        return stringList;
    }

    private static List<String> getFhsArchivedCriteriaMemberStates() {
        stringList = new ArrayList<>();
        stringList.add(FHS_MEMBER_STATE_ARCHIVED);
        stringList.add(FHS_MEMBER_STATE_ARCHIVED_FHW_REVERIFIED);
        stringList.add(FHS_MEMBER_STATE_ARCHIVED_FHSR_VERIFIED);
        stringList.add(FHS_MEMBER_STATE_ARCHIVED_MO_VERIFIED);
        stringList.add(FHS_MEMBER_STATE_ARCHIVED_MO_FHW_REVERIFIED);
        return stringList;
    }

    private static List<String> getFhsDeadCriteriaMemberStates() {
        stringList = new ArrayList<>();
        stringList.add(CFHC_MEMBER_STATE_DEAD);
        stringList.add(FHS_MEMBER_STATE_DEAD);
        stringList.add(FHS_MEMBER_STATE_DEAD_FHSR_VERIFIED);
        stringList.add(FHS_MEMBER_STATE_DEAD_FHW_REVERIFIED);
        stringList.add(FHS_MEMBER_STATE_DEAD_MO_FHW_REVERIFIED);
        stringList.add(FHS_MEMBER_STATE_DEAD_MO_VERIFIED);
        return stringList;
    }

    private static List<String> getFhsInReverificationMemberStates() {
        stringList = new ArrayList<>();
        stringList.add(FHS_MEMBER_STATE_DEAD_FHSR_REVERIFICATION);
        stringList.add(FHS_MEMBER_STATE_DEAD_MO_REVERIFICATION);
        stringList.add(FHS_MEMBER_STATE_ARCHIVED_FHSR_REVERIFICATION);
        stringList.add(FHS_MEMBER_STATE_ARCHIVED_MO_REVERIFICATION);
        stringList.add(CFHC_MEMBER_STATE_IN_REVERIFICATION);
        return stringList;
    }

    private static List<String> getFhsVerifiedCriteriaMemberStates() {
        stringList = new ArrayList<>();
        stringList.add(FHS_MEMBER_STATE_VERIFIED);
        stringList.add(FHS_MEMBER_STATE_FHW_REVERIFIED);
        stringList.add(FHS_MEMBER_STATE_MO_FHW_REVERIFIED);
        stringList.add(CFHC_MEMBER_STATE_VERIFIED);
        stringList.add(CFHC_MEMBER_STATE_MO_VERIFIED);
        return stringList;
    }

    private static List<String> getFhsNewCriteriaMemberStates() {
        stringList = new ArrayList<>();
        stringList.add(FHS_MEMBER_STATE_NEW);
        stringList.add(FHS_MEMBER_STATE_NEW_FHW_REVERIFIED);
        stringList.add(CFHC_MEMBER_STATE_NEW);
        return stringList;
    }

    private static List<String> getFhsMigratedCriteriaMemberStates() {
        stringList = new ArrayList<>();
        stringList.add(FHS_MEMBER_STATE_MIGRATED);
        stringList.add(FHS_MEMBER_STATE_MIGRATED_OUT_OF_STATE);
        stringList.add(FHS_MEMBER_STATE_MIGRATED_LFU);
        return stringList;
    }

    private static List<String> getFhsActiveCriteriaMemberStates() {
        stringList = new ArrayList<>();
        stringList.addAll(FHS_VERIFIED_CRITERIA_MEMBER_STATES);
        stringList.addAll(FHS_NEW_CRITERIA_MEMBER_STATES);
        return stringList;
    }

    private static List<String> getFhsInactiveCriteriaMemberStates() {
        stringList = new ArrayList<>();
        stringList.add(IDSP_MEMBER_STATE_IDSP_TEMP);
        stringList.addAll(FHS_MIGRATED_CRITERIA_MEMBER_STATES);
        stringList.addAll(FHS_DEAD_CRITERIA_MEMBER_STATES);
        stringList.addAll(FHS_ARCHIVED_CRITERIA_MEMBER_STATES);
        return stringList;
    }
}
