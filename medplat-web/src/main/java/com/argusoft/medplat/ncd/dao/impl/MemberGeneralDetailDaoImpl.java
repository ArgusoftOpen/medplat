package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.course.model.CourseMaster;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.MemberGeneralDetailDao;
import com.argusoft.medplat.ncd.dao.MemberInitialAssessmentDao;
import com.argusoft.medplat.ncd.dto.MedicineDto;
import com.argusoft.medplat.ncd.dto.MemberGeneralDto;
import com.argusoft.medplat.ncd.dto.MemberTreatmentHistoryDto;
import com.argusoft.medplat.ncd.enums.DoneBy;
import com.argusoft.medplat.ncd.model.MemberGeneralDetail;
import com.argusoft.medplat.ncd.model.MemberInitialAssessmentDetail;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.type.EnumType;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Repository
public class MemberGeneralDetailDaoImpl extends GenericDaoImpl<MemberGeneralDetail, Integer> implements MemberGeneralDetailDao {

    @Override
    public MemberGeneralDetail retrieveForReferralId(Integer referralId, Date screeningDate) {

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MemberGeneralDetail> criteriaQuery = cb.createQuery(MemberGeneralDetail.class);
        Root<MemberGeneralDetail> root = criteriaQuery.from(MemberGeneralDetail.class);
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
        MemberGeneralDetail memberGeneralDetail = session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
        if (memberGeneralDetail == null && screeningDate != null) {
            memberGeneralDetail = retrieveForReferralId(referralId, null);
            if (memberGeneralDetail == null) {
                memberGeneralDetail = new MemberGeneralDetail();
                memberGeneralDetail.setReferralId(referralId);
            }
        }
        return memberGeneralDetail;
    }

    @Override
    public MemberGeneralDetail retrieveByMemberIdAndScreeningDate(Integer memberId, Date screeningDate, DoneBy type) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberGeneralDetail> criteriaQuery = criteriaBuilder.createQuery(MemberGeneralDetail.class);
        Root<MemberGeneralDetail> root = criteriaQuery.from(MemberGeneralDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberGeneralDetail.Fields.MEMBER_ID), memberId);
        Predicate screeningDateEqual = criteriaBuilder.equal(root.get(MemberGeneralDetail.Fields.SCREENING_DATE), screeningDate);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberGeneralDetail.Fields.DONE_BY), type);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, screeningDateEqual, doneByMoEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberGeneralDetail.Fields.SCREENING_DATE)));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberGeneralDetail.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    @Override
    public List<MemberGeneralDetail> retrieveByMemberIdAndHealthInfrasturctureId(Integer memberId, Integer healthInfraId) {
        return super.findByCriteriaList((root, criteriaBuilder, criteriaQuery) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get(MemberGeneralDetail.Fields.MEMBER_ID), memberId));
            predicates.add(criteriaBuilder.equal(root.get(MemberGeneralDetail.Fields.HEALTH_INFRA_ID), healthInfraId));
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberGeneralDetail.Fields.REFERRED_ON)));
            return predicates;
        });
    }

    @Override
    public MemberGeneralDetail retrieveLastRecordByMemberId(Integer memberId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<MemberGeneralDetail> criteriaQuery = criteriaBuilder.createQuery(MemberGeneralDetail.class);
        Root<MemberGeneralDetail> root = criteriaQuery.from(MemberGeneralDetail.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(MemberGeneralDetail.Fields.MEMBER_ID), memberId);
        Predicate doneByMoEqual = criteriaBuilder.equal(root.get(MemberGeneralDetail.Fields.DONE_BY), DoneBy.MO);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, doneByMoEqual));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberGeneralDetail.Fields.SCREENING_DATE)));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(MemberGeneralDetail.Fields.ID)));
        return session.createQuery(criteriaQuery).setMaxResults(1).uniqueResult();
    }

    @Override
    public List<MedicineDto> retrieveGeneralDrugs() {
        String query = "select id as id,value as name from listvalue_field_value_detail" +
                " where field_key='drugInventoryMedicine' ";
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MedicineDto> sQLQuery = session.createNativeQuery(query);
        //return sQLQuery.list();
        return sQLQuery.setResultTransformer(Transformers.aliasToBean(MedicineDto.class)).getResultList();
    }

    @Override
    public MemberGeneralDto retrieveLastCommentForGeneralByMemberIdAndType(Integer memberId, DoneBy type) {
        String sql = "select ncd_member_general_detail.member_id \"memberId\",ncd_member_general_detail.done_by \"doneBy\",ncd_member_general_detail.screening_date \"screeningDate\",ncd_member_general_detail.comment \"comment\",concat(um_user.first_name,' ',um_user.last_name) \"commentBy\" from ncd_member_general_detail\n" +
                "left join um_user on ncd_member_general_detail.created_by = um_user.id\n" +
                "where member_id=:memberId and done_by=:doneBy and comment is not null order by ncd_member_general_detail.id desc";
        Properties params = new Properties();
        params.put("enumClass", DoneBy.class.getName());
        params.put("type", "12");
        Type doneEnumType = sessionFactory.getTypeHelper().custom(EnumType.class,params);
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<MemberGeneralDto> sqlQuery = session.createNativeQuery(sql);
        return sqlQuery
                .addScalar("memberId", StandardBasicTypes.INTEGER)
                .addScalar("doneBy", doneEnumType)
                .addScalar("screeningDate", StandardBasicTypes.DATE)
                .addScalar("comment", StandardBasicTypes.STRING)
                .addScalar("commentBy", StandardBasicTypes.STRING)
                .setParameter("memberId", memberId)
                .setParameter("doneBy", type.toString())
                .setResultTransformer(Transformers.aliasToBean(MemberGeneralDto.class))
                .setMaxResults(1).uniqueResult();
    }
}
