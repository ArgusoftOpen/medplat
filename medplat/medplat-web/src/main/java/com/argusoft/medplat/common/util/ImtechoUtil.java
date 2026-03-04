package com.argusoft.medplat.common.util;

import com.argusoft.medplat.event.dto.EventConfigurationDetailDto;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.systemfunction.FunctionParametersDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import org.joda.time.Period;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An util class for common methods
 * @author akshar
 * @since 31/08/2020 4:30
 */
public class ImtechoUtil {

    public static final String INTEGER = "java.lang.Integer";
    public static final String STRING = "java.lang.String";
    public static final String LONG = "java.lang.Long";
    public static final String FLOAT = "java.lang.Float";
    public static final String DOUBLE = "java.lang.Double";
    public static final String SHORT = "java.lang.Short";
    public static final String DATE = "java.util.Date";
    public static final String BOOLEAN = "java.lang.Boolean";


    /**
     * Returns date object based on given parameter
     * @param input An instance of Date
     * @param hours A value for hours
     * @param minutes A value for minutes
     * @param seconds A value for seconds
     * @param milliseconds A value for hour milliseconds
     * @return An instance of date
     */
    public static Date prepareDate(Date input,
                                   int hours,
                                   int minutes,
                                   int seconds,
                                   int milliseconds)
             {
        if (input == null) {
            throw new ImtechoUserException("Please provide valid DATE", 0);
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(input);
        instance.set(Calendar.HOUR_OF_DAY, hours);
        instance.set(Calendar.MINUTE, minutes);
        instance.set(Calendar.SECOND, seconds);
        instance.set(Calendar.MILLISECOND, milliseconds);
        return instance.getTime();
    }


    public static final JsonSerializer<Date> jsonDateSerializer = (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.getTime());

    public static final JsonDeserializer<Date> jsonDateDeserializer = (json, typeOfT, context) -> json == null ? null : new Date(json.getAsLong());

    /**
     * Converts json to date
     */
    public static final JsonDeserializer<Date> jsonDateDeserializerStringFormat = (json, typeOfT, context) -> {

        if (json == null || json.getAsString().isEmpty()) {
            return null;
        } else {
            Timestamp ts = new Timestamp(Long.parseLong(json.getAsString()));
            return new Date(ts.getTime());

        }

    };

    /**
     * Calculates new date excluding sunday
     * @param effectiveDate A value for effective date
     * @param noOfDays A value for number of days
     * @return An instance of Date
     */
    public static Date calculateNewDateExcludingSunday(Date effectiveDate,
                                                       int noOfDays)
             {
        if (noOfDays == 0) {
            throw new ImtechoUserException("Please provide valid training duration.!", 1);
        }
        if (noOfDays > 0) {
            Calendar instance = Calendar.getInstance();
            instance.setTime(effectiveDate);
            for (int dayCounter = 1; dayCounter < noOfDays; dayCounter++) {
                instance.add(Calendar.DAY_OF_MONTH, 1);
                int day = instance.get(Calendar.DAY_OF_WEEK);
                if (day == Calendar.SUNDAY) {
                    instance.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
            return instance.getTime();
        } else {
            Calendar instance = Calendar.getInstance();
            instance.setTime(effectiveDate);
            noOfDays *= -1;
            for (int dayCounter = 1; dayCounter < noOfDays; dayCounter++) {
                instance.add(Calendar.DAY_OF_MONTH, -1);
                int day = instance.get(Calendar.DAY_OF_WEEK);
                if (day == Calendar.SUNDAY) {
                    instance.add(Calendar.DAY_OF_MONTH, -1);
                }
            }
            return instance.getTime();
        }
    }

    /**
     * Calculates working days between two dates
     * @param startDate A value of start date
     * @param endDate A value of end date
     * @return Number of days
     */
    public static int calculateWorkingDaysBetweenTwoDates(Date startDate,
                                                          Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return workDays;
    }

    /**
     * Calculates percentage based on given parameter
     * @param totalValue A total vale
     * @param value A value
     * @return A percentage value
     */
    public static float calculatePercentageFloat(int totalValue, int value) {
        return (float) Math.round(((value * 100.00) / totalValue) * 100) / 100;
    }

    /**
     * Calculates value from percentage
     * @param number A int value
     * @param percentage A percentage value
     * @return A calculated value
     */
    public static long calculateValueByPercentage(int number, double percentage) {
        double value = (number * percentage) / 100;
        return Math.round(Math.ceil(value));
    }

    /**
     * Replaced implicit parameters in query
     * @param query A query
     * @param userId A user id
     * @return A replaced query
     */
    public static String replaceImplicitParametersFromQuery(String query, Integer userId) {
        if (query.contains(SystemConstantUtil.IMPLICIT_PARAMETERS_MAP.get(SystemConstantUtil.USER_ID_IMPLICIT_PARAMETER))) {
            query = query.replaceAll(Pattern.quote(SystemConstantUtil.IMPLICIT_PARAMETERS_MAP.get(SystemConstantUtil.USER_ID_IMPLICIT_PARAMETER)), userId + "");
        }
        return query;
    }

    /**
     * Replaced implicit parameters in query
     * @param query A query
     * @param paramsMap A map of param and value
     * @return A replaced query
     */
    public static String replaceImplicitParametersFromQuery(String query, Map<String, Object> paramsMap) {
        for (Map.Entry<String, Object> entrySet : paramsMap.entrySet()) {
            if (query.contains(SystemConstantUtil.HASH + entrySet.getKey() + SystemConstantUtil.HASH)) {
                String value = entrySet.getValue() != null ? entrySet.getValue().toString() : "null";
                query = query.replaceAll(SystemConstantUtil.HASH + entrySet.getKey() + SystemConstantUtil.HASH, value);
            }
        }

        return query;
    }

    /**
     * Replaced implicit parameters in query
     * @param query A query
     * @param parameterMap A map of param and json node value
     * @return A replaced query
     */
    public static String replaceParameterInQuery(String query, Map<String, JsonNode> parameterMap) {
        for (Map.Entry<String, JsonNode> entrySet : parameterMap.entrySet()) {
            String value = entrySet.getValue() != null ? entrySet.getValue().asText() : "null";
            query = query.replaceAll(Pattern.quote(SystemConstantUtil.HASH + entrySet.getKey() + SystemConstantUtil.HASH), value);
        }
        return query;
    }

    /**
     * Replaced implicit parameters in query
     * @param query A query
     * @param parameterMap A map of param and json node value
     * @return A replaced query
     */
    public static String replaceParameterInQueryByParamMap(String query, Map<String, String> parameterMap) {
        Pattern regex = Pattern.compile("#(.*?)#");
        Matcher matcher = regex.matcher(query);
        List<String> listMatches = new ArrayList<>();
        while (matcher.find()) {
            listMatches.add(matcher.group(1));
        }
        for (String parameter : listMatches) {
            if (parameterMap.get(parameter) != null) {
                query = query.replaceAll(Pattern.quote(SystemConstantUtil.HASH + parameter + SystemConstantUtil.HASH), parameterMap.get(parameter));
            } else if (!parameter.equals("limit_offset")) {
                query = query.replaceAll(Pattern.quote(SystemConstantUtil.HASH + parameter + SystemConstantUtil.HASH), "null");

            }
        }

        return query;
    }

    /**
     * Converts json to string
     * @param json An instance of JsonNode
     * @return A converted string
     */
    public static String convertJsonToString(JsonNode json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (json != null) {
                return mapper.writeValueAsString(json);
            }
        } catch (IOException ex) {
            Logger.getLogger(ImtechoUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Converts string to json
     * @param jsonString A string to be converted
     * @return An instance of JsonNode
     */
    public static JsonNode convertStringToJson(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (jsonString != null) {
                return mapper.readTree(jsonString);
            }
        } catch (IOException ex) {
            Logger.getLogger(ImtechoUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Method to calculate Age based on given DOB
     * @param dob An instance of Date
     * @return An age value
     */
    public static int calculateAge(Date dob) {
        Calendar now = Calendar.getInstance();
        Calendar dobCal = Calendar.getInstance();
        dobCal.setTime(dob);
        int year1 = now.get(Calendar.YEAR);
        int year2 = dobCal.get(Calendar.YEAR);
        int month1 = now.get(Calendar.MONTH);
        int month2 = dobCal.get(Calendar.MONTH);
        if (month2 < month1) {
            return year1 - year2;
        } else if (month2 == month1) {
            int day1 = now.get(Calendar.DAY_OF_MONTH);
            int day2 = dobCal.get(Calendar.DAY_OF_MONTH);
            if (day1 < day2) {
                return year1 - year2;
            } else {
                return year1 - year2 + 1;
            }
        } else {
            return year1 - year2 - 1;
        }
    }

    /**
     * Add number of days to given date
     * @param inputDate An instance of Date
     * @param noOfDays Number of days
     * @return An instance of Date
     */
    public static Date calculateNewDate(Date inputDate,
                                        int noOfDays) {

        if (noOfDays > 0) {
            Calendar instance = Calendar.getInstance();
            instance.setTime(inputDate);
            instance.add(Calendar.DAY_OF_YEAR, noOfDays);
            return instance.getTime();
        }
        return inputDate;
    }

    /**
     * Converts json to a list of event configuration details
     * @param json A json string
     * @return A list of EventConfigurationDetailDto
     * @throws IOException Signals that an I/O exception has occurred
     */
    public static List<EventConfigurationDetailDto> convertJsonToNotificationConfigList(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            if (json != null) {
                return mapper.readValue(json, new TypeReference<>() {
                });
            }
        } catch (IOException ex) {
            Logger.getLogger(ImtechoUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.emptyList();

    }

    /**
     * Converts notification list to json
     * @param notificationConfigDetail A list of EventConfigurationDetailDto
     * @return A json string
     * @throws IOException Signals that an I/O exception has occurred
     */
    public static String convertNotificationConfigListToJson(List<EventConfigurationDetailDto> notificationConfigDetail) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (notificationConfigDetail != null) {
            return mapper.writeValueAsString(notificationConfigDetail);
        }
        return null;

    }

    /**
     * Converts json to a list of SmsStaffUserDetailDto
     * @param json A json string
     * @return A list of SmsStaffUserDetailDto
     * @throws IOException Signals that an I/O exception has occurred
     */
//    public static List<SmsStaffUserDetailDto> convertJsonToUserMasterList(String json) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            if (json != null) {
//                return mapper.readValue(json, new TypeReference<>() {
//                });
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(ImtechoUtil.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return Collections.emptyList();
//
//    }

    /**
     *  Converts json to a list of FunctionParametersDto
     * @param json A json string
     * @return A list of FunctionParametersDto
     */
    public static List<FunctionParametersDto> convertJsonToFunctionParametersDto(String json){
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (json != null) {
                return mapper.readValue(json, new TypeReference<>() {
                });
            }
        } catch (IOException ex) {
            Logger.getLogger(ImtechoUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.emptyList();
    }

    /**
     * Converts SmsStaffUserDetailDto to json
     * @param userList A list of SmsStaffUserDetailDto
     * @return A json string
     * @throws IOException Signals that an I/O exception has occurred
     */
//    public static String convertUserMasterListToJson(List<SmsStaffUserDetailDto> userList) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        if (userList != null) {
//            return mapper.writeValueAsString(userList);
//        }
//        return null;
//
//    }

    /**
     * Converts object to json
     * @param o An instance of Object
     * @return A json string
     * @throws JsonProcessingException A json preoccesssing exception
     */
    public static String convertObjectToJson(Object o) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        if (o != null) {
            return mapper.writeValueAsString(o);
        }
        return null;
    }

    /**
     *  Converts json to object
     * @param json A json string
     * @param <T> An instance of TypeReference
     * @return An instance of TypeReference
     */
    public static <T> T convertJsonToObject(String json) {
        //JSON from String to Object
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (json != null) {
                return mapper.readValue(json, new TypeReference<>() {
                });
            }
        } catch (IOException ex) {
            Logger.getLogger(ImtechoUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Clear time from given date
     * @param date An instance of Date
     * @return An instance of Date
     */
    public static Date clearTimeFromDate(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            return calendar.getTime();
        }
        return null;
    }

    /**
     * Returns true false based on given initial value
     * @param trueOrFalse A string value
     * @return A boolean true or false
     */
    public static Boolean returnTrueFalseFromInitials(String trueOrFalse) {
        Boolean bool = null;
        switch (trueOrFalse) {
            case "T":
            case "1":
                bool = true;
                break;
            case "F":
            case "2":
                bool = false;
                break;
            default:
        }
        return bool;
    }

    /**
     * Converts boolean to short value
     * @param bool A boolean value
     * @return A short value
     */
    public static Short returnShortFromBoolean(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return Boolean.TRUE.equals(bool) ? (short)(1) : (short)(0);
    }

    /**
     * Handle period value.
     * @param period Period details.
     * @return Returns
     */
    private static StringBuilder handleString(Period period){
        StringBuilder str = new StringBuilder();
        if (period.getYears() > 0) {
            str.append(period.getYears()).append(" ").append("years ");
        }
        if (period.getMonths() > 0) {
            str.append(period.getMonths()).append(" ").append("months ");
        }
        if (period.getDays() > 0) {
            str.append(period.getDays()).append(" ").append("days ");
        }
        if (period.getHours() > 0) {
            str.append(period.getHours()).append(" ").append("hours ");
        }
        if (period.getMinutes() > 0) {
            str.append(period.getMinutes()).append(" ").append("minutes ");
        }
        if (period.getSeconds() > 0) {
            str.append(period.getSeconds()).append(" ").append("seconds ");
        }
        if (period.getMillis() > 0) {
            str.append(period.getSeconds()).append(" ").append("milliseconds ");
        }
        return str;
    }

    /**
     * Converts string to given type
     * @param value A string to be convert
     * @param type A type of conversion
     * @return An instance of Object
     * @throws ParseException An exception occur during parsing
     */
    public static Object convertStringToGivenType(String value, Class<?> type) throws ParseException {
        switch (type.getName()) {
            case INTEGER:
                return Integer.parseInt(value);
            case LONG:
                return Long.parseLong(value);
            case SHORT:
                return Short.parseShort(value);
            case FLOAT:
                return Float.parseFloat(value);
            case DOUBLE:
                return Double.parseDouble(value);
            case STRING:
                return value;
            case DATE:
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                return format.parse(value);
            case BOOLEAN:
                return Boolean.parseBoolean(value);
            default:
                return value;
        }
    }

    public static String convertPDFtoBase64String(String filePath) throws IOException {
        byte[] inFileBytes = Files.readAllBytes(Paths.get(filePath));
        byte[] encoded = Base64.getEncoder().encode(inFileBytes);
        return new String(encoded);
    }

    public static String convertBase64ToPDF(String encodedString, String outputFilePath) throws IOException {
        byte[] encoded = encodedString.getBytes();
        byte[] decoded = org.apache.commons.codec.binary.Base64.decodeBase64(encoded);
        try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
            fos.write(decoded);
            fos.flush();
        }
        return outputFilePath;
    }

    public static String convertXMLJSONStringToBase64String(String inputString) {
        return Base64.getEncoder().encodeToString(inputString.getBytes());
    }

    public static String base64StringToXMLJSONString(String encodedString) {
        byte[] encoded = encodedString.getBytes();
        byte[] result = Base64.getDecoder().decode(encoded);
        return new String(result);
    }

    public static String convertStringToBase64String(String toBeEncodedString) {
        return Base64.getEncoder().encodeToString(toBeEncodedString.getBytes());
    }

    /**
     * Convert Date object ot LocalDateTime Object by truncating seconds
     * @param dateToConvert An instance of Date
     * @return An instance of LocalDateTime
     */
    public static LocalDateTime convertDateToLocalDateTime(Date dateToConvert) {
        try {
            return LocalDateTime.ofInstant(
                    dateToConvert.toInstant(), ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS);
        } catch (DateTimeException e) {
            Logger.getLogger(ImtechoUtil.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * Converts object to string using Gson
     * @param o Object to be converted
     * @return Converted string
     */
    public static String convertObjectToStringUsingGson(Object o){
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    /**
     * Converts object from given string using Gson
     * @param jsonStr Json string for conversion
     * @param typeOfT Type of object to be converted
     * @param <T> Type of object
     * @return An instance of given type
     */
    public static <T> T convertObjectFromStringUsingGson(String jsonStr, Type typeOfT){
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, typeOfT);
    }
    
    public static String getFinancialYearFromDate(Date date) {
        Calendar instance = Calendar.getInstance();

        if (date != null) {
            instance.setTime(date);
        }

        int year = instance.get(Calendar.MONTH) < 3 ? instance.get(Calendar.YEAR) - 1 : instance.get(Calendar.YEAR);
        return year + "-" + (year + 1);
    }

    public static String addCommaSeparatedStringIfNotExists(String previousString, String stringToAdd) {
        if (previousString == null || previousString.isEmpty()) {
            return stringToAdd;
        } else if (previousString.contains(stringToAdd)) {
            return previousString;
        } else {
            return previousString + "," + stringToAdd;
        }
    }

    public static Date getMobileStartOrEndDateFromString(String dateString) {
        if (dateString != null && !dateString.isEmpty() && !dateString.equals("null")) {
            return new Date(Long.parseLong(dateString));
        } else {
            return new Date(0L);
        }
    }

    public static Date addDaysInDate(Date date, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static <T> T getValueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
