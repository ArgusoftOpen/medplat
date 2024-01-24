package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.DrugInventoryDao;
import com.argusoft.medplat.ncd.dao.MbbsMOReviewDao;
import com.argusoft.medplat.ncd.dto.MbbsMOReviewDto;
import com.argusoft.medplat.ncd.dto.MemberGeneralDto;
import com.argusoft.medplat.ncd.model.DrugInventoryDetail;
import com.argusoft.medplat.ncd.model.MbbsMOReviewDetail;
import com.argusoft.medplat.ncd.model.MemberHypertensionDetail;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class MbbsMOReviewDaoImpl extends GenericDaoImpl<MbbsMOReviewDetail, Integer> implements MbbsMOReviewDao {

    @Override
    public MbbsMOReviewDetail retrieveLastRecordByMemberId(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MbbsMOReviewDetail> criteriaQuery = criteriaBuilder.createQuery(MbbsMOReviewDetail.class);
        Root<MbbsMOReviewDetail> root = criteriaQuery.from(MbbsMOReviewDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MbbsMOReviewDetail.Fields.MEMBER_ID), memberId);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MbbsMOReviewDetail.Fields.DONE_BY),MbbsMOReviewDetail.DoneBy.MO);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, doneByMoEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MbbsMOReviewDetail.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    @Override
    public MbbsMOReviewDto retrieveLastCommentByMBBS(Integer memberId) {
        String sql = "select ncd_mbbsmo_review_detail.member_id \"memberId\",ncd_mbbsmo_review_detail.comment \"comment\",concat(um_user.first_name,' ',um_user.last_name) \"commentBy\" from ncd_mbbsmo_review_detail\n" +
                "left join um_user on ncd_mbbsmo_review_detail.created_by = um_user.id\n" +
                "where member_id=:memberId and comment is not null order by ncd_mbbsmo_review_detail.id desc";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MbbsMOReviewDto> sqlQuery = session.createNativeQuery(sql);
        return sqlQuery
                .addScalar("memberId", StandardBasicTypes.INTEGER)
                .addScalar("comment", StandardBasicTypes.STRING)
                .addScalar("commentBy", StandardBasicTypes.STRING)
                .setParameter("memberId", memberId)
                .setResultTransformer(Transformers.aliasToBean(MbbsMOReviewDto.class))
                .setMaxResults(1).uniqueResult();
    }
}
