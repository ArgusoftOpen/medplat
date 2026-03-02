package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.SystemConfigurationDao;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Implements methods of SystemConfigurationDao
 * </p>
 *
 * @author harsh
 * @since 31/08/2020 4:30
 */
@Repository
public class SystemConfigurationDaoImpl extends GenericDaoImpl<SystemConfiguration, Integer> implements SystemConfigurationDao {


    @Autowired
    @Qualifier("appSessionFactory")
    SessionFactory sessionFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public SystemConfiguration retrieveSystemConfigurationByKey(String key) {
        if (key != null) {
            Session session = sessionFactory.getCurrentSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<SystemConfiguration> cq = cb.createQuery(SystemConfiguration.class);
            Root<SystemConfiguration> root = cq.from(SystemConfiguration.class);

            cq.select(root);
            cq.where(cb.and(cb.equal(root.get(SystemConfiguration.Fields.SYSTEM_KEY), key), cb.equal(root.get(SystemConfiguration.Fields.IS_ACTIVE), Boolean.TRUE)));

            return session.createQuery(cq).uniqueResult();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SystemConfiguration> retrieveSystemConfigurationsBasedOnLikeKeySearch(String startsWithString) {
        PredicateBuilder<SystemConfiguration> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.like(builder.lower(root.get(SystemConfiguration.Fields.SYSTEM_KEY)), builder.lower(builder.literal(startsWithString + "%"))));
            predicates.add(builder.equal(root.get(SystemConfiguration.Fields.IS_ACTIVE), true));
            return predicates;
        };
        return new ArrayList<>(super.findByCriteriaList(predicateBuilder));
    }
}
