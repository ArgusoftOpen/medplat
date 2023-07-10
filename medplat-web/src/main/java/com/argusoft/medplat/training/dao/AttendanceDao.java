/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.training.model.Attendance;
import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 * Define methods for attendance.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
public interface AttendanceDao extends GenericDao<Attendance, Integer>{
    /**
     * Used to get attendance of given trainingId and given date
     *
     * @param trainingId Training id.
     * @param onDate Training date.
     * @return Returns list of attendance details.
     */
    List<Attendance> getAttendanceByTrainingOnDate(Integer trainingId, Date onDate);

    /**
     * Used to get attendance of given trainingId for given Date
     *
     * @param trainingId Training id.
     * @param forDate Training for date.
     * @return Returns list of attendance details.
     */
    List<Attendance> getAttendanceByTrainingForDate(Integer trainingId, Date forDate);

    /**
     * Retrieves attendances by training id.
     * @param trainingId Training id.
     * @return Returns list of attendances.
     */
    List<Attendance> getAttendancesByTraining(Integer trainingId);

    /**
     * Retrieves attendances by list of training ids.
     * @param trainingIds List of training ids.
     * @return Returns list of attendances.
     */
    List<Attendance> getAttendancesByTrainingIds(List<Integer> trainingIds);
    
}
