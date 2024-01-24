package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberCervicalDetailDao;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberCervicalDetail;
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
 * Implementation of methods define in member cervical details dao.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 10:19 AM
 */
@Repository
public class MemberCervicalDetailDaoImpl extends GenericDaoImpl<MemberCervicalDetail, Integer> implements MemberCervicalDetailDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberCervicalDetail retrieveForReferralId(Integer referralId, Date screeningDate) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MemberCervicalDetail> criteriaQuery = cb.createQuery(MemberCervicalDetail.class);
        Root<MemberCervicalDetail> root = criteriaQuery.from(MemberCervicalDetail.class);
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
        MemberCervicalDetail memberCervicalDetail = session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
        if (memberCervicalDetail == null && screeningDate != null) {
            memberCervicalDetail = retrieveForReferralId(referralId, null);
            if (memberCervicalDetail == null) {
                memberCervicalDetail = new MemberCervicalDetail();
                memberCervicalDetail.setReferralId(referralId);
            }
        }
        return memberCervicalDetail;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberCervicalDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberCervicalDetail> criteriaQuery = criteriaBuilder.createQuery(MemberCervicalDetail.class);
        Root<MemberCervicalDetail> root = criteriaQuery.from(MemberCervicalDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberCervicalDetail.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(criteriaBuilder.function("DATE", Date.class, root.get(MemberCervicalDetail.Fields.SCREENING_DATE)), screeningDate);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberCervicalDetail.Fields.DONE_BY), type);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual, doneByMoEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberCervicalDetail.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MemberCervicalDetail retrieveLastRecordByMemberId(Integer memberId) {
        String query = "select distinct on(member_id) *\n" +
                "from ncd_member_cervical_detail\n" +
                "where member_id = :memberId\n" +
                "order by member_id, screening_date desc, id desc;\n";

        NativeQuery<MemberCervicalDetail> sqlQuery = sessionFactory.getCurrentSession().createNativeQuery(query);
        sqlQuery.setParameter("memberId", memberId);
        sqlQuery.addEntity(MemberCervicalDetail.class);
        return sqlQuery.uniqueResult();
    }

}
