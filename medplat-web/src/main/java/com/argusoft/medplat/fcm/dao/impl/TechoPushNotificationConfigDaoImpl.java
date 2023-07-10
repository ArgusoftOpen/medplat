package com.argusoft.medplat.fcm.dao.impl;

import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import com.argusoft.medplat.fcm.dao.TechoPushNotificationConfigDao;
import com.argusoft.medplat.fcm.dto.TechoPushNotificationDisplayDto;
import com.argusoft.medplat.fcm.model.TechoPushNotificationConfig;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.EnumType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

/**
 * @author nihar
 * @since 14/10/22 3:53 PM
 */
@Repository
@Transactional
public class TechoPushNotificationConfigDaoImpl extends
        GenericDaoImpl<TechoPushNotificationConfig, Integer>
        implements TechoPushNotificationConfigDao {

    private static final String ENUM_CLASS = "enumClass";

    @Override
    public List<TechoPushNotificationDisplayDto> getNotificationConfig(BigInteger limit, Integer offset) {
        String query = "\n" +
                "select tpncm.id,name,tpncm.description,config_type as configType,\n" +
                "\ttrigger_type as triggerType,tpncm.status,tpncm.state , tpnt.type as notificationType   \n" +
                "\tfrom techo_push_notification_config_master tpncm \n" +
                "\tleft join techo_push_notification_type tpnt on \n" +
                "\t\ttpncm.notification_type_id =tpnt.id limit " + limit + " offset " + offset;

        Session session = sessionFactory.getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(query);

        Properties configTypeParams = new Properties();
        configTypeParams.put(ENUM_CLASS, TechoPushNotificationConfig.ConfigType.class.getName());
        configTypeParams.put("type", "12");// EnumType.STRING type = 12
        Type configTypeEnum = sessionFactory.getTypeHelper().custom(EnumType.class, configTypeParams);

        Properties triggerTypeParams = new Properties();
        triggerTypeParams.put(ENUM_CLASS, TechoPushNotificationConfig.TRIGGER_TYPE.class.getName());
        triggerTypeParams.put("type", "12");// EnumType.STRING type = 12
        Type triggerTypeEnum = sessionFactory.getTypeHelper().custom(EnumType.class, triggerTypeParams);

        Properties statusParams = new Properties();
        statusParams.put(ENUM_CLASS, TechoPushNotificationConfig.Status.class.getName());
        statusParams.put("type", "12");// EnumType.STRING type = 12
        Type statusEnum = sessionFactory.getTypeHelper().custom(EnumType.class, statusParams);

        Properties stateParams = new Properties();
        stateParams.put(ENUM_CLASS, TechoPushNotificationConfig.State.class.getName());
        stateParams.put("type", "12");// EnumType.STRING type = 12
        Type stateEnum = sessionFactory.getTypeHelper().custom(EnumType.class, stateParams);


        sqlQuery.addScalar("id", StandardBasicTypes.INTEGER)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("notificationType", StandardBasicTypes.STRING)
                .addScalar("configType", configTypeEnum)
                .addScalar("triggerType", triggerTypeEnum)
                .addScalar("status", statusEnum)
                .addScalar("state", stateEnum)
                .setResultTransformer(Transformers.aliasToBean(TechoPushNotificationDisplayDto.class));

        return sqlQuery.list();
    }
}
