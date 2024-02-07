package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
//import com.argusoft.medplat.mobile.dto.AnemiaMemberDataBean;
import com.argusoft.medplat.ncd.dao.AnemiaSurveyDao;
import com.argusoft.medplat.ncd.model.AnemiaMemberSurveyDetail;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@Repository
public class AnemiaSurveyDaoImpl extends GenericDaoImpl<AnemiaMemberSurveyDetail, Integer> implements AnemiaSurveyDao {

//    public List<AnemiaMemberDataBean> retrieveAnemiaMemberDetailsByUserId(Integer userId, Date lastModifiedOn) {
//        String query = "with t1 as (select child_id from location_hierchy_closer_det where parent_id in (select loc_id from um_user_location where user_id = :userId and state = 'ACTIVE')) \n" +
//                "select msdm.id as \"actualId\",\n" +
//                "opd_id as \"opdId\",\n" +
//                "amd.lab_id as \"labId\",\n" +
//                "location_id as \"locationId\",\n" +
//                "first_name as \"firstName\",\n" +
//                "middle_name as \"middleName\",\n" +
//                "last_name as \"lastName\",\n" +
//                "gender as \"gender\",\n" +
//                "(extract(epoch from dob) * 1000) as \"dob\",\n" +
//                "mobile_number as \"mobileNumber\",\n" +
//                "is_pregnant as \"isPregnantFlag\",\n" +
//                "eligible_for_anemia as \"isEligibleForAnemia\",\n" +
//                "(extract(epoch from lmp) * 1000) as \"lmpDate\",\n" +
//                "caste as \"caste\",\n" +
//                "religion as \"religion\",\n" +
//                "occupation as \"occupation\",\n" +
//                "msdm.modified_on as \"modifiedOn\"\n" +
//                "from member_survey_detail_master msdm\n" +
//                "inner join anemia_member_detail amd on msdm.id = amd.member_id \n" +
//                "where location_id in (select * from t1) and eligible_for_anemia is true\n" +
//                "and created_from = 'ANEMIA_SURVEY'";
//        if (lastModifiedOn != null) {
//            query = query + " and msdm.modified_on > :lastModifiedOn";
//        }
//
//        NativeQuery<AnemiaMemberDataBean> q = getCurrentSession().createNativeQuery(query);
//                q.addScalar("actualId", StandardBasicTypes.INTEGER)
//                .addScalar("opdId", StandardBasicTypes.STRING)
//                .addScalar("labId", StandardBasicTypes.INTEGER)
//                .addScalar("locationId", StandardBasicTypes.INTEGER)
//                .addScalar("firstName", StandardBasicTypes.STRING)
//                .addScalar("middleName", StandardBasicTypes.STRING)
//                .addScalar("lastName", StandardBasicTypes.STRING)
//                .addScalar("gender", StandardBasicTypes.STRING)
//                .addScalar("dob", StandardBasicTypes.LONG)
//                .addScalar("mobileNumber", StandardBasicTypes.STRING)
//                .addScalar("isPregnantFlag", StandardBasicTypes.BOOLEAN)
//                .addScalar("isEligibleForAnemia", StandardBasicTypes.BOOLEAN)
//                .addScalar("lmpDate", StandardBasicTypes.LONG)
//                .addScalar("caste", StandardBasicTypes.STRING)
//                .addScalar("religion", StandardBasicTypes.STRING)
//                .addScalar("occupation", StandardBasicTypes.STRING)
//                .addScalar("modifiedOn", StandardBasicTypes.TIMESTAMP);
//        q.setParameter("userId", userId);
//        if (lastModifiedOn != null) {
//            q.setParameter("lastModifiedOn", lastModifiedOn);
//        }
//        return q.setResultTransformer(Transformers.aliasToBean(AnemiaMemberDataBean.class)).list();
//    }

    @Override
    public AnemiaMemberSurveyDetail retrieveAnemiaMemberDetailsByLabId(Integer labId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<AnemiaMemberSurveyDetail> root = cq.from(AnemiaMemberSurveyDetail.class);
        cq.select(root).where(cb.equal(root.get("labId"), labId));
        Query<AnemiaMemberSurveyDetail> query = session.createQuery(cq);
        return query.uniqueResult();
    }
}
