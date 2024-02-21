package com.argusoft.medplat.ncddnhdd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncddnhdd.dao.MemberDiabetesDetailDao;
import com.argusoft.medplat.ncddnhdd.enums.DoneBy;
import com.argusoft.medplat.ncddnhdd.model.MemberDiabetesDetail;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods defined in member diabetes details dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberDiabetesDetailDaoImpl extends GenericDaoImpl<MemberDiabetesDetail, Integer> implements MemberDiabetesDetailDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDiabetesDetail retrieveForReferralId(Integer referralId, Date screeningDate) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MemberDiabetesDetail> criteriaQuery = cb.createQuery(MemberDiabetesDetail.class);
        Root<MemberDiabetesDetail> root = criteriaQuery.from(MemberDiabetesDetail.class);
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
        MemberDiabetesDetail memberDiabetesDetail = session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
        if (memberDiabetesDetail == null && screeningDate != null) {
            memberDiabetesDetail = retrieveForReferralId(referralId, null);
            if (memberDiabetesDetail == null) {
                memberDiabetesDetail = new MemberDiabetesDetail();
                memberDiabetesDetail.setReferralId(referralId);
            }
        }
        return memberDiabetesDetail;
    }

    @Override
    public List<MemberDiabetesDetail> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(MemberDiabetesDetail.Fields.MEMBER_ID), memberId));
            Predicate healthInfraPredicate = criteriaBuilder.equal(root.get(MemberDiabetesDetail.Fields.HEALTH_INFRA_ID), healthInfraId);
            Predicate hmisIdPredicate = criteriaBuilder.equal(root.get(MemberDiabetesDetail.Fields.HMIS_HEALTH_INFRA_ID), healthInfraId);
            predicates.add(criteriaBuilder.or(healthInfraPredicate, hmisIdPredicate));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberDiabetesDetail.Fields.REFERRED_ON)));
            return predicates;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDiabetesDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberDiabetesDetail> criteriaQuery = criteriaBuilder.createQuery(MemberDiabetesDetail.class);
        Root<MemberDiabetesDetail> root = criteriaQuery.from(MemberDiabetesDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberDiabetesDetail.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(criteriaBuilder.function("DATE", Date.class, root.get(MemberDiabetesDetail.Fields.SCREENING_DATE)), screeningDate);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberDiabetesDetail.Fields.DONE_BY), type);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual, doneByMoEqual));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberDiabetesDetail retrieveLastRecordByMemberId(Integer memberId) {
        String query = "select distinct on(member_id) *\n" +
                "from ncd_member_diabetes_detail\n" +
                "where member_id = :memberId\n" +
                "order by member_id, screening_date desc, id desc;\n";

        NativeQuery<MemberDiabetesDetail> sqlQuery = sessionFactory.getCurrentSession().createNativeQuery(query);
        sqlQuery.setParameter("memberId", memberId);
        sqlQuery.addEntity(MemberDiabetesDetail.class);
        return sqlQuery.uniqueResult();
    }

    @Override
    public List<MemberDiabetesDetail> retrieveLastNRecordsByMemberIdAndMeasurement(Integer memberId, String measurementType, String visitType, Integer numberOfRecords) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberDiabetesDetail> criteriaQuery = criteriaBuilder.createQuery(MemberDiabetesDetail.class);
        Root<MemberDiabetesDetail> root = criteriaQuery.from(MemberDiabetesDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberDiabetesDetail.Fields.MEMBER_ID), memberId);
        if (visitType != null && measurementType != null) {
            Predicate measurement = criteriaBuilder.equal(root.get(MemberDiabetesDetail.Fields.MEASUREMENT_TYPE), measurementType);
            Predicate visitTypeEq = criteriaBuilder.equal(root.get(MemberDiabetesDetail.Fields.VISIT_TYPE), visitType);
            criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, measurement, visitTypeEq));
        } else if (measurementType != null) {
            Predicate measurement = criteriaBuilder.equal(root.get(MemberDiabetesDetail.Fields.MEASUREMENT_TYPE), measurementType);
            criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, measurement));
        } else {
            criteriaQuery.select(root).where(memberIdEqual);
        }
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberDiabetesDetail.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(numberOfRecords).getResultList();
    }


    @Override
    public void updateDiabetesDetailsInNcdMemberDetail(Integer memberId) {
        String query = "UPDATE imt_member_ncd_detail AS nd\n" +
                "SET modified_on = now(), diabetes_details  = (\n" +
                "SELECT CAST(json_agg(\n" +
                "json_build_object(\n" +
                "'screeningDate', extract(epoch from cast(dd.screening_date as date))*1000,\n" +
                "'bloodSugar', dd.blood_sugar,\n" +
                "'doneBy',dd.done_by,\n" +
                "'status',dd.status\n" +
                ")\n" +
                ") as text)\n" +
                "FROM (\n" +
                "select *\n" +
                "FROM ncd_member_diabetes_detail AS dd\n" +
                "WHERE dd.member_id = nd.member_id and dd.measurement_type = 'RBS'\n" +
                "ORDER BY dd.created_on DESC\n" +
                "LIMIT 3\n" +
                ") as dd\n" +
                ")\n" +
                "WHERE nd.member_id = :memberId";

        NativeQuery<Integer> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("memberId", memberId);
        nativeQuery.executeUpdate();
    }
}
