/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.notification.dao.impl;

import com.argusoft.medplat.dashboard.task.dto.WebTaskMasterDto;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.notification.dao.TechoWebNotificationMasterDao;
import com.argusoft.medplat.notification.model.TechoWebNotificationMaster;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * <p>
 * Implementation of methods define in techo web notification master dao.
 * </p>
 *
 * @author kunjan
 * @since 26/08/20 10:19 AM
 */
@Repository
public class TechoWebNotificationMasterDaoImpl extends GenericDaoImpl<TechoWebNotificationMaster, Integer> implements TechoWebNotificationMasterDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WebTaskMasterDto> getWebTaskCount(Integer userId) {
        String query = "with role_id as (select role_id from um_user where id = :userId )\n"
                + "select ntm.id as \"id\",\n"
                + "sum(case when twn.id is not null then 1 else 0 end) as \"count\",\n"
                + "sum(case when twn.due_on < now() then 1 else 0 end) as \"dueCount\",\n"
                + "ntm.name as \"name\",\n"
                + "ntm.color_code as \"colorCode\",\n"
                + "ntm.is_location_filter_required as \"isLocationBasedFilterRequired\",\n"
                + "ntm.fetch_up_to_level as \"fetchUptoLevel\",\n"
                + "ntm.required_up_to_level as \"requiredUptoLevel\",\n"
                + "ntm.is_fetch_according_aoi as \"isFetchAccordingAOI\",\n"
                + "ntm.order_no as \"orderNo\"\n"
                + "from (select distinct CONCAT(nt.id,'_',el.id) as notification_esc_id,nt.id \n"
                + "from notification_type_master nt, escalation_level_master el,\n"
                + "escalation_level_role_rel elr where nt.id in \n"
                + "(select notification_type_id from notification_type_role_rel where role_id in \n"
                + "(select * from role_id))\n"
                + "and el.id in (select escalation_level_id from escalation_level_role_rel \n"
                + "where role_id in (select * from role_id)) and nt.id = el.notification_type_id) as t1\n"
                + "left join notification_type_master ntm on t1.id = ntm.id\n"
                + "left join techo_web_notification_master twn on t1.notification_esc_id = twn.notification_type_escalation_id and twn.location_id in \n"
                + "(select child_id from location_hierchy_closer_det where parent_id in \n"
                + "(select loc_id from um_user_location  where user_id  = :userId and state = 'ACTIVE')) and twn.state = 'PENDING' \n"
                + "group by ntm.id,ntm.name;";

        NativeQuery<WebTaskMasterDto> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.addScalar("id", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("count", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("dueCount", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("name", StandardBasicTypes.STRING);
        nativeQuery.addScalar("colorCode", StandardBasicTypes.STRING);
        nativeQuery.addScalar("orderNo", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("isLocationBasedFilterRequired", StandardBasicTypes.BOOLEAN);
        nativeQuery.addScalar("fetchUptoLevel", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("requiredUptoLevel", StandardBasicTypes.INTEGER);
        nativeQuery.addScalar("isFetchAccordingAOI", StandardBasicTypes.BOOLEAN);
        nativeQuery.setParameter("userId", userId);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(WebTaskMasterDto.class)).list();
    }
    @Override
    public void markNotificationAsCompleted(Integer notificationId, Integer userId) {
        String query = "update techo_web_notification_master set state = 'COMPLETED', \n"
                + "action_by = :userId, modified_by = :userId, modified_on = now() \n"
                + "where id = :notificationId";

        NativeQuery<Integer> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("userId", userId);
        nativeQuery.setParameter("notificationId", notificationId);
        nativeQuery.executeUpdate();
    }

    @Override
    public boolean checkIfMemberNotificationExists(Integer notificationId, Integer memberId) {

        String query = "select * from techo_web_notification_master " +
                "where id = :notificationId and member_id = :memberId " +
                "and state = 'COMPLETED'";
        NativeQuery<WebTaskMasterDto> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("notificationId", notificationId);
        nativeQuery.setParameter("memberId",memberId);
        List<WebTaskMasterDto> result = nativeQuery.getResultList();
        return result.isEmpty();
    }
}
