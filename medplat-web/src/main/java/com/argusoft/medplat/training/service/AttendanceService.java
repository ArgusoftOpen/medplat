/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.service;

import com.argusoft.medplat.training.dto.AttendanceDto;

import java.util.Date;
import java.util.List;

/**
 *
 * <p>
 *     Define services for attendance.
 * </p>
 * @author rahul
 * @since 26/08/20 11:00 AM
 *
 */
public interface AttendanceService {

    /**
     * Used to create attendance.
     * @param attendanceDto Attendance details.
     */
    void createAttendance(AttendanceDto attendanceDto);

    /**
     * Used to update attendance.
     * @param attendanceDto Attendance details.
     */
    void updateAttendance(AttendanceDto attendanceDto);

    /**
     * Used to delete attendance of given attendanceId.
     * @param attendanceId Attendance id.
     */
    void deleteAttendance(Integer attendanceId);

    /**
     * Used to get attendance of given trainingId and given date
     *
     * @param trainingId Training id.
     * @param date Training date.
     * @return Returns list of attendance details.
     */
    List<AttendanceDto> getAttendanceByTrainingOnDate(Integer trainingId, Date date);

    /**
     * Retrieves a list of Attendance for a training.
     * @param trainingId a unique identifier for a Training.
     * @return Returns list of attendance or an empty list if none found.
     */
    List<AttendanceDto> getAttendancesByTraining(Integer trainingId);

    /**
     * Used to get attendance of given trainingId for given Date.
     * @param trainingId Training id.
     * @param forDate Training for date.
     * @return Returns list of attendance details.
     */
    List<AttendanceDto> getAttendanceByTrainingForDate(Integer trainingId, Date forDate);

    /**
     * Retrieves attendances by list of training ids.
     * @param trainingIds List of training ids.
     * @return Returns list of attendances.
     */
    List<AttendanceDto> getAttendancesByTrainingIds(List<Integer> trainingIds);

    /**
     * Retrieves attendances based on training id and date.
     * @param topicId Topic id.
     * @param userId User id.
     * @param trainingId Training id.
     * @param onDate Training effective date.
     * @param forDate Training date.
     * @return Returns list of attendances based on defined criteria.
     */
    List<AttendanceDto> searchForAttendances(Integer topicId, Integer userId, Integer trainingId, Long onDate, Long forDate);
}
