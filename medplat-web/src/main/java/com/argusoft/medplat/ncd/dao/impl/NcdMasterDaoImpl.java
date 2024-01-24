package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.NcdMasterDao;
import com.argusoft.medplat.ncd.dto.MemberDetailDto;
import com.argusoft.medplat.ncd.model.NcdMaster;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Repository
public class NcdMasterDaoImpl extends GenericDaoImpl<NcdMaster, Integer> implements NcdMasterDao {
    @Override
    public NcdMaster retriveByMemberIdAndDiseaseCode(Integer memberId, String diseaseCode) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<NcdMaster> criteriaQuery = criteriaBuilder.createQuery(NcdMaster.class);
        Root<NcdMaster> root = criteriaQuery.from(NcdMaster.class);
        Predicate checkMemberId = criteriaBuilder.equal(root.get("memberId"), memberId);
        Predicate checkDiseaseCode = criteriaBuilder.equal(root.get("diseaseCode"), diseaseCode);
        Predicate isActive = criteriaBuilder.equal(root.get("active"), true);
        criteriaQuery.select(root).where(criteriaBuilder.and(checkMemberId, checkDiseaseCode, isActive));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    @Override
    public List<MemberDetailDto> retrieveAllMembers(Integer userId,String type,Integer limit, Integer offset) {
        String whereClause = null;
        switch (type){
            case "patientList" :
                whereClause= "and status='SUSPECTED'";
                break;
            case "followupTreatement":
                whereClause= "and status like '%UNDERTREATMENT%'";
                break;
            case "followup":
                whereClause= "and status='FOLLOWUP'";
                break;
            case "followupReferral":
                whereClause= "and status like '%REFERRED_NO_VISIT%'";
                break;
            case "consultant":
                whereClause= "and status='REFERRED_CONSULTANT'";
                break;
            case "referDue":
            case "moReview":
            case "moReviewFollowup":
            case "CVCList":
            case "MOAlertList":
                break;
            default:
                return null;
        }

        String query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                        "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                        "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                        "members as(\n" +
                        "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                        "where is_active is true "+ whereClause + " group by member_id), member_with_location as (select members.*,imf.location_id as locationId,lm.name as village,\n" +
                "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                "inner join imt_member imm on members.id=imm.id\n" +
                "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                "left join location_master lm on lm.id=imf.location_id where imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY') and location_id in (select child_id\n" +
                "    from location_hierchy_closer_det\n" +
                "    where parent_id in (select uul.loc_id\n" +
                "\tfrom um_user_location uul where uul.user_id = " + userId +" and state='ACTIVE')))\n" +
                "select member_with_location.*,lm.name as \"subCenter\" from member_with_location \n" +
                "left join location_hierchy_closer_det lhcd on member_with_location.locationId=lhcd.child_id\n" +
                "left join location_master lm on lhcd.parent_id=lm.id\n" +
                "where parent_loc_type='SC' order by member_with_location.name ASC limit " + limit + " offset " + offset;

        if(type.equals("referDue")){
            query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                    "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                    "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                    "members as(select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data\n" +
                    "where is_active is true and status='REFERRED_MO' group by member_id),\n" +
                    "member_with_location as (select members.*,imf.location_id as locationId,lm.name as village,\n" +
                    "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name,\n" +
                    "date_part('year',age(imm.dob)) as age,imm.gender from members\n" +
                    "inner join imt_member imm on members.id=imm.id\n" +
                    "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                    "left join location_master lm on lm.id=imf.location_id where location_id in (select child_id\n" +
                    "from location_hierchy_closer_det\n" +
                    "where parent_id in (select uul.loc_id\n" +
                    "from um_user_location uul where uul.user_id = " + userId + " and state='ACTIVE'))),\n" +
                    "followup_data as (select distinct member_id from ncd_member_general_detail where (followup_place='doctor' or refferal_place='MO') and followup_date<=current_date\n" +
                    ")\n" +
                    "select member_with_location.*,lm.name as \"subCenter\" from member_with_location\n" +
                    "left join location_hierchy_closer_det lhcd on member_with_location.locationId=lhcd.child_id\n" +
                    "left join location_master lm on lhcd.parent_id=lm.id\n" +
                    "inner join followup_data on followup_data.member_id=member_with_location.id\n" +
                    "where parent_loc_type='SC' order by member_with_location.name ASC limit " + limit + " offset " + offset;
        }
        if(type.equals("moReview")){
            query = "with members as (select distinct member_id from ncd_mo_review_members),\n" +
                    "final_members as (select nmrm.member_id from members nmrm\n" +
                    "left join (select max(id) as id,member_id from ncd_mo_review_detail group by member_id) as nmrd\n" +
                    "on nmrm.member_id=nmrd.member_id\n" +
                    "left join ncd_mo_review_detail nmrd2 on nmrd.id=nmrd2.id\n" +
                    "left join (select max(id) as id,member_id from ncd_mo_review_followup_detail group by member_id) as nmrfd\n" +
                    "on nmrm.member_id=nmrfd.member_id\n" +
                    "left join ncd_mo_review_followup_detail nmrfd2 on nmrfd.id=nmrfd2.id\n" +
                    "where ((nmrd.id is null and nmrfd.id is null)\n" +
                    "or (nmrd.id is not null and (case when current_date>date(nmrd2.created_on) then true else\n" +
                    "nmrd2.is_followup is false and current_date>date(nmrfd2.created_on) end)))),\n" +
                    "base_data as(\n" +
                    "select imm.id,\n" +
                    "        concat(\n" +
                    "            imm.first_name,\n" +
                    "            ' ',\n" +
                    "            imm.middle_name,\n" +
                    "            ' ',\n" +
                    "            imm.last_name\n" +
                    "        ) as name,\n" +
                    "        date_part('year', age(imm.dob)) as age,\n" +
                    "        imm.gender,\n" +
                    "        STRING_AGG(distinct (case when nmrm.disease_code!= 'G' then nmrm.disease_code end),',') as diseases,\n" +
                    "        COALESCE(STRING_AGG(DISTINCT nm.status, ','),'-') as status,\n" +
                    "        nmrm.type as \"subStatus\",\n" +
                    "        lm.name as village,\n" +
                    "        imf.location_id as locationId\n" +
                    "from final_members fm\n" +
                    "inner join ncd_mo_review_members nmrm on fm.member_id=nmrm.member_id\n" +
                    "inner join imt_member imm on fm.member_id = imm.id\n" +
                    "inner join imt_family imf on imm.family_id = imf.family_id\n" +
                    "inner join location_master lm on lm.id = imf.location_id\n" +
                    "left join ncd_master nm on nm.member_id = fm.member_id\n" +
                    "        and nm.disease_code = nmrm.disease_code\n" +
                    "where imm.basic_state in ('NEW', 'VERIFIED', 'REVERIFICATION', 'TEMPORARY')\n" +
                    "and imf.location_id in (\n" +
                    "            select child_id\n" +
                    "            from location_hierchy_closer_det\n" +
                    "            where parent_id in (\n" +
                    "                    select uul.loc_id\n" +
                    "                    from um_user_location uul\n" +
                    "                    where uul.user_id = " + userId + "\n" +
                    "                        and state = 'ACTIVE'\n" +
                    "                )\n" +
                    "        )\n" +
                    "group by imm.id,\n" +
                    "        imm.dob,\n" +
                    "        imm.gender,\n" +
                    "        nmrm.type,\n" +
                    "        lm.name,\n" +
                    "        imf.location_id,\n" +
                    "        imm.first_name,\n" +
                    "        imm.middle_name,\n" +
                    "        imm.last_name)\n" +
                    "select base_data.*,\n" +
                    "    lm.name as \"subCenter\"\n" +
                    "from base_data\n" +
                    "    left join location_hierchy_closer_det lhcd on base_data.locationId = lhcd.child_id\n" +
                    "    left join location_master lm on lhcd.parent_id = lm.id\n" +
                    "where parent_loc_type = 'SC' AND ((status != 'NORMAL')\n" +
                    "\tOR (base_data.\"subStatus\"='CVC')) order by base_data.name ASC limit " + limit + " offset " + offset;
        }
        if(type.equals("moReviewFollowup")){
            query = "with members as (select distinct member_id from ncd_mo_review_members),\n" +
                    "final_members as (select nmrm.member_id from members nmrm\n" +
                    "left join (select max(id) as id,member_id from ncd_mo_review_detail group by member_id) as nmrd\n" +
                    "on nmrm.member_id=nmrd.member_id\n" +
                    "left join ncd_mo_review_detail nmrd2 on nmrd.id=nmrd2.id\n" +
                    "left join (select max(id) as id,member_id from ncd_mo_review_followup_detail group by member_id) as nmrfd\n" +
                    "on nmrm.member_id=nmrfd.member_id\n" +
                    "left join ncd_mo_review_followup_detail nmrfd2 on nmrfd.id=nmrfd2.id\n" +
                    "where (case when nmrfd2.created_on<nmrd2.created_on then nmrd2.is_followup is true else\n" +
                    "nmrd2.id is not null and nmrd2.is_followup is true and nmrfd2.is_remove is not true end)),\n" +
                    "base_data as(\n" +
                    "select imm.id,\n" +
                    "        concat(\n" +
                    "            imm.first_name,\n" +
                    "            ' ',\n" +
                    "            imm.middle_name,\n" +
                    "            ' ',\n" +
                    "            imm.last_name\n" +
                    "        ) as name,\n" +
                    "        date_part('year', age(imm.dob)) as age,\n" +
                    "        imm.gender,\n" +
                    "        STRING_AGG(distinct (case when nmrm.disease_code!= 'G' then nmrm.disease_code end),',') as diseases,\n" +
                    "        string_agg(distinct nm.status, ',') as status,\n" +
                    "        nmrm.type as \"subStatus\",\n" +
                    "        lm.name as village,\n" +
                    "        imf.location_id as locationId\n" +
                    "from final_members fm\n" +
                    "inner join ncd_mo_review_members nmrm on fm.member_id=nmrm.member_id\n" +
                    "inner join imt_member imm on fm.member_id = imm.id\n" +
                    "inner join imt_family imf on imm.family_id = imf.family_id\n" +
                    "inner join location_master lm on lm.id = imf.location_id\n" +
                    "left join ncd_master nm on nm.member_id = fm.member_id\n" +
                    "        and nm.disease_code = nmrm.disease_code\n" +
                    "where imm.basic_state in ('NEW', 'VERIFIED', 'REVERIFICATION', 'TEMPORARY')\n" +
                    "and imf.location_id in (\n" +
                    "            select child_id\n" +
                    "            from location_hierchy_closer_det\n" +
                    "            where parent_id in (\n" +
                    "                    select uul.loc_id\n" +
                    "                    from um_user_location uul\n" +
                    "                    where uul.user_id = " + userId + "\n" +
                    "                        and state = 'ACTIVE'\n" +
                    "                )\n" +
                    "        )\n" +
                    "group by imm.id,\n" +
                    "        imm.dob,\n" +
                    "        imm.gender,\n" +
                    "        nmrm.type,\n" +
                    "        lm.name,\n" +
                    "        imf.location_id,\n" +
                    "        imm.first_name,\n" +
                    "        imm.middle_name,\n" +
                    "        imm.last_name)\n" +
                    "select base_data.*,\n" +
                    "    lm.name as \"subCenter\"\n" +
                    "from base_data\n" +
                    "    left join location_hierchy_closer_det lhcd on base_data.locationId = lhcd.child_id\n" +
                    "    left join location_master lm on lhcd.parent_id = lm.id\n" +
                    "where parent_loc_type = 'SC' order by base_data.name ASC limit " + limit + " offset " + offset;
        }

        if(type.equals("CVCList")){
            query = "with details as(\n" +
                    "    select unnest(string_to_array(disease_history, ',')) as disease_id,\n" +
                    "        *\n" +
                    "    from ncd_member_hypertension_detail nmhd\n" +
                    "    where member_id in (\n" +
                    "            select distinct member_id\n" +
                    "            from ncd_member_hypertension_detail\n" +
                    "            where does_suffering is false\n" +
                    "        )\n" +
                    "),\n" +
                    "final_members as(\n" +
                    "    select distinct nm.member_id,\n" +
                    "        details.disease_history\n" +
                    "    from ncd_master nm\n" +
                    "        left join ncd_master nmd on nmd.member_id = nm.member_id\n" +
                    "        and nmd.disease_code = 'D'\n" +
                    "        left join ncd_master nmm on nmm.member_id = nm.member_id\n" +
                    "        and nmm.disease_code = 'MH'\n" +
                    "        left join details on details.member_id = nm.member_id\n" +
                    "        left join ncd_member_initial_assessment_detail nmiad on nm.member_id = nmiad.member_id\n" +
                    "    where nm.disease_code = 'HT'\n" +
                    "        and nm.status = 'NORMAL'\n" +
                    "        and (\n" +
                    "            nmd.status is null\n" +
                    "            or nmd.status = 'NORMAL'\n" +
                    "        )\n" +
                    "        and (\n" +
                    "            nmm.status is null\n" +
                    "            or nmm.status = 'NORMAL'\n" +
                    "        )\n" +
                    "        and (\n" +
                    "                (details.disease_history is not null\n" +
                    "                and details.disease_history not ilike 'none')\n" +
                    "                or\n" +
                    "                (nmiad.history_disease is not null\n" +
                    "                and nmiad.history_disease not ilike 'none')\n" +
                    "        )\n" +
                    "),\n" +
                    "base_data as(\n" +
                    "    select distinct imm.id,\n" +
                    "        concat(\n" +
                    "            imm.first_name,\n" +
                    "            ' ',\n" +
                    "            imm.middle_name,\n" +
                    "            ' ',\n" +
                    "            imm.last_name\n" +
                    "        ) as name,\n" +
                    "        date_part('year', age(imm.dob)) as age,\n" +
                    "        imm.gender,\n" +
                    "        lm.name as village,\n" +
                    "        imf.location_id as locationId\n" +
                    "    from final_members fm\n" +
                    "        inner join imt_member imm on imm.id = fm.member_id\n" +
                    "        inner join imt_family imf on imm.family_id = imf.family_id\n" +
                    "        inner join location_hierchy_closer_det lhcd on lhcd.child_id = imf.location_id\n" +
                    "        inner join um_user_location uul on uul.loc_id = lhcd.parent_id\n" +
                    "        and uul.state = 'ACTIVE'\n" +
                    "        and uul.user_id = "+ userId+"\n" +
                    "        inner join location_master lm on lm.id = imf.location_id\n" +
                    "    where imm.basic_state in ('NEW', 'VERIFIED', 'REVERIFICATION', 'TEMPORARY')\n" +
                    ")\n" +
                    "select base_data.*,\n" +
                    "    lm.name as \"subCenter\",\n" +
                    "    null as diseases,\n" +
                    "    null as status,\n" +
                    "    null as \"subStatus\"\n" +
                    "from base_data\n" +
                    "    left join location_hierchy_closer_det lhcd on base_data.locationId = lhcd.child_id\n" +
                    "    left join location_master lm on lhcd.parent_id = lm.id\n" +
                    "    left join ncd_cvc_form_details ncfd on base_data.id = ncfd.member_id\n" +
                    "where parent_loc_type = 'SC'\n" +
                    "    and ncfd.id is null order by base_data.name ASC\n" +
                    "limit " + limit + " offset " + offset;
        }

        if(type.equals("MOAlertList")){
            query = "with members as(\n" +
                    "    select member_id as id\n" +
                    "    from imt_member_ncd_detail\n" +
                    "    where last_mo_visit <= current_date - interval '3 months'\n" +
                    "),\n" +
                    "member_with_location as (\n" +
                    "    select members.*,\n" +
                    "        imf.location_id as locationId,\n" +
                    "        lm.name as village,\n" +
                    "        concat(\n" +
                    "            imm.first_name,\n" +
                    "            ' ',\n" +
                    "            imm.middle_name,\n" +
                    "            ' ',\n" +
                    "            imm.last_name\n" +
                    "        ) as name,\n" +
                    "        date_part('year', age(imm.dob)) as age,\n" +
                    "        imm.gender\n" +
                    "    from members\n" +
                    "        inner join imt_member imm on members.id = imm.id\n" +
                    "        inner join imt_family imf on imm.family_id = imf.family_id\n" +
                    "        left join location_master lm on lm.id = imf.location_id\n" +
                    "    where imm.basic_state in ('NEW', 'VERIFIED', 'REVERIFICATION', 'TEMPORARY')\n" +
                    "        and location_id in (\n" +
                    "            select child_id\n" +
                    "            from location_hierchy_closer_det\n" +
                    "            where parent_id in (\n" +
                    "                    select uul.loc_id\n" +
                    "                    from um_user_location uul\n" +
                    "                    where uul.user_id = " + userId + "\n" +
                    "                        and state = 'ACTIVE'\n" +
                    "                )\n" +
                    "        )\n" +
                    ")\n" +
                    "select member_with_location.*,\n" +
                    "    lm.name as \"subCenter\",\n" +
                    "    null as diseases,\n" +
                    "    null as status,\n" +
                    "    null as \"subStatus\"\n" +
                    "from member_with_location\n" +
                    "    left join location_hierchy_closer_det lhcd on member_with_location.locationId = lhcd.child_id\n" +
                    "    left join location_master lm on lhcd.parent_id = lm.id\n" +
                    "where parent_loc_type = 'SC'\n" +
                    "order by member_with_location.name ASC\n" +
                    "limit " + limit + " offset " + offset;
        }

//        System.out.println(query);

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberDetailDto> q = session.createNativeQuery(query);

        return q.addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("diseases", StandardBasicTypes.STRING)
                .addScalar("status", StandardBasicTypes.STRING)
                .addScalar("subStatus", StandardBasicTypes.STRING)
                .addScalar("locationId", StandardBasicTypes.INTEGER)
                .addScalar("village", StandardBasicTypes.STRING)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("age", StandardBasicTypes.INTEGER)
                .addScalar("gender", StandardBasicTypes.STRING)
                .addScalar("subCenter", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(MemberDetailDto.class)).list();
    }

    @Override
    public List<MemberDetailDto> retreiveSearchedMembers(Integer limit, Integer offset, String searchBy, String searchString, Boolean flag,Integer userId, Boolean review) {
        String query = "",whereClause = "",finalSearchString = "";
        String[] status;
        switch (searchBy){
            case "uniqueHealthId" :
                if(flag != null && flag){
                    query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true and flag group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where location_id in (select child_id\n" +
                            "    from location_hierchy_closer_det\n" +
                            "    where imm.unique_health_id='" + searchString + "' and imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY') and\n" +
                            "parent_id in (select uul.loc_id\n" +
                            "\tfrom um_user_location uul where uul.user_id = " +userId+ " and state='ACTIVE')))";
                }
                else{
                    query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where location_id in (select child_id\n" +
                            "    from location_hierchy_closer_det\n" +
                            "    where imm.unique_health_id='" + searchString + "' and imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY') and\n" +
                            "parent_id in (select uul.loc_id\n" +
                            "\tfrom um_user_location uul where uul.user_id = " +userId+ " and state='ACTIVE')))";
                }
                break;
            case "familyId" :
                if(flag != null && flag){
                    query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true and flag group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where location_id in (select child_id\n" +
                            "    from location_hierchy_closer_det\n" +
                            "    where imm.family_id='" + searchString + "' and imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY') and\n" +
                            "parent_id in (select uul.loc_id\n" +
                            "\tfrom um_user_location uul where uul.user_id = " +userId+ " and state='ACTIVE')))";
                }
                else{
                    query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where location_id in (select child_id\n" +
                            "    from location_hierchy_closer_det\n" +
                            "    where imm.family_id='" + searchString + "' and imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY') and\n" +
                            "parent_id in (select uul.loc_id\n" +
                            "\tfrom um_user_location uul where uul.user_id = " +userId+ " and state='ACTIVE')))";
                }
                break;
            case "subCenterName":
                if(flag != null && flag){
                    query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true and flag group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY')\n" +
                            "and (imf.location_id= " + searchString + " or imf.location_id in (\n" +
                            "select child_id from location_hierchy_closer_det where parent_id = " + searchString + ")))";
                }
                else{
                    query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY')\n" +
                            "and (imf.location_id= " + searchString + " or imf.location_id in (\n" +
                            "select child_id from location_hierchy_closer_det where parent_id = " + searchString + ")))";
                }
                break;
            case "orgUnit" :
                String[] orgUnitSearchStrings = null;
                if (searchString != null) {
                    orgUnitSearchStrings = searchString.split("#");
                }
                if(orgUnitSearchStrings.length>1 && !orgUnitSearchStrings[1].equalsIgnoreCase("")){
                    status = orgUnitSearchStrings[1].split(",");
                    for(int i=0; i < status.length; i++){
                        finalSearchString = finalSearchString + "'" + status[i] + "'";
                        if(i != status.length-1){
                            finalSearchString = finalSearchString + ",";
                        }
                    }
                    whereClause = whereClause + " and (status in (" + finalSearchString + ") or sub_status in (" + finalSearchString + ")) ";
                }
                if(orgUnitSearchStrings.length>2 && !orgUnitSearchStrings[2].equalsIgnoreCase("")){
                    whereClause = whereClause + " and disease_code='" + orgUnitSearchStrings[2] + "' ";
                }
                if(flag != null && flag){
                    whereClause = whereClause + " and flag ";
                }

                if((flag != null && flag) || orgUnitSearchStrings.length>1){
                    query= "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true " + whereClause + " group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY')\n" +
                            "and (imf.location_id= " + orgUnitSearchStrings[0] + " or imf.location_id in (\n" +
                            "select child_id from location_hierchy_closer_det where parent_id = " + orgUnitSearchStrings[0] + ")))";
                }
                else{
                    query= "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY')\n" +
                            "and (imf.location_id= " + orgUnitSearchStrings[0] + " or imf.location_id in (\n" +
                            "select child_id from location_hierchy_closer_det where parent_id = " + orgUnitSearchStrings[0] + ")))";
                }
                break;
            case "name" :
                String[] nameSearchStrings = null;
                if (searchString != null) {
                    nameSearchStrings = searchString.split("#");
                }
                String whereClause1 = "and imf.location_id in (\n" +
                        "select child_id from location_hierchy_closer_det where parent_id = " + nameSearchStrings[0] +")" +
                        " and similarity('"+ nameSearchStrings[1] + "',imm.first_name) >= 0.5 and similarity('"+ nameSearchStrings[2] + "',imm.last_name) >= 0.5";
                if (nameSearchStrings.length >= 4) {
                    whereClause1 = whereClause1 + " and similarity('"+ nameSearchStrings[3] + "',imm.middle_name) >= 0.5";
                }
                if(flag != null && flag){
                    query= "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true and flag group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY')\n" +
                            "" + whereClause1 + ")";
                }
                else{
                    query= "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY')\n" +
                            "" + whereClause1 + ")";
                }
                break;
            case "disease":
                whereClause = " and disease_code='" + searchString + "' ";
                if(flag != null && flag){
                    whereClause = whereClause + " and flag ";
                }
                query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                        "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                        "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                        "members as(\n" +
                        "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                        "\twhere is_active is true " + whereClause + " group by member_id),\n" +
                        "member_with_location as (select members.*,imf.location_id as locationId,lm.name as village,\n" +
                        "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                        "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                        "inner join imt_member imm on members.id=imm.id\n" +
                        "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                        "left join location_master lm on lm.id=imf.location_id where location_id in (select child_id\n" +
                        "    from location_hierchy_closer_det\n" +
                        "    where parent_id in (select uul.loc_id\n" +
                        "\tfrom um_user_location uul where uul.user_id = " + userId + " and state='ACTIVE')))";
                break;
            case "status":
            case "subStatus":
                status = searchString.split(",");
                for(int i=0; i < status.length; i++){
                    finalSearchString = finalSearchString + "'" + status[i] + "'";
                    if(i != status.length-1){
                        finalSearchString = finalSearchString + ",";
                    }
                }
                whereClause = " and (status in (" + finalSearchString + ") or sub_status in (" + finalSearchString + "))";
                if(flag != null && flag){
                    whereClause = whereClause + " and flag ";
                }
                query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                        "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                        "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                        "members as(\n" +
                        "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                        "\twhere is_active is true " + whereClause + " group by member_id),\n" +
                        "member_with_location as (select members.*,imf.location_id as locationId,lm.name as village,\n" +
                        "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                        "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                        "inner join imt_member imm on members.id=imm.id\n" +
                        "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                        "left join location_master lm on lm.id=imf.location_id where location_id in (select child_id\n" +
                        "    from location_hierchy_closer_det\n" +
                        "    where parent_id in (select uul.loc_id\n" +
                        "\tfrom um_user_location uul where uul.user_id = " + userId + " and state='ACTIVE')))";
                break;
            case "medicinechange":
                query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                        "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                        "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                        "raw_data as(select ncd_visit_history.member_id,max(visit_date) as visit_date from ncd_visit_history inner join base_data on ncd_visit_history.member_id=base_data.member_id\n" +
                        "where visit_by='MO' group by ncd_visit_history.member_id),\n" +
                        "members as(select ncdm.member_id,STRING_AGG(distinct (case when ncdm.disease_code!= 'G' then ncdm.disease_code end),',') as diseases from ncd_member_disesase_medicine ncdm inner join raw_data on\n" +
                        "ncdm.member_id=raw_data.member_id where ncdm.modified_on >= raw_data.visit_date and\n" +
                        "ncdm.created_on!=ncdm.modified_on group by ncdm.member_id),\n" +
                        "member_with_location as(select members.member_id as id,members.diseases,imf.location_id as locationId,lm.name as village, concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name,\n" +
                        "date_part('year',age(imm.dob)) as age,imm.gender from members\n" +
                        "inner join imt_member imm on members.member_id=imm.id\n" +
                        "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                        "left join location_master lm on lm.id=imf.location_id where location_id in (select child_id\n" +
                        "    from location_hierchy_closer_det\n" +
                        "    where parent_id in (select uul.loc_id\n" +
                        "\tfrom um_user_location uul where uul.user_id = " + userId + " and state='ACTIVE')))\n" +
                        "select member_with_location.*,lm.name as \"subCenter\",null as status,null as subStatus from member_with_location\n" +
                        "left join location_hierchy_closer_det lhcd on member_with_location.locationId=lhcd.child_id\n" +
                        "left join location_master lm on lhcd.parent_id=lm.id\n" +
                        "where parent_loc_type='SC' limit " + limit + " offset " + offset;
                break;
            case "nomedicinechange":
                query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                        "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                        "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                        "raw_data as(select ncd_visit_history.member_id,max(visit_date) as visit_date from ncd_visit_history inner join base_data on ncd_visit_history.member_id=base_data.member_id\n" +
                        "where visit_by='MO' group by ncd_visit_history.member_id),\n" +
                        "members as(select ncdm.member_id,STRING_AGG(distinct (case when ncdm.disease_code!= 'G' then ncdm.disease_code end),',') as diseases from ncd_member_disesase_medicine ncdm inner join raw_data on\n" +
                        "ncdm.member_id=raw_data.member_id where\n" +
                        "ncdm.created_on=ncdm.modified_on group by ncdm.member_id),\n" +
                        "member_with_location as(select members.member_id as id,members.diseases,imf.location_id as locationId,lm.name as village, concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name,\n" +
                        "date_part('year',age(imm.dob)) as age,imm.gender from members\n" +
                        "inner join imt_member imm on members.member_id=imm.id\n" +
                        "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                        "left join location_master lm on lm.id=imf.location_id where location_id in (select child_id\n" +
                        "    from location_hierchy_closer_det\n" +
                        "    where parent_id in (select uul.loc_id\n" +
                        "\tfrom um_user_location uul where uul.user_id = " + userId + " and state='ACTIVE')))\n" +
                        "select member_with_location.*,lm.name as \"subCenter\",null as status,null as subStatus from member_with_location\n" +
                        "left join location_hierchy_closer_det lhcd on member_with_location.locationId=lhcd.child_id\n" +
                        "left join location_master lm on lhcd.parent_id=lm.id\n" +
                        "where parent_loc_type='SC' limit " + limit + " offset " + offset;
                break;
            case "mobileNo" :
                if(flag != null && flag){
                    query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true and flag group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where location_id in (select child_id\n" +
                            "    from location_hierchy_closer_det\n" +
                            "    where imm.mobile_number='" + searchString + "' and imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY') and\n" +
                            "parent_id in (select uul.loc_id\n" +
                            "\tfrom um_user_location uul where uul.user_id = " +userId+ " and state='ACTIVE')))";
                }
                else{
                    query = "with base_data as(select nm.*,imm.dob from ncd_master nm left join imt_member imm on nm.member_id=imm.id\n" +
                            "where (disease_code='MH' and imm.dob<= current_date - interval '15 years') or \n" +
                            "(disease_code!='MH' and imm.dob<= current_date - interval '40 years')),\n" +
                            "members as(\n" +
                            "select member_id as id,STRING_AGG(distinct (case when disease_code!= 'G' then disease_code end),',') as diseases,max(status) as status,max(sub_status) as subStatus from base_data \n" +
                            "\twhere is_active is true group by member_id),\n" +
                            "member_with_location as (select members.diseases,members.status,members.subStatus,imm.id,imf.location_id as locationId,lm.name as village,\n" +
                            "concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name, \n" +
                            "date_part('year',age(imm.dob)) as age,imm.gender from members \n" +
                            "inner join imt_member imm on members.id=imm.id\n" +
                            "inner join imt_family imf on imm.family_id=imf.family_id\n" +
                            "left join location_master lm on lm.id=imf.location_id where location_id in (select child_id\n" +
                            "    from location_hierchy_closer_det\n" +
                            "    where imm.mobile_number='" + searchString + "' and imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY') and\n" +
                            "parent_id in (select uul.loc_id\n" +
                            "\tfrom um_user_location uul where uul.user_id = " +userId+ " and state='ACTIVE')))";
                }
                break;
            default:
                break;
        }

        if(!(searchBy.equals("medicinechange") || searchBy.equals("nomedicinechange"))){
            if(review!=null && review){
                query = query + ",\n" +
                        "review_data as (select distinct member_id from ncd_member_general_detail where mark_review)\n" +
                        "select member_with_location.*,lm.name as \"subCenter\" from member_with_location \n" +
                        "left join location_hierchy_closer_det lhcd on member_with_location.locationId=lhcd.child_id\n" +
                        "left join location_master lm on lhcd.parent_id=lm.id\n" +
                        "inner join review_data on member_with_location.id=review_data.member_id\n" +
                        "where parent_loc_type='SC' order by member_with_location.name ASC limit " + limit + " offset " + offset;
            }
            else{
                query = query + "select member_with_location.*,lm.name as \"subCenter\" from member_with_location \n" +
                        "left join location_hierchy_closer_det lhcd on member_with_location.locationId=lhcd.child_id\n" +
                        "left join location_master lm on lhcd.parent_id=lm.id\n" +
                        "where parent_loc_type='SC' order by member_with_location.name ASC limit " + limit + " offset " + offset;
            }
        }

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberDetailDto> q = session.createNativeQuery(query);

        return q.addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("status", StandardBasicTypes.STRING)
                .addScalar("subStatus", StandardBasicTypes.STRING)
                .addScalar("locationId", StandardBasicTypes.INTEGER)
                .addScalar("village", StandardBasicTypes.STRING)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("age", StandardBasicTypes.INTEGER)
                .addScalar("gender", StandardBasicTypes.STRING)
                .addScalar("subCenter", StandardBasicTypes.STRING)
                .addScalar("diseases", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(MemberDetailDto.class)).list();
    }
}
