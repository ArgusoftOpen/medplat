package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MoReviewDao;
import com.argusoft.medplat.ncd.dto.MOReviewDto;
import com.argusoft.medplat.ncd.dto.MbbsMOReviewDto;
import com.argusoft.medplat.ncd.dto.MemberDetailDto;
import com.argusoft.medplat.ncd.model.MOReviewDetail;
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
public class MoReviewDaoImpl extends GenericDaoImpl<MOReviewDetail, Integer> implements MoReviewDao {

    @Override
    public MOReviewDetail retrieveMoReviewByMemberAndDate(Integer memberId, Date screeningDate) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MOReviewDetail> criteriaQuery = criteriaBuilder.createQuery(MOReviewDetail.class);
        Root<MOReviewDetail> root = criteriaQuery.from(MOReviewDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MOReviewDetail.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(root.get(MOReviewDetail.Fields.SCREENING_DATE), screeningDate);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MOReviewDetail.Fields.ID)));
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    @Override
    public List<MOReviewDetail> retrieveMOReviewsByMemberId(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MOReviewDetail> criteriaQuery = criteriaBuilder.createQuery(MOReviewDetail.class);
        Root<MOReviewDetail> root = criteriaQuery.from(MOReviewDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MOReviewDetail.Fields.MEMBER_ID), memberId);
        Predicate isFollowup = criteriaBuilder.isTrue(root.get(MOReviewDetail.Fields.IS_FOLLOWUP));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MOReviewDetail.Fields.ID)));
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, isFollowup));
        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<MemberDetailDto> retreiveSearchedMembersMOReview(Integer limit, Integer offset, String searchBy, String searchString, Boolean flag) {
        String query = "";
        if(searchBy.equals("type")){
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
                    "        string_agg(distinct nmrm.disease_code, ',') as diseases,\n" +
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
                    "where imm.basic_state in ('NEW', 'VERIFIED', 'REVERIFICATION', 'TEMPORARY') and nmrm.type= '" + searchString + "'\n" +
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
                    "where parent_loc_type = 'SC'" +
                    " AND ((base_data.status != 'NORMAL') OR (base_data.status='-' and base_data.diseases = '-')) order by base_data.name ASC limit " + limit + " offset " + offset;
        }
        else if(searchBy.equals("orgUnit")){
            String[] orgUnitSearchStrings = null;
            String whereClause = "";
            if (searchString != null) {
                orgUnitSearchStrings = searchString.split("#");
            }
            if(orgUnitSearchStrings.length > 1 && !orgUnitSearchStrings[1].equalsIgnoreCase("")){
                whereClause = whereClause + " and nmrm.type='" + orgUnitSearchStrings[1] + "'";
            }
//            query = "with base_data as(select imm.id,concat(imm.first_name,' ',imm.middle_name,' ',imm.last_name) as name,\n" +
//                    "date_part('year',age(imm.dob)) as age,imm.gender, string_agg(distinct nmrm.disease_code,',') as diseases,string_agg(distinct nm.status,',') as status,nmrm.type as \"subStatus\",lm.name as village,imf.location_id as locationId from ncd_mo_review_members nmrm\n" +
//                    "left join ncd_mo_review_detail nmrd on nmrm.member_id=nmrd.member_id left join ncd_mo_review_followup_detail nmrfd on nmrm.member_id=nmrfd.member_id\n" +
//                    "inner join imt_member imm on nmrm.member_id=imm.id\n" +
//                    "inner join imt_family imf on imm.family_id=imf.family_id\n" +
//                    "left join ncd_master nm on nm.member_id = nmrm.member_id and nm.disease_code=nmrm.disease_code\n" +
//                    "left join location_master lm on lm.id=imf.location_id where imm.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY') \n" +
//                    "and (nmrd.id is null or (nmrd.is_followup is not true and nmrfd.is_remove is not true)\n" +
//                    "or nmrm.on_date > nmrfd.screening_date) \n" + whereClause +
//                    "and imf.location_id in (select child_id\n" +
//                    "from location_hierchy_closer_det where parent_id = " + orgUnitSearchStrings[0] + ") group by imm.id,imm.dob,imm.gender,nmrm.type,lm.name,imf.location_id,imm.first_name,imm.middle_name,imm.last_name)\n" +
//                    "select base_data.*, lm.name as \"subCenter\" from base_data\n" +
//                    "left join location_hierchy_closer_det lhcd on base_data.locationId=lhcd.child_id\n" +
//                    "left join location_master lm on lhcd.parent_id=lm.id\n" +
//                    "where parent_loc_type='SC' limit " + limit + " offset " + offset;

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
                    "        string_agg(distinct nmrm.disease_code, ',') as diseases,\n" +
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
                    "where imm.basic_state in ('NEW', 'VERIFIED', 'REVERIFICATION', 'TEMPORARY') " + whereClause + "\n" +
                    "and imf.location_id in (select child_id\n" +
                    "from location_hierchy_closer_det where parent_id = " + orgUnitSearchStrings[0] + ") group by imm.id,\n" +
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
                    "where parent_loc_type = 'SC' AND ((base_data.status != 'NORMAL') OR (base_data.status='-' and base_data.diseases = '-')) order by base_data.name ASC limit " + limit + " offset " + offset;
        } else if (searchBy.equals("familyId")) {
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
                    "        string_agg(distinct nmrm.disease_code, ',') as diseases,\n" +
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
                    "where imm.basic_state in ('NEW', 'VERIFIED', 'REVERIFICATION', 'TEMPORARY') and imm.family_id= '" + searchString + "'\n" +
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
                    "where parent_loc_type = 'SC'" +
                    " AND ((base_data.status != 'NORMAL') OR (base_data.status='-' and base_data.diseases = '-')) order by base_data.name ASC  limit " + limit + " offset " + offset;
        } else if (searchBy.equals("uniqueHealthId")) {
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
                    "        string_agg(distinct nmrm.disease_code, ',') as diseases,\n" +
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
                    "where imm.basic_state in ('NEW', 'VERIFIED', 'REVERIFICATION', 'TEMPORARY') and imm.unique_health_id= '" + searchString + "'\n" +
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
                    "where parent_loc_type = 'SC'" +
                    " AND ((base_data.status != 'NORMAL') OR (base_data.status='-' and base_data.diseases = '-')) order by base_data.name ASC  limit " + limit + " offset " + offset;
        } else if (searchBy.equals("name")) {

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
                    "        string_agg(distinct nmrm.disease_code, ',') as diseases,\n" +
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
                    "where imm.basic_state in ('NEW', 'VERIFIED', 'REVERIFICATION', 'TEMPORARY') " + whereClause1 + "\n" +
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
                    "where parent_loc_type = 'SC'" +
                    " AND ((base_data.status != 'NORMAL') OR (base_data.status='-' and base_data.diseases = '-')) order by base_data.name ASC  limit " + limit + " offset " + offset;
        }

        //System.out.println(query);
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

    @Override
    public MOReviewDto retrieveLastCommentByMOReview(Integer memberId) {
        String sql = "select ncd_mo_review_detail.member_id \"memberId\",ncd_mo_review_detail.comment \"comment\",concat(um_user.first_name,' ',um_user.last_name) \"commentBy\" from ncd_mo_review_detail\n" +
                "left join um_user on ncd_mo_review_detail.created_by = um_user.id\n" +
                "where member_id= :memberId and comment is not null order by ncd_mo_review_detail.id desc";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MOReviewDto> sqlQuery = session.createNativeQuery(sql);
        return sqlQuery
                .addScalar("memberId", StandardBasicTypes.INTEGER)
                .addScalar("comment", StandardBasicTypes.STRING)
                .addScalar("commentBy", StandardBasicTypes.STRING)
                .setParameter("memberId", memberId)
                .setResultTransformer(Transformers.aliasToBean(MOReviewDto.class))
                .setMaxResults(1).uniqueResult();
    }
}
