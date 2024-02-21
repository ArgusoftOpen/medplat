package com.argusoft.medplat.ncddnhdd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncddnhdd.dao.MemberHypertensionDetailDao;
import com.argusoft.medplat.ncddnhdd.enums.DoneBy;
import com.argusoft.medplat.ncddnhdd.model.MemberGeneralDetail;
import com.argusoft.medplat.ncddnhdd.model.MemberHypertensionDetail;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods defined in member hypertension details dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberHypertensionDetailDaoImpl extends GenericDaoImpl<MemberHypertensionDetail, Integer> implements MemberHypertensionDetailDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberHypertensionDetail retrieveForReferralId(Integer referralId, Date screeningDate) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MemberHypertensionDetail> criteriaQuery = cb.createQuery(MemberHypertensionDetail.class);
        Root<MemberHypertensionDetail> root = criteriaQuery.from(MemberHypertensionDetail.class);
        criteriaQuery.orderBy(cb.desc(root.get("screeningDate")));
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("id"),referralId));

        if (screeningDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("screeningDate"),screeningDate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(screeningDate);
            cal.add(Calendar.DATE, 1);
            predicates.add(cb.lessThanOrEqualTo(root.get("screeningDate").as(Date.class),cal.getTime()));
        }
        criteriaQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        MemberHypertensionDetail memberHypertensionDetail = session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
        if (memberHypertensionDetail == null && screeningDate != null) {
            memberHypertensionDetail = retrieveForReferralId(referralId, null);
            if (memberHypertensionDetail == null) {
                memberHypertensionDetail = new MemberHypertensionDetail();
                memberHypertensionDetail.setReferralId(referralId);
            }
        }
        return memberHypertensionDetail;
    }

    @Override
    public List<MemberHypertensionDetail> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(MemberHypertensionDetail.Fields.MEMBER_ID), memberId));
            Predicate healthInfraPredicate = criteriaBuilder.equal(root.get(MemberHypertensionDetail.Fields.HEALTH_INFRA_ID), healthInfraId);
            Predicate hmisIdPredicate = criteriaBuilder.equal(root.get(MemberHypertensionDetail.Fields.HMIS_HEALTH_INFRA_ID), healthInfraId);
            predicates.add(criteriaBuilder.or(healthInfraPredicate, hmisIdPredicate));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberHypertensionDetail.Fields.REFERRED_ON)));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberHypertensionDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberHypertensionDetail> criteriaQuery = criteriaBuilder.createQuery(MemberHypertensionDetail.class);
        Root<MemberHypertensionDetail> root = criteriaQuery.from(MemberHypertensionDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberHypertensionDetail.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(criteriaBuilder.function("DATE", Date.class, root.get(MemberHypertensionDetail.Fields.SCREENING_DATE)), screeningDate);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberHypertensionDetail.Fields.DONE_BY), type);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual, doneByMoEqual));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MemberHypertensionDetail> retrieveLastRecordByMemberId(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberHypertensionDetail> criteriaQuery = criteriaBuilder.createQuery(MemberHypertensionDetail.class);
        Root<MemberHypertensionDetail> root = criteriaQuery.from(MemberHypertensionDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberHypertensionDetail.Fields.MEMBER_ID), memberId);
//        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberHypertensionDetail.Fields.DONE_BY), MemberHypertensionDetail.DoneBy.MO);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberHypertensionDetail.Fields.SCREENING_DATE)));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberHypertensionDetail.Fields.ID)));
        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public MemberHypertensionDetail retrieveLastSingleRecordByMemberId(Integer memberId) {
        String query = "select distinct on(member_id) *\n" +
                "from ncd_member_hypertension_detail\n" +
                "where member_id = :memberId\n" +
                "order by member_id, screening_date desc, id desc;\n";

        NativeQuery<MemberHypertensionDetail> sqlQuery = sessionFactory.getCurrentSession().createNativeQuery(query);
        sqlQuery.setParameter("memberId", memberId);
        sqlQuery.addEntity(MemberHypertensionDetail.class);
        return sqlQuery.uniqueResult();
    }

    @Override
    public List<MemberHypertensionDetail> retrieveLastNRecordsByMemberId(Integer memberId, String visitType, Integer numberOfRecords) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberHypertensionDetail> criteriaQuery = criteriaBuilder.createQuery(MemberHypertensionDetail.class);
        Root<MemberHypertensionDetail> root = criteriaQuery.from(MemberHypertensionDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberHypertensionDetail.Fields.MEMBER_ID), memberId);
        if (visitType != null) {
            Predicate visitTypeEq = criteriaBuilder.equal(root.get(MemberHypertensionDetail.Fields.VISIT_TYPE), visitType);
            criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, visitTypeEq));
        } else {
            criteriaQuery.select(root).where(memberIdEqual);
        }
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberHypertensionDetail.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(numberOfRecords).getResultList();
    }

    @Override
    public void updateHypertensionDetailsInNcdMemberDetail(Integer memberId) {
        String query = "UPDATE imt_member_ncd_detail AS nd\n" +
                "SET modified_on = now(), hypertension_details  = (\n" +
                "SELECT CAST(json_agg(\n" +
                "json_build_object(\n" +
                "'screeningDate', extract(epoch from cast(hd.screening_date as date))*1000,\n" +
                "'systolicBp', hd.systolic_bp,\n" +
                "'diastolicBp', hd.diastolic_bp,\n" +
                "'pulseRate',hd.pulse_rate,\n" +
                "'doneBy',hd.done_by,\n" +
                "'status',hd.status\n" +
                ")\n" +
                ") as text)\n" +
                "FROM (\n" +
                "select *\n" +
                "FROM ncd_member_hypertension_detail  AS hd\n" +
                "WHERE hd.member_id = nd.member_id\n" +
                "ORDER BY hd.created_on DESC\n" +
                "LIMIT 3\n" +
                ") as hd\n" +
                ")\n" +
                "WHERE nd.member_id = :memberId";

        NativeQuery<Integer> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("memberId", memberId);
        nativeQuery.executeUpdate();
    }

}
