/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.rch.dao.WpdMotherDao;
import com.argusoft.medplat.rch.dto.WpdChildDto;
import com.argusoft.medplat.rch.dto.WpdMasterDto;
import com.argusoft.medplat.rch.model.VisitCommonFields;
import com.argusoft.medplat.rch.model.WpdMotherMaster;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * Implementation of methods define in wpd mother dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class WpdMotherDaoImpl extends GenericDaoImpl<WpdMotherMaster, Integer> implements WpdMotherDao {

    private static final String MEMBERID = "memberId";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WpdMasterDto> retrievePendingDischargeList(Integer userId) {
        String query = "with health_infra_details as (\n" +
                "\tselect health_infrastrucutre_id from user_health_infrastructure where user_id = :userId  and state = 'ACTIVE'\n" +
                "),member_details as (\n" +
                "\tselect *\n" +
                "\tfrom rch_wpd_mother_master\n" +
                "\twhere (rch_wpd_mother_master.is_discharged = false)\n" +
                "\tand rch_wpd_mother_master.is_from_web\n" +
                "\tand rch_wpd_mother_master.health_infrastructure_id in (select health_infrastrucutre_id from health_infra_details)\n" +
                "),details as (\n" +
                "\tselect member_details.id as \"id\",\n" +
                "\timt_member.unique_health_id as \"uniqueHealthId\",\n" +
                "\tconcat(imt_member.first_name,' ',imt_member.middle_name,' ',imt_member.last_name) as \"name\",\n" +
                "\tmember_details.date_of_delivery as \"deliveryDate\",\n" +
                "\tmember_details.type_of_delivery as \"typeOfDelivery\",\n" +
                "\trch_wpd_child_master.id as \"childId\",\n" +
                "\trch_wpd_child_master.pregnancy_outcome as \"pregnancyOutcome\",\n" +
                "\trch_wpd_child_master.member_id as \"childMemberId\",\n" +
                "\trch_wpd_child_master.name as \"childName\",\n" +
                "\trch_wpd_child_master.family_id as \"familyId\",\n" +
                "\trch_wpd_child_master.location_id as \"locationId\"\n" +
                "\tfrom member_details\n" +
                "\tinner join imt_member on member_details.member_id = imt_member.id\n" +
                "\tand imt_member.cur_preg_reg_det_id = member_details.pregnancy_reg_det_id\n" +
                "\tleft join rch_wpd_child_master on member_details.id = rch_wpd_child_master.wpd_mother_id\n" +
                "\tand rch_wpd_child_master.pregnancy_outcome = 'LBIRTH'\n" +
                "),immunisation_details as (\n" +
                "\tselect details.\"childMemberId\",\n" +
                "\tjson_agg(rch_immunisation_master.immunisation_given) as \"givenChildImmuns\"\n" +
                "\tfrom details\n" +
                "\tleft join rch_immunisation_master on details.\"childMemberId\" = rch_immunisation_master.member_id\n" +
                "\tand details.\"childMemberId\" != -1\n" +
                "\tand rch_immunisation_master.immunisation_given in ('" + MobileConstantUtil.IMMUNISATION_BCG + "','" + MobileConstantUtil.IMMUNISATION_OPV_0 + "','" + MobileConstantUtil.IMMUNISATION_HEPATITIS_B_0 + "','" + MobileConstantUtil.IMMUNISATION_VITAMIN_K + "')\n" +
                "\tgroup by details.\"childMemberId\"\n" +
                ")select details.*,\n" +
                "immunisation_details.\"givenChildImmuns\"\n" +
                "from details\n" +
                "left join immunisation_details on details.\"childMemberId\" = immunisation_details.\"childMemberId\"\n" +
                "order by details.\"deliveryDate\" desc";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<WpdMasterDto> q = session.createNativeQuery(query);
        q.setParameter("userId", userId);
        List<WpdMasterDto> wpdMasterDtoList = q
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("uniqueHealthId", StandardBasicTypes.STRING)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("deliveryDate", StandardBasicTypes.DATE)
                .addScalar("typeOfDelivery", StandardBasicTypes.STRING)
                .addScalar("pregnancyOutcome", StandardBasicTypes.STRING)
                .addScalar("childName", StandardBasicTypes.STRING)
                .addScalar("childId", StandardBasicTypes.INTEGER)
                .addScalar("childMemberId", StandardBasicTypes.INTEGER)
                .addScalar("familyId", StandardBasicTypes.INTEGER)
                .addScalar("locationId", StandardBasicTypes.INTEGER)
                .addScalar("givenChildImmuns", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(WpdMasterDto.class)).list();

        Map<Integer, WpdMasterDto> map = new HashMap<>();
        List<WpdMasterDto> mappedWpdMasterDtoList = new ArrayList<>();
        for (WpdMasterDto wpdMasterDto : wpdMasterDtoList) {
            Integer key = wpdMasterDto.getId();
            if (map.containsKey(key)) {
                if (wpdMasterDto.getChildId() != null) {
                    WpdMasterDto mappedWpdMasterDto = map.get(key);
                    List<WpdChildDto> childDtoList = mappedWpdMasterDto.getChildDetails();
                    WpdChildDto childDto = new WpdChildDto();
                    childDto.setName(wpdMasterDto.getChildName());
                    childDto.setId(wpdMasterDto.getChildId());
                    childDto.setMemberId(wpdMasterDto.getChildMemberId());
                    childDto.setFamilyId(wpdMasterDto.getFamilyId());
                    childDto.setLocationId(wpdMasterDto.getLocationId());
                    childDto.setGivenImmunisations(wpdMasterDto.getGivenChildImmuns());
                    childDtoList.add(childDto);
                    mappedWpdMasterDto.setChildDetails(childDtoList);
                }
            } else {
                WpdMasterDto wpdDto = new WpdMasterDto();
                wpdDto.setId(wpdMasterDto.getId());
                wpdDto.setUniqueHealthId(wpdMasterDto.getUniqueHealthId());
                wpdDto.setName(wpdMasterDto.getName());
                wpdDto.setDeliveryDate(wpdMasterDto.getDeliveryDate());
                wpdDto.setTypeOfDelivery(wpdMasterDto.getTypeOfDelivery());
                if (wpdMasterDto.getChildId() != null) {
                    List<WpdChildDto> childDtoList = new ArrayList<>();
                    WpdChildDto childDto = new WpdChildDto();
                    childDto.setName(wpdMasterDto.getChildName());
                    childDto.setId(wpdMasterDto.getChildId());
                    childDto.setMemberId(wpdMasterDto.getChildMemberId());
                    childDto.setFamilyId(wpdMasterDto.getFamilyId());
                    childDto.setLocationId(wpdMasterDto.getLocationId());
                    childDto.setGivenImmunisations(wpdMasterDto.getGivenChildImmuns());
                    childDtoList.add(childDto);
                    wpdDto.setChildDetails(childDtoList);
                }
                mappedWpdMasterDtoList.add(wpdDto);
                map.put(key, wpdDto);
            }
        }

        return mappedWpdMasterDtoList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WpdMotherMaster> getWpdMotherbyMemberid(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<WpdMotherMaster> criteriaQuery = criteriaBuilder.createQuery(WpdMotherMaster.class);
        Root<WpdMotherMaster> root = criteriaQuery.from(WpdMotherMaster.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(VisitCommonFields.Fields.MEMBER_ID), memberId);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(EntityAuditInfo.Fields.CREATED_ON)));
        return session.createQuery(criteriaQuery).setMaxResults(3).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WpdMotherMaster> getWpdMotherbyMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfrasturctureId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<WpdMotherMaster> criteriaQuery = criteriaBuilder.createQuery(WpdMotherMaster.class);
        Root<WpdMotherMaster> root = criteriaQuery.from(WpdMotherMaster.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(VisitCommonFields.Fields.MEMBER_ID), memberId);
        Predicate healthInfraEqual = criteriaBuilder.equal(root.get(WpdMotherMaster.Fields.HEALTH_INFRA_ID), healthInfrasturctureId);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, healthInfraEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(EntityAuditInfo.Fields.CREATED_ON)));
        return session.createQuery(criteriaQuery).setMaxResults(3).getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WpdMotherMaster> getWpdMotherMasterByCriteria(Integer pregnancyRegistrationId, Integer memberId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(VisitCommonFields.Fields.MEMBER_ID), memberId));
            predicates.add(criteriaBuilder.equal(root.get(WpdMotherMaster.Fields.PREGNANCY_REG_DET_ID), pregnancyRegistrationId));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WpdMotherMaster getWpdMotherMasterForBreastFeedingUpdate(Date actionDate, Integer memberId) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(actionDate);
        cal.add(Calendar.MINUTE, -1);
        Date from = cal.getTime();
        cal.add(Calendar.MINUTE, 2);
        Date to = cal.getTime();
        return super.findEntityByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.between(root.get(EntityAuditInfo.Fields.CREATED_ON), from, to));
            predicates.add(criteriaBuilder.equal(root.get(VisitCommonFields.Fields.MEMBER_ID), memberId));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateDischargeDetailsOfMember(Integer memberId, Date dischargeDate, Integer pregRegId, Integer userId) {
        String query = "update rch_wpd_mother_master set is_discharged = true, discharge_date = :dischargeDate \n"
                + "where member_id = :memberId and pregnancy_reg_det_id = :pregRegId \n"
                + "and delivery_place in ('HOSP', 'THISHOSP') and (is_discharged is null or is_discharged = false);\n"
                + "\n"
                + "update techo_notification_master set state = 'COMPLETED' , modified_by = :userId , modified_on = now()  where member_id = :memberId and state = 'PENDING' and \n"
                + "ref_code = :pregRegId and notification_type_id = (SELECT id FROM notification_type_master WHERE code = 'DISCHARGE');";

        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query);
        q.setParameter("dischargeDate", dischargeDate);
        q.setParameter(MEMBERID, memberId);
        q.setParameter("pregRegId", pregRegId);
        q.setParameter("userId", userId);
        q.executeUpdate();
    }

    @Override
    public List<MemberDto> retrieveWpdMembers(Boolean byId, Boolean byMemberId, Boolean byFamilyId, Boolean byMobileNumber, Boolean byName, Boolean byLmp, Boolean byEdd, Boolean byOrganizationUnit, Boolean byAbhaNumber, Boolean byAbhaAddress, Integer locationId, String searchString, Boolean byFamilyMobileNumber, Integer limit, Integer offSet) {
        if (searchString != null) {
            searchString = searchString.replaceAll("[']", "''");
        }
        String query = "";
        String states = "'PREGNANT','PENDING','DELIVERY_DONE'";
        final String DETAILS_QUERY = "\tselect imt_member.id as id,\n" +
                "\timt_member.unique_health_id as uniqueHealthId,\n" +
                "\timt_member.health_id as healthId,\n" +
                "\timt_member.health_id_number as healthIdNumber,\n" +
                "\timt_member.dob as dob,\n" +
                "\timt_member.family_id as familyId,\n" +
                "\timt_member.first_name as firstName,\n" +
                "\timt_member.middle_name as middleName,\n" +
                "\timt_member.last_name as lastName,\n" +
                "\timt_member.gender as gender,\n" +
                "\timt_member.mobile_number as mobileNumber,\n" +
                "\timt_member.account_number as accountNumber,\n" +
                "\timt_member.ifsc as ifsc,\n" +
                "\timt_member.jsy_beneficiary as isJsyBeneficiary,\n" +
                "\timt_member.kpsy_beneficiary as isKpsyBeneficiary,\n" +
                "\timt_member.iay_beneficiary as isIayBeneficiary,\n" +
                "\timt_member.chiranjeevi_yojna_beneficiary as isChiranjeeviYojnaBeneficiary,\n" +
                "\timt_member.cur_preg_reg_det_id as curPregRegDetId,\n" +
                "\timt_member.cur_preg_reg_date as curPregRegDate,\n" +
                "\timt_member.early_registration as isEarlyRegistration,\n" +
                "\timt_member.is_high_risk_case as isHighRiskCase,\n" +
                "\timt_member.blood_group as bloodGroup,\n" +
                "\timt_member.weight as weight,\n" +
                "\timt_member.haemoglobin as haemoglobin,\n" +
                "\timt_member.is_pregnant as isPregnantFlag,\n" +
                "\timt_member.current_gravida as currentGravida,\n" +
                "\timt_member.family_planning_method as familyPlanningMethod,\n" +
                "\timt_member.last_method_of_contraception as lastMethodOfContraception,\n" +
                "\timt_member.immunisation_given as immunisationGiven,\n" +
                "\timt_member.mother_id as motherId,\n" +
                "\tcast(imt_member.last_delivery_date as date) as lastDeliveryDate,\n" +
                "\timt_member.additional_info as additionalInfo,\n" +
                "\trch_pregnancy_registration_det.state as wpdState,\n" +
                "\trch_pregnancy_registration_det.edd as edd,\n" +
                "\trch_pregnancy_registration_det.lmp_date as lmpDate,\n" +
                "\tget_location_hierarchy(rch_pregnancy_registration_det.current_location_id) as locationHierarchy,\n" +
                "\timt_family.area_id as areaId,\n" +
                "\timt_family.location_id as locationId,\n" +
                "\timt_family.id as fid,\n" +
                "\timt_family.bpl_flag as bplFlag,\n" +
                "\tlistvalue_field_value_detail.value as caste,\n" +
                "\thof.mobile_number as hofMobileNumber\n";
        final String FROM_WHERE_CONDITIONS = "\tfrom rch_pregnancy_registration_det\n" +
                "\tinner join imt_member on rch_pregnancy_registration_det.id = imt_member.cur_preg_reg_det_id\n" +
                "\tand rch_pregnancy_registration_det.member_id = imt_member.id\n" +
                "\tinner join imt_family on imt_member.family_id = imt_family.family_id\n" +
                "\tleft join imt_member hof on imt_family.hof_id = hof.id\n" +
                "\tleft join listvalue_field_value_detail on imt_family.caste = cast(listvalue_field_value_detail.id as text)\n" +
                "\twhere rch_pregnancy_registration_det.state in (" + states + ")\n" +
                "\tand (\n" +
                "\t(rch_pregnancy_registration_det.state = 'DELIVERY_DONE' and rch_pregnancy_registration_det.delivery_date > now() - interval '1 month')\n" +
                "\tor rch_pregnancy_registration_det.state != 'DELIVERY_DONE'\n" +
                "\t)\n" +
                "\tand rch_pregnancy_registration_det.edd > now() - interval '2 years'\n" +
                "\tand imt_member.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY')\n";
        final String RECORDS_QUERY = "select details.*\n" +
                "from details\n";
        if (Boolean.TRUE.equals(byMemberId)) {
            searchString = Arrays.asList(searchString.split(",")).stream().map(e -> "'" + e + "'").collect(Collectors.joining(","));
            query = "with details as (\n"
                    + DETAILS_QUERY
                    + FROM_WHERE_CONDITIONS
                    + "and imt_member.unique_health_id in (" + searchString + ")\n"
                    + ")\n"
                    + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byFamilyId)) {
            query = "with details as (\n"
                    + DETAILS_QUERY
                    + FROM_WHERE_CONDITIONS
                    + "and imt_member.family_id in ('" + searchString + "')\n"
                    + ")\n"
                    + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byAbhaAddress)) {
            query = "with details as (\n"
                    + DETAILS_QUERY
                    + FROM_WHERE_CONDITIONS
                    + "and imt_member.health_id in ('" + searchString + "')\n"
                    + ")\n"
                    + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byAbhaNumber)) {
            query = "with details as (\n"
                    + DETAILS_QUERY
                    + FROM_WHERE_CONDITIONS
                    + "and imt_member.health_id_number in ('" + searchString + "')\n"
                    + ")\n"
                    + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byMobileNumber)) {
            query = "with details as (\n"
                    + DETAILS_QUERY
                    + FROM_WHERE_CONDITIONS;
            if (Boolean.TRUE.equals(byFamilyMobileNumber)) {
                query += "and imt_member.family_id in (select family_id from imt_member where mobile_number in ('" + searchString + "'))\n";
            } else {
                query += "and imt_member.mobile_number in ('" + searchString + "')\n";
            }
            query += "order by rch_pregnancy_registration_det.edd\n"
                    + "limit " + limit + " offset " + offSet + "\n"
                    + ")\n"
                    + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byName)) {
            searchString = Arrays.stream(searchString.split(" ")).map(e -> "%" + e + "%").collect(Collectors.joining());
            query = "with details as (\n"
                    + DETAILS_QUERY
                    + FROM_WHERE_CONDITIONS
                    + "and rch_pregnancy_registration_det.current_location_id in (select child_id from location_hierchy_closer_det where parent_id = " + locationId + ")\n"
                    + "and concat(imt_member.first_name,' ',imt_member.middle_name,' ',imt_member.last_name) ilike '" + searchString + "'\n"
                    + "order by rch_pregnancy_registration_det.edd\n"
                    + "limit " + limit + " offset " + offSet + "\n"
                    + ")\n"
                    + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byLmp) || Boolean.TRUE.equals(byEdd)) {
            query = "with details as (\n"
                    + DETAILS_QUERY
                    + FROM_WHERE_CONDITIONS
                    + "and rch_pregnancy_registration_det.current_location_id in (select child_id from location_hierchy_closer_det where parent_id = " + locationId + ")\n";
            if (Boolean.TRUE.equals(byLmp)) {
                query += "and rch_pregnancy_registration_det.lmp_date between (to_date('" + searchString + "','DD-MM-YYYY') - interval '10 days') and (to_date('" + searchString + "','DD-MM-YYYY') + interval '10 days')\n";
            } else {
                query += "and rch_pregnancy_registration_det.edd between (to_date('" + searchString + "','DD-MM-YYYY') - interval '10 days') and (to_date('" + searchString + "','DD-MM-YYYY') + interval '10 days')\n";
            }
            query += "order by rch_pregnancy_registration_det.edd\n"
                    + "limit " + limit + " offset " + offSet + "\n"
                    + ")\n"
                    + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byOrganizationUnit)) {
            query = "with details as (\n"
                    + DETAILS_QUERY
                    + FROM_WHERE_CONDITIONS
                    + "and rch_pregnancy_registration_det.current_location_id in (select child_id from location_hierchy_closer_det where parent_id = " + locationId + ")\n"
                    + "order by rch_pregnancy_registration_det.edd\n"
                    + "limit " + limit + " offset " + offSet + "\n"
                    + ")\n"
                    + RECORDS_QUERY;
        }
        try {
            Session session = sessionFactory.getCurrentSession();
            NativeQuery<MemberDto> q = session.createNativeQuery(query);
            List<MemberDto> memberEntities = q
                    .addScalar("id", StandardBasicTypes.INTEGER)
                    .addScalar("fid", StandardBasicTypes.INTEGER)
                    .addScalar("additionalInfo", StandardBasicTypes.STRING)
                    .addScalar("lastDeliveryDate", StandardBasicTypes.DATE)
                    .addScalar("familyId", StandardBasicTypes.STRING)
                    .addScalar("locationId", StandardBasicTypes.INTEGER)
                    .addScalar("areaId", StandardBasicTypes.STRING)
                    .addScalar("firstName", StandardBasicTypes.STRING)
                    .addScalar("middleName", StandardBasicTypes.STRING)
                    .addScalar("lastName", StandardBasicTypes.STRING)
                    .addScalar("motherId", StandardBasicTypes.INTEGER)
                    .addScalar("uniqueHealthId", StandardBasicTypes.STRING)
                    .addScalar("healthId", StandardBasicTypes.STRING)
                    .addScalar("healthIdNumber", StandardBasicTypes.STRING)
                    .addScalar("dob", StandardBasicTypes.DATE)
                    .addScalar("immunisationGiven", StandardBasicTypes.STRING)
                    .addScalar("wpdState", StandardBasicTypes.STRING)
                    .addScalar("edd", StandardBasicTypes.DATE)
                    .addScalar("lmpDate", StandardBasicTypes.DATE)
                    .addScalar("mobileNumber", StandardBasicTypes.STRING)
                    .addScalar("accountNumber", StandardBasicTypes.STRING)
                    .addScalar("ifsc", StandardBasicTypes.STRING)
                    .addScalar("isJsyBeneficiary", StandardBasicTypes.BOOLEAN)
                    .addScalar("isKpsyBeneficiary", StandardBasicTypes.BOOLEAN)
                    .addScalar("isIayBeneficiary", StandardBasicTypes.BOOLEAN)
                    .addScalar("isChiranjeeviYojnaBeneficiary", StandardBasicTypes.BOOLEAN)
                    .addScalar("bplFlag", StandardBasicTypes.BOOLEAN)
                    .addScalar("caste", StandardBasicTypes.STRING)
                    .addScalar("locationHierarchy", StandardBasicTypes.STRING)
                    .addScalar("isPregnantFlag", StandardBasicTypes.BOOLEAN)
                    .addScalar("gender", StandardBasicTypes.STRING)
                    .addScalar("currentGravida", StandardBasicTypes.SHORT)
                    .addScalar("familyPlanningMethod", StandardBasicTypes.STRING)
                    .addScalar("lastMethodOfContraception", StandardBasicTypes.STRING)
                    .addScalar("hofMobileNumber", StandardBasicTypes.STRING)
                    .addScalar("curPregRegDetId", StandardBasicTypes.INTEGER)
                    .addScalar("curPregRegDate", StandardBasicTypes.DATE)
                    .addScalar("isEarlyRegistration", StandardBasicTypes.BOOLEAN)
                    .addScalar("isHighRiskCase", StandardBasicTypes.BOOLEAN)
                    .addScalar("bloodGroup", StandardBasicTypes.STRING)
                    .addScalar("haemoglobin", StandardBasicTypes.FLOAT)
                    .addScalar("weight", StandardBasicTypes.FLOAT)
                    .setResultTransformer(Transformers.aliasToBean(MemberDto.class)).list();
            if (CollectionUtils.isEmpty(memberEntities)) {
                return Collections.emptyList();
            }
            return memberEntities;
        } catch (Exception e) {
            throw new ImtechoUserException("Please enter valid value", 101);
        }
    }


}
