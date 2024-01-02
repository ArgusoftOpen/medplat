package com.argusoft.medplat.web.users.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.web.users.dao.UserLoginDetailMasterDao;
import com.argusoft.medplat.web.users.model.UserLoginDetailMaster;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * <p>
 *     Implements methods of UserLoginDetailMasterDao
 * </p>
 *
 * @author shrey
 * @since 31/08/2020 4:30
 */
@Repository
@Transactional
public class UserLoginDetailDaoImpl
        extends GenericDaoImpl<UserLoginDetailMaster, Integer>
        implements UserLoginDetailMasterDao {

    public UserLoginDetailMaster getLastLoginDetailByUserId(Integer userId) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserLoginDetailMaster> cq = cb.createQuery(UserLoginDetailMaster.class);
        Root<UserLoginDetailMaster> root = cq.from(UserLoginDetailMaster.class);
        cq.select(root).where(cb.equal(root.get(UserLoginDetailMaster.Fields.USER_ID), userId));
        cq.orderBy(cb.desc(root.get(UserLoginDetailMaster.Fields.ID)));
        return session.createQuery(cq).setMaxResults(1).uniqueResult();
    }


}
