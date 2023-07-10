package com.argusoft.medplat.course.dao.impl;

import com.argusoft.medplat.course.dao.LmsUserMetaDataDao;
import com.argusoft.medplat.course.model.LmsUserMetaData;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
//import com.argusoft.medplat.drtecho.model.DrTechoUser;
//import com.argusoft.medplat.ndhmmobile.healthid.model.HealthIdUserDetail;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class LmsUserMetaDataDaoImpl extends GenericDaoImpl<LmsUserMetaData, Integer> implements LmsUserMetaDataDao {

    @Override
    public List<LmsUserMetaData> retrieveByUserId(Integer userId) {
        var session = getCurrentSession();
        var cb = session.getCriteriaBuilder();
        CriteriaQuery<LmsUserMetaData> cq = cb.createQuery(LmsUserMetaData.class);
        Root<LmsUserMetaData> root = cq.from(LmsUserMetaData.class);
        cq.select(root).where(
                cb.equal(root.get("userId"), userId)
        );
        return session.createQuery(cq).getResultList();
    }

    public LmsUserMetaData retrieveByUserIdAndCourseId(Integer userId, Integer courseId) {

        var session = getCurrentSession();
        var cb = session.getCriteriaBuilder();
        CriteriaQuery<LmsUserMetaData> cq = cb.createQuery(LmsUserMetaData.class);
        Root<LmsUserMetaData> root = cq.from(LmsUserMetaData.class);
        cq.select(root).where(
                cb.and(cb.equal(root.get("userId"), userId),
                        cb.equal(root.get("courseId"), courseId))
        );
        return session.createQuery(cq).uniqueResult();
    }
}
