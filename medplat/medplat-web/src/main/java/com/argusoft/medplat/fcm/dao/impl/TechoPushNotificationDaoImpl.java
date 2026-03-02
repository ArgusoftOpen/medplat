package com.argusoft.medplat.fcm.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.fcm.dao.TechoPushNotificationDao;
import com.argusoft.medplat.fcm.dto.TechoPushNotificationDto;
import com.argusoft.medplat.fcm.model.TechoPushNotificationMaster;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author nihar
 * @since 02/08/22 2:30 PM
 */
@Repository
@Transactional
public class TechoPushNotificationDaoImpl extends GenericDaoImpl<TechoPushNotificationMaster, Long>
        implements TechoPushNotificationDao {

    @Override
    public List<TechoPushNotificationDto> getPushNotificationsByType(String type) {
        String query = "select tpnm.id,\n" +
                "    tpnm.user_id as userId,\n" +
                "    tpnm.\"type\" as \"type\",\n" +
                "   tpnm.response," +
                "   tpnm.exception," +
                "   tpnm.is_sent as isSent," +
                "   tpnm.is_priority as isPriority," +
                "   tpnm.created_on as createdOn," +
                "   tpnm.created_by as createdBy," +
                "   tpnm.exception," +
                "    tpnt.message,\n" +
                "    tpnt.description,\n" +
                "    tpnt.heading,\n" +
                "    ft.\"token\" \n" +
                "from techo_push_notification_master tpnm\n" +
                "    inner join techo_push_notification_type tpnt on tpnm.type = tpnt.\"type\"\n" +
                "     and tpnt.state ='ACTIVE'\n" +
                "    inner join firebase_token ft on tpnm.user_id = ft.user_id\n" +
                "where tpnm.\"type\" = :type and tpnm.is_sent is not true ;";

        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(query);

        sqlQuery.addScalar("id", StandardBasicTypes.LONG)
                .addScalar("userId", StandardBasicTypes.INTEGER)
                .addScalar("type", StandardBasicTypes.STRING)
                .addScalar("response", StandardBasicTypes.STRING)
                .addScalar("exception", StandardBasicTypes.STRING)
                .addScalar("isSent", StandardBasicTypes.BOOLEAN)
                .addScalar("isPriority", StandardBasicTypes.BOOLEAN)
                .addScalar("createdOn", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.INTEGER)
                .addScalar("message", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("heading", StandardBasicTypes.STRING)
                .addScalar("token", StandardBasicTypes.STRING)
                .setParameter("type", type)
                .setResultTransformer(Transformers.aliasToBean(TechoPushNotificationDto.class));

        return sqlQuery.list();
    }

    @Override
    public List<TechoPushNotificationDto> getPushNotifications() {
        String query = "select tpnm.id,\n" +
                "    tpnm.user_id as userId,\n" +
                "    tpnm.\"type\" as \"type\",\n" +
                "   tpnm.response," +
                "   tpnm.exception," +
                "   tpnm.is_sent as isSent," +
                "   tpnm.is_priority as isPriority," +
                "   tpnm.created_on as createdOn," +
                "   tpnm.created_by as createdBy," +
                "    tpnt.message,\n" +
                "    tpnt.description,\n" +
                "    tpnt.heading,\n" +
                "    ft.\"token\" \n" +
                "from techo_push_notification_master tpnm\n" +
                "    inner join techo_push_notification_type tpnt on tpnm.type = tpnt.\"type\"\n" +
                "     and tpnt.state ='ACTIVE'\n" +
                "    inner join firebase_token ft on tpnm.user_id = ft.user_id\n" +
                "where tpnm.is_sent is not true limit 500 ;";

        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(query);

        sqlQuery.addScalar("id", StandardBasicTypes.LONG)
                .addScalar("userId", StandardBasicTypes.INTEGER)
                .addScalar("type", StandardBasicTypes.STRING)
                .addScalar("response", StandardBasicTypes.STRING)
                .addScalar("exception", StandardBasicTypes.STRING)
                .addScalar("isSent", StandardBasicTypes.BOOLEAN)
                .addScalar("isPriority", StandardBasicTypes.BOOLEAN)
                .addScalar("createdOn", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.INTEGER)
                .addScalar("message", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("heading", StandardBasicTypes.STRING)
                .addScalar("token", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(TechoPushNotificationDto.class));

        return sqlQuery.list();
    }

    @Override
    public List<TechoPushNotificationDto> getPushNotification(Date toTime, int limit) {
        String query = "with details as (\n" +
                "\tselect tpnm.id,\n" +
                "\ttpnm.user_id as \"userId\",\n" +
                "\ttpnm.type,\n" +
                "\ttpnt.message,\n" +
                "\ttpnt.description,\n" +
                "\ttpnt.heading,\n" +
                "\ttpnm.created_on as \"createdOn\",\n" +
                "\ttpnm.event_id as \"eventId\",\n" +
                "\ttpnm.message as \"messageEvent\",\n" +
                "\ttpnm.heading as \"headingEvent\",\n" +
                "\ttpnm.created_by as \"createdBy\"\n" +
                "\tfrom techo_push_notification_master tpnm\n" +
                "\tinner join techo_push_notification_type tpnt on tpnm.type = tpnt.type\n" +
                "\tand tpnt.is_active is true\n" +
                "\twhere tpnm.created_on <= :toTime and tpnm.is_sent is not true and tpnm.is_processed is not true limit :limit\n" +
                "),tokens as (\n" +
                "\tselect distinct on (details.\"userId\")\n" +
                "\tdetails.\"userId\",\n" +
                "\tfirebase_token.\"token\"\n" +
                "\tfrom details\n" +
                "\tinner join firebase_token on details.\"userId\" = firebase_token.user_id\n" +
                "\torder by details.\"userId\" asc, firebase_token.id desc\n" +
                ")select details.*,\n" +
                "tokens.token\n" +
                "from details\n" +
                "inner join tokens on details.\"userId\" = tokens.\"userId\"";

        Session session = sessionFactory.getCurrentSession();
        NativeQuery<TechoPushNotificationDto> sqlQuery = session.createNativeQuery(query);

        sqlQuery.addScalar("id", StandardBasicTypes.LONG)
                .addScalar("userId", StandardBasicTypes.INTEGER)
                .addScalar("type", StandardBasicTypes.STRING)
                .addScalar("message", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("heading", StandardBasicTypes.STRING)
                .addScalar("token", StandardBasicTypes.STRING)
                .addScalar("createdOn", StandardBasicTypes.DATE)
                .addScalar("createdBy", StandardBasicTypes.INTEGER)
                .addScalar("eventId", StandardBasicTypes.STRING)
                .addScalar("messageEvent", StandardBasicTypes.STRING)
                .addScalar("headingEvent", StandardBasicTypes.STRING)
                .setParameter("toTime", toTime)
                .setParameter("limit", limit)
                .setResultTransformer(Transformers.aliasToBean(TechoPushNotificationDto.class));

        return sqlQuery.list();
    }

    @Override
    public void markAsProcessed(Long id) {
        String hql = "update TechoPushNotificationMaster set " +
                TechoPushNotificationMaster.Fields.PROCESSED_ON + " = :time ,"
                + TechoPushNotificationMaster.Fields.IS_PROCESSED + "= " + true + ""
                + " where " + TechoPushNotificationMaster.Fields.ID + " = " + id + "";
        Query query = getCurrentSession().createQuery(hql);
        query.setTimestamp("time", new Date());
        query.executeUpdate();
    }

    @Override
    public void markAsSentAndSetResponse(Long id, String response, String exception, boolean b) {
        if (id != null) {
            String hql = "update TechoPushNotificationMaster set " + TechoPushNotificationMaster.Fields.RESPONSE + "=  :response , "
                    + TechoPushNotificationMaster.Fields.EXCEPTION + "= :exception "
                    + "," + TechoPushNotificationMaster.Fields.COMPLETED_ON + " = :time ,"
                    + TechoPushNotificationMaster.Fields.IS_SENT + " = true where " + TechoPushNotificationMaster.Fields.ID + " = :id";
            Query query = getCurrentSession().createQuery(hql);
            query.setTimestamp("time", new Date());
            query.setParameter("id", id);
            query.setParameter("response", response);
            query.setParameter("exception", exception);

            query.executeUpdate();
        }
    }
}
