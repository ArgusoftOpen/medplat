package com.argusoft.medplat.ncddnhdd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncddnhdd.dao.MemberOralDetailDao;
import com.argusoft.medplat.ncddnhdd.enums.DoneBy;
import com.argusoft.medplat.ncddnhdd.model.MemberOralDetail;
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
 * Implementation of methods defined in member oral details dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberOralDetailDaoImpl extends GenericDaoImpl<MemberOralDetail, Integer> implements MemberOralDetailDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberOralDetail retrieveForReferralId(Integer referralId, Date screeningDate) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MemberOralDetail> criteriaQuery = cb.createQuery(MemberOralDetail.class);
        Root<MemberOralDetail> root = criteriaQuery.from(MemberOralDetail.class);
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
        MemberOralDetail memberOralDetail = session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
        if (memberOralDetail == null && screeningDate != null) {
            memberOralDetail = retrieveForReferralId(referralId, null);
            if (memberOralDetail == null) {
                memberOralDetail = new MemberOralDetail();
                memberOralDetail.setReferralId(referralId);
            }
        }
        return memberOralDetail;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MemberOralDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberOralDetail> criteriaQuery = criteriaBuilder.createQuery(MemberOralDetail.class);
        Root<MemberOralDetail> root = criteriaQuery.from(MemberOralDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberOralDetail.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(criteriaBuilder.function("DATE", Date.class, root.get(MemberOralDetail.Fields.SCREENING_DATE)), screeningDate);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberOralDetail.Fields.DONE_BY), type);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual, doneByMoEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberOralDetail.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberOralDetail retrieveLastRecordByMemberId(Integer memberId) {
        String query = "select distinct on(member_id) *\n" +
                "from ncd_member_oral_detail\n" +
                "where member_id = :memberId\n" +
                "order by member_id, screening_date desc, id desc;\n";

        NativeQuery<MemberOralDetail> sqlQuery = sessionFactory.getCurrentSession().createNativeQuery(query);
        sqlQuery.setParameter("memberId", memberId);
        sqlQuery.addEntity(MemberOralDetail.class);
        return sqlQuery.uniqueResult();
    }
    @Override
    public MemberOralDetail retrieveFirstRecordByMemberId(Integer memberId) {
        String query = "select distinct on(member_id) *\n" +
                "from ncd_member_oral_detail\n" +
                "where member_id = :memberId\n" +
                "order by member_id, screening_date asc, id asc;\n";

        NativeQuery<MemberOralDetail> sqlQuery = sessionFactory.getCurrentSession().createNativeQuery(query);
        sqlQuery.setParameter("memberId", memberId);
        sqlQuery.addEntity(MemberOralDetail.class);
        return sqlQuery.uniqueResult();
    }
}
