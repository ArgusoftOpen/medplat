/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.fhs.dao.impl;

import com.argusoft.medplat.common.util.AESEncryption;
import com.argusoft.medplat.dashboard.fhs.constants.FamilyHealthSurveyServiceConstants;
import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.fhs.dao.MemberDao;
import com.argusoft.medplat.fhs.dto.ElasticSearchMemberDto;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.fhs.dto.MemberInformationDto;
import com.argusoft.medplat.fhs.dto.PregnancyRegistrationDetailDto;
import com.argusoft.medplat.fhs.model.FamilyEntity;
import com.argusoft.medplat.fhs.model.MemberEntity;
import com.argusoft.medplat.ncddnhdd.dto.MemberDetailDto;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * Implementation of methods define in member dao.
 * </p>
 *
 * @author harsh
 * @since 26/08/20 10:19 AM
 */
@Repository
@Transactional
public class MemberDaoImpl extends GenericDaoImpl<MemberEntity, Integer> implements MemberDao {

    public static final String ID_PROPERTY = "id";
    public static final String MEMBER_ID_PROPERTY = "memberId";
    public static final String FAMILY_ID_PROPERTY = "familyId";
    public static final String FAMILY_ID_STR_PROPERTY = "famId";

    public static final String UNIQUE_HEALTH_ID_PROPERTY = "uniqueHealthId";
    public static final String MOTHER_ID_PROPERTY = "motherId";
    public static final String STATE_PROPERTY = "state";
    public static final String LOCATION_ID_PROPERTY = "locationId";
    public static final String AREA_ID_PROPERTY = "areaId";
    public static final String ACCOUNT_NUMBER_PROPERTY = "accountNumber";
    public static final String MODIFIED_ON_PROPERTY = "modifiedOn";
    public static final String LOCATION_HIERARCHY_PROPERTY = "locationHierarchy";
    public static final String LOCATION_NAME_PROPERTY = "locationName";
    public static final String MOBILE_NUMBER_PROPERTY = "mobileNumber";
    public static final String NAME_PROPERTY = "name";
    public static final String FAMILY_MOBILE_NUMBER_PROPERTY = "familyMobileNumber";
    public static final String ORG_UNIT_PROPERTY = "orgUnit";
    public static final String VILLAGE_NAME_PROPERTY = "villageName";
    public static final String AADHAR_PROPERTY = "aadhar";
    public static final String DISEASE_PROPERTY = "disease";
    public static final String REFERRED_FOR_HYPERTENSION = "referredForHypertension";
    public static final String REFERRED_FOR_DIABETES = "referredForDiabetes";
    public static final String REFERRED_FOR_BREAST = "referredForBreast";
    public static final String REFERRED_FOR_ORAL = "referredForOral";
    public static final String REFERRED_FOR_CERVICAL = "referredForCervical";
    public static final String FOLLOW_UP_DATE = "followUpDate";
    public static final String OFFSET = "offset";
    public static final String LIMIT = "limit";
    public static final String HOF_MOBILE_NUMBER_PROPERTY = "hofMobileNumber";



    /**
     * {@inheritDoc}
     */
    @Override
    public MemberEntity updateMember(MemberEntity memberEntity) {
        super.update(memberEntity);
        return memberEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberEntity retrieveMemberById(Integer id) {
        if (id == null) {
            return null;
        }
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(ID_PROPERTY), id));
            return predicates;
        };
        return super.findEntityByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberEntity retrieveMemberByUniqueHealthId(String uniqueHealthId) {
        if (Objects.isNull(uniqueHealthId)) {
            return null;
        }
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(UNIQUE_HEALTH_ID_PROPERTY), uniqueHealthId));
            return predicates;
        };
        return super.findEntityByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> getMembers(List<String> states, List<Integer> locationIds, List<String> projectionList, List<String> familyIdsForQuery, Date lastUpdatedDate, List<String> basicStates) {
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (locationIds != null && !locationIds.isEmpty()) {
                Subquery<FamilyEntity> subquery = query.subquery(FamilyEntity.class);
                Root<FamilyEntity> from = subquery.from(FamilyEntity.class);

                subquery.select(from.get(FAMILY_ID_PROPERTY));
                subquery.where(builder.or(from.get(FamilyEntity.Fields.LOCATION_ID).in(locationIds), from.get(FamilyEntity.Fields.AREA_ID).in(locationIds)));

                predicates.add(root.get(FAMILY_ID_PROPERTY).in(subquery));
            }

            if (states != null && !states.isEmpty()) {
                predicates.add(builder.in(root.get(STATE_PROPERTY)).value(states));
            }

            if (!CollectionUtils.isEmpty(familyIdsForQuery)) {
                predicates.add(builder.in(root.get(FAMILY_ID_PROPERTY)).value(familyIdsForQuery));
            }

            if (lastUpdatedDate != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get(MODIFIED_ON_PROPERTY), lastUpdatedDate));
            }

            return predicates;
        };
        return findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> getMembersForAsha(List<String> states, List<Integer> areaIds, List<String> projectionList, List<String> familyIdsForQuery, Date lastUpdatedDate) {
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (areaIds != null && !areaIds.isEmpty()) {
                String q = "select family_id as \"id\" from imt_family where area_id in :areaIds";
                NativeQuery<String> nativeQuery = getCurrentSession().createNativeQuery(q);
                nativeQuery.setParameter("areaIds", areaIds).addScalar("id", StandardBasicTypes.STRING);
                List<String> familyIds = nativeQuery.getResultList();
                predicates.add(builder.in(root.get(FAMILY_ID_PROPERTY)).value(familyIds));
            }
            if (states != null && !states.isEmpty()) {
                predicates.add(builder.in(root.get(STATE_PROPERTY)).value(states));
            }

            if (!CollectionUtils.isEmpty(familyIdsForQuery)) {
                predicates.add(builder.in(root.get(FAMILY_ID_PROPERTY)).value(familyIdsForQuery));
            }

            if (lastUpdatedDate != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get(MODIFIED_ON_PROPERTY), lastUpdatedDate));
            }
            return predicates;
        };
        return findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> retrieveMemberEntitiesByFamilyId(String familyId) {
        if (familyId == null) {
            return new ArrayList<>();
        }
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(FAMILY_ID_PROPERTY), familyId));
            return predicates;
        };
        return new ArrayList<>(super.findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> retrieveMemberIdsByFamilyId(String familyId) {
        String query = "select id from imt_member where family_id = :familyId " + "and (basic_state in ('NEW','VERIFIED','REVERIFICATION') or state = 'IDSP_TEMP')";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery(query);
        q.setParameter(FAMILY_ID_PROPERTY, familyId);
        q.addScalar(ID_PROPERTY, StandardBasicTypes.INTEGER);
        return q.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> searchMembers(String searchString) {
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.like(builder.lower(root.get(UNIQUE_HEALTH_ID_PROPERTY)), builder.lower(builder.literal(searchString))));
            return predicates;
        };
        return new ArrayList<>(super.findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberEntity createMember(MemberEntity memberEntity) {
        super.create(memberEntity);
        return memberEntity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberEntity getMemberByUniqueHealthIdAndFamilyId(String uniqueHealthId, String familyId) {
        if (!(uniqueHealthId != null && !uniqueHealthId.isEmpty()) && !(familyId != null && !familyId.isEmpty())) {
            return null;
        }
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (uniqueHealthId != null && !uniqueHealthId.isEmpty()) {
                predicates.add(builder.equal(root.get(UNIQUE_HEALTH_ID_PROPERTY), uniqueHealthId));
            }
            if (familyId != null && !familyId.isEmpty()) {
                predicates.add(builder.equal(root.get(FAMILY_ID_PROPERTY), familyId));
            }
            return predicates;
        };
        return super.findEntityByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDiseaseRelationsOfMember(Integer memberId) {
        String query = "DELETE FROM imt_member_chronic_disease_rel  where member_id = :memberId ; " + " DELETE FROM imt_member_current_disease_rel  where member_id = :memberId ; " + " DELETE FROM imt_member_eye_issue_rel  where member_id = :memberId ; " + " DELETE FROM imt_member_congenital_anomaly_rel where member_id = :memberId ; ";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery(query);
        q.setParameter(MEMBER_ID_PROPERTY, memberId);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertDiseaseRelationsOfMember(Integer memberId, Set<Integer> diseaseIds, String diseaseType) {
        String tableName = "";
        StringBuilder queryString = new StringBuilder();
        switch (diseaseType) {
            case "CHRONIC":
                tableName = "insert into imt_member_chronic_disease_rel values ";
                break;
            case "CONGENITAL":
                tableName = "insert into imt_member_congenital_anomaly_rel values ";
                break;
            case "CURRENT":
                tableName = "insert into imt_member_current_disease_rel values ";
                break;
            case "EYE":
                tableName = "insert into imt_member_eye_issue_rel values ";
                break;
            default:
        }

        for (Integer diseaseId : diseaseIds) {
            queryString.append(String.format("%s (%d,%d);%n", tableName, memberId, diseaseId));
        }

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery(queryString.toString());
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFamilyIdByMemberUniqueHealthId(String uniqueHealthId) {
        String query = "select family_id as \"familyId\" from imt_member where unique_health_id like :uniqueHealthId ;";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberEntity> q = session.createNativeQuery(query);
        q.setParameter(UNIQUE_HEALTH_ID_PROPERTY, uniqueHealthId);
        List<MemberEntity> memberEntities = q.setResultTransformer(Transformers.aliasToBean(MemberEntity.class)).list();
        if (!CollectionUtils.isEmpty(memberEntities)) {
            return memberEntities.get(0).getFamilyId();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getFamilyIdsByMemberUniqueHealthIds(List<String> uniqueHealthId) {
        List<String> familyids = new ArrayList<>();
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!CollectionUtils.isEmpty(uniqueHealthId)) {
                predicates.add(builder.in(root.get(UNIQUE_HEALTH_ID_PROPERTY)).value(uniqueHealthId));
            }
            return predicates;
        };
        List<MemberEntity> memberEntities = findByCriteriaList(predicateBuilder);
        if (!CollectionUtils.isEmpty(memberEntities)) {
            for (MemberEntity member : memberEntities) {
                familyids.add(member.getFamilyId());
            }
            return familyids;
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> getChildMembersByMotherId(Integer motherId, Boolean noDeadMember) {
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (noDeadMember != null && noDeadMember) {
                predicates.add(builder.notEqual(root.get(MemberEntity.Fields.STATE), FamilyHealthSurveyServiceConstants.FHS_MEMBER_STATE_DEAD));
            }
            predicates.add(builder.equal(root.get(MemberEntity.Fields.MOTHER_ID), motherId));
            return predicates;
        };
        return new ArrayList<>(super.findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDto retrieveDetailsByMemberId(Integer memberId) {
        final String query = "\tselect imt_member.id as id,\n" + "\timt_member.unique_health_id as uniqueHealthId,\n" + "\timt_member.health_id as healthId,\n" + "\timt_member.health_id_number as healthIdNumber,\n" + "\timt_member.dob as dob,\n" + "\timt_member.family_id as familyId,\n" + "\timt_member.first_name as firstName,\n" + "\timt_member.middle_name as middleName,\n" + "\timt_member.last_name as lastName,\n" + "\timt_member.gender as gender,\n" + "\timt_member.mobile_number as mobileNumber,\n" + "\timt_member.account_number as accountNumber,\n" + "\timt_member.ifsc as ifsc,\n" + "\timt_member.jsy_beneficiary as isJsyBeneficiary,\n" + "\timt_member.kpsy_beneficiary as isKpsyBeneficiary,\n" + "\timt_member.iay_beneficiary as isIayBeneficiary,\n" + "\timt_member.chiranjeevi_yojna_beneficiary as isChiranjeeviYojnaBeneficiary,\n" + "\timt_member.cur_preg_reg_det_id as curPregRegDetId,\n" + "\timt_member.cur_preg_reg_date as curPregRegDate,\n" + "\timt_member.early_registration as isEarlyRegistration,\n" + "\timt_member.is_high_risk_case as isHighRiskCase,\n" + "\timt_member.blood_group as bloodGroup,\n" + "\timt_member.weight as weight,\n" + "\timt_member.haemoglobin as haemoglobin,\n" + "\timt_member.is_pregnant as isPregnantFlag,\n" + "\timt_member.current_gravida as currentGravida,\n" + "\timt_member.family_planning_method as familyPlanningMethod,\n" + "\timt_member.last_method_of_contraception as lastMethodOfContraception,\n" + "\timt_member.immunisation_given as immunisationGiven,\n" + "\timt_member.mother_id as motherId,\n" + "\tcast(imt_member.last_delivery_date as date) as lastDeliveryDate,\n" + "\timt_member.additional_info as additionalInfo,\n" + "\trch_pregnancy_registration_det.state as wpdState,\n" + "\trch_pregnancy_registration_det.edd as edd,\n" + "\trch_pregnancy_registration_det.lmp_date as lmpDate,\n" + "\tget_location_hierarchy(rch_pregnancy_registration_det.current_location_id) as locationHierarchy,\n" + "\timt_family.area_id as areaId,\n" + "\timt_family.location_id as locationId,\n" + "\timt_family.id as fid,\n" + "\timt_family.bpl_flag as bplFlag,\n" + "\tlistvalue_field_value_detail.value as caste,\n" + "\thof.mobile_number as hofMobileNumber\n" + "\tfrom imt_member\n" + "\tleft join rch_pregnancy_registration_det on imt_member.cur_preg_reg_det_id = rch_pregnancy_registration_det.id\n" + "\tand rch_pregnancy_registration_det.member_id = imt_member.id\n" + "\tinner join imt_family on imt_member.family_id = imt_family.family_id\n" + "\tleft join imt_member hof on imt_family.hof_id = hof.id\n" + "\tleft join listvalue_field_value_detail on imt_family.caste = cast(listvalue_field_value_detail.id as text)\n" + "\twhere imt_member.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY')\n" + "and imt_member.id = :memberId";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberDto> q = session.createNativeQuery(query);
        return q.setParameter(MEMBER_ID_PROPERTY, memberId).addScalar("id", StandardBasicTypes.INTEGER).addScalar("fid", StandardBasicTypes.INTEGER).addScalar("additionalInfo", StandardBasicTypes.STRING).addScalar("lastDeliveryDate", StandardBasicTypes.DATE).addScalar(FAMILY_ID_PROPERTY, StandardBasicTypes.STRING).addScalar(LOCATION_ID_PROPERTY, StandardBasicTypes.INTEGER).addScalar(AREA_ID_PROPERTY, StandardBasicTypes.STRING).addScalar("firstName", StandardBasicTypes.STRING).addScalar("middleName", StandardBasicTypes.STRING).addScalar("lastName", StandardBasicTypes.STRING).addScalar(MOTHER_ID_PROPERTY, StandardBasicTypes.INTEGER).addScalar(UNIQUE_HEALTH_ID_PROPERTY, StandardBasicTypes.STRING).addScalar("healthId", StandardBasicTypes.STRING).addScalar("healthIdNumber", StandardBasicTypes.STRING).addScalar("dob", StandardBasicTypes.DATE).addScalar("immunisationGiven", StandardBasicTypes.STRING).addScalar("wpdState", StandardBasicTypes.STRING).addScalar("edd", StandardBasicTypes.DATE).addScalar("lmpDate", StandardBasicTypes.DATE).addScalar(MOBILE_NUMBER_PROPERTY, StandardBasicTypes.STRING).addScalar(ACCOUNT_NUMBER_PROPERTY, StandardBasicTypes.STRING).addScalar("ifsc", StandardBasicTypes.STRING).addScalar("isJsyBeneficiary", StandardBasicTypes.BOOLEAN).addScalar("isKpsyBeneficiary", StandardBasicTypes.BOOLEAN).addScalar("isIayBeneficiary", StandardBasicTypes.BOOLEAN).addScalar("isChiranjeeviYojnaBeneficiary", StandardBasicTypes.BOOLEAN).addScalar("bplFlag", StandardBasicTypes.BOOLEAN).addScalar("caste", StandardBasicTypes.STRING).addScalar(LOCATION_HIERARCHY_PROPERTY, StandardBasicTypes.STRING).addScalar("isPregnantFlag", StandardBasicTypes.BOOLEAN).addScalar("gender", StandardBasicTypes.STRING).addScalar("currentGravida", StandardBasicTypes.SHORT).addScalar("familyPlanningMethod", StandardBasicTypes.STRING).addScalar("lastMethodOfContraception", StandardBasicTypes.STRING).addScalar("hofMobileNumber", StandardBasicTypes.STRING).addScalar("curPregRegDetId", StandardBasicTypes.INTEGER).addScalar("curPregRegDate", StandardBasicTypes.DATE).addScalar("isEarlyRegistration", StandardBasicTypes.BOOLEAN).addScalar("isHighRiskCase", StandardBasicTypes.BOOLEAN).addScalar("bloodGroup", StandardBasicTypes.STRING).addScalar("haemoglobin", StandardBasicTypes.FLOAT).addScalar("weight", StandardBasicTypes.FLOAT).setResultTransformer(Transformers.aliasToBean(MemberDto.class)).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> retrieveChildDetails(Integer id) {
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(MOTHER_ID_PROPERTY), id));
            query.orderBy(builder.desc(root.get("dob")));
            return predicates;
        };
        return findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean checkIfMemberAlreadyMarkedDead(Integer memberId) {
        String query = "select count(*) from rch_member_death_deatil where member_id = :memberId";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<BigInteger> q = session.createNativeQuery(query);
        q.setParameter(MEMBER_ID_PROPERTY, memberId);
        BigInteger uniqueResult = q.uniqueResult();
        return uniqueResult != null && uniqueResult.intValue() != 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> retrieveAshaPhoneNumberByMemberId(Integer memberId) {
        String query = "select contact_number from um_user \n" + "inner join um_role_master on\n" + "um_user.role_id = um_role_master.id\n" + "where um_user.id in\n" + "(select user_id from um_user_location where loc_id in\n" + "(select area_id from imt_family where family_id in\n" + "(select family_id from imt_member where id=" + memberId + ")))\n" + "and um_role_master.code='ASHA'";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<String> sQLQuery = session.createNativeQuery(query);
        return sQLQuery.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberInformationDto getMemberInfoByUniqueHealthId(String memberUniqueHealthId) {
        String query = "select m.id as \"memberId\", m.first_name || ' ' || m.middle_name || ' ' || m.last_name  as \"memberName\", \n"
                + "to_char(m.created_on, 'dd/mm/yyyy') as \"createdOn\",to_char(m.modified_on, 'dd/mm/yyyy') as \"modifiedOn\",m.modified_by as \"modifiedBy\","
                + "m.family_id as \"familyId\", to_char(m.dob, 'dd/mm/yyyy') as \"dob\", \n"
                + "case when m.aadhar_number_encrypted is not null then 'Yes' else 'No' end as \"aadharAvailable\",\n"
                + "m.mobile_number as \"mobileNumber\", m.is_pregnant as \"isPregnantFlag\", m.gender, \n"
                + "m.state as \"memberState\", m.ifsc, m.account_number as \"accountNumber\", \n"
                + "m.family_head as \"familyHeadFlag\", m.immunisation_given as \"immunisationGiven\",\n"
                + "case when (m.dob > now() - interval '5 years') then 'Yes' else 'No' end as \"isChild\",\n"
                + "case when (m.dob < now() - interval '18 years' and m.dob > now() - interval '45 years')  then 'Yes' else 'No' end as \"isEligibleCouple\","
                + "(select string_agg(to_char(created_on, 'dd/mm/yyyy'),',' order by created_on desc) \n"
                + "from rch_child_service_master  where member_id  = m.id group by member_id) as \"childServiceVisitDatesList\",\n"
                + "m.weight as weight, m.haemoglobin,\n"
                + "usr.first_name || ' ' || usr.middle_name || ' ' || usr.last_name  as \"fhwName\",\n"
                + "ashaName.first_name || ' ' || ashaName.middle_name || ' ' || ashaName.last_name  as \"ashaName\","
                + "usr.user_name as \"fhwUserName\", usr.contact_number as \"fhwMobileNumber\",\n"
                + "(select first_name || ' ' || middle_name || ' ' || last_name from imt_member where id = m.mother_id) as \"motherName\" ,\n"
                + "(select string_agg(to_char(created_on, 'dd/mm/yyyy'),',' order by created_on desc) \n"
                + "from rch_anc_master where member_id  = m.id group by member_id) as \"ancVisitDatesList\",\n"
                + "f.state as \"familyState\",\n"
                + "string_agg(lm.name,'> ' order by lhcd.depth desc) as \"memberLocation\"\n"
                + "from imt_member m \n"
                + "inner join imt_family f on f.family_id = m.family_id\n"
                + "left join um_user_location ul on f.location_id = ul.loc_id  and ul.state = 'ACTIVE'\n"
                + "left join um_user usr on ul.user_id = usr.id and usr.role_id = 30 and usr.state = 'ACTIVE'\n"
                + "left join um_user_location ashaLoc on f.area_id = ashaLoc.loc_id  and ashaLoc.state = 'ACTIVE'\n"
                + "left join um_user ashaName on ashaLoc.user_id = ashaname.id and ashaName.role_id = 24 and ashaName.state = 'ACTIVE'"
                + "left join location_hierchy_closer_det lhcd on (case when f.area_id is null then f.location_id else cast(f.area_id as bigint) end) = lhcd.child_id\n"
                + "left join location_master lm on lm.id = lhcd.parent_id\n"
                + "left join location_type_master loc_name on lm.type = loc_name.type\n"
                + "where unique_health_id = :uniqueHealthId\n"
                + "group by m.id,usr.first_name,usr.middle_name,usr.last_name,usr.user_name,usr.contact_number,f.state,ashaName.first_name,ashaName.middle_name,ashaName.last_name";

        NativeQuery<MemberInformationDto> q = getCurrentSession().createNativeQuery(query)
                .addScalar(MEMBER_ID_PROPERTY, StandardBasicTypes.INTEGER)
                .addScalar("memberName", StandardBasicTypes.STRING)
                .addScalar(FAMILY_ID_PROPERTY, StandardBasicTypes.STRING)
                .addScalar("dob", StandardBasicTypes.STRING)
                .addScalar("aadharAvailable", StandardBasicTypes.STRING)
                .addScalar(MOBILE_NUMBER_PROPERTY, StandardBasicTypes.STRING)
                .addScalar("isPregnantFlag", StandardBasicTypes.BOOLEAN)
                .addScalar("isChild", StandardBasicTypes.STRING)
                .addScalar("gender", StandardBasicTypes.STRING)
                .addScalar("familyHeadFlag", StandardBasicTypes.BOOLEAN)
                .addScalar("memberState", StandardBasicTypes.STRING)
                .addScalar("ifsc", StandardBasicTypes.STRING)
                .addScalar(ACCOUNT_NUMBER_PROPERTY, StandardBasicTypes.STRING)
                .addScalar("motherName", StandardBasicTypes.STRING)
                .addScalar("haemoglobin", StandardBasicTypes.FLOAT)
                .addScalar("weight", StandardBasicTypes.FLOAT)
                .addScalar("childServiceVisitDatesList", StandardBasicTypes.STRING)
                .addScalar("ancVisitDatesList", StandardBasicTypes.STRING)
                .addScalar("immunisationGiven", StandardBasicTypes.STRING)
                .addScalar("familyState", StandardBasicTypes.STRING)
                .addScalar("fhwName", StandardBasicTypes.STRING)
                .addScalar("fhwMobileNumber", StandardBasicTypes.STRING)
                .addScalar("fhwUserName", StandardBasicTypes.STRING)
                .addScalar("memberLocation", StandardBasicTypes.STRING)
                .addScalar("isEligibleCouple", StandardBasicTypes.STRING)
                .addScalar("ashaName", StandardBasicTypes.STRING)
                .addScalar("createdOn", StandardBasicTypes.STRING)
                .addScalar(MODIFIED_ON_PROPERTY, StandardBasicTypes.STRING)
                .addScalar("modifiedBy", StandardBasicTypes.INTEGER);

        q.setParameter(UNIQUE_HEALTH_ID_PROPERTY, memberUniqueHealthId);
        List<MemberInformationDto> memberInformationDto
                = q.setResultTransformer(Transformers.aliasToBean(MemberInformationDto.class)).list();

        if (memberInformationDto.isEmpty()) {
            return null;
        }

        return memberInformationDto.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PregnancyRegistrationDetailDto> getPregnancyRegistrationDetailByMemberId(Integer memberId) {
        String query = "select mthr_reg_no as \"motherRegNo\", to_char(lmp_date, 'dd/mm/yyyy') as \"lmpDate\", \n"
                + "to_char(edd, 'dd/mm/yyyy') as \"expectedDeliveryDate\",to_char(reg_date, 'dd/mm/yyyy') as \"registrationDate\",\n"
                + "state as \"pregnancyState\" from rch_pregnancy_registration_det where member_id = :memberId";

        NativeQuery<PregnancyRegistrationDetailDto> q = getCurrentSession().createNativeQuery(query)
                .addScalar("motherRegNo", StandardBasicTypes.STRING)
                .addScalar("lmpDate", StandardBasicTypes.STRING)
                .addScalar("expectedDeliveryDate", StandardBasicTypes.STRING)
                .addScalar("registrationDate", StandardBasicTypes.STRING)
                .addScalar("pregnancyState", StandardBasicTypes.STRING);

        q.setParameter(MEMBER_ID_PROPERTY, memberId);
        List<PregnancyRegistrationDetailDto> pregnancyRegistrationDetailDtos
                = q.setResultTransformer(Transformers.aliasToBean(PregnancyRegistrationDetailDto.class)).list();

        if (pregnancyRegistrationDetailDtos.isEmpty()) {
            return Collections.emptyList();
        }
        return pregnancyRegistrationDetailDtos;
    }


    @Override
    public List<String> retrieveMembersOnStatus(Integer memberId) {
        String query = "select distinct status from ncd_member_diseases_diagnosis where member_id =" + memberId;
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<String> sQLQuery = session.createNativeQuery(query);
        return sQLQuery.list();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> retrieveMemberForMigration(Boolean byAadhar, String aadhar, Boolean byHealthId, String healthId) {
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Boolean.TRUE.equals(byAadhar)) {
                String aadharEncrypted = AESEncryption.getInstance().encrypt(aadhar);
                predicates.add(builder.equal(root.get(MemberEntity.Fields.AADHAR_NUMBER), aadharEncrypted));
            }
            if (Boolean.TRUE.equals(byHealthId)) {
                predicates.add(builder.equal(root.get(MemberEntity.Fields.UNIQUE_HEALTH_ID), healthId.toUpperCase()));
            }
            return predicates;
        };
        return findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> retrieveChildUnder5ByMotherId(Integer motherId) {
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.orderBy(builder.desc(root.get(MemberEntity.Fields.DOB)));

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, -5);
            Calendar truncate = DateUtils.truncate(calendar, Calendar.DATE);

            predicates.add(builder.equal(root.get(MemberEntity.Fields.MOTHER_ID), motherId));
            predicates.add(builder.lessThanOrEqualTo(root.get(MemberEntity.Fields.DOB), truncate.getTime()));

            return predicates;
        };
        return findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberDto> searchMembers(String searchString, String searchBy, Integer limit, Integer offset) {
        if (searchString != null) {
            searchString = searchString.replace("'", "''");
        }
        String query = "";
        switch (searchBy) {
            case MEMBER_ID_PROPERTY:
                query += "with  members as(\n" + "select m.id , m.first_name  ,m.last_name  ,\n" + "m.family_id, \n" + "m.mobile_number ,  \n" + " m.account_number, \n" + " f.location_id , \n" + " m.middle_name , \n" + " f.area_id  \n" + "\n" + "from imt_member m \n" + "inner join imt_family f on f.family_id = m.family_id\n" + "where unique_health_id ='" + searchString + "' \n" + "limit :limit " + "offset :offset\n" + ")";

                break;
            case FAMILY_ID_PROPERTY:
                query += "with  members as(\n" + "select m.id , m.first_name  ,m.last_name  ,\n" + "m.family_id, \n" + "m.mobile_number ,  \n" + " m.account_number, \n" + " f.location_id , \n" + " m.middle_name , \n" + " f.area_id  \n" + "\n" + "from imt_member m \n" + "inner join imt_family f on f.family_id = m.family_id\n" + "where m.family_id ='" + searchString + "' \n" + "limit :limit offset :offset\n" + ")";

                break;
            case FAMILY_MOBILE_NUMBER_PROPERTY:

                query += "with  members as(\n" + "select m.id , m.first_name  ,m.last_name  ,\n" + "m.family_id, \n" + "m.mobile_number ,  \n" + " m.account_number, \n" + " f.location_id , \n" + " m.middle_name , \n" + " f.area_id  \n" + "\n" + "from imt_member m \n" + "inner join imt_family f on f.family_id = m.family_id\n" + "where f.family_id in (select family_id from imt_member where mobile_number='" + searchString + "')" + "limit :limit offset :offset\n" + ")";

                break;

            case MOBILE_NUMBER_PROPERTY:
                query += "with  members as(\n" + "select m.id , m.first_name  ,m.last_name  ,\n" + "m.family_id, \n" + "m.mobile_number ,  \n" + " m.account_number, \n" + " f.location_id , \n" + " m.middle_name , \n" + " f.area_id  \n" + "\n" + "from imt_member m \n" + "inner join imt_family f on f.family_id = m.family_id\n" + "\n" + "where m.mobile_number ='" + searchString + "' \n" + "limit :limit offset :offset\n" + ")" + "";

                break;
            case ORG_UNIT_PROPERTY:
                query = "with  members as(\n" + "select m.id , m.first_name  ,m.last_name  ,\n" + "m.family_id, \n" + "m.mobile_number ,  \n" + " m.account_number, \n" + " f.location_id , \n" + " m.middle_name , \n" + " f.area_id  \n" + "\n" + "from imt_member m \n" + "inner join imt_family f on f.family_id = m.family_id\n" + "\n" + "where f.location_id in ( select child_id from location_hierchy_closer_det   where parent_id = " + searchString + ")\n" + "limit :limit offset :offset\n" + ")";
                break;
            default:

        }
        query += " select m.id as \"id\", m.first_name as firstName ,m.last_name  as \"lastName\", \n" + "m.family_id as \"familyId\", \n" + "m.mobile_number as \"mobileNumber\",  \n" + " m.account_number as \"accountNumber\", \n" + " f.location_id as \"locationId\", \n" + " m.middle_name as \"middleName\", \n" + " cast(f.area_id as text) as \"areaId\", \n" + "string_agg(lm.name,'> ' order by lhcd.depth desc) as \"locationHierarchy\"\n" + "from members m \n" + "inner join imt_family f on f.family_id = m.family_id\n" + "left join location_hierchy_closer_det lhcd on (case when f.area_id is null then f.location_id else cast(f.area_id as bigint) end) = lhcd.child_id\n" + "left join location_master lm on lm.id = lhcd.parent_id\n" + "left join location_type_master loc_name on lm.type = loc_name.type\n" + " group by m.id, f.location_id,f.area_id ,m.first_name,m.last_name,m.family_id,m.mobile_number,m.account_number ,m.middle_name ";
        Session currentSession = sessionFactory.getCurrentSession();
        NativeQuery<MemberDto> nativeQuery = currentSession.createNativeQuery(query);
        return nativeQuery.addScalar(ID_PROPERTY, StandardBasicTypes.INTEGER).addScalar(LOCATION_ID_PROPERTY, StandardBasicTypes.INTEGER).addScalar("firstName", StandardBasicTypes.STRING).addScalar("middleName", StandardBasicTypes.STRING).addScalar("lastName", StandardBasicTypes.STRING).addScalar(AREA_ID_PROPERTY, StandardBasicTypes.STRING).addScalar(MOBILE_NUMBER_PROPERTY, StandardBasicTypes.STRING).addScalar(ACCOUNT_NUMBER_PROPERTY, StandardBasicTypes.STRING).addScalar(FAMILY_ID_PROPERTY, StandardBasicTypes.STRING).addScalar(LOCATION_HIERARCHY_PROPERTY, StandardBasicTypes.STRING).setParameter(LIMIT, limit).setParameter(OFFSET, offset).setResultTransformer(Transformers.aliasToBean(MemberDto.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMotherIdInChildren(Integer motherId, List<Integer> childIds) {
        String query = "update imt_member set mother_id = :motherId, modified_on = now() where id in (:childIds)";

        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.setParameter(MOTHER_ID_PROPERTY, motherId);
        q.setParameterList("childIds", childIds);
        q.executeUpdate();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> retrieveMembersByFamilyList(List<String> familyIds) {
        if (familyIds == null || familyIds.isEmpty()) {
            return new ArrayList<>();
        }

        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(root.get(MemberEntity.Fields.FAMILY_ID).in(familyIds));
            return predicates;
        };
        return findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> retrieveMembersByPhoneNumber(String phoneNumber, String familyId) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return Collections.emptyList();
        }

        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(MemberEntity.Fields.MOBILE_NUMBER), phoneNumber));
            predicates.add(root.get(MemberEntity.Fields.BASIC_STATE).in(FamilyHealthSurveyServiceConstants.VALID_MEMBERS_BASIC_STATES));
            if (familyId != null) {
                predicates.add(builder.equal(root.get(MemberEntity.Fields.FAMILY_ID), familyId));
            }
            return predicates;
        };

        return findByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean checkIfDOBIsUnique(String familyId, String mobileNumber) {
        String query = "select CASE WHEN count(distinct dob)= count(dob)\n" + "THEN true ELSE false END \n" + " FROM imt_member  where family_id=:familyId and mobile_number =:mobileNumber and basic_state in ('VERIFIED','NEW')";
        return (Boolean) getCurrentSession().createNativeQuery(query).setParameter(FAMILY_ID_PROPERTY, familyId).setParameter(MOBILE_NUMBER_PROPERTY, mobileNumber).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> verifyMemberDetailByFamilyId(String familyId, Long dob, String aadharNumber) {
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (dob != null) {
                predicates.add(builder.equal(root.get(MemberEntity.Fields.DOB), new Date(dob)));
            } else if (aadharNumber != null && !familyId.isEmpty()) {
                String aadharEncrypted = AESEncryption.getInstance().encrypt(aadharNumber);
                predicates.add(builder.equal(root.get(MemberEntity.Fields.AADHAR_NUMBER), aadharEncrypted));
            }
            predicates.add(builder.equal(root.get(MemberEntity.Fields.FAMILY_ID), familyId));
            predicates.add(builder.in(root.get(MemberEntity.Fields.BASIC_STATE)).value(FamilyHealthSurveyServiceConstants.VALID_MEMBERS_BASIC_STATES));
            return predicates;
        };
        List<MemberEntity> memberEntities = super.findByCriteriaList(predicateBuilder);
        if (CollectionUtils.isEmpty(memberEntities)) {
            return Collections.emptyList();
        }
        return memberEntities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMobileNumberInFamilyExceptVerifiedFamily(String verifiedFamilyId, String mobileNumber) {
        String query = "update imt_member set mobile_number = null where mobile_number =:mobileNumber and family_id <> :familyId";

        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.setParameter(FAMILY_ID_PROPERTY, verifiedFamilyId);
        q.setParameter(MOBILE_NUMBER_PROPERTY, mobileNumber);

        q.executeUpdate();
    }
    @Override
    public List<MemberDetailDto> retrieveNcdMembersForFollowup(Integer userId, Integer limit, Integer offset, String healthInfrastructureType, String[] status) {
        String fromColumn = status != null && "REFERRED".equals(status[0]) ? "nmr.health_infrastructure_id" : "nmr.referred_from_health_infrastructure_id";
        String orWhereClause = "";
        if (("REFERRED").equals(status[0])) {
            orWhereClause = " OR ((status = 'SUSPECTED' OR status = 'CONFIRMATION_PENDING') AND health_infrastructure_id IS NOT NULL) ";
        } else if (("CONFIRMED").equals(status[0])) {
            orWhereClause = " OR ((status = 'SUSPECTED' OR status = 'CONFIRMATION_PENDING') AND health_infrastructure_id IS NULL) ";
        }
        String query = "with referred_members as (select nmr.member_id,nmr.follow_up_date, string_agg(nmr.disease_code,',') disease_code from ncd_member_referral nmr\n"
                + "where nmr.disease_code in ('O','HT','C','B','D') AND nmr.state = 'PENDING' AND \n"
                + "(nmr.status IN ('" + String.join("', '", status) + "') " + orWhereClause + ")\n"
                + "AND follow_up_date < current_date + interval '1 day' - interval '00:00:01'\n"
                + "and " + fromColumn + " in (\n"
                + "(select hid.id from health_infrastructure_details hid, user_health_infrastructure uhi\n"
                + "where uhi.health_infrastrucutre_id = hid.id and uhi.user_id = " + userId + " and uhi.state = 'ACTIVE')\n"
                + ") group by nmr.member_id,nmr.follow_up_date\n"
                + "limit :limit "
                + "offset :offset\n"
                + ")\n"
                + "select distinct m.id, m.family_id as \"famId\", r.disease_code,to_char(r.follow_up_date,'MM/DD/YYYY') as \"followUpDate\",r.follow_up_date ,hyp.id,\n"
                + "m.unique_health_id as uniqueHealthId,\n"
                + "get_location_hierarchy(if.location_id) as locationHierarchy,\n"
                + "lm.name as locationName,\n"
                + "m.mobile_number as mobileNumber,\n"
                + "concat(m.first_name,' ',m.middle_name,' ',m.last_name) as \"name\",\n"
                + "(case when 'HT' = ANY(string_to_array(r.disease_code,',')) then CONCAT('Hypertension - ', case when hyp.systolic_bp is not null then cast(hyp.systolic_bp as text) else 'N.A' end, '/', case when hyp.diastolic_bp is not null then cast(hyp.diastolic_bp as text) else 'N.A' end) else null end) as referredForHypertension,\n"
                + "(case when 'D' = ANY(string_to_array(r.disease_code,',')) then CONCAT('Diabetes - ', case when diab.blood_sugar is not null then cast(diab.blood_sugar as text) else 'N.A' end) else null end) as referredForDiabetes,\n"
                + "(case when 'B' = ANY(string_to_array(r.disease_code,',')) then true else false end) as referredForBreast,\n"
                + "(case when 'O' = ANY(string_to_array(r.disease_code,',')) then true else false end) as referredForOral,\n"
                + "(case when 'C' = ANY(string_to_array(r.disease_code,',')) then true else false end) as referredForCervical\n"
                + "from referred_members r \n"
                + "inner join imt_member m on m.id = r.member_id\n"
                + "inner join imt_family if on m.family_id = if.family_id \n"
                + "left join location_master lm on lm.id = if.location_id \n"
                + "left join ncd_member_hypertension_detail hyp on m.id = hyp.member_id and hyp.modified_on = (select max(modified_on) from ncd_member_hypertension_detail where member_id = m.id)\n"
                + "left join ncd_member_diabetes_detail diab on m.id = diab.member_id and diab.modified_on = (select max(modified_on) from ncd_member_diabetes_detail where member_id = m.id) \n"
                + "order by r.follow_up_date desc";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberDetailDto> sqlQuery = session.createNativeQuery(query);
        return sqlQuery
                .addScalar(ID_PROPERTY, StandardBasicTypes.INTEGER)
                .addScalar(FAMILY_ID_STR_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(UNIQUE_HEALTH_ID_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(NAME_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(LOCATION_HIERARCHY_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(LOCATION_NAME_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(MOBILE_NUMBER_PROPERTY, StandardBasicTypes.STRING)
                .addScalar(FOLLOW_UP_DATE, StandardBasicTypes.STRING)
                .addScalar(REFERRED_FOR_HYPERTENSION, StandardBasicTypes.STRING)
                .addScalar(REFERRED_FOR_DIABETES, StandardBasicTypes.STRING)
                .addScalar(REFERRED_FOR_BREAST, StandardBasicTypes.STRING)
                .addScalar(REFERRED_FOR_ORAL, StandardBasicTypes.STRING)
                .addScalar(REFERRED_FOR_CERVICAL, StandardBasicTypes.STRING)
                .setParameter(LIMIT, limit)
                .setParameter(OFFSET, offset)
                .setResultTransformer(Transformers.aliasToBean(MemberDetailDto.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberEntity getFamilyHeadMemberDetail(String familyId) {
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(MemberEntity.Fields.FAMILY_HEAD_FLAG), true));
            predicates.add(builder.equal(root.get(MemberEntity.Fields.FAMILY_ID), familyId));
            predicates.add(builder.in(root.get(MemberEntity.Fields.BASIC_STATE)).value(FamilyHealthSurveyServiceConstants.VALID_MEMBERS_BASIC_STATES));
            return predicates;
        };
        return super.findEntityByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ElasticSearchMemberDto> retrieveMembersByIds(List<Integer> ids) {
        String query = "SELECT id,unique_health_id as \"uniquehealthid\"," + "first_name as \"firstname\" ,last_name as \"lastname\" ," + "middle_name as \"middlename\"  from imt_member where id in ( :ids ) ";

        NativeQuery<ElasticSearchMemberDto> sQLQuery = getCurrentSession().createNativeQuery(query);
        List<ElasticSearchMemberDto> elasticSearchMemberDtos = sQLQuery.addScalar(ID_PROPERTY, StandardBasicTypes.INTEGER).addScalar(UNIQUE_HEALTH_ID_PROPERTY, StandardBasicTypes.STRING).addScalar("firstname", StandardBasicTypes.STRING).addScalar("lastname", StandardBasicTypes.STRING).addScalar("middlename", StandardBasicTypes.STRING).setParameterList("ids", ids).setResultTransformer(Transformers.aliasToBean(ElasticSearchMemberDto.class)).list();

        if (CollectionUtils.isEmpty(elasticSearchMemberDtos)) {
            return Collections.emptyList();
        }
        return elasticSearchMemberDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ElasticSearchMemberDto> retrieveMembersByQuery(String quries) {
        String query = "SELECT id,unique_health_id as \"uniquehealthid\"," + "first_name as \"firstname\" ,last_name as \"lastname\" ," + "middle_name as \"middlename\"  from imt_member where first_name ilike '%" + quries + "%' limit 10;";

        NativeQuery<ElasticSearchMemberDto> sQLQuery = getCurrentSession().createNativeQuery(query);
        List<ElasticSearchMemberDto> elasticSearchMemberDtos = sQLQuery.addScalar(ID_PROPERTY, StandardBasicTypes.INTEGER).addScalar("uniquehealthid", StandardBasicTypes.STRING).addScalar("firstname", StandardBasicTypes.STRING).addScalar("lastname", StandardBasicTypes.STRING).addScalar("middlename", StandardBasicTypes.STRING).setResultTransformer(Transformers.aliasToBean(ElasticSearchMemberDto.class)).list();

        if (CollectionUtils.isEmpty(elasticSearchMemberDtos)) {
            return Collections.emptyList();
        }
        return elasticSearchMemberDtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> getEqualDobMembers(String familyId, String dob) {
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dob);
            PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (dob != null) {
                    predicates.add(builder.equal(root.get(MemberEntity.Fields.DOB), new Date(date1.getTime())));
                }
                predicates.add(builder.equal(root.get(MemberEntity.Fields.FAMILY_ID), familyId));
                return predicates;
            };
            return new ArrayList<>(super.findByCriteriaList(predicateBuilder));
        } catch (ParseException e) {
            return Collections.emptyList();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberEntity> retriveMemberByFamilyIdAndStates(String familyId, List<String> states) {
        if (familyId == null) {
            return new ArrayList<>();
        }
        PredicateBuilder<MemberEntity> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(FAMILY_ID_PROPERTY), familyId));
            predicates.add(builder.in(root.get(STATE_PROPERTY)).value(states));
            return predicates;
        };
        return new ArrayList<>(super.findByCriteriaList(predicateBuilder));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIfDeathEntryExists(Integer memberId) {
        String query = "select count(*) from rch_member_death_deatil where member_id = :memberId";
        NativeQuery<BigInteger> sqlQuery = getCurrentSession().createNativeQuery(query);
        sqlQuery.setParameter(MEMBER_ID_PROPERTY, memberId);
        BigInteger count = sqlQuery.uniqueResult();
        return count.intValue() != 0;
    }

    @Override
    public String getFamilyIdByPhoneNumber(String hofMobileNumber, String mobileNumber) {
        String query = "select imt_member.family_id as \"familyId\", imt_family.state as \"familyState\" from imt_member\n" +
                "inner join imt_family on imt_member.family_id = imt_family.family_id\n" +
                "where (imt_member.mobile_number = :hofMobileNumber or imt_member.mobile_number = :mobileNumber) and imt_member.basic_state in ('VERIFIED','REVERIFICATION','NEW','TEMPORARY') ;";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberInformationDto> q = session.createNativeQuery(query);
        q.setParameter(HOF_MOBILE_NUMBER_PROPERTY, hofMobileNumber);
        q.setParameter(MOBILE_NUMBER_PROPERTY, mobileNumber);
        List<MemberInformationDto> memberInformationDtos = q.setResultTransformer(Transformers.aliasToBean(MemberInformationDto.class)).list();
        if (!CollectionUtils.isEmpty(memberInformationDtos)) {
            if (memberInformationDtos.size() > 1) {
                List<MemberInformationDto> filteredList = memberInformationDtos.stream()
                        .filter(member -> member.getFamilyState().equals("CFHC_FN"))
                        .collect(Collectors.toList());
                if (!filteredList.isEmpty()) {
                    return filteredList.get(0).getFamilyId();
                }
            }
            return memberInformationDtos.get(0).getFamilyId();
        } else {
            return null;
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer retrieveIdOfListValuesByFieldKeyAndValue(String fieldKey, String value) {
        String query = "select id from listvalue_field_value_detail where value = :value and field_key = :fieldKey ";
        NativeQuery<Integer> sqlQuery = getCurrentSession().createNativeQuery(query);
        sqlQuery.setParameter("fieldKey", fieldKey);
        sqlQuery.setParameter("value", value);
        return sqlQuery.uniqueResult();
    }

    @Override
    public void createPregnancyRegistrationDetEntry(Integer memberId, Integer familyId, Date lmpDate, Date edd, Date regDate,
                                                    Integer locationId, Integer userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        String query = "with rows as (\n" +
                "INSERT INTO rch_pregnancy_registration_det(\n" +
                "            member_id, family_id, lmp_date, edd, reg_date, state,location_id,current_location_id, created_on, \n" +
                "            created_by, modified_on, modified_by)\n" +
                "    VALUES ("+memberId+", "+familyId+", '"+sdf.format(lmpDate)+"', '"+sdf.format(edd)+"', '"+sdf.format(regDate)+"', 'PENDING', "+locationId+", "+locationId+" ,now(), \n" +
                "            "+userId+", now(), "+userId+") RETURNING id\n" +
                ")\n" +
                "\n" +
                "    update imt_member set is_pregnant = true, edd =  '"+sdf.format(edd)+"', \n" +
                "    early_registration = (cast('"+sdf.format(lmpDate)+"' as date) + INTERVAL '84 days') >= '"+sdf.format(regDate)+"' , \n" +
                "    cur_preg_reg_det_id = (select id from rows),\n" +
                "    cur_preg_reg_date= '"+sdf.format(regDate)+"',\n" +
                "    anc_visit_dates = null,\n" +
                "    immunisation_given = null,\n" +
                "    modified_by = "+userId+", \n" +
                "    modified_on = now()\n" +
                "    where id= "+memberId;

        NativeQuery<Integer> sqlQuery = getCurrentSession().createNativeQuery(query);
        sqlQuery.executeUpdate();
    }
}