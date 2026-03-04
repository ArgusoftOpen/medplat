/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.event.util;

import com.argusoft.medplat.event.dto.EventConfigurationDto;
import com.argusoft.medplat.event.model.EventConfiguration;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Define utility methods for events.
 * </p>
 *
 * @author Harshit
 * @since 26/08/20 11:00 AM
 */
public class EventFunctionUtil {

    private EventFunctionUtil() {
        throw new IllegalStateException("Utility Class");
    }

    /**
     * Replace parameters with its value.
     *
     * @param template                Define template in which value will be assigned.
     * @param templateParameterString Contains parameters.
     * @param parameterValue          Contains value of parameters.
     * @return Returns template with value.
     */
    public static String replaceParameterWithValue(String template, String templateParameterString, Map<String, Object> parameterValue) {
        String parameterReplacedTemplateString = template;
        if (templateParameterString != null) {
            String[] templateParameters = templateParameterString.split(",");
            for (String templateParameter : templateParameters) {
                Object value = parameterValue.get(templateParameter);
                String val = value == null ? "null" : value.toString();
                parameterReplacedTemplateString = parameterReplacedTemplateString.replace("#" + templateParameter + "#", val);
            }
        }
        return parameterReplacedTemplateString;
    }

    /**
     * Replace user id and other parameters with its value.
     *
     * @param template                Define template in which value will be assigned.
     * @param templateParameterString Contains parameters.
     * @param parameterValue          Contains value of parameters.
     * @param userId                  User id.
     * @return Returns template with value.
     */
    public static String replaceParamWithValue(String template, String templateParameterString, Map<String, Object> parameterValue, int userId) {
        String parameterReplacedTemplateString = template;
        if (Objects.nonNull(templateParameterString) && !templateParameterString.isEmpty()) {
            String[] templateParameters = templateParameterString.split(",");
            for (String templateParameter : templateParameters) {
                Object value = parameterValue.get(templateParameter);
                if (templateParameter.equals("loggedInUserId") && userId != 0) {
                    parameterReplacedTemplateString = parameterReplacedTemplateString.replace("#" + templateParameter + "#", String.valueOf(userId));
                } else if (Objects.isNull(value)) {
                    parameterReplacedTemplateString = parameterReplacedTemplateString.replace("#" + templateParameter + "#", "null");
                    parameterValue.remove(templateParameter);
                } else {
                    parameterReplacedTemplateString = parameterReplacedTemplateString.replace("#" + templateParameter + "#", " :" + templateParameter + " ");
                }
            }
        }
        return parameterReplacedTemplateString;
    }


    /**
     * Find parameters from template.
     *
     * @param template Define template in which value will be assigned.
     * @return Returns parameters.
     */
    public static String findParamsFromTemplate(String template) {
        String paramList = null;
        if (template != null) {
            Pattern myPattern = Pattern.compile("#(.*?)#");
            Matcher mat = myPattern.matcher(template);
            Set<String> strs = new HashSet<>();
            while (mat.find()) {
                strs.add(mat.group(1));
            }

            if (!CollectionUtils.isEmpty(strs)) {
                paramList = String.join(",", strs);
            }
        }
        return paramList;
    }

    /**
     * Retrieves next date for events.
     *
     * @param trigerWhen                   Triger when date.
     * @param notificationConfigurationDto Notification configuration details.
     * @return Returns next date for events.
     */
    public static Date getNextDate(EventConfiguration.TriggerWhen trigerWhen, EventConfigurationDto notificationConfigurationDto) {
        Calendar cal = Calendar.getInstance();
        switch (trigerWhen) {
            case MONTHLY:
                cal.add(Calendar.MONTH, 1);
                cal.set(Calendar.DAY_OF_MONTH, notificationConfigurationDto.getDay());
                if (notificationConfigurationDto.getHour() != null) {
                    cal.set(Calendar.HOUR_OF_DAY, notificationConfigurationDto.getHour());
                } else {
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                }
                if (notificationConfigurationDto.getMinute() != null) {
                    cal.set(Calendar.MINUTE, notificationConfigurationDto.getMinute());
                } else {
                    cal.set(Calendar.MINUTE, 0);
                }
                cal.set(Calendar.SECOND, 0);
                break;
            case DAILY:
                cal.add(Calendar.DAY_OF_MONTH, setNextDateForDailyTriggerWhen(notificationConfigurationDto, cal));
                if (notificationConfigurationDto.getHour() != null) {
                    cal.set(Calendar.HOUR_OF_DAY, notificationConfigurationDto.getHour());
                } else {
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                }
                if (notificationConfigurationDto.getMinute() != null) {
                    cal.set(Calendar.MINUTE, notificationConfigurationDto.getMinute());
                } else {
                    cal.set(Calendar.MINUTE, 0);
                }
                cal.set(Calendar.SECOND, 0);
                break;
            case HOURLY:
                cal.add(Calendar.HOUR_OF_DAY, 1);
                cal.set(Calendar.MINUTE, setNextDateForHourlyTriggerWhen(notificationConfigurationDto));
                cal.set(Calendar.SECOND, 0);
                break;
            case MINUTE:
                cal.add(Calendar.MINUTE, notificationConfigurationDto.getMinute());
                cal.set(Calendar.SECOND, 0);
                break;
            default:
        }
        return cal.getTime();
    }

    /**
     * Set next date for hourly trigger when type.
     *
     * @param notificationConfigurationDto Event details.
     * @return Returns next date.
     */
    private static Short setNextDateForHourlyTriggerWhen(EventConfigurationDto notificationConfigurationDto) {
        if (notificationConfigurationDto.getMinute() != null) {
            return notificationConfigurationDto.getMinute();
        } else {
            return 0;
        }
    }

    /**
     * Set next date for daily trigger when type.
     *
     * @param notificationConfigurationDto Event details.
     * @param cal                          Calendar details.
     * @return Returns next date.
     */
    private static short setNextDateForDailyTriggerWhen(EventConfigurationDto notificationConfigurationDto, Calendar cal) {
        if (notificationConfigurationDto.getHour() > cal.get(Calendar.HOUR_OF_DAY)
                || (cal.get(Calendar.HOUR_OF_DAY) == notificationConfigurationDto.getHour() && notificationConfigurationDto.getMinute() > cal.get(Calendar.MINUTE))) {
            return 0;
        } else {
            return 1;
        }
    }
}
