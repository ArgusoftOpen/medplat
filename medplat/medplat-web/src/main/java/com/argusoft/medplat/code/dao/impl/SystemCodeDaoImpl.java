package com.argusoft.medplat.code.dao.impl;

import com.argusoft.medplat.code.dao.SystemCodeDao;
import com.argusoft.medplat.code.mapper.CodeType;
import com.argusoft.medplat.code.mapper.TableType;
import com.argusoft.medplat.code.model.SystemCodeMaster;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 *
 * @author Hiren Morzariya
 */
@Repository
public class SystemCodeDaoImpl extends GenericDaoImpl<SystemCodeMaster, UUID> implements SystemCodeDao {

    @Override
    public SystemCodeMaster retrieveByUUID(UUID uuid) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SystemCodeMaster.class);
        criteria.add(Restrictions.eq(SystemCodeMaster.Fields.ID, uuid));
        return (SystemCodeMaster) criteria.uniqueResult();
    }

    @Override
    public List<SystemCodeMaster> getSystemCodes() {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(SystemCodeMaster.class);
        criteria.addOrder(Order.desc(SystemCodeMaster.Fields.MODIFIED_ON));
        return criteria.list();
    }

    @Override
    public List<SystemCodeMaster> getSystemCodesByTypeAndCode(TableType tableType, String tableId, CodeType codeType) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(SystemCodeMaster.class);
        criteria.add(
                Restrictions.and(
                        Restrictions.eq(SystemCodeMaster.Fields.TABLE_TYPE, tableType.name()),
                        Restrictions.eq(SystemCodeMaster.Fields.TABLE_ID, Integer.valueOf(tableId)),
                        Restrictions.eq(SystemCodeMaster.Fields.CODE_TYPE, codeType.name()))
        );
        criteria.addOrder(Order.desc(SystemCodeMaster.Fields.MODIFIED_ON));
        return criteria.list();
    }

    @Override
    public List<SystemCodeMaster> getSystemCodesByTableType(String tableType) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(SystemCodeMaster.class);
        criteria.add(Restrictions.eq(SystemCodeMaster.Fields.TABLE_TYPE, tableType));
        criteria.addOrder(Order.desc(SystemCodeMaster.Fields.MODIFIED_ON));

        return criteria.list();
    }

    @Override
    public List<SystemCodeMaster> getSystemCodesByCodeType(String codeType) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(SystemCodeMaster.class);
        criteria.add(Restrictions.eq(SystemCodeMaster.Fields.CODE_TYPE, codeType));
        criteria.addOrder(Order.desc(SystemCodeMaster.Fields.MODIFIED_ON));

        return criteria.list();

    }

}
