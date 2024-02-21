package com.argusoft.medplat.ncddnhdd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncddnhdd.dao.MemberMentalHealthDetailDao;
import com.argusoft.medplat.ncddnhdd.enums.DoneBy;

import com.argusoft.medplat.ncddnhdd.model.MemberMentalHealthDetails;
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
 * <p>
 * Add description here
 * </p>
 *
 * @author namrata
 * @since 28/04/2022 10:11 AM
 */
@Repository
public class MemberMentalHealthDetailDaoImpl extends GenericDaoImpl<MemberMentalHealthDetails, Integer> implements MemberMentalHealthDetailDao {

    @Override
    public MemberMentalHealthDetails retrieveForReferralId(Integer referralId, Date screeningDate) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MemberMentalHealthDetails> criteriaQuery = cb.createQuery(MemberMentalHealthDetails.class);
        Root<MemberMentalHealthDetails> root = criteriaQuery.from(MemberMentalHealthDetails.class);
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
        MemberMentalHealthDetails memberMentalHealthDetails = session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
        if (memberMentalHealthDetails == null && screeningDate != null) {
            memberMentalHealthDetails = retrieveForReferralId(referralId, null);
            if (memberMentalHealthDetails == null) {
                memberMentalHealthDetails = new MemberMentalHealthDetails();
                memberMentalHealthDetails.setReferralId(referralId);
            }
        }
        return memberMentalHealthDetails;
    }

    @Override
    public List<MemberMentalHealthDetails> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(MemberMentalHealthDetails.Fields.MEMBER_ID), memberId));
            predicates.add(criteriaBuilder.equal(root.get(MemberMentalHealthDetails.Fields.HEALTH_INFRA_ID), healthInfraId));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberMentalHealthDetails.Fields.SCREENING_DATE)));
            return predicates;
        });
    }

    @Override
    public MemberMentalHealthDetails retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberMentalHealthDetails> criteriaQuery = criteriaBuilder.createQuery(MemberMentalHealthDetails.class);
        Root<MemberMentalHealthDetails> root = criteriaQuery.from(MemberMentalHealthDetails.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberMentalHealthDetails.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(root.get(MemberMentalHealthDetails.Fields.SCREENING_DATE), screeningDate);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberMentalHealthDetails.Fields.DONE_BY), type);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberMentalHealthDetails.Fields.ID)));
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual, doneByMoEqual));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    @Override
    public MemberMentalHealthDetails retrieveLastRecordByMemberId(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberMentalHealthDetails> criteriaQuery = criteriaBuilder.createQuery(MemberMentalHealthDetails.class);
        Root<MemberMentalHealthDetails> root = criteriaQuery.from(MemberMentalHealthDetails.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberMentalHealthDetails.Fields.MEMBER_ID), memberId);
        //Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberMentalHealthDetails.Fields.DONE_BY), MemberMentalHealthDetails.DoneBy.MO);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberMentalHealthDetails.Fields.SCREENING_DATE)));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberMentalHealthDetails.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }


    @Override
    public List<MemberMentalHealthDetails> retrieveLastNRecordsByMemberId(Integer memberId, String visitType, Integer numberOfRecords) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberMentalHealthDetails> criteriaQuery = criteriaBuilder.createQuery(MemberMentalHealthDetails.class);
        Root<MemberMentalHealthDetails> root = criteriaQuery.from(MemberMentalHealthDetails.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberMentalHealthDetails.Fields.MEMBER_ID), memberId);
        if (visitType != null) {
            Predicate visitTypeEq = criteriaBuilder.equal(root.get(MemberMentalHealthDetails.Fields.VISIT_TYPE), visitType);
            criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, visitTypeEq));
        } else {
            criteriaQuery.select(root).where(memberIdEqual);
        }
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberMentalHealthDetails.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(numberOfRecords).getResultList();
    }

    @Override
    public void updateMentalHealthDetailsInNcdMemberDetail(Integer memberId) {
        String query = "UPDATE imt_member_ncd_detail AS nd\n" +
                "SET modified_on = now(), mental_health_details  = (\n" +
                "SELECT CAST ( json_agg(\n" +
                "json_build_object(\n" +
                "'screeningDate', extract(epoch from cast(md.screening_date as date))*1000,\n" +
                "'talk', md.talk,\n" +
                "'ownDailyWork',md.own_daily_work,\n" +
                "'socialWork',md.social_work,\n" +
                "'understanding',md.understanding,\n" +
                "'doneBy',md.done_by,\n" +
                "'status',md.today_result\n" +
                ")\n" +
                ") as text)\n" +
                "FROM (\n" +
                "select *\n" +
                "from ncd_member_mental_health_detail AS dd\n" +
                "WHERE dd.member_id = nd.member_id and dd.status is not null\n" +
                "ORDER BY dd.created_on DESC\n" +
                "LIMIT 3\n" +
                ") AS md\n" +
                ")\n" +
                "WHERE nd.member_id = :memberId";

        NativeQuery<Integer> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("memberId", memberId);
        nativeQuery.executeUpdate();
    }
}
