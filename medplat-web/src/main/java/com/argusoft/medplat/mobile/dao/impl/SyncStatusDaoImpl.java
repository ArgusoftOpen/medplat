/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.mobile.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dao.SyncStatusDao;
import com.argusoft.medplat.mobile.model.SyncStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author prateek
 */
@Repository
public class SyncStatusDaoImpl extends GenericDaoImpl<SyncStatus, String> implements SyncStatusDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncStatusDaoImpl.class);


    @Override
    public void updateProcessingStringsToPendingOnServerStartup() {
        LOGGER.info("Updating System Sync Status From Processed To Pending");
        String query = "update system_sync_status set status = 'P' where status = 'PR'";
        getCurrentSession().createNativeQuery(query).executeUpdate();
    }

    @Override
    public List<SyncStatus> retrieveSyncStatusBetweenActionDates(Date from, Date to) {
        String query = "select \n" +
                "id as \"id\", \n" +
                "action_date as \"actionDate\", \n" +
                "relative_id as \"relativeId\", \n" +
                "status as \"status\", \n" +
                "record_string as \"recordString\", \n" +
                "mobile_date as \"mobileDate\", \n" +
                "user_id as \"userId\", \n" +
                "device as \"device\", \n" +
                "client_id as \"clientId\", \n" +
                "lastmodified_by as \"lastModifiedBy\", \n" +
                "lastmodified_date as \"lastModifiedDate\", \n" +
                "duration_of_processing as \"durationOfProcessing\", \n" +
                "error_message as \"errorMessage\", \n" +
                "exception as \"exception\", \n" +
                "mail_sent as \"mailSent\" \n" +
                "from system_sync_status \n" +
                "where action_date >= :from \n" +
                "and action_date <= :to \n" +
                "and record_string ilike :record_string ;";
        NativeQuery<SyncStatus> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("from", from)
                .setParameter("to", to)
                .setParameter("record_string", "%|FHS|%")
                .addScalar("id", StandardBasicTypes.STRING)
                .addScalar("actionDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("relativeId", StandardBasicTypes.INTEGER)
                .addScalar("status", StandardBasicTypes.STRING)
                .addScalar("recordString", StandardBasicTypes.STRING)
                .addScalar("mobileDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("userId", StandardBasicTypes.INTEGER)
                .addScalar("device", StandardBasicTypes.STRING)
                .addScalar("clientId", StandardBasicTypes.INTEGER)
                .addScalar("lastModifiedBy", StandardBasicTypes.INTEGER)
                .addScalar("lastModifiedDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("durationOfProcessing", StandardBasicTypes.INTEGER)
                .addScalar("errorMessage", StandardBasicTypes.STRING)
                .addScalar("exception", StandardBasicTypes.STRING)
                .addScalar("mailSent", StandardBasicTypes.BOOLEAN);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(SyncStatus.class)).getResultList();
    }

    @Override
    public List<SyncStatus> retrieveSyncStatusBetweenActionDatesLikeRecordString(Date from, Date to, String recordString, Integer userId, String status) {

        String query = "select \n" +
                "id as \"id\", \n" +
                "action_date as \"actionDate\", \n" +
                "relative_id as \"relativeId\", \n" +
                "status as \"status\", \n" +
                "record_string as \"recordString\", \n" +
                "mobile_date as \"mobileDate\", \n" +
                "user_id as \"userId\", \n" +
                "device as \"device\", \n" +
                "client_id as \"clientId\", \n" +
                "lastmodified_by as \"lastModifiedBy\", \n" +
                "lastmodified_date as \"lastModifiedDate\", \n" +
                "duration_of_processing as \"durationOfProcessing\", \n" +
                "error_message as \"errorMessage\", \n" +
                "exception as \"exception\", \n" +
                "mail_sent as \"mailSent\" \n" +
                "from system_sync_status \n" +
                "where 1 = 1 ";

        if (from != null) query += "\nand action_date >= :from ";
        if (to != null) query += "\nand action_date <= :to ";
        if (recordString != null && !recordString.isEmpty())
            query += "\nand record_string ilike '%" + recordString + "%'";
        if (status != null) query += "\nand status = 'S'";
        if (userId != null) query += "\nand user_id = " + userId;

        NativeQuery<SyncStatus> nativeQuery = getCurrentSession().createNativeQuery(query);

        if (from != null) nativeQuery.setParameter("from", from);
        if (to != null) nativeQuery.setParameter("to", to);

        nativeQuery.addScalar("id", StandardBasicTypes.STRING)
                .addScalar("actionDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("relativeId", StandardBasicTypes.INTEGER)
                .addScalar("status", StandardBasicTypes.STRING)
                .addScalar("recordString", StandardBasicTypes.STRING)
                .addScalar("mobileDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("userId", StandardBasicTypes.INTEGER)
                .addScalar("device", StandardBasicTypes.STRING)
                .addScalar("clientId", StandardBasicTypes.INTEGER)
                .addScalar("lastModifiedBy", StandardBasicTypes.INTEGER)
                .addScalar("lastModifiedDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("durationOfProcessing", StandardBasicTypes.INTEGER)
                .addScalar("errorMessage", StandardBasicTypes.STRING)
                .addScalar("exception", StandardBasicTypes.STRING)
                .addScalar("mailSent", StandardBasicTypes.BOOLEAN);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(SyncStatus.class)).getResultList();
    }

    @Override
    public List<SyncStatus> retrieveSyncStatusForUpdatingBreastFeedingForWPD() {
        String query = "select id, action_date as \"actionDate\", record_string as \"recordString\" from system_sync_status \n"
                + "where record_string ilike '%|FHW_WPD|%' and action_date > '2019-01-07 17:07:17' and status = 'S'\n"
                + "union \n"
                + "select id, action_date, record_string from system_sync_status_dump \n"
                + "where record_string ilike '%|FHW_WPD|%' and action_date > '2019-01-07 17:07:17' and status = 'S'";

        NativeQuery<SyncStatus> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.addScalar("id", StandardBasicTypes.STRING)
                .addScalar("actionDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("recordString", StandardBasicTypes.STRING);

        return sQLQuery.setResultTransformer(Transformers.aliasToBean(SyncStatus.class)).list();
    }

    @Override
    public void updateSyncedWpdMotherMaster(String syncStatusId) {
        String query = "insert into synced_wpd_mother(sync_status_id) values (:syncStatusId)";

        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(query);
        sQLQuery.setParameter("syncStatusId", syncStatusId);
        sQLQuery.executeUpdate();
    }
}
