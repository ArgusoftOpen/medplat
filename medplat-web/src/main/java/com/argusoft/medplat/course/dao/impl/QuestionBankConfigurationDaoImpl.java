package com.argusoft.medplat.course.dao.impl;

import com.argusoft.medplat.course.dao.QuestionBankConfigurationDao;
import com.argusoft.medplat.course.model.LmsUserMetaData;
import com.argusoft.medplat.course.model.QuestionBankConfiguration;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class QuestionBankConfigurationDaoImpl extends GenericDaoImpl<QuestionBankConfiguration, Integer> implements QuestionBankConfigurationDao {

    public List<QuestionBankConfiguration> getQuestionBanksByQuestionSetId(Integer questionSetId) {
        var session = getCurrentSession();
        var cb = session.getCriteriaBuilder();
        CriteriaQuery<QuestionBankConfiguration> cq = cb.createQuery(QuestionBankConfiguration.class);
        Root<QuestionBankConfiguration> root = cq.from(QuestionBankConfiguration.class);
        cq.select(root).where(
                cb.equal(root.get("questionSetId"), questionSetId)
        );
        return session.createQuery(cq).getResultList();
    }
}
