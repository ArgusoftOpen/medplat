
package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.ServerManagementDao;
import com.argusoft.medplat.common.dto.SyncServerDto;
import com.argusoft.medplat.common.dto.SyncWithServerDto;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * <p>
 *     Implements methods of ServerManagementDao
 * </p>
 * @author ashish
 * @since 31/08/2020 4:30
 */
@Repository
@Transactional
public class ServerManagementDaoImpl implements ServerManagementDao {

    private static final String FEATURE_UUID ="featureUUID";
    private static final String SERVER_IDS ="serverIds";

    @Autowired
    protected SessionFactory sessionFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonNode getRemoteConfigurations(Integer id,String serverName, String password, String serverUrl) {
        JsonNode configJson = null;
        if (!ConstantUtil.SERVER_TYPE.equals("LIVE")) {
            try {
                HttpClient client = HttpClients.custom().build();
                HttpGet getRequest = new HttpGet(serverUrl);
                getRequest.addHeader("accept", "application/json");
                getRequest.addHeader("serverName", serverName);
                getRequest.addHeader("password", password);
                getRequest.addHeader("id", id.toString());
                HttpResponse response = client.execute(getRequest);
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new ImtechoSystemException("Failed : retriving json config from server with  HTTP error code : " , response.getStatusLine().getStatusCode());
                } else {
                    configJson = ImtechoUtil.convertStringToJson(EntityUtils.toString(response.getEntity()));
                }

            } catch (IOException e) {
                Logger.getLogger(ServerManagementDaoImpl.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return configJson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insertSystemSyncWith(Integer serverId, Integer configId) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> sql;

        if (configId != null) {
            sql = session.createNativeQuery("insert into sync_system_configuration_server_access_details (config_id,server_id,is_in_sync) "
                    + "values (:configId,:serverId,:isInSync) returning id;");
            sql.setParameter("configId", configId);
        } else {
            sql = session.createNativeQuery("insert into sync_system_configuration_server_access_details (server_id,is_in_sync) "
                    + "values (:serverId,:isInSync) returning id;");
        }

        sql.setParameter("serverId", serverId);
        sql.setParameter("isInSync", Boolean.FALSE);
        return sql.uniqueResult();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addOrUpdateServer(SyncServerDto syncServerDto) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> sql;
        if (syncServerDto.getId() != null) {
            sql = session.createNativeQuery("update server_list_master set server_name = :serverName ,username= :userName , host_url = :hostUrl, is_active = :isActive where id = :id ;");

            sql.setParameter("id",syncServerDto.getId());
        }
        else{
            sql = session.createNativeQuery("insert into server_list_master (server_name,username,password,host_url,is_active) values\n" +
                    "(:serverName,:userName,:password,:hostUrl,:isActive)");


            sql.setParameter("password", syncServerDto.getPassword());
        }

        sql.setParameter("serverName", syncServerDto.getServerName());
        sql.setParameter("userName", syncServerDto.getUsername());
        sql.setParameter("hostUrl", syncServerDto.getHost());
        sql.setParameter("isActive", syncServerDto.getIsActive());

        sql.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer createMapperRecord(SyncWithServerDto syncWithServerDto, Integer serverId) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> sql = session.createNativeQuery("insert into sync_server_feature_mapping_detail (feature_uuid,server_id) "
                + "values ('" + syncWithServerDto.getFeatureUUID().toString() + "',:serverId) ON CONFLICT ON CONSTRAINT sync_server_feature_mapping_detail_pkey DO NOTHING returning server_id;");
        sql.setParameter("serverId", serverId);
        return sql.uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNoNSyncServerEntry(List<Integer> serverIds, UUID featureUUID) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> sql = session.createNativeQuery("Delete from sync_server_feature_mapping_detail where cast(feature_uuid as text) = :featureUUID and server_id not in ( :serverIds ) ;");
        sql.setParameter(FEATURE_UUID, featureUUID.toString());
        sql.setParameterList(SERVER_IDS, serverIds);
        sql.executeUpdate();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNoNSyncNonActiveServerEntry(List<Integer> serverIds, UUID featureUUID) {
        if (serverIds.isEmpty()) {
            serverIds.add(-1);
        }
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> sql = session.createNativeQuery("delete from sync_system_configuration_server_access_details where id in (\n"
                + "select id from sync_server_feature_mapping_detail sfmfs inner join sync_system_configuration_server_access_details acc on \n"
                + "sfmfs.server_id  = acc.server_id \n"
                + "where \n"
                + "acc.is_in_sync = false\n"
                + "and cast(sfmfs.feature_uuid as text) = :featureUUID \n"
                + "and sfmfs.server_id not in (:serverIds))");
        sql.setParameter(FEATURE_UUID, featureUUID.toString());
        sql.setParameterList(SERVER_IDS, serverIds);
        sql.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getActiveServerIdFromFeature(UUID featureUUID) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> sql = session.createNativeQuery("select distinct server_id from sync_server_feature_mapping_detail where cast(feature_uuid as text) = :featureUUID order by server_id ");
        sql.setParameter(FEATURE_UUID, featureUUID.toString());
        return sql.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteActiveServerRecord(List<Integer> serverIds, UUID featureUUID) {
        Session session = sessionFactory.getCurrentSession();
//        Delete from Access table
        NativeQuery<Integer> sql = session.createNativeQuery("delete from sync_system_configuration_server_access_details where id in (\n"
                + "select id from sync_server_feature_mapping_detail sfmfs inner join sync_system_configuration_server_access_details acc on \n"
                + "sfmfs.server_id  = acc.server_id \n"
                + "where \n"
                + "acc.is_in_sync = false\n"
                + "and cast(sfmfs.feature_uuid as text) = :featureUUID \n"
                + "and sfmfs.server_id in (:serverIds))");
        sql.setParameter(FEATURE_UUID, featureUUID.toString());
        sql.setParameterList(SERVER_IDS, serverIds);
        sql.executeUpdate();

//        Delete from mapper table

        sql = session.createNativeQuery("Delete from sync_server_feature_mapping_detail where cast(feature_uuid as text) = :featureUUID and server_id in ( :serverIds ) ;");
        sql.setParameter(FEATURE_UUID, featureUUID.toString());
        sql.setParameterList(SERVER_IDS, serverIds);
        sql.executeUpdate();

    }

}
