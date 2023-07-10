/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.service;

import com.argusoft.medplat.common.service.EmailService;
import com.argusoft.medplat.common.service.SmsService;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.event.dto.EventConfigTypeDto;
import com.argusoft.medplat.event.dto.QueryMasterParamterDto;
import com.argusoft.medplat.event.model.EventConfigurationType;
import com.argusoft.medplat.fcm.service.TechoPushNotificationService;
import com.argusoft.medplat.notification.service.MobileNotificationHandler;
import com.argusoft.medplat.query.service.TableService;
import com.argusoft.medplat.systemfunction.FunctionParametersDto;
import com.argusoft.medplat.systemfunction.model.SystemFunctionMaster;
import com.argusoft.medplat.systemfunction.service.SystemFunctionMasterService;
import com.argusoft.medplat.timer.dto.TimerEventDto;
import com.argusoft.medplat.timer.model.TimerEvent;
import com.argusoft.medplat.timer.service.TimerEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * <p>
 *     Define methods for notification handler.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
@Service("NotificationHandler")
@Transactional // requirement of this could be seen later
public class NotificationHandler {

    @Autowired
    private SmsService smsService;

    @Autowired
    private MobileNotificationHandler mobileNotificationHandler;

    @Autowired
    @Qualifier("timerEventServiceDefault")
    private TimerEventService timerEventService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TableService tableService;

    @Autowired
    private SystemFunctionMasterService systemFunctionMasterService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TechoPushNotificationService techoPushNotificationService;

    /**
     * Handle notification.
     * @param notificationConfigTypeDto Notification config type details.
     * @param queryDataLists List of query data.
     */
    public void handle(EventConfigTypeDto notificationConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists) {
        switch (notificationConfigTypeDto.getType()) {
            case SMS:
                if (notificationConfigTypeDto.getTrigerWhen().equals(EventConfigurationType.TriggerWhen.IMMEDIATELY)) {
                    smsService.handle(notificationConfigTypeDto, queryDataLists);
                } else {
                    Date trigerOnDate = getTriggerOnDate(notificationConfigTypeDto);
                    timerEventService.scheduleTimerEvent(new TimerEventDto(null,
                            TimerEvent.TYPE.SMS, trigerOnDate, null, notificationConfigTypeDto.getId(), queryDataLists));
                }
                break;
            case EMAIL:
                if (notificationConfigTypeDto.getTrigerWhen().equals(EventConfigurationType.TriggerWhen.IMMEDIATELY)) {
                    emailService.handle(notificationConfigTypeDto, queryDataLists);
                } else {
                    Date trigerOnDate = getTriggerOnDate(notificationConfigTypeDto);
                    timerEventService.scheduleTimerEvent(new TimerEventDto(null,
                            TimerEvent.TYPE.EMAIL, trigerOnDate, null, notificationConfigTypeDto.getId(), queryDataLists));
                }
                break;
            case QUERY:
                if (notificationConfigTypeDto.getTrigerWhen().equals(EventConfigurationType.TriggerWhen.IMMEDIATELY)) {
                    tableService.queryHandler(notificationConfigTypeDto, queryDataLists);
                } else {
                    Date trigerOnDate = getTriggerOnDate(notificationConfigTypeDto);
                    timerEventService.scheduleTimerEvent(new TimerEventDto(null,
                            TimerEvent.TYPE.QUERY, trigerOnDate, null, notificationConfigTypeDto.getId(), queryDataLists));
                }
                break;
            case MOBILE:
                mobileNotificationHandler.handle(notificationConfigTypeDto, queryDataLists);
                break;
            case SYSTEM_FUNCTION:
                this.systemFunctionCallHandler(notificationConfigTypeDto, queryDataLists);
                break;
            case PUSH_NOTIFICATION:
                if (notificationConfigTypeDto.getTrigerWhen().equals(EventConfigurationType.TriggerWhen.IMMEDIATELY)) {
                    techoPushNotificationService.handle(notificationConfigTypeDto, queryDataLists);
                } else {
                    Date trigerOnDate = getTriggerOnDate(notificationConfigTypeDto);
                    timerEventService.scheduleTimerEvent(new TimerEventDto(null,
                            TimerEvent.TYPE.PUSH_NOTIFICATION, trigerOnDate, null, notificationConfigTypeDto.getId(), queryDataLists));
                }
                break;
            default:

        }
    }

    /**
     * Handle system function call.
     * @param notificationConfigTypeDto Notification config type details.
     * @param queryDataLists List of query data.
     */
    public void systemFunctionCallHandler(EventConfigTypeDto notificationConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists) {

        for (LinkedHashMap<String, Object> queryDataList : queryDataLists) {
            if (!CollectionUtils.isEmpty(notificationConfigTypeDto.getFunctionParams())) {
                for (QueryMasterParamterDto functionMasterParamterDto : notificationConfigTypeDto.getFunctionParams()) {
                    if (Boolean.FALSE.equals(functionMasterParamterDto.getIsFixed())) {
                        Object value = queryDataList.get(functionMasterParamterDto.getMappingValue());
                        String val = value == null ? "null" : value.toString();
                        functionMasterParamterDto.setMappingValue(val);

                    }
                }
            }
            this.callSystemFunction(notificationConfigTypeDto);
        }

    }

    /**
     * Call system function.
     * @param notificationDto Notification details.
     */
    public void callSystemFunction(EventConfigTypeDto notificationDto) {

        SystemFunctionMaster systemFunction = this.systemFunctionMasterService.retrieveSystemFunctionById(notificationDto.getSystemFunctionId());
        String className = systemFunction.getClassName();
        String methodName = systemFunction.getName();

        try {
            Class<?> c = Class.forName(className);
            Object bean = this.applicationContext.getBean(c);
            List<FunctionParametersDto> parameters = ImtechoUtil.convertJsonToFunctionParametersDto(systemFunction.getParameters());
            List<Class<?>> parameterClasses = new ArrayList<>();
            Class[] parameterClassesArray = new Class[parameters.size()];
            if (!parameters.isEmpty()) {
                for (FunctionParametersDto parameter : parameters) {
                    if (parameter.getParameterType() != null) {
                        Class<?> parameterClass = Class.forName(parameter.getParameterType());
                        parameterClasses.add(parameterClass);
                    }
                }
            }
            Method method = bean.getClass().getDeclaredMethod(methodName, parameterClasses.toArray(parameterClassesArray));
            List<Object> paramValues = new ArrayList<>();
            for (QueryMasterParamterDto paramDto : notificationDto.getFunctionParams()) {
                Class<?> parameterType = this.findParameterType(paramDto.getParameterName(), parameters);
                paramValues.add(ImtechoUtil.convertStringToGivenType(paramDto.getMappingValue(), parameterType));
            }

            method.invoke(bean, paramValues.toArray());
        } catch (ReflectiveOperationException | ParseException exception) {
            Logger.getLogger(NotificationHandler.class.getName()).log(Level.INFO, exception.getMessage(), exception);
        }
    }

    /**
     * Find type of parameter.
     * @param parameterName Parameter name.
     * @param parameters List of parameters.
     * @return Returns type of parameter.
     * @throws ClassNotFoundException Exception occurred when class not found.
     */
    private Class<?> findParameterType(String parameterName, List<FunctionParametersDto> parameters) throws ClassNotFoundException {
        for (FunctionParametersDto param : parameters) {
            if (param.getParameterName().equals(parameterName)) {
                return Class.forName(param.getParameterType());
            }
        }
        return null;
    }

    /**
     * Retrieves trigger date.
     * @param notificationConfigTypeDto Notification config type details.
     * @return Returns trigger date.
     */
    private Date getTriggerOnDate(EventConfigTypeDto notificationConfigTypeDto) {
        int milisecondtoadd = 0;
        if (notificationConfigTypeDto.getDay() != null) {
            milisecondtoadd += (notificationConfigTypeDto.getDay() * (1000 * 60 * 60 * 24));
        }
        if (notificationConfigTypeDto.getHour() != null) {
            milisecondtoadd += (notificationConfigTypeDto.getHour() * (1000 * 60 * 60));
        }
        if (notificationConfigTypeDto.getMiniute() != null) {
            milisecondtoadd += (notificationConfigTypeDto.getMiniute() * (1000 * 60));
        }
        return new Date(new Date().getTime() + milisecondtoadd);
    }

}
