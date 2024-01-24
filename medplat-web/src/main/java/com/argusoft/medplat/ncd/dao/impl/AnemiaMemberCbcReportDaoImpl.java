package com.argusoft.medplat.ncd.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.ncd.dao.AnemiaMemberCbcReportDao;
import com.argusoft.medplat.ncd.model.AnemiaMemberCbcReport;
import com.argusoft.medplat.ncd.model.AnemiaMemberSurveyDetail;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigInteger;

@Repository
@Transactional
public class AnemiaMemberCbcReportDaoImpl extends GenericDaoImpl<AnemiaMemberCbcReport, Integer> implements AnemiaMemberCbcReportDao {
    @Override
    public boolean checkCbcReportEntry(Integer labId) {
        String query = "select count(*) from anemia_member_cbc_report where lab_id = :labId";
        NativeQuery<BigInteger> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("labId", labId);
        BigInteger count = sQLQuery.uniqueResult();
        return count.longValue() == 0;
    }

    @Override
    public AnemiaMemberCbcReport retrieveAnemiaMemberCbcReportByLabId(Integer labId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<AnemiaMemberCbcReport> root = cq.from(AnemiaMemberCbcReport.class);
        cq.select(root).where(cb.equal(root.get("labId"), labId));
        Query<AnemiaMemberCbcReport> query = session.createQuery(cq);
        return query.uniqueResult();
    }
}
