/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.dao.impl;

import com.argusoft.medplat.database.common.PredicateBuilder;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.event.dao.EventConfigurationDao;
import com.argusoft.medplat.event.dto.EventConfigurationDto;
import com.argusoft.medplat.event.model.EventConfiguration;
import com.argusoft.medplat.web.users.model.UserMaster;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.EnumType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 *
 * <p>
 * Implementation of methods define in event configuration dao.
 * </p>
 *
 * @author vaishali
 * @since 26/08/20 10:19 AM
 */
@Repository
public class EventConfigurationDaoImpl extends GenericDaoImpl<EventConfiguration, Integer> implements EventConfigurationDao {

    private static final String ENUMCLASS="enumClass";

    /**
     * {@inheritDoc}
     */
    @Override
    public EventConfiguration retrieveById(Integer id) {
        Properties stateEnumParams = new Properties();
        stateEnumParams.put(ENUMCLASS, EventConfiguration.State.class.getName());
        stateEnumParams.put("type", "12");
        Type stateEnum = sessionFactory.getTypeHelper().custom(EnumType.class, stateEnumParams);

        Properties triggerWhenEnumParams = new Properties();
        triggerWhenEnumParams.put(ENUMCLASS, EventConfiguration.TriggerWhen.class.getName());
        triggerWhenEnumParams.put("type", "12");
        Type triggerWhenEnum = sessionFactory.getTypeHelper().custom(EnumType.class, triggerWhenEnumParams);

        String query = "select\n" +
                "id as \"id\",\n" +
                "uuid as \"uuid\",\n" +
                "day as \"day\",\n" +
                "description as \"description\",\n" +
                "event_type as \"eventType\",\n" +
                "event_type_detail_id as \"eventTypeDetailId\",\n" +
                "form_type_id as \"formTypeId\",\n" +
                "hour as \"hour\",\n" +
                "minute as \"minute\",\n" +
                "name as \"name\",\n" +
                "config_json as \"notificationConfigurationDetailJson\",\n" +
                "state as \"state\",\n" +
                "trigger_when as \"triggerWhen\",\n" +
                "event_type_detail_code as \"eventTypeDetailCode\",\n" +
                "created_by as \"createdBy\",\n" +
                "created_on as \"createdOn\",\n" +
                "modified_by as \"modifiedBy\",\n" +
                "modified_on as \"modifiedOn\"\n" +
                "from event_configuration\n" +
                "where id = :id ;";
        NativeQuery<EventConfiguration> nativeQuery = getCurrentSession().createNativeQuery(query);
        nativeQuery.setParameter("id", id)
                .addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("uuid",StandardBasicTypes.UUID_CHAR)
                .addScalar("day", StandardBasicTypes.SHORT)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("eventType", StandardBasicTypes.STRING)
                .addScalar("eventTypeDetailId", StandardBasicTypes.INTEGER)
                .addScalar("formTypeId", StandardBasicTypes.INTEGER)
                .addScalar("hour", StandardBasicTypes.SHORT)
                .addScalar("minute", StandardBasicTypes.SHORT)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("notificationConfigurationDetailJson", StandardBasicTypes.STRING)
                .addScalar("state", stateEnum)
                .addScalar("triggerWhen", triggerWhenEnum)
                .addScalar("eventTypeDetailCode", StandardBasicTypes.STRING)
                .addScalar("createdBy", StandardBasicTypes.INTEGER)
                .addScalar("createdOn", StandardBasicTypes.TIMESTAMP)
                .addScalar("modifiedBy", StandardBasicTypes.INTEGER)
                .addScalar("modifiedOn", StandardBasicTypes.TIMESTAMP);

        return nativeQuery.setResultTransformer(Transformers.aliasToBean(EventConfiguration.class)).uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventConfiguration retrieveByUUID(UUID uuid) {
        PredicateBuilder<EventConfiguration> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(EventConfiguration.Fields.UUID), uuid));
            return predicates;
        };
        return super.findEntityByCriteriaList(predicateBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventConfigurationDto> retrieveAll(Boolean isActive) {
        Session session = sessionFactory.getCurrentSession();
        String sqlForActive = "";
        if (Boolean.TRUE.equals(isActive)) {
            sqlForActive = "where event_conf.state = 'ACTIVE'";
        } else if (Boolean.FALSE.equals(isActive)) {
            sqlForActive = "where event_conf.state = 'INACTIVE'";
        }
        NativeQuery<EventConfigurationDto> query = session.createNativeQuery("select  "
                + "event_conf.id , cast(event_conf.uuid as text) as uuid, event_conf.name, event_conf.description , event_conf.event_type as \"eventType\",event_conf.event_type_detail_id as \"eventTypeDetailId\","
                + "event_conf.form_type_id as \"formTypeId\", event_conf.event_type_detail_code as \"eventTypeDetailCode\", event_conf.trigger_when as \"trigerWhen\", event_conf.day,"
                + "event_conf.hour, event_conf.minute, event_conf.state, event_conf.created_by as \"createdBy\", event_conf.created_on \"createdOn\", event_conf.config_json as \"configJson\","
                + "event_timer.status , event_timer.completed_on as \"completionTime\" from event_configuration event_conf left join  (select event_config_id , status ,completed_on  "
                + "from timer_event where id in (select max(id) as id from timer_event where status !='NEW' group by  event_config_id )) event_timer on event_conf.id = event_timer.event_config_id "
                + sqlForActive
                + ";");

        Properties stateParams = new Properties();
        stateParams.put(ENUMCLASS, EventConfiguration.State.class.getName());
        stateParams.put("type", "12");
        Type stateEnum = sessionFactory.getTypeHelper().custom(EnumType.class, stateParams);
        Properties params = new Properties();
        params.put(ENUMCLASS, EventConfiguration.TriggerWhen.class.getName());
        params.put("type", "12");
        Type trigerWhenEnumType = sessionFactory.getTypeHelper().custom(EnumType.class, params);

        query.addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("uuid",StandardBasicTypes.UUID_CHAR)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("eventType", StandardBasicTypes.STRING)
                .addScalar("eventTypeDetailId", StandardBasicTypes.INTEGER)
                .addScalar("formTypeId", StandardBasicTypes.INTEGER)
                .addScalar("trigerWhen", trigerWhenEnumType)
                .addScalar("day", StandardBasicTypes.SHORT)
                .addScalar("hour", StandardBasicTypes.SHORT)
                .addScalar("minute", StandardBasicTypes.SHORT)
                .addScalar("state", stateEnum)
                .addScalar("createdBy", StandardBasicTypes.INTEGER)
                .addScalar("createdOn", StandardBasicTypes.DATE)
                .addScalar("status", StandardBasicTypes.STRING)
                .addScalar("completionTime", StandardBasicTypes.TIMESTAMP)
                .addScalar("configJson", StandardBasicTypes.STRING);
        return  query.setResultTransformer(Transformers.aliasToBean(EventConfigurationDto.class)).list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<EventConfiguration> retrieveEventConfigsByEventTypeAndEventTypeDetailCode(String eventType, String eventTypeDetailCode) {
        PredicateBuilder<EventConfiguration> predicateBuilder = (root, builder, query) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get(EventConfiguration.Fields.EVENT_TYPE), eventType));
            if (eventTypeDetailCode != null) {
                predicates.add(builder.equal(root.get(EventConfiguration.Fields.EVENT_TYPE_DETAIL_CODE), eventTypeDetailCode));
            }
            predicates.add(builder.equal(root.get(UserMaster.Fields.STATE), UserMaster.State.ACTIVE));
            return predicates;
        };

        return findByCriteriaList(predicateBuilder);
    }

}
