/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.dao.impl;

import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.location.dao.LocationWiseAnalyticsDao;
import com.argusoft.medplat.web.location.model.LocationWiseAnalytics;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

/**
 *
 * <p>
 * Implementation of methods define in location wise analytics dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class LocationWiseAnalyticsDaoImpl extends GenericDaoImpl<LocationWiseAnalytics, Integer> implements LocationWiseAnalyticsDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFHSDetail() {
        String sqlQuery = "update location_wise_analytics "
                + "set fhs_imported_from_emamta_family = fhs_det.imported"
                + ",fhs_imported_from_emamta_member = fhs_det.imported_from_emamta_mem"
                + ",fhs_to_be_processed_family = fhs_det.toBeProcessed"
                + ",fhs_verified_family = fhs_det.Verified"
                + ",fhs_archived_family = fhs_det.Archived"
                + ",fhs_new_family = fhs_det.newFamily"
                + ",fhs_total_member = fhs_det.total_member"
                + ",fhs_inreverification_family = fhs_det.inReverification"
                + "from ("
                + "select loc.id, fam_det.imported,mem_det.imported_from_emamta_mem"
                + ",fam_det.toBeProcessed,fam_det.Verified ,fam_det.Archived ,fam_det.newFamily,mem_det.total_member,fam_det.inReverification"
                + "from location_master loc "
                + "left join (select fam.location_id"
                + ",sum(case when fam.created_by is null then 1 else 0 end) as imported_from_emamta_mem "
                + ",sum(case when fam.state in (:familyStateForTotalMembers) "
                + "    and mem.state in (:memberStateForTotalMembers) then 1 else 0 end) as total_member "
                + "from imt_family fam"
                + "inner join imt_member mem on fam.family_id = mem.family_id group by fam.location_id) as mem_det"
                + "on loc.id = mem_det.location_id"
                + "left join ("
                + "select location_id"
                + ",sum(case when fam.created_by is null then 1 else 0 end) as imported "
                + ",sum(case when fam.state in (:toBeProcessedState) then 1 else 0 end) as toBeProcessed"
                + ",sum(case when fam.state in (:verifiedState) then 1 else 0 end) as Verified"
                + ",sum(case when fam.state in (:archivedState) then 1 else 0 end) as Archived"
                + ",sum(case when fam.state in (:newState) then 1 else 0 end) as newFamily"
                + ",sum(case when fam.state in (:inReverificationState) then 1 else 0 end) as inReverification"
                + "from imt_family fam"
                + "group by location_id"
                + ") as fam_det on loc.id = fam_det.location_id) as fhs_det"
                + "where fhs_det.id = location_wise_analytics.loc_id;";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(sqlQuery);
        q.setParameterList("toBeProcessedState", FamilyHealthSurveyServiceConstants.FHS_TO_BE_PROCESSED_CRITERIA_FAMILY_STATES);
        q.setParameterList("verifiedState", FamilyHealthSurveyServiceConstants.FHS_VERIFIED_CRITERIA_FAMILY_STATES);
        q.setParameterList("archivedState", FamilyHealthSurveyServiceConstants.FHS_ARCHIVED_CRITERIA_FAMILY_STATES);
        q.setParameterList("newState", FamilyHealthSurveyServiceConstants.FHS_NEW_CRITERIA_FAMILY_STATES);
        q.setParameterList("inReverificationState", FamilyHealthSurveyServiceConstants.FHS_IN_REVERIFICATION_CRITERIA_FAMILY_STATES);
        q.setParameterList("familyStateForTotalMembers", FamilyHealthSurveyServiceConstants.FHS_TOTAL_MEMBERS_CRITERIA_FAMILY_STATES);
        q.setParameterList("memberStateForTotalMembers", FamilyHealthSurveyServiceConstants.FHS_TOTAL_MEMBERS_CRITERIA_MEMBER_STATES);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFHSDataForReport() {
        String query = "update location_wise_analytics lwa "
                + "set total_family = t.count "
                + "from (select location_id,count(*) from imt_family group by location_id) t "
                + "where t.location_id = lwa.loc_id";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.executeUpdate();
        query = "update location_wise_analytics lwa "
                + "set total_members = r.total_members, "
                + "total_alive = r.total_alive,total_female = r.total_female,total_male = r.total_male,total_women = r.total_women, "
                + " total_children = r.total_children,total_adolescents = r.total_adolescents "
                + "from (select f.location_id ,count(*) as total_members, "
                + "sum(case when m.state not in('com.argusoft.imtecho.member.state.dead','com.argusoft.imtecho.member.state.dead.fhsr.verified','com.argusoft.imtecho.member.state.dead.fhsr.reverification') then 1 else 0 end) as total_alive, "
                + "sum(case when m.gender = 'F' then 1 else 0 end) as total_female, "
                + "sum(case when m.gender = 'M' then 1 else 0 end) as total_male, "
                + "sum(case when m.gender = 'F' and extract(year from age(m.dob)) between 15 and 49 then 1 else 0 end) as total_women, "
                + "sum(case when extract(year from age(m.dob)) between 0 and 5 then 1 else 0 end) as total_children, "
                + "sum(case when extract(year from age(m.dob)) between 10 and 19 then 1 else 0 end) as total_adolescents "
                + "from "
                + "imt_member m inner join imt_family f on m.family_id = f.family_id "
                + "group by f.location_id "
                + ") r "
                + "where r.location_id = lwa.loc_id";
        q = getCurrentSession().createNativeQuery(query);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSickleCellDataForReport() {
        String query = "update location_wise_analytics lwa "
                + "set sickle_male = t.\"Female\",sickle_female = t.\"Male\",sickle_0_5 = t.\"0-5\",sickle_5_15 = t.\"5-15\","
                + "sickle_15_45  = t.\"15-45\",sickle_above_45 = t.\"Above 45\",sickle_bpl = t.\"BPL\","
                + "sickle_apl  = t.\"APL\",sickle_sc = t.\"SC\",sickle_st = t.\"ST\",sickle_obc = t.\"OBC\",sickle_gen = t.\"GEN\", "
                + "sickle_abandon = t.\"Abandon\",sickle_married = t.\"Married\",sickle_unmarried = t.\"Unmarried\",sickle_widow = t.\"Widow\","
                + "sickle_widower = t.\"Widower\" "
                + "from "
                + "( "
                + "select r1.location_id,"
                + "sum(case when r1.gender = 'F' then 1 else 0 end) as \"Female\","
                + "sum(case when r1.gender = 'M' then 1 else 0 end) as \"Male\","
                + "sum(case when date_part('year',age(dob)) >= 0 and date_part('year',age(dob)) < 5 then 1 else 0 end) as \"0-5\","
                + "sum(case when date_part('year',age(dob)) >= 5 and date_part('year',age(dob)) < 15 then 1 else 0 end) as \"5-15\","
                + "sum(case when date_part('year',age(dob)) >= 15 and date_part('year',age(dob)) < 45 then 1 else 0 end) as \"15-45\","
                + "sum(case when date_part('year',age(dob)) >= 45  then 1 else 0 end) as \"Above 45\","
                + "sum(case when r1.bpl_flag = true then 1 else 0 end) as \"BPL\","
                + "sum(case when r1.bpl_flag = false then 1 else 0 end) as \"APL\","
                + "sum(case when r1.value = 'SC' then 1 else 0 end) as \"SC\","
                + "sum(case when r1.value = 'ST' then 1 else 0 end) as \"ST\","
                + "sum(case when r1.value = 'OBC/SEBC' then 1 else 0 end) as \"OBC\","
                + "sum(case when r1.value = 'GENERAL' then 1 else 0 end) as \"GEN\","
                + "sum(case when r1.mvalue = 'ABANDON' then 1 else 0 end) as \"Abandon\","
                + "sum(case when r1.mvalue = 'MARRIED' then 1 else 0 end) as \"Married\","
                + "sum(case when r1.mvalue = 'UNMARRIED' then 1 else 0 end) as \"Unmarried\","
                + "sum(case when r1.mvalue = 'WIDOW' then 1 else 0 end) as \"Widow\","
                + "sum(case when r1.mvalue = 'WIDOWER' then 1 else 0 end) as \"Widower\" "
                + "from "
                + "(select * from "
                + "imt_member mem inner join imt_family fam on mem.family_id = fam.family_id "
                + "inner join imt_member_current_disease_rel cd on mem.id = cd.member_id "
                + "inner join listvalue_field_value_detail lfvd on lfvd.id = cast(fam.caste as bigint) "
                + "inner join "
                + "(select id,value as mvalue from listvalue_field_value_detail) r2 on r2.id = cast(mem.marital_status as bigint)  "
                + "where cd.current_disease_id = (select id from listvalue_field_value_detail where value = 'Sickle Cell Disease') "
                + ") r1 "
                + "group by (r1.location_id) "
                + ") t where t.location_id = lwa.loc_id";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDiseaseDataForReport() {
        String query = "update location_wise_analytics lwa "
                + "set blindness = t.\"Blindness\",hiv = t.\"HIV\",sickle_cell = t.\"Sickle Cell Disease\",diabetes = t.\"Diabetes\","
                + "leprosy = t.\"Leprosy\",thalessemia = t.\"Thalessemia\",tb = t.\"TB\" from "
                + "(select floc.location_id,"
                + "sum (case when floc.chronic_disease_id = 724 then 1 else 0 end) as \"Blindness\","
                + "sum (case when floc.chronic_disease_id = 715 then 1 else 0 end) as \"TB\","
                + "sum (case when floc.chronic_disease_id = 729 then 1 else 0 end) as \"Sickle Cell Disease\","
                + "sum (case when floc.chronic_disease_id = 726 then 1 else 0 end) as \"Diabetes\","
                + "sum (case when floc.chronic_disease_id = 730 then 1 else 0 end) as \"Thalessemia\","
                + "sum (case when floc.chronic_disease_id = 735 then 1 else 0 end) as \"HIV\","
                + "sum (case when floc.chronic_disease_id = 716 then 1 else 0 end) as \"Leprosy\" "
                + "from "
                + "((select * from imt_member_chronic_disease_rel cd union select * from imt_member_congenital_anomaly_rel cng union "
                + "select * from imt_member_current_disease_rel cud union select * from imt_member_eye_issue_rel ed ) dmem "
                + "left join imt_member mem "
                + "on dmem.member_id = mem.id "
                + "left join imt_family fam on fam.family_id = mem.family_id) floc  "
                + "group by floc.location_id) t "
                + "where t.location_id = lwa.loc_id";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateWomenSurveyData() {
        String query = "update location_wise_analytics lwa "
                + "set r11_15_49 = res.\"No of 15-49 yrs women in FHS\",r11_migrated = res.\"No of Women Migrated\",r11_dead = res.\"Death\","
                + "r11_eligible_couple = res.\"Eligible Couple\" from "
                + "(select t.location_id, "
                + "t.t1 as \"No of 15-49 yrs women in FHS\",t.t2 as \"No of Women Migrated\","
                + "t.t3 as \"Death\",t.t4 as \"Eligible Couple\" "
                + "from "
                + "(select fam.location_id, "
                + "count(*) as \"t1\","
                + "sum(case when mem.state = 'com.argusoft.imtecho.member.state.archived' then 1 else 0 end) as \"t2\","
                + "sum(case when mem.state = 'com.argusoft.imtecho.member.state.dead' then 1 else 0 end) as \"t3\","
                + "sum(case when lfvd.value = 'MARRIED' then 1 else 0 end) as \"t4\" "
                + "from imt_member mem,imt_family fam,listvalue_field_value_detail lfvd "
                + "where mem.family_id = fam.family_id  "
                + "and mem.gender = 'F' and extract(year from age(mem.dob)) between 15 and 49 "
                + "and lfvd.id = cast(mem.marital_status as bigint) "
                + "group by rollup(fam.location_id)) t ) res "
                + "where lwa.loc_id = res.location_id";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateFamilyAndMemberData() {
        String query = "update location_wise_analytics lwa "
                + "set new_fam = res.new_fam,deleted_fam = res.deleted_fam,new_mem = res.new_mem,deleted_mem = res.deleted_mem "
                + "from "
                + "(select fam.location_id,"
                + "sum(case when fam.state = 'com.argusoft.imtecho.family.state.new' then 1 else 0 end) as \"new_fam\","
                + "sum(case when fam.state = 'com.argusoft.imtecho.family.state.archived' then 1 else 0 end) as \"deleted_fam\","
                + "sum(case when mem.state = 'com.argusoft.imtecho.member.state.new' then 1 else 0 end) as \"new_mem\","
                + "sum(case when mem.state = 'com.argusoft.imtecho.member.state.archived' then 1 else 0 end) as \"deleted_mem\" "
                + "from imt_member mem,imt_family fam "
                + "where mem.family_id = fam.family_id   "
                + "group by fam.location_id) res "
                + "where lwa.loc_id = res.location_id";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateChildDataForReport() {
        String query = "update location_wise_analytics lwa "
                + "set child_0_5 = res.t1,child_dead = res.t3,child_new = res.t4, "
                + "child_migrated = res.t2 from  "
                + "(select fam.location_id, "
                + "count(*) as t1,"
                + "sum(case when mem.state = 'com.argusoft.imtecho.member.state.archived' then 1 else 0 end) as t2,"
                + "sum(case when mem.state = 'com.argusoft.imtecho.member.state.dead' then 1 else 0 end) as t3,"
                + "sum(case when mem.state = 'com.argusoft.imtecho.member.state.new' then 1 else 0 end) as t4 "
                + "from imt_member mem,imt_family fam "
                + "where mem.family_id = fam.family_id "
                + "and extract(year from age(mem.dob)) between 0 and 5  "
                + "group by fam.location_id) res "
                + "where lwa.loc_id = res.location_id";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertFHSProgressData() {
        String query = "insert into verfied_families_village_wise_records (loc_id,created_on,total,verified) "
                + "select loc_id,current_date,fhs_imported_from_emamta_family,fhs_verified_family+fhs_archived_family "
                + "from location_wise_analytics "
                + "where fhs_imported_from_emamta_family > 0 ";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.executeUpdate();
    }
}
