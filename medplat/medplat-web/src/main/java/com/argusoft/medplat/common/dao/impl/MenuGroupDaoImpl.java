package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.MenuGroupDao;
import com.argusoft.medplat.common.model.MenuConfig;
import com.argusoft.medplat.common.model.MenuGroup;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * <p>
 *     Implements methods of MenuGroupDao
 * </p>
 *
 * @author charmi
 * @since 31/08/2020 4:30
 */
@Repository
public class MenuGroupDaoImpl extends GenericDaoImpl<MenuGroup, Integer> implements MenuGroupDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuGroup> getActiveGroups(String groupName, Integer offset, Integer limit, Boolean subGroupRequired, String groupType) {
        DetachedCriteria criteria = DetachedCriteria.forClass(MenuGroup.class);
        criteria.add(Restrictions.eq(MenuGroup.Fields.IS_ACTIVE, true));
        if (StringUtils.hasText(groupName)) {
            criteria.add(Restrictions.ilike(MenuGroup.Fields.GROUP_NAME, groupName, MatchMode.ANYWHERE));
        }
        if (offset == null) {
            offset = -1;
        }
        if (limit == null) {
            limit = -1;
        }
        if (subGroupRequired == null || !subGroupRequired) {
            criteria.add(Restrictions.eqOrIsNull(MenuGroup.Fields.PARENT_GROUP, null));
        }
        if (StringUtils.hasText(groupType)) {
            criteria.add(Restrictions.eq(MenuGroup.Fields.TYPE, groupType));
        }
        return findByCriteria(criteria, offset, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isReportAssociatedWithGrouporSubGroup(Integer groupId) {
        Session session = sessionFactory.getCurrentSession();
        Boolean reportExists = Boolean.FALSE;

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MenuConfig> cq = cb.createQuery(MenuConfig.class);
        Root<MenuConfig> root = cq.from(MenuConfig.class);
        if (groupId != null) {
            cq.where(cb.and(cb.or(
                    cb.equal(root.get(MenuConfig.Fields.GROUP_ID), groupId),
                    cb.equal(root.get(MenuConfig.Fields.SUB_GROUP_ID), groupId)
                    ),
                    cb.equal(root.get(MenuConfig.Fields.IS_ACTIVE), Boolean.TRUE)
            ));
        }
        if (!session.createQuery(cq).getResultList().isEmpty()) {
            reportExists = Boolean.TRUE;
        }
        return reportExists;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isSubgroupAssociatedWithGroup(Integer groupId) {
        Session session = sessionFactory.getCurrentSession();
        Boolean reportExists = Boolean.FALSE;
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MenuGroup> cq = cb.createQuery(MenuGroup.class);
        Root<MenuGroup> root = cq.from(MenuGroup.class);

        cq.where(cb.and(
                cb.equal(root.get(MenuGroup.Fields.PARENT_GROUP), groupId),
                cb.equal(root.get(MenuGroup.Fields.IS_ACTIVE), Boolean.TRUE)
        ));
        if (!session.createQuery(cq).getResultList().isEmpty()) {
            reportExists = Boolean.TRUE;
        }
        return reportExists;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MenuGroup retrieveMenuGroupByGroupNameAndType(String groupType, String groupName) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MenuGroup> cq = cb.createQuery(MenuGroup.class);
        Root<MenuGroup> root = cq.from(MenuGroup.class);

        cq.where(cb.and(
                cb.equal(root.get(MenuGroup.Fields.GROUP_NAME), groupName),
                cb.equal(root.get(MenuGroup.Fields.TYPE), groupType)
        ));

        return session.createQuery(cq).uniqueResult();
    }


}
