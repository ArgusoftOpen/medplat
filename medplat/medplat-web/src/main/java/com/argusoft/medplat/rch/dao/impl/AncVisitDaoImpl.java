/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.fhs.dto.MemberDto;
import com.argusoft.medplat.mobile.dto.MemberServiceDateDto;
import com.argusoft.medplat.rch.dao.AncVisitDao;
import com.argusoft.medplat.rch.model.AncVisit;
import com.argusoft.medplat.rch.model.VisitCommonFields;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Implementation of methods define in anc dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class AncVisitDaoImpl extends GenericDaoImpl<AncVisit, Integer> implements AncVisitDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberServiceDateDto> retrieveMembersWithServiceDateAsFuture() {
        String query = "select member_id as \"memberId\", max(mobile_start_date) as \"serviceDate\" from rch_anc_master \n" + "where service_date > current_timestamp group by member_id";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberServiceDateDto> q = session.createNativeQuery(query);
        q.addScalar("memberId", StandardBasicTypes.INTEGER).addScalar("serviceDate", StandardBasicTypes.TIMESTAMP);

        return q.setResultTransformer(Transformers.aliasToBean(MemberServiceDateDto.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AncVisit> retrieveByMemberId(Integer memberId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(VisitCommonFields.Fields.MEMBER_ID), memberId));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(AncVisit.Fields.SERVICE_DATE)));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AncVisit> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfrasturctureId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(VisitCommonFields.Fields.MEMBER_ID), memberId));
            predicates.add(criteriaBuilder.equal(root.get(AncVisit.Fields.HEALTH_INFRA_ID), healthInfrasturctureId));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(AncVisit.Fields.SERVICE_DATE)));
            return predicates;
        });
    }

    @Override
    public List<MemberDto> retrieveAncMembers(Boolean byId, Boolean byMemberId, Boolean byFamilyId, Boolean byMobileNumber, Boolean byName, Boolean byLmp, Boolean byEdd, Boolean byOrganizationUnit, Boolean byAbhaNumber, Boolean byAbhaAddress, Integer locationId, String searchString, Boolean byFamilyMobileNumber, Integer limit, Integer offSet) {
        if (searchString != null) {
            searchString = searchString.replaceAll("[']", "''");
        }
        String query = "";
        String states = "'PREGNANT','PENDING'";
        final String DETAILS_QUERY = "\tselect imt_member.id as id,\n" + "\timt_member.unique_health_id as uniqueHealthId,\n" + "\timt_member.health_id as healthId,\n" + "\timt_member.health_id_number as healthIdNumber,\n" + "\timt_member.dob as dob,\n" + "\timt_member.family_id as familyId,\n" + "\timt_member.first_name as firstName,\n" + "\timt_member.middle_name as middleName,\n" + "\timt_member.last_name as lastName,\n" + "\timt_member.gender as gender,\n" + "\timt_member.mobile_number as mobileNumber,\n" + "\timt_member.account_number as accountNumber,\n" + "\timt_member.ifsc as ifsc,\n" + "\timt_member.jsy_beneficiary as isJsyBeneficiary,\n" + "\timt_member.kpsy_beneficiary as isKpsyBeneficiary,\n" + "\timt_member.iay_beneficiary as isIayBeneficiary,\n" + "\timt_member.chiranjeevi_yojna_beneficiary as isChiranjeeviYojnaBeneficiary,\n" + "\timt_member.cur_preg_reg_det_id as curPregRegDetId,\n" + "\timt_member.cur_preg_reg_date as curPregRegDate,\n" + "\timt_member.early_registration as isEarlyRegistration,\n" + "\timt_member.is_high_risk_case as isHighRiskCase,\n" + "\timt_member.blood_group as bloodGroup,\n" + "\timt_member.weight as weight,\n" + "\timt_member.haemoglobin as haemoglobin,\n" + "\timt_member.is_pregnant as isPregnantFlag,\n" + "\timt_member.current_gravida as currentGravida,\n" + "\timt_member.family_planning_method as familyPlanningMethod,\n" + "\timt_member.last_method_of_contraception as lastMethodOfContraception,\n" + "\timt_member.immunisation_given as immunisationGiven,\n" + "\timt_member.mother_id as motherId,\n" + "\tcast(imt_member.last_delivery_date as date) as lastDeliveryDate,\n" + "\timt_member.additional_info as additionalInfo,\n" + "\trch_pregnancy_registration_det.state as wpdState,\n" + "\trch_pregnancy_registration_det.edd as edd,\n" + "\trch_pregnancy_registration_det.lmp_date as lmpDate,\n" + "\tget_location_hierarchy(rch_pregnancy_registration_det.current_location_id) as locationHierarchy,\n" + "\timt_family.area_id as areaId,\n" + "\timt_family.location_id as locationId,\n" + "\timt_family.id as fid,\n" + "\timt_family.bpl_flag as bplFlag,\n" + "\tlistvalue_field_value_detail.value as caste,\n" + "\thof.mobile_number as hofMobileNumber\n";
        final String FROM_WHERE_CONDITIONS = "\tfrom rch_pregnancy_registration_det\n" + "\tinner join imt_member on rch_pregnancy_registration_det.id = imt_member.cur_preg_reg_det_id\n" + "\tand rch_pregnancy_registration_det.member_id = imt_member.id\n" + "\tinner join imt_family on imt_member.family_id = imt_family.family_id\n" + "\tleft join imt_member hof on imt_family.hof_id = hof.id\n" + "\tleft join listvalue_field_value_detail on imt_family.caste = cast(listvalue_field_value_detail.id as text)\n" + "\twhere rch_pregnancy_registration_det.state in (" + states + ")\n" + "\tand (\n" + "\t(rch_pregnancy_registration_det.state = 'DELIVERY_DONE' and rch_pregnancy_registration_det.delivery_date > now() - interval '1 month')\n" + "\tor rch_pregnancy_registration_det.state != 'DELIVERY_DONE'\n" + "\t)\n" + "\tand rch_pregnancy_registration_det.edd > now() - interval '2 years'\n" + "\tand imt_member.basic_state in ('NEW','VERIFIED','REVERIFICATION','TEMPORARY')\n";
        final String RECORDS_QUERY = "select details.*\n" + "from details\n";
        if (Boolean.TRUE.equals(byMemberId)) {
            searchString = Arrays.asList(searchString.split(",")).stream().map(e -> "'" + e + "'").collect(Collectors.joining(","));
            query = "with details as (\n" + DETAILS_QUERY + FROM_WHERE_CONDITIONS + "and imt_member.unique_health_id in (" + searchString + ")\n" + ")\n" + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byFamilyId)) {
            query = "with details as (\n" + DETAILS_QUERY + FROM_WHERE_CONDITIONS + "and imt_member.family_id in ('" + searchString + "')\n" + ")\n" + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byAbhaNumber)) {
            query = "with details as (\n" + DETAILS_QUERY + FROM_WHERE_CONDITIONS + "and imt_member.health_id_number in ('" + searchString + "')\n" + ")\n" + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byAbhaAddress)) {
            query = "with details as (\n" + DETAILS_QUERY + FROM_WHERE_CONDITIONS + "and imt_member.health_id in ('" + searchString + "')\n" + ")\n" + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byMobileNumber)) {
            query = "with details as (\n" + DETAILS_QUERY + FROM_WHERE_CONDITIONS;
            if (Boolean.TRUE.equals(byFamilyMobileNumber)) {
                query += "and imt_member.family_id in (select family_id from imt_member where mobile_number in ('" + searchString + "'))\n";
            } else {
                query += "and imt_member.mobile_number in ('" + searchString + "')\n";
            }
            query += "order by rch_pregnancy_registration_det.edd\n" + "limit " + limit + " offset " + offSet + "\n" + ")\n" + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byName)) {
            searchString = Arrays.stream(searchString.split(" ")).map(e -> "%" + e + "%").collect(Collectors.joining());
            query = "with details as (\n" + DETAILS_QUERY + FROM_WHERE_CONDITIONS + "and rch_pregnancy_registration_det.current_location_id in (select child_id from location_hierchy_closer_det where parent_id = " + locationId + ")\n" + "and concat(imt_member.first_name,' ',imt_member.middle_name,' ',imt_member.last_name) ilike '" + searchString + "'\n" + "order by rch_pregnancy_registration_det.edd\n" + "limit " + limit + " offset " + offSet + "\n" + ")\n" + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byLmp) || Boolean.TRUE.equals(byEdd)) {
            query = "with details as (\n" + DETAILS_QUERY + FROM_WHERE_CONDITIONS + "and rch_pregnancy_registration_det.current_location_id in (select child_id from location_hierchy_closer_det where parent_id = " + locationId + ")\n";
            if (Boolean.TRUE.equals(byLmp)) {
                query += "and rch_pregnancy_registration_det.lmp_date between (to_date('" + searchString + "','DD-MM-YYYY') - interval '10 days') and (to_date('" + searchString + "','DD-MM-YYYY') + interval '10 days')\n";
            } else {
                query += "and rch_pregnancy_registration_det.edd between (to_date('" + searchString + "','DD-MM-YYYY') - interval '10 days') and (to_date('" + searchString + "','DD-MM-YYYY') + interval '10 days')\n";
            }
            query += "order by rch_pregnancy_registration_det.edd\n" + "limit " + limit + " offset " + offSet + "\n" + ")\n" + RECORDS_QUERY;
        } else if (Boolean.TRUE.equals(byOrganizationUnit)) {
            query = "with details as (\n" + DETAILS_QUERY + FROM_WHERE_CONDITIONS + "and rch_pregnancy_registration_det.current_location_id in (select child_id from location_hierchy_closer_det where parent_id = " + locationId + ")\n" + "order by rch_pregnancy_registration_det.edd\n" + "limit " + limit + " offset " + offSet + "\n" + ")\n" + RECORDS_QUERY;
        }
        try {
            Session session = sessionFactory.getCurrentSession();
            NativeQuery<MemberDto> q = session.createNativeQuery(query);
            List<MemberDto> memberEntities = q.addScalar("id", StandardBasicTypes.INTEGER).addScalar("fid", StandardBasicTypes.INTEGER).addScalar("additionalInfo", StandardBasicTypes.STRING).addScalar("lastDeliveryDate", StandardBasicTypes.DATE).addScalar("familyId", StandardBasicTypes.STRING).addScalar("locationId", StandardBasicTypes.INTEGER).addScalar("areaId", StandardBasicTypes.STRING).addScalar("firstName", StandardBasicTypes.STRING).addScalar("middleName", StandardBasicTypes.STRING).addScalar("lastName", StandardBasicTypes.STRING).addScalar("motherId", StandardBasicTypes.INTEGER).addScalar("uniqueHealthId", StandardBasicTypes.STRING).addScalar("healthId", StandardBasicTypes.STRING).addScalar("healthIdNumber", StandardBasicTypes.STRING).addScalar("dob", StandardBasicTypes.DATE).addScalar("immunisationGiven", StandardBasicTypes.STRING).addScalar("wpdState", StandardBasicTypes.STRING).addScalar("edd", StandardBasicTypes.DATE).addScalar("lmpDate", StandardBasicTypes.DATE).addScalar("mobileNumber", StandardBasicTypes.STRING).addScalar("accountNumber", StandardBasicTypes.STRING).addScalar("ifsc", StandardBasicTypes.STRING).addScalar("isJsyBeneficiary", StandardBasicTypes.BOOLEAN).addScalar("isKpsyBeneficiary", StandardBasicTypes.BOOLEAN).addScalar("isIayBeneficiary", StandardBasicTypes.BOOLEAN).addScalar("isChiranjeeviYojnaBeneficiary", StandardBasicTypes.BOOLEAN).addScalar("bplFlag", StandardBasicTypes.BOOLEAN).addScalar("caste", StandardBasicTypes.STRING).addScalar("locationHierarchy", StandardBasicTypes.STRING).addScalar("isPregnantFlag", StandardBasicTypes.BOOLEAN).addScalar("gender", StandardBasicTypes.STRING).addScalar("currentGravida", StandardBasicTypes.SHORT).addScalar("familyPlanningMethod", StandardBasicTypes.STRING).addScalar("lastMethodOfContraception", StandardBasicTypes.STRING).addScalar("hofMobileNumber", StandardBasicTypes.STRING).addScalar("curPregRegDetId", StandardBasicTypes.INTEGER).addScalar("curPregRegDate", StandardBasicTypes.DATE).addScalar("isEarlyRegistration", StandardBasicTypes.BOOLEAN).addScalar("isHighRiskCase", StandardBasicTypes.BOOLEAN).addScalar("bloodGroup", StandardBasicTypes.STRING).addScalar("haemoglobin", StandardBasicTypes.FLOAT).addScalar("weight", StandardBasicTypes.FLOAT).setResultTransformer(Transformers.aliasToBean(MemberDto.class)).list();
            if (CollectionUtils.isEmpty(memberEntities)) {
                return Collections.emptyList();
            }
            return memberEntities;
        } catch (Exception e) {
            throw new ImtechoUserException("Please enter valid value", 101);
        }
    }

}
