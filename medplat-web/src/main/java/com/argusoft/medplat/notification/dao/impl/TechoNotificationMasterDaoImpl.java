/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.mobile.dto.TechoNotificationDataBean;
import com.argusoft.medplat.notification.dao.TechoNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoNotificationMaster;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in techo notification master dao.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 10:19 AM
 */
@Repository
@Transactional
public class TechoNotificationMasterDaoImpl extends GenericDaoImpl<TechoNotificationMaster, Integer> implements TechoNotificationMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Integer> getDeletedNotificationsForUserByLastModifiedOn(Integer userId, Date lastModifiedOn) {
        String query = "select distinct notification_id from techo_notification_location_change_detail,location_hierchy_closer_det,um_user_location\n"
                + "where techo_notification_location_change_detail.created_on > :lastModifiedOn and from_location_id = location_hierchy_closer_det.child_id\n"
                + "and location_hierchy_closer_det.parent_id = um_user_location.loc_id and um_user_location.user_id = :userId \n"
                + "and um_user_location.state = 'ACTIVE'";

        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(query)
                .addScalar("notification_id", StandardBasicTypes.INTEGER);
        q.setParameter("lastModifiedOn", lastModifiedOn);
        q.setParameter(TechoNotificationMaster.Fields.USER_ID, userId);

        return q.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TechoNotificationDataBean> retrieveAllNotificationsForUser(Integer userId, Integer roleId) {
        String query = " with  t1 as (select child_id from location_hierchy_closer_det "
                + " where parent_id in (select loc_id from um_user_location  where user_id = :userId and state = 'ACTIVE')) \n"
                + " select n.id as id, \n"
                + " :userId as \"ashaId\",\n"
                + " (case when n.member_id is null then n.family_id else n.member_id end) as \"beneficiaryId\",\n"
                + " nt.code as \"task\",\n"
                + " (case when n.due_on is null then null else n.due_on end) as \"expectedDueDate\",\n"
                + " (case when n.schedule_date is null then null else n.schedule_date end) as \"scheduledDate\",\n"
                + " n.location_id as \"locationId\",\n"
                + " n.member_id as \"memberId\",\n"
                + " n.family_id as \"familyId\",\n"
                + " n.other_details as \"otherDetails\",\n"
                + " n.migration_id as \"migrationId\",\n"
                + " (case when n.due_on is not null and n.due_on > now() then false when n.due_on is null then false else true end) as \"overdueFlag\",\n"
                + " n.notification_code as \"customField\",\n"
                + " (case when n.expiry_date is null then null else n.expiry_date end) as \"expiryDate\",\n"
                + " n.modified_on as modifiedOn,\n"
                + " n.state,\n"
                + " n.header\n"
                + " from techo_notification_master n "
                + " inner join notification_type_master nt on n.notification_type_id = nt.id "
                + " inner join notification_type_role_rel ntr on n.notification_type_id = ntr.notification_type_id and ntr.role_id = :roleId"
                + " where location_id in (select * from t1) and n.state in ('PENDING','RESCHEDULE')";

        NativeQuery<TechoNotificationDataBean> q = getCurrentSession().createNativeQuery(query)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("ashaId", StandardBasicTypes.INTEGER)
                .addScalar("beneficiaryId", StandardBasicTypes.INTEGER)
                .addScalar("expectedDueDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("scheduledDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("locationId", StandardBasicTypes.INTEGER)
                .addScalar("memberId", StandardBasicTypes.INTEGER)
                .addScalar("familyId", StandardBasicTypes.INTEGER)
                .addScalar("otherDetails", StandardBasicTypes.STRING)
                .addScalar("migrationId", StandardBasicTypes.INTEGER)
                .addScalar("overdueFlag", StandardBasicTypes.BOOLEAN)
                .addScalar("customField", StandardBasicTypes.STRING)
                .addScalar("expiryDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("modifiedOn", StandardBasicTypes.TIMESTAMP)
                .addScalar("state", StandardBasicTypes.STRING)
                .addScalar("header", StandardBasicTypes.STRING)
                .addScalar("task", StandardBasicTypes.STRING);
        q.setParameter(TechoNotificationMaster.Fields.USER_ID, userId);
        q.setParameter("roleId", roleId);
        return q.setResultTransformer(Transformers.aliasToBean(TechoNotificationDataBean.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TechoNotificationDataBean> retrieveNotificationsForUserByLastModifiedOn(Integer userId, Integer roleId, Date lastModifiedOn) {
        String query = " with t1 as (select child_id from location_hierchy_closer_det "
                + " where parent_id in (select loc_id from um_user_location where user_id = :userId and state = 'ACTIVE')) \n"
                + " select n.id as id, \n"
                + " :userId as \"ashaId\",\n"
                + " (case when n.member_id is null then n.family_id else n.member_id end) as \"beneficiaryId\",\n"
                + " nt.code as \"task\",\n"
                + " (case when n.due_on is null then null else n.due_on end) as \"expectedDueDate\",\n"
                + " (case when n.schedule_date is null then null else n.schedule_date end) as \"scheduledDate\",\n"
                + " n.location_id as \"locationId\",\n"
                + " n.member_id as \"memberId\",\n"
                + " n.family_id as \"familyId\",\n"
                + " n.other_details as \"otherDetails\",\n"
                + " n.migration_id as \"migrationId\",\n"
                + " (case when n.due_on is not null and n.due_on > now() then false when n.due_on is null then false else true end) as \"overdueFlag\",\n"
                + " n.notification_code as \"customField\",\n"
                + " (case when n.expiry_date is null then null else n.expiry_date end) as \"expiryDate\",\n"
                + " n.modified_on as modifiedOn,\n"
                + " n.state,\n"
                + " n.header\n"
                + " from techo_notification_master n "
                + " inner join notification_type_master nt on n.notification_type_id = nt.id "
                + " inner join notification_type_role_rel ntr on n.notification_type_id = ntr.notification_type_id and ntr.role_id = :roleId"
                + " where location_id in (select * from t1) and n.modified_on > :lastModifiedOn ";

        NativeQuery<TechoNotificationDataBean> q = getCurrentSession().createNativeQuery(query)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("ashaId", StandardBasicTypes.INTEGER)
                .addScalar("beneficiaryId", StandardBasicTypes.INTEGER)
                .addScalar("expectedDueDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("scheduledDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("locationId", StandardBasicTypes.INTEGER)
                .addScalar("memberId", StandardBasicTypes.INTEGER)
                .addScalar("familyId", StandardBasicTypes.INTEGER)
                .addScalar("otherDetails", StandardBasicTypes.STRING)
                .addScalar("migrationId", StandardBasicTypes.INTEGER)
                .addScalar("overdueFlag", StandardBasicTypes.BOOLEAN)
                .addScalar("customField", StandardBasicTypes.STRING)
                .addScalar("expiryDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("modifiedOn", StandardBasicTypes.TIMESTAMP)
                .addScalar("state", StandardBasicTypes.STRING)
                .addScalar("header", StandardBasicTypes.STRING)
                .addScalar("task", StandardBasicTypes.STRING);
        q.setParameter(TechoNotificationMaster.Fields.USER_ID, userId);
        q.setParameter("roleId", roleId);
        q.setParameter("lastModifiedOn", lastModifiedOn);
        return q.setResultTransformer(Transformers.aliasToBean(TechoNotificationDataBean.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markNotificationAsCompleted(Integer notificationId, Integer userId) {
        String query = "update techo_notification_master set state = 'COMPLETED', \n"
                + "action_by = :userId, modified_by = :userId, modified_on = now() \n"
                + "where id = :notificationId";

        NativeQuery<Integer> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("notificationId", notificationId);
        nativeQuery.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TechoNotificationMaster> retrieveNotificationForMigration(Integer memberId, Integer migrationId, Integer notificationTypeId, Enum state) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<TechoNotificationMaster> cq = cb.createQuery(TechoNotificationMaster.class);
        Root<TechoNotificationMaster> root = cq.from(TechoNotificationMaster.class);

        cq.select(root).where(cb.and(
                cb.equal(root.get(TechoNotificationMaster.Fields.STATE), state),
                cb.equal(root.get(TechoNotificationMaster.Fields.MEMBER_ID), memberId),
                cb.equal(root.get(TechoNotificationMaster.Fields.MIGRATION_ID), migrationId),
                cb.equal(root.get(TechoNotificationMaster.Fields.NOTIFICATION_TYPE_ID), notificationTypeId)
        ));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<TechoNotificationMaster> retrieveNotificationByTypeAndMemberIdAndState(Integer memberId, List<Integer> notificationTypeIds, TechoNotificationMaster.State state) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<TechoNotificationMaster> criteriaQuery = criteriaBuilder.createQuery(TechoNotificationMaster.class);
        Root<TechoNotificationMaster> root = criteriaQuery.from(TechoNotificationMaster.class);
        Predicate memberIdEqual = criteriaBuilder.equal(root.get(TechoNotificationMaster.Fields.MEMBER_ID), memberId);
        Predicate stateEqual = criteriaBuilder.equal(root.get(TechoNotificationMaster.Fields.STATE), state);
        Predicate notificationIdIn = criteriaBuilder.in(root.get(TechoNotificationMaster.Fields.NOTIFICATION_TYPE_ID)).value(notificationTypeIds);
        criteriaQuery.select(root).where(criteriaBuilder.and(memberIdEqual, stateEqual, notificationIdIn));
        return session.createQuery(criteriaQuery).getResultList();
    }
}
