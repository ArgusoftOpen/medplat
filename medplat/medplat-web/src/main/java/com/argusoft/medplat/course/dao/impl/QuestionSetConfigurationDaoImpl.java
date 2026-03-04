package com.argusoft.medplat.course.dao.impl;

import com.argusoft.medplat.course.dao.QuestionSetConfigurationDao;
import com.argusoft.medplat.course.model.QuestionBankConfiguration;
import com.argusoft.medplat.course.model.QuestionSetConfiguration;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class QuestionSetConfigurationDaoImpl extends GenericDaoImpl<QuestionSetConfiguration, Integer> implements QuestionSetConfigurationDao {

    @Override
    public List<QuestionSetConfiguration> getQuestionSetByReferenceIdAndType(Integer refId, String refType, Integer questionSetType) {

        var session = getCurrentSession();
        var cb = session.getCriteriaBuilder();
        CriteriaQuery<QuestionSetConfiguration> cq = cb.createQuery(QuestionSetConfiguration.class);
        Root<QuestionSetConfiguration> root = cq.from(QuestionSetConfiguration.class);
        cq.select(root).where(
                cb.and(cb.equal(root.get("refId"), refId),
                        cb.equal(root.get("refType"), refType),
                        cb.equal(root.get("questionSetType"), questionSetType),
                        cb.equal(root.get("status"), "ACTIVE")
                )
        );
        return session.createQuery(cq).getResultList();
    }
}
