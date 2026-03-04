package com.argusoft.medplat.training.util;

import com.argusoft.medplat.course.service.CourseMasterService;
import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.training.service.TrainingService;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * <p>
 * Define utility methods for training.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
public class TrainingUtil {

    CourseMasterService courseMasterService;
    TrainingService trainingService;

    public static final String CREATE_VALIDATION = "create.validation";
    public static final String UPDATE_VALIDATION = "update.validation";

    /**
     * Prepare date.
     * @param input Input date.
     * @param hours Hours.
     * @param mins Minutes.
     * @param seconds Seconds.
     * @param miliseconds Mili seconds.
     * @return Returns date.
     */
    public static Date prepareDate(Date input,
            int hours,
            int mins,
            int seconds,
            int miliseconds)
    {
        if (input == null) {
            throw new ImtechoUserException("Please provide valid DATE",0);
        }
        Calendar instance = Calendar.getInstance();
        instance.setTime(input);
        instance.set(Calendar.HOUR_OF_DAY, hours);
        instance.set(Calendar.MINUTE, mins);
        instance.set(Calendar.SECOND, seconds);
        instance.set(Calendar.MILLISECOND, miliseconds);
        return instance.getTime();
    }

    /**
     * Calculate new date excluding sunday.
     * @param effectiveDate Effective date.
     * @param noOfDays No of days.
     * @return Returns valid date.
     */
    public static Date calculateNewDateExcludingSunday(Date effectiveDate,
            int noOfDays) {
// TODO:Add course type check if offline then check noOfDays
//        if (noOfDays == 0) {
//            throw new ImtechoUserException("Please provide valid training duration.!", 0);
//        }
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
     * Calculate percentage.
     * @param totalValue Total value.
     * @param value Value.
     * @return Returns percentage.
     */
    // calculate percentage :
    public static float calculatePercentageFloat(int totalValue, int value) {
        return (float) Math.round(((value * 100.00) / totalValue) * 100) / 100;
    }
}
