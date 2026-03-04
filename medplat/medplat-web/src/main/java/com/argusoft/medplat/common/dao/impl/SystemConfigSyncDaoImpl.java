
package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.SystemConfigSyncDao;
import com.argusoft.medplat.common.model.SystemConfigSync;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * <p>
 *     Implements methods of SystemConfigSyncDao
 * </p>
 * @author ashish
 * @since 31/08/2020 4:30
 */
@Repository
@Transactional
public class SystemConfigSyncDaoImpl extends GenericDaoImpl<SystemConfigSync, Integer> implements SystemConfigSyncDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SystemConfigSync> retrieveSystemConfigBasedOnAccess(Integer id, String serverName) {
        List<SystemConfigSync> syncList = new LinkedList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        Session session = sessionFactory.getCurrentSession();

        String query = "select \n"
                + "access.id,\n"
                + "cast (config.feature_uuid as text) as feature_uuid ,\n"
                + "config.feature_name ,\n"
                + "config.feature_type,\n"
                + "config.config_json ,\n"
                + "config.created_by ,\n"
                + "config.created_on,\n"
                + "master.id as server_id \n"
                + "from sync_system_configuration_server_access_details access\n"
                + "inner join sync_system_configuration_master config on access.config_id = config.id  \n"
                + "inner join server_list_master master on master.server_name = :serverName \n"
                + "where\n"
                + "access.server_id = master.id\n"
                + "and access.id > :id  \n"
                + "order by config.id asc;";

        NativeQuery<Integer> sql = session.createNativeQuery(query);
        sql.setParameter("serverName", serverName);
        sql.setParameter("id", id);

        List<Integer> list = sql.list();
        for (Object obj : list) {
            Object[] resultArray = (Object[]) obj;
//            check If UUID is not null
            if (resultArray[1] != null) {
//                add id entry for access table
                ids.add((Integer) resultArray[0]);

                syncList.add(new SystemConfigSync(
                        (Integer) resultArray[0],
                        UUID.fromString((String) resultArray[1]),
                        (String) resultArray[2],
                        (String) resultArray[3],
                        (String) resultArray[4],
                        (Integer) resultArray[5],
                        (Date) resultArray[6]
                ));
            }

        }

//      set is_in_sync to true for access table
        query = "update sync_system_configuration_server_access_details acc set is_in_sync = true where acc.id in (:ids);";
        sql = session.createNativeQuery(query);
        for (Integer access_id : ids) {
            sql.setParameter("ids", access_id);
            sql.executeUpdate();
        }
        ids.clear();

        return syncList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int deleteAllRow() {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery("delete from sync_system_configuration_master ;");
        return q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkPassword(String serverName, String password) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery("select id from server_list_master slm where server_name = :serverName and password = :password ;");
        q.setParameter("serverName", serverName);
        q.setParameter("password", password);
        Integer id = q.uniqueResult();
        return !Objects.isNull(id);
    }



}
