/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.constants.MobileConstantUtil;
import com.argusoft.medplat.rch.constants.RchConstants;
import com.argusoft.medplat.rch.dao.ImmunisationMasterDao;
import com.argusoft.medplat.rch.model.ImmunisationMaster;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Implementation of methods define in immunisation master dao.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
@Repository
public class ImmunisationMasterDaoImpl extends GenericDaoImpl<ImmunisationMaster, Integer> implements ImmunisationMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkImmunisationEntry(Integer memberId, String immunisationGiven, Integer pregnancyRegistrationId) {

        if (immunisationGiven.trim().equals(MobileConstantUtil.IMMUNISATION_VITAMIN_A)) { //MULTIPLE VIT A ALLOWED TO BE GIVEN TO A CHILD
            return true;
        }

        String query = "select count(*) from rch_immunisation_master where member_id = :memberId and immunisation_given = :immunisationGiven ;";
        if (pregnancyRegistrationId != null && pregnancyRegistrationId != -1L) {
            query = "select count(*) from rch_immunisation_master where pregnancy_reg_det_id = :pregnancyRegistrationId and \n"
                    + " member_id = :memberId and immunisation_given = :immunisationGiven ;";
        }
        NativeQuery<BigInteger> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("memberId", memberId);
        sQLQuery.setParameter("immunisationGiven", immunisationGiven);
        if (pregnancyRegistrationId != null && pregnancyRegistrationId != -1L) {
            sQLQuery.setParameter("pregnancyRegistrationId", pregnancyRegistrationId);
        }
        BigInteger count = sQLQuery.uniqueResult();
        return count.longValue() == 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getTotalVitaminADoseGiven(Integer memberId) {
        List<ImmunisationMaster> immunisationMasterList = super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(ImmunisationMaster.Fields.MEMBER_ID), memberId));
            predicates.add(criteriaBuilder.equal(root.get(ImmunisationMaster.Fields.IMMUNISATION_GIVEN), RchConstants.VaccinationType.VITAMIN_A));
            return predicates;
        });
        if (immunisationMasterList == null || immunisationMasterList.isEmpty()) {
            return 0;
        } else {
            return immunisationMasterList.size();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ImmunisationMaster> getAllVaccinesByVaccineType(Integer memberId, String immunisationGiven) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(ImmunisationMaster.Fields.MEMBER_ID), memberId));
            predicates.add(criteriaBuilder.equal(root.get(ImmunisationMaster.Fields.IMMUNISATION_GIVEN), immunisationGiven));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(ImmunisationMaster.Fields.GIVEN_ON)));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ImmunisationMaster> getAllVaccinesByMemberId(Integer memberId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(ImmunisationMaster.Fields.MEMBER_ID), memberId));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(ImmunisationMaster.Fields.GIVEN_ON)));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ImmunisationMaster> getAllVaccinesByVisitId(List<Integer> visitId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(root.get(ImmunisationMaster.Fields.VISIT_ID).in(visitId));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(ImmunisationMaster.Fields.GIVEN_ON)));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ImmunisationMaster getVaccineByMemberIdAndVaccineName(Integer memberId, String vaccine) {
        Session session = getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ImmunisationMaster> cq = cb.createQuery(ImmunisationMaster.class);
        Root<ImmunisationMaster> root = cq.from(ImmunisationMaster.class);

        cq.orderBy(cb.desc(root.get(ImmunisationMaster.Fields.GIVEN_ON)));
        cq.where(cb.and(
                cb.equal(root.get(ImmunisationMaster.Fields.MEMBER_ID), memberId),
                cb.equal(root.get(ImmunisationMaster.Fields.IMMUNISATION_GIVEN), vaccine)
        ));

        return session.createQuery(cq).uniqueResult();
    }
}
