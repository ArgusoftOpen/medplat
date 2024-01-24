package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberBreastDetailDao;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberBreastDetail;
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
 * Implementation of methods define in member breast details dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberBreastDetailDaoImpl extends GenericDaoImpl<MemberBreastDetail, Integer> implements MemberBreastDetailDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberBreastDetail retrieveForReferralId(Integer referralId, Date screeningDate) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MemberBreastDetail> criteriaQuery = cb.createQuery(MemberBreastDetail.class);
        Root<MemberBreastDetail> root = criteriaQuery.from(MemberBreastDetail.class);
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
        MemberBreastDetail memberBreastDetail = session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
        if (memberBreastDetail == null && screeningDate != null) {
            memberBreastDetail = retrieveForReferralId(referralId, null);
            if (memberBreastDetail == null) {
                memberBreastDetail = new MemberBreastDetail();
                memberBreastDetail.setReferralId(referralId);
            }
        }
        return memberBreastDetail;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MemberBreastDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberBreastDetail> criteriaQuery = criteriaBuilder.createQuery(MemberBreastDetail.class);
        Root<MemberBreastDetail> root = criteriaQuery.from(MemberBreastDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberBreastDetail.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(criteriaBuilder.function("DATE", Date.class, root.get(MemberBreastDetail.Fields.SCREENING_DATE)), screeningDate);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberBreastDetail.Fields.DONE_BY), type);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual, doneByMoEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberBreastDetail.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberBreastDetail retrieveLastRecordByMemberId(Integer memberId) {
        String query = "select distinct on(member_id) *\n" +
                "from ncd_member_breast_detail\n" +
                "where member_id = :memberId\n" +
                "order by member_id, screening_date desc, id desc;\n";

        NativeQuery<MemberBreastDetail> sqlQuery = sessionFactory.getCurrentSession().createNativeQuery(query);
        sqlQuery.setParameter("memberId", memberId);
        sqlQuery.addEntity(MemberBreastDetail.class);
        return sqlQuery.uniqueResult();
    }

}
