package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.NcdMOReviewFollowupDao;
import com.argusoft.medplat.ncd.dto.MOReviewDto;
import com.argusoft.medplat.ncd.dto.MOReviewFollowupDto;
import com.argusoft.medplat.ncd.model.MOReviewDetail;
import com.argusoft.medplat.ncd.model.MOReviewFollowupDetail;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

@Repository
public class NcdMOReviewFollowupDaoImpl extends GenericDaoImpl<MOReviewFollowupDetail, Integer> implements NcdMOReviewFollowupDao {
    @Override
    public MOReviewFollowupDetail retrieveMoReviewFollowupByMemberAndDate(Integer memberId, Date date) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MOReviewFollowupDetail> criteriaQuery = criteriaBuilder.createQuery(MOReviewFollowupDetail.class);
        Root<MOReviewFollowupDetail> root = criteriaQuery.from(MOReviewFollowupDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MOReviewFollowupDetail.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(root.get(MOReviewFollowupDetail.Fields.SCREENING_DATE), date);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MOReviewFollowupDetail.Fields.ID)));
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    @Override
    public MOReviewFollowupDto retrieveLastCommentByMOReviewFollowup(Integer memberId) {
        String sql = "select ncd_mo_review_followup_detail.member_id \"memberId\",ncd_mo_review_followup_detail.comment \"comment\",concat(um_user.first_name,' ',um_user.last_name) \"commentBy\" from ncd_mo_review_followup_detail\n" +
                "left join um_user on ncd_mo_review_followup_detail.created_by = um_user.id\n" +
                "where member_id= :memberId and comment is not null order by ncd_mo_review_followup_detail.id desc";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MOReviewFollowupDto> sqlQuery = session.createNativeQuery(sql);
        return sqlQuery
                .addScalar("memberId", StandardBasicTypes.INTEGER)
                .addScalar("comment", StandardBasicTypes.STRING)
                .addScalar("commentBy", StandardBasicTypes.STRING)
                .setParameter("memberId", memberId)
                .setResultTransformer(Transformers.aliasToBean(MOReviewFollowupDto.class))
                .setMaxResults(1).uniqueResult();
    }
}
