/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.ncddnhdd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncddnhdd.dao.MemberReferralDao;
import com.argusoft.medplat.ncddnhdd.dto.MemberReferralDto;
import com.argusoft.medplat.ncddnhdd.enums.DiseaseCode;
import com.argusoft.medplat.ncddnhdd.model.MemberReferral;
import com.argusoft.medplat.ncddnhdd.dto.MemberReferralDnhddDto;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.LongType;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 *
 * <p>
 * Implementation of methods defined in member referral dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberReferralDaoImpl extends GenericDaoImpl<MemberReferral, Integer> implements MemberReferralDao {
    public static final String ID_PROPERTY = "id";
    public static final String MEMBER_ID_PROPERTY = "memberId";
    public static final String FAMILY_ID_PROPERTY = "familyId";
    public static final String UNIQUE_HEALTH_ID_PROPERTY = "uniqueHealthId";
    public static final String LOCATION_ID_PROPERTY = "locationId";
    public static final String LOCATION_HIERARCHY_PROPERTY = "locationHierarchy";
    public static final String LOCATION_NAME_PROPERTY = "locationName";
    public static final String MOBILE_NUMBER_PROPERTY = "mobileNumber";
    public static final String REFERRED_DATE_PROPERTY = "referredDate";
    public static final String DOB_PROPERTY = "dob";
    public static final String GENDER_PROPERTY = "gender";
    public static final String NAME_PROPERTY = "name";
    public static final String FAMILY_MOBILE_NUMBER_PROPERTY = "familyMobileNumber";
    public static final String ORG_UNIT_PROPERTY = "orgUnit";
    public static final String VILLAGE_NAME_PROPERTY = "villageName";
    public static final String ABHA_NUMBER_PROPERTY = "abhaNumber";
    public static final String ABHA_ADDRESS_PROPERTY = "abhaAddress";
    public static final String DISEASE_PROPERTY = "disease";
    public static final String DISEASE_CODE_PROPERTY = "diseaseCode";

    public static final String REFERRED_FOR_DISEASES = "referredForDiseases";
    public static final String REFERRED_FOR_HYPERTENSION = "referredForHypertension";
    public static final String REFERRED_FOR_DIABETES = "referredForDiabetes";
    public static final String REFERRED_FOR_BREAST = "referredForBreast";
    public static final String REFERRED_FOR_ORAL = "referredForOral";
    public static final String REFERRED_FOR_CERVICAL = "referredForCervical";
    private static final String HMIS_ID_PROPERTY = "hmisId";
    public static final String OFFSET = "offset";
    public static final String LIMIT = "limit";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberReferralDto> retrieveReffForToday(Integer memberId, Integer referredBy) {
        String sql = "select \n"
                + "case when disease_code ='HT' then 'Hypertension'\n"
                + "when disease_code='D' then 'Diabetes'\n"
                + "when disease_code='C' then 'Cervical'\n"
                + "when disease_code='B' then 'Breast'\n"
                + "when disease_code='O' then 'Oral'\n"
                + "end as diseaseName,reason as reason, h.name as healthInfraName from ncd_member_referral m\n"
                + "inner join health_infrastructure_details h on m.health_infrastructure_id = h.id\n"
                + "where cast (follow_up_date as date) = CURRENT_DATE and m.member_id=:memberId and m.created_by=:referredBy";
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createNativeQuery(sql)
                .addScalar("diseaseName", StandardBasicTypes.STRING)
                .addScalar("reason", StandardBasicTypes.STRING)
                .addScalar("healthInfraName", StandardBasicTypes.STRING)
                .setParameter(MemberReferral.Fields.MEMBER_ID, memberId)
                .setParameter("referredBy", referredBy)
                .setResultTransformer(Transformers.aliasToBean(MemberReferralDto.class))
                .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberReferral retrievePendingFollowUpByMemberIdAndDiseaseType(Integer memberId, DiseaseCode diseaseCode) {
        String query = "select distinct on(referred_on) *\n" +
                "from ncd_member_referral\n" +
                "where member_id = :memberId and disease_code = :diseaseCode\n" +
                "order by referred_on desc limit 1";

        NativeQuery<MemberReferral> sqlQuery = sessionFactory.getCurrentSession().createNativeQuery(query);
        sqlQuery.setParameter(MemberReferral.Fields.MEMBER_ID, memberId);
        sqlQuery.setParameter(MemberReferral.Fields.DISEASE_CODE, diseaseCode.toString());
        sqlQuery.addEntity(MemberReferral.class);
        return sqlQuery.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberReferral> retrieveByMemberId(Integer memberId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(MemberReferral.Fields.MEMBER_ID), memberId));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberReferral.Fields.REFERRED_ON)));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberReferral> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId,  Integer healthInfrasturctureId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(MemberReferral.Fields.MEMBER_ID), memberId));
            predicates.add(criteriaBuilder.equal(root.get(MemberReferral.Fields.HEALTH_INFRA_ID), healthInfrasturctureId));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberReferral.Fields.REFERRED_ON)));
            return predicates;
        });
    }
    @Override
    public List<MemberReferralDnhddDto> retrieveMembersNew(Integer userId, Integer limit, Integer offset, String healthInfrastructureType, String searchBy, String searchString, Boolean isSus) {

        String[] diseaseCodes = {"O", "HT", "C", "B", "D"};
        String diseases = DISEASE_PROPERTY.equals(searchBy) && Arrays.asList(diseaseCodes).contains(searchString) ? "= '"+ searchString + "'": "in ('O','HT','C','B','D')";

        String doneBy = Boolean.TRUE.equals(isSus) ? "('CHO','MPW','FHW','RBSK')" : "('MO')";

        String refState = Boolean.TRUE.equals(isSus) ? "'PENDING'" : "'COMPLETED' and ref.status = 'NO_ABNORMALITY'";

        String whereCondition = "";

        String finalQuery = "";

        String queryTables = "";

        String baseQuery = "with data as (\n" +
                "\tselect ref.member_id,\n" +
                "\tmax(ref.referred_on) as ref_date,\n" +
                "\tmax(hyp.id) as hyp_id,\n" +
                "\tmax(dia.id) as dia_id,\n" +
                "\tmax(oral.id) as oral_id,\n" +
                "\tmax(breast.id) as breast_id,\n" +
                "\tmax(cervical.id) as cervical_id,\n" +
                "\tmax(cbac.id) as cbac_id\n" +
                "\tfrom ncd_member_referral ref\n" +
                "\tleft join ncd_member_hypertension_detail hyp on ref.member_id = hyp.member_id\n" +
                "\tand hyp.done_by in " + doneBy + "\n" +
                "\tleft join ncd_member_diabetes_detail dia on ref.member_id = dia.member_id\n" +
                "\tand dia.done_by in " + doneBy + "\n" +
                "\tleft join ncd_member_oral_detail oral on ref.member_id = oral.member_id\n" +
                "\tand oral.done_by in " + doneBy + "\n" +
                "\tleft join ncd_member_breast_detail breast on ref.member_id = breast.member_id\n" +
                "\tand breast.done_by in " + doneBy + "\n" +
                "\tleft join ncd_member_cervical_detail cervical on ref.member_id = cervical.member_id\n" +
                "\tand cervical.done_by in " + doneBy + "\n" +
                "\tleft join ncd_member_cbac_detail cbac on ref.member_id = cbac.member_id\n" +
                "\twhere ref.state = "+ refState + "\n" +
                "\tand ref.referred_from_health_infrastructure_id in (\n" +
                "\t\tselect health_infrastrucutre_id\n" +
                "\t\tfrom user_health_infrastructure\n" +
                "\t\twhere user_id = " + userId + "\n" +
                "\t\tand state = 'ACTIVE'\n" +
                "\t)\n" +
                "\tand ref.disease_code " + diseases + "\n"+
                "\tand ref.referred_from in "+ doneBy + "\n" +
                "\tgroup by ref.member_id\n" +
                ")\n" +
                "select imt_member.id as id,\n" +
                "imt_member.unique_health_id as \"uniqueHealthId\",\n" +
                "imt_family.family_id as \"familyId\",\n" +
                "get_location_hierarchy(case when imt_family.area_id is not null then imt_family.area_id else imt_family.location_id end) as \"locationHierarchy\",\n" +
                "location_master.name as \"locationName\",\n" +
                "location_master.id as \"locationId\",\n" +
                "imt_member.mobile_number as \"mobileNumber\",\n" +
                "data.ref_date as \"referredDate\",\n" +
                "concat_ws(' ',imt_member.first_name,imt_member.middle_name,imt_member.last_name) as \"name\",\n" +
                "imt_member.dob as \"dob\",\n" +
                "imt_member.gender as \"gender\",\n" +
                "cbac.hmis_id as \"hmisId\",\n" +
                "concat_ws('<br>',\n" +
                "\tcase when hyp.id is not null and hyp.systolic_bp is not null and hyp.diastolic_bp is not null\n" +
                "\t\t then concat(\n" +
                "\t\t \t'BP reading - ',\n" +
                "\t\t \tcase when hyp.systolic_bp >= 140\n" +
                "\t\t \t\t then concat(\n" +
                "\t\t \t\t \t'<b style=\"color:red;\">',\n" +
                "\t\t \t\t \tcast(hyp.systolic_bp as text),\n" +
                "\t\t \t\t \t'</b>'\n" +
                "\t\t \t\t )\n" +
                "\t\t \t\t else cast(hyp.systolic_bp as text)\n" +
                "\t\t \t\t end,\n" +
                "\t\t \t'/',\n" +
                "\t\t \tcase when hyp.diastolic_bp >= 90\n" +
                "\t\t \t\t then concat(\n" +
                "\t\t \t\t \t'<b style=\"color:red;\">',\n" +
                "\t\t \t\t \tcast(hyp.diastolic_bp as text),\n" +
                "\t\t \t\t \t'</b>'\n" +
                "\t\t \t\t )\n" +
                "\t\t \t\t else cast(hyp.diastolic_bp as text)\n" +
                "\t\t \t\t end\n" +
                "\t\t )\n" +
                "\t\t else 'BP reading - Not done'\n" +
                "\t\t end,\n" +
                "\tcase when dia.id is not null\n" +
                "\t\t then concat(\n" +
                "\t\t \t'Sugar Reading - ',\n" +
                "\t\t \tcase when (dia.measurement_type ='FBS' and dia.blood_sugar >= 126) or\n" +
                "\t\t \t          (dia.measurement_type ='PP2BS' and dia.blood_sugar >= 201) or\n" +
                "\t\t \t          (dia.measurement_type ='RBS' and dia.blood_sugar >= 141)\n" +
                "\t\t \t\t then concat(\n" +
                "\t\t \t\t \t\t   '<b style=\"color:red;\">',\n" +
                "\t\t \t\t \t\t   cast(dia.blood_sugar as text),\n" +
                "\t\t \t\t \t\t   '</b>'\n" +
                "\t\t \t\t \t\t   )\n" +
                "\t\t \t\t else cast(dia.blood_sugar as text)\n" +
                "\t\t \t\t end\n" +
                "\t\t )\n" +
                "\t\t else 'Sugar reading - Not done'\n" +
                "\t\t end,\n"+
                "\tcase when oral.id is not null\n" +
                "\t\t then concat(\n" +
                "\t\t \t'Oral Cancer - ',\n" +
                "\t\t \tcase when oral.is_suspected\n" +
                "\t\t \t\t then concat(\n" +
                "\t\t \t\t \t'<b style=\"color:red;\">',\n" +
                "\t\t \t\t \t'Abnormality Detected',\n" +
                "\t\t \t\t \t'</b>'\n" +
                "\t\t \t\t )\n" +
                "\t\t \t\t else 'No abnormality'\n" +
                "\t\t \t\t end\n" +
                "\t\t )\n" +
                "\t\t else 'Oral Cancer - Not done'\n" +
                "\t\t end,\n" +
                "\tcase when breast.id is not null\n" +
                "\t\t then concat(\n" +
                "\t\t \t'Breast Cancer - ',\n" +
                "\t\t \tcase when breast.is_suspected\n" +
                "\t\t \t\t then concat(\n" +
                "\t\t \t\t \t'<b style=\"color:red;\">',\n" +
                "\t\t \t\t \t'Abnormality Detected',\n" +
                "\t\t \t\t \t'</b>'\n" +
                "\t\t \t\t )\n" +
                "\t\t \t\t else 'No abnormality'\n" +
                "\t\t \t\t end\n" +
                "\t\t )\n" +
                "\t\t else 'Breast Cancer - Not done'\n" +
                "\t\t end,\n" +
                "\t'Cervical Cancer - Not done'\n" +
                ") as \"referredForDiseases\"\n";

        String basicQueryTables = "from data\n" +
                "left join ncd_member_hypertension_detail hyp on data.hyp_id = hyp.id\n" +
                "left join ncd_member_diabetes_detail dia on data.dia_id = dia.id\n" +
                "left join ncd_member_oral_detail oral on data.oral_id = oral.id\n" +
                "left join ncd_member_breast_detail breast on data.breast_id = breast.id\n" +
                "left join ncd_member_cervical_detail cervical on data.cervical_id = cervical.id\n" +
                "left join ncd_member_cbac_detail cbac on data.cbac_id = cbac.id\n" +
                "inner join imt_member on data.member_id = imt_member.id\n" +
                "inner join imt_family on imt_member.family_id = imt_family.family_id\n" +
                "inner join location_master on location_master.id = case when imt_family.area_id is not null then imt_family.area_id else imt_family.location_id end\n" +
                "where date_part('year',age(imt_member.dob)) >= 30\n" +
                "and (imt_member.basic_state in ('NEW','VERIFIED','REVERIFICATION') or (imt_member.state = 'com.argusoft.imtecho.member.state.temporary'))\n";

        String orderBy = "order by \"name\",\"referredDate\" desc ";

        String limitQuery = "limit :limit offset :offset\n";
        finalQuery = baseQuery + basicQueryTables + orderBy;

        if (Objects.nonNull(searchString)) {
            searchString = searchString.replace("',", "''");
        }
        if (Objects.isNull(searchBy) || Objects.isNull(searchString) || searchString.isEmpty()) {
            searchBy = "";
        }

        switch (searchBy) {
            case MEMBER_ID_PROPERTY:
                whereCondition = "and imt_member.unique_health_id= '" + searchString + "'\n";
                finalQuery = baseQuery + basicQueryTables + whereCondition + orderBy;
                break;

            case FAMILY_ID_PROPERTY:
                whereCondition = "and imt_member.family_id= '" + searchString + "'\n";
                finalQuery = baseQuery + basicQueryTables + whereCondition + orderBy;
                break;

            case FAMILY_MOBILE_NUMBER_PROPERTY:
                whereCondition = "and imt_family.family_id in (select family_id from imt_member where mobile_number = '" + searchString + "'\n";
                finalQuery = baseQuery + basicQueryTables + whereCondition + orderBy;
                break;

            case MOBILE_NUMBER_PROPERTY:
                whereCondition = "and imt_member.mobile_number='" + searchString + "'\n";
                finalQuery = baseQuery + basicQueryTables + whereCondition + orderBy;
                break;

            case NAME_PROPERTY:
                String[] nameSearchStrings = null;
                nameSearchStrings = searchString.split("#");
                queryTables = "from data\n" +
                        "left join ncd_member_hypertension_detail hyp on data.hyp_id = hyp.id\n" +
                        "left join ncd_member_diabetes_detail dia on data.dia_id = dia.id\n" +
                        "left join ncd_member_oral_detail oral on data.oral_id = oral.id\n" +
                        "left join ncd_member_breast_detail breast on data.breast_id = breast.id\n" +
                        "left join ncd_member_cervical_detail cervical on data.cervical_id = cervical.id\n" +
                        "left join ncd_member_cbac_detail cbac on data.cbac_id = cbac.id\n" +
                        "inner join imt_member on data.member_id = imt_member.id\n" +
                        "inner join imt_family on imt_member.family_id = imt_family.family_id\n" +
                        "inner join location_master on location_master.id = case when imt_family.area_id is not null then imt_family.area_id else imt_family.location_id end\n" +
                        "inner join location_hierchy_closer_det lhcd on (case when imt_family.area_id is not null then imt_family.area_id else imt_family.location_id end) = lhcd.child_id " +
                        "and lhcd.parent_id = " + nameSearchStrings[0] +"\n" +
                        "where date_part('year',age(imt_member.dob)) >= 30\n" +
                        "and (imt_member.basic_state in ('NEW','VERIFIED','REVERIFICATION') or (imt_member.state = 'com.argusoft.imtecho.member.state.temporary'))\n" +
                        "and similarity('" + nameSearchStrings[1] + "',imt_member.first_name) >= 0.50\n" +
                        "or similarity('" + nameSearchStrings[2] + "',imt_member.last_name) >= 0.60\n";

                if (nameSearchStrings.length >= 4) {
                    queryTables += "and similarity('" + nameSearchStrings[3] + "',imt_member.middle_name) >= 0.50\n";
                }
                finalQuery = baseQuery + queryTables + orderBy;
                break;

            case ORG_UNIT_PROPERTY:
            case VILLAGE_NAME_PROPERTY:
                queryTables = "from data\n" +
                        "left join ncd_member_hypertension_detail hyp on data.hyp_id = hyp.id\n" +
                        "left join ncd_member_diabetes_detail dia on data.dia_id = dia.id\n" +
                        "left join ncd_member_oral_detail oral on data.oral_id = oral.id\n" +
                        "left join ncd_member_breast_detail breast on data.breast_id = breast.id\n" +
                        "left join ncd_member_cervical_detail cervical on data.cervical_id = cervical.id\n" +
                        "left join ncd_member_cbac_detail cbac on data.cbac_id = cbac.id\n" +
                        "inner join imt_member on data.member_id = imt_member.id\n" +
                        "inner join imt_family on imt_member.family_id = imt_family.family_id\n" +
                        "inner join location_master on location_master.id = case when imt_family.area_id is not null then imt_family.area_id else imt_family.location_id end\n" +
                        "inner join location_hierchy_closer_det lhcd on (case when imt_family.area_id is not null then imt_family.area_id else imt_family.location_id end) = lhcd.child_id " +
                        "and lhcd.parent_id = " + searchString +"\n" +
                        "where date_part('year',age(imt_member.dob)) >= 30\n" +
                        "and (imt_member.basic_state in ('NEW','VERIFIED','REVERIFICATION') or (imt_member.state = 'com.argusoft.imtecho.member.state.temporary'))\n";

                finalQuery = baseQuery + queryTables + orderBy;
                break;

            case DISEASE_PROPERTY:
//                query = defaultPartialQuery;
//
//                String[] diseaseCodes = {"O", "HT", "C", "B", "D"};
//                if (Arrays.asList(diseaseCodes).contains(searchString)) {
//                    query += "having '" + searchString + "' = ANY(string_to_array(string_agg(r.disease_code, ','),','))\n";
//                }
//
//                query += limitQuery;
                break;

            case ABHA_NUMBER_PROPERTY:
                whereCondition += "\t\tand imt_member.health_id_number='" + searchString + "'\n";
                finalQuery = baseQuery + basicQueryTables + whereCondition + orderBy;
                break;

            case ABHA_ADDRESS_PROPERTY:
                whereCondition += "and imt_member.health_id='" + searchString + "'\n";
                finalQuery = baseQuery + basicQueryTables + whereCondition + orderBy;
                break;

            default:
                finalQuery = baseQuery + basicQueryTables + orderBy;
                break;

        };
        finalQuery+= limitQuery;

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberReferralDnhddDto> sqlQuery = session.createNativeQuery(finalQuery);
        sqlQuery.addScalar(LOCATION_ID_PROPERTY,StandardBasicTypes.INTEGER)
                .addScalar(ID_PROPERTY, StandardBasicTypes.INTEGER)
                .addScalar(UNIQUE_HEALTH_ID_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(FAMILY_ID_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(LOCATION_HIERARCHY_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(LOCATION_NAME_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(MOBILE_NUMBER_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(NAME_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(GENDER_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(DOB_PROPERTY, StandardBasicTypes.DATE)
                .addScalar(REFERRED_DATE_PROPERTY, StandardBasicTypes.TIMESTAMP)
                .addScalar(REFERRED_FOR_DISEASES, StandardBasicTypes.STRING)
                .addScalar(HMIS_ID_PROPERTY, StandardBasicTypes.LONG);
        if (limit != null) {
            sqlQuery.setParameter(LIMIT, limit)
                    .setParameter(OFFSET, offset);
        } else {
            sqlQuery.setParameter(LIMIT, limit, LongType.INSTANCE)
                    .setParameter(OFFSET, offset, LongType.INSTANCE);
        }
        return sqlQuery.setResultTransformer(Transformers.aliasToBean(MemberReferralDnhddDto.class)).list();
    };
    @Override
    public List<MemberReferralDnhddDto> retrieveMembers(Integer userId, Integer limit, Integer offset, String healthInfrastructureType, String searchBy, String searchString, Boolean isSus) {
        String suspected = "\t\tand r.disease_code in ('O','HT','C','B','D') \n" +
                "\t\tand r.referred_from in ('FHW', 'MPHW', 'CHO', 'MPW', 'RBSK', 'ASHA', 'OTHERS', 'MO') \n" +
                "\t\tand r.state = 'PENDING' and r.status is null \n";
        String examinedAndNormal = "\t\tand r.status = 'NO_ABNORMALITY' \n";
        String statusCondition = Boolean.TRUE.equals(isSus) ? suspected : examinedAndNormal;

        String withAsQuery = "with max_member_ids as (\n" +
                "\tselect member_id, max(id) as max_id, max(id) filter (where referred_from in ('FHW', 'MPHW', 'CHO', 'MPW', 'RBSK', 'ASHA', 'OTHERS')) as max_mob_service\n" +
                "\tfrom ncd_member_referral r\n" +
                "\twhere referred_on >= current_date - interval '1 year'\n" +
                statusCondition +
                "\t\tand r.referred_from_health_infrastructure_id in (\n" +
                "\t\t\tselect h.id from health_infrastructure_details h, user_health_infrastructure u \n" +
                "\t\t\twhere u.health_infrastrucutre_id = h.id and u.user_id =" + userId + " and u.state = 'ACTIVE')\n" +
                "\tgroup by member_id, disease_code\n" +
                "), \n";

        String query = withAsQuery + "referred_members as (\n" +
                "\tselect \n" +
                "\t\tm.id as member_id, \n" +
                "\t\tcbac.hmis_id, \n" +
                "\t\tstring_agg(nmr.disease_code,',') as disease_code, \n" +
                "\t\tmax(r.referred_on) as ref_date \n" +
                "\tfrom imt_member m\n" +
                "\tleft join ncd_member_cbac_detail cbac on cbac.member_id = m.id \n" +
                "\tleft join ncd_master on ncd_master.member_id = m.id \n" +
                "\tleft join max_member_ids mmi on mmi.member_id = m.id \n" +
                "\tinner join ncd_member_referral r on r.id = mmi.max_id \n" +
                "\tleft join ncd_member_referral nmr on nmr.id = mmi.max_mob_service\n" +
                "\twhere date_part('year',age(m.dob)) >= 30\n" +
                "\t\tand ((m.basic_state in ('NEW','VERIFIED','REVERIFICATION')) or (m.state = 'com.argusoft.imtecho.member.state.temporary'))\n";
        String limitQuery = "limit :limit offset :offset)\n";
        String groupByQuery = "\tgroup by m.id, cbac.hmis_id\n";
        if (Objects.nonNull(searchString)) {
            searchString = searchString.replace("',", "''");
        }
        if (Objects.isNull(searchBy) || Objects.isNull(searchString) || searchString.isEmpty()) {
            searchBy = "";
        }

        String defaultPartialQuery = withAsQuery +
                "referred_members as (\n" +
                "\tselect \n" +
                "\t\tr.member_id, \n" +
                "\t\tcbac.hmis_id, \n" +
                "\t\tstring_agg(nmr.disease_code, ',') as disease_code, \n" +
                "\t\tmax(r.referred_on) as ref_date \n" +
                "\tfrom ncd_member_referral r \n" +
                "\tinner join max_member_ids m on r.id = m.max_id\n" +
                "\tinner join imt_member im on im.id = r.member_id\n" +
                "\tleft join ncd_member_referral nmr on nmr.id = m.max_mob_service\n" +
                "\tleft join ncd_member_cbac_detail cbac on cbac.member_id = r.member_id \n" +
                "\tleft join ncd_master on ncd_master.member_id = r.member_id \n" +
                "\twhere date_part('year',age(im.dob)) >= 30 \n" +
                "\t\tand ((im.basic_state in ('NEW','VERIFIED','REVERIFICATION')) or (im.state = 'com.argusoft.imtecho.member.state.temporary'))\n" +
                "\tgroup by r.member_id, cbac.hmis_id\n";

        switch (searchBy) {
            case MEMBER_ID_PROPERTY:
                query += "\t\tand m.unique_health_id= '" + searchString + "'\n" + groupByQuery;
                query += limitQuery;
                break;

            case FAMILY_ID_PROPERTY:
                query += "\t\tand m.family_id= '" + searchString + "'\n" + groupByQuery;
                query += limitQuery;
                break;

            case FAMILY_MOBILE_NUMBER_PROPERTY:
                query = withAsQuery +
                        "referred_members as (\n" +
                        "\tselect \n" +
                        "\t\tm.id as member_id, \n" +
                        "\t\tcbac.hmis_id, \n" +
                        "\t\tstring_agg(nmr.disease_code,',') as disease_code, \n" +
                        "\t\tmax(r.referred_on) as ref_date \n" +
                        "\tfrom imt_member m\n" +
                        "\tleft join ncd_member_cbac_detail cbac on cbac.member_id = r.member_id \n" +
                        "\tleft join ncd_master on ncd_master.member_id = r.member_id \n" +
                        "\tleft join max_member_ids mmi on mmi.member_id = m.id \n" +
                        "\tinner join ncd_member_referral r on r.id = mmi.max_id \n" +
                        "\tleft join ncd_member_referral nmr on nmr.id = mmi.max_mob_service\n" +
                        "\tinner join imt_family f on f.family_id = m.family_id \n" +
                        "\t\tand f.family_id in (select family_id from imt_member where mobile_number= '" + searchString + "'\n" +
                        "\twhere date_part('year',age(m.dob)) >= 30\n" +
                        "\t\tand ((m.basic_state in ('NEW','VERIFIED','REVERIFICATION')) or (m.state = 'com.argusoft.imtecho.member.state.temporary'))\n" +
                        groupByQuery;
                query += limitQuery;
                break;

            case MOBILE_NUMBER_PROPERTY:
                query += "\t\tand m.mobile_number='" + searchString + "'\n" + groupByQuery;
                query += limitQuery;
                break;

            case NAME_PROPERTY:
                String[] nameSearchStrings = null;
                nameSearchStrings = searchString.split("#");
                query = withAsQuery +
                        "referred_members as (\n" +
                        "\tselect \n" +
                        "\t\tm.id as member_id, \n" +
                        "\t\tcbac.hmis_id, \n" +
                        "\t\tstring_agg(nmr.disease_code,',') as disease_code, \n" +
                        "\t\tmax(r.referred_on) as ref_date \n" +
                        "\tfrom imt_member m\n" +
                        "\tleft join ncd_member_cbac_detail cbac on cbac.member_id = m.id \n" +
                        "\tleft join ncd_master on ncd_master.member_id = m.id \n" +
                        "\tleft join max_member_ids mmi on mmi.member_id = m.id \n" +
                        "\tinner join ncd_member_referral r on r.id = mmi.max_id \n" +
                        "\tleft join ncd_member_referral nmr on nmr.id = mmi.max_mob_service\n" +
                        "\tinner join imt_family f on f.family_id=m.family_id\n" +
                        "\tinner join location_hierchy_closer_det lhcd on (case when f.area_id is not null then f.area_id else f.location_id end) = lhcd.child_id\n" +
                        "\t\tand lhcd.parent_id = " + nameSearchStrings[0] +
                        "\twhere date_part('year',age(m.dob)) >= 30\n" +
                        "\t\tand ((m.basic_state in ('NEW','VERIFIED','REVERIFICATION')) or (m.state = 'com.argusoft.imtecho.member.state.temporary'))\n" +
                        "\t\tand similarity('" + nameSearchStrings[1] + "',m.first_name) >= 0.50\n" +
                        "\t\tor similarity('" + nameSearchStrings[2] + "',m.last_name) >= 0.60\n";

                if (nameSearchStrings.length >= 4) {
                    query += "\t\tand similarity('" + nameSearchStrings[3] + "',m.middle_name) >= 0.50\n";
                }
                query += groupByQuery;
                query += limitQuery;
                break;

            case ORG_UNIT_PROPERTY:
            case VILLAGE_NAME_PROPERTY:
                query = withAsQuery +
                        "referred_members as (\n" +
                        "\tselect \n" +
                        "\t\tm.id as member_id, \n" +
                        "\t\tcbac.hmis_id, \n" +
                        "\t\tstring_agg(r.disease_code,',') as disease_code, \n" +
                        "\t\tmax(referred_on) as ref_date \n" +
                        "\tfrom imt_member m\n" +
                        "\tleft join ncd_member_cbac_detail cbac on cbac.member_id = m.id \n" +
                        "\tleft join ncd_master on ncd_master.member_id = m.id \n" +
                        "\tinner join imt_family f on f.family_id=m.family_id\n" +
                        "\tinner join location_hierchy_closer_det lhcd on (case when f.area_id is not null then f.area_id else f.location_id end) = lhcd.child_id\n" +
                        "\t\tand lhcd.parent_id = " + searchString +
                        "\tleft join max_member_ids mmi on mmi.member_id = m.id \n" +
                        "\tinner join ncd_member_referral r on r.id = mmi.max_id \n" +
                        "\tleft join ncd_member_referral nmr on nmr.id = mmi.max_mob_service\n" +
                        "\twhere date_part('year',age(m.dob)) >= 30\n" +
                        "\t\tand ((m.basic_state in ('NEW','VERIFIED','REVERIFICATION')) or (m.state = 'com.argusoft.imtecho.member.state.temporary'))\n" +
                        groupByQuery;

                query += limitQuery;
                break;

            case DISEASE_PROPERTY:
                query = defaultPartialQuery;

                String[] diseaseCodes = {"O", "HT", "C", "B", "D"};
                if (Arrays.asList(diseaseCodes).contains(searchString)) {
                    query += "having '" + searchString + "' = ANY(string_to_array(string_agg(r.disease_code, ','),','))\n";
                }

                query += limitQuery;
                break;

            case ABHA_NUMBER_PROPERTY:
                query += "\t\tand m.health_id_number='" + searchString + "'\n" + groupByQuery;

                query += limitQuery;
                break;

            case ABHA_ADDRESS_PROPERTY:
                query += "and m.health_id='" + searchString + "'\n" + groupByQuery;

                query += limitQuery;
                break;

            default:
                query = defaultPartialQuery + limitQuery;
                break;

        }
        query += "select \n" +
                "\tm.id,\n" +
                "\tm.unique_health_id as \"uniqueHealthId\",\n" +
                "\tm.family_id as \"familyId\",\n" +
                "\tget_location_hierarchy(if.location_id) as \"locationHierarchy\",\n" +
                "\tlm.name as \"locationName\",\n" +
                "\tlm.id as \"locationId\", \n" +
                "\tm.mobile_number as \"mobileNumber\",\n" +
                "\tr.ref_date as \"referredDate\",\n" +
                "\tconcat(m.first_name,' ',m.middle_name,' ',m.last_name) as \"name\",\n" +
                "\tm.dob as dob,\n" +
                "\tm.gender as gender,\n" +
                "\t(case when 'HT' = ANY(string_to_array(r.disease_code,',')) and hyp.systolic_bp is not null and hyp.diastolic_bp is not null then CONCAT('BP Reading - ', case when hyp.systolic_bp >= 140 then concat('<b style=\"color:red;\">', cast(hyp.systolic_bp as text), '</b>') else cast(hyp.systolic_bp as text) end, '/', case when hyp.diastolic_bp >= 90 then concat('<b style=\"color:red;\">', cast(hyp.diastolic_bp as text), '</b>') else cast(hyp.diastolic_bp as text) end) else null end) as \"referredForHypertension\",\n" +
                "\t(case when 'D' = any(string_to_array(r.disease_code, ',')) then concat('Sugar Reading - ', case when diab.fasting_blood_sugar is not null then case when diab.fasting_blood_sugar >= 126 then concat('<b style=\"color:red;\">', cast(diab.fasting_blood_sugar as text), '</b>') else cast(diab.fasting_blood_sugar as text) end when diab.post_prandial_blood_sugar is not null then case when diab.post_prandial_blood_sugar >= 200 then concat('<b style=\"color:red;\">', cast(diab.post_prandial_blood_sugar as text), '</b>') else cast(diab.post_prandial_blood_sugar as text) end when diab.blood_sugar is not null then case when diab.blood_sugar >= 200 then concat('<b style=\"color:red;\">', cast(diab.blood_sugar as text), '</b>') else cast(diab.blood_sugar as text) end end) else null end\n) as \"referredForDiabetes\",\n" +
                "\t(case when 'B' = ANY(string_to_array(r.disease_code,',')) then concat('Breast Cancer - ', case when breast.is_suspected then concat('<b style=\"color:red;\">','Abnormality detected','</b>') else 'No abnormality' end) else 'NA' end) as \"referredForBreast\",\n" +
                "\t(case when 'O' = ANY(string_to_array(r.disease_code,',')) then concat('Oral Cancer - ' ,case when oral.is_suspected then concat('<b style=\"color:red;\">','Abnormality detected','</b>') else 'No abnormality' end) else 'NA' end) as \"referredForOral\",\n" +
                "\t(case when 'C' = ANY(string_to_array(r.disease_code,',')) then true else false end) as \"referredForCervical\",\n" +
                "\tr.hmis_id as \"hmisId\"\n" +
                "from referred_members r \n" +
                "inner join imt_member m on r.member_id = m.id \n" +
                "inner join imt_family if on m.family_id = if.family_id \n" +
                "left join location_master lm on lm.id = if.location_id \n" +
                "left join ncd_member_hypertension_detail hyp on m.id = hyp.member_id and hyp.modified_on = (select max(modified_on) from ncd_member_hypertension_detail where member_id = m.id and done_by in ('FHW', 'CHO', 'MPW', 'MPHW', 'RBSK')) \n" +
                "left join ncd_member_diabetes_detail diab on m.id = diab.member_id and diab.modified_on = (select max(modified_on) from ncd_member_diabetes_detail where member_id = m.id and done_by in ('FHW', 'CHO', 'MPW', 'MPHW', 'RBSK')) \n" +
                "left join ncd_member_breast_detail breast on m.id = breast.member_id and breast.modified_on = (select max(modified_on) from ncd_member_breast_detail where member_id = m.id and done_by in ('FHW', 'CHO', 'MPW', 'MPHW', 'RBSK')) \n" +
                "left join ncd_member_oral_detail oral on m.id = oral.member_id and oral.modified_on = (select max(modified_on) from ncd_member_oral_detail where member_id = m.id and done_by in ('FHW', 'CHO', 'MPW', 'MPHW', 'RBSK')) \n" +
                "where date_part('year',age(m.dob)) >= 30 \n" +
                "order by \"name\", r.ref_date desc";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberReferralDnhddDto> sqlQuery = session.createNativeQuery(query);
        sqlQuery.addScalar(LOCATION_ID_PROPERTY,StandardBasicTypes.INTEGER)
                .addScalar(ID_PROPERTY, StandardBasicTypes.INTEGER)
                .addScalar(UNIQUE_HEALTH_ID_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(FAMILY_ID_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(LOCATION_HIERARCHY_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(LOCATION_NAME_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(MOBILE_NUMBER_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(NAME_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(GENDER_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(DOB_PROPERTY, StandardBasicTypes.DATE)
                .addScalar(REFERRED_DATE_PROPERTY, StandardBasicTypes.TIMESTAMP)
                .addScalar(REFERRED_FOR_HYPERTENSION, StandardBasicTypes.STRING)
                .addScalar(REFERRED_FOR_DIABETES, StandardBasicTypes.STRING)
                .addScalar(REFERRED_FOR_BREAST, StandardBasicTypes.STRING)
                .addScalar(REFERRED_FOR_ORAL, StandardBasicTypes.STRING)
                .addScalar(REFERRED_FOR_CERVICAL, StandardBasicTypes.STRING)
                .addScalar(HMIS_ID_PROPERTY, StandardBasicTypes.LONG);
        if (limit != null) {
            sqlQuery.setParameter(LIMIT, limit)
                    .setParameter(OFFSET, offset);
        } else {
            sqlQuery.setParameter(LIMIT, limit, LongType.INSTANCE)
                    .setParameter(OFFSET, offset, LongType.INSTANCE);
        }
        return sqlQuery.setResultTransformer(Transformers.aliasToBean(MemberReferralDnhddDto.class)).list();
    }
}
