/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.training.mapper;

import com.argusoft.medplat.training.dto.AttendanceDto;
import com.argusoft.medplat.training.model.Attendance;
import java.util.List;
import java.util.LinkedList;

/**
 *
 * <p>
 *     Mapper for attendance in order to convert dto to model or model to dto.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
public class AttendanceMapper {
    private AttendanceMapper(){}

    /**
     * Convert attendance dto into entity.
     * @param attendanceDto Details of attendance.
     * @param attendance Entity of attendance.
     * @return Returns entity of attendance.
     */
    public static Attendance dtoToEntityAttendance(AttendanceDto attendanceDto , Attendance attendance) {

        if(attendance == null){
            attendance = new Attendance();
        }
        
        attendance.setId(attendanceDto.getId());
        attendance.setName(attendanceDto.getName());
        attendance.setDesc(attendanceDto.getDesc());
        attendance.setState(attendanceDto.getState());
        attendance.setEffectiveDate(attendanceDto.getEffectiveDate());
        attendance.setExpirationDate(attendanceDto.getExpirationDate());
        attendance.setReason(attendanceDto.getReason());
        attendance.setRemarks(attendanceDto.getRemarks());
        attendance.setCompletedOn(attendanceDto.getCompletedOn());
        attendance.setTopicIds(attendanceDto.getTopicIds());
        attendance.setTrainingId(attendanceDto.getTrainingId());
        attendance.setUserId(attendanceDto.getUserId());
        attendance.setIsPresent(attendanceDto.getPresent());
        attendance.setType(attendanceDto.getType());

        return attendance;
    }

    /**
     * Convert attendance entity into details.
     * @param attendance Entity of attendance.
     * @return Returns details of attendance.
     */
    public static AttendanceDto entityToDtoAttendance(Attendance attendance) {

        AttendanceDto attendanceDto = new AttendanceDto();

        attendanceDto.setId(attendance.getId());
        attendanceDto.setName(attendance.getName());
        attendanceDto.setDesc(attendance.getDesc());
        attendanceDto.setState(attendance.getState());
        attendanceDto.setEffectiveDate(attendance.getEffectiveDate());
        attendanceDto.setExpirationDate(attendance.getExpirationDate());
        attendanceDto.setReason(attendance.getReason());
        attendanceDto.setRemarks(attendance.getRemarks());
        attendanceDto.setCompletedOn(attendance.getCompletedOn());
        attendanceDto.setTopicIds(attendance.getTopicIds());
        attendanceDto.setTrainingId(attendance.getTrainingId());
        attendanceDto.setUserId(attendance.getUserId());
        attendanceDto.setPresent(attendance.getPresent());
        attendanceDto.setType(attendance.getType());

        return attendanceDto;
    }

    /**
     * Convert list of attendance entities into list of details.
     * @param attendances List of attendance entities.
     * @return Returns list of attendance details.
     */
    public static List<AttendanceDto> entityToDtoAttendanceList(List<Attendance> attendances) {
        List<AttendanceDto> attendanceDtos = new LinkedList<>();
        for(Attendance attendance:attendances){
            attendanceDtos.add(AttendanceMapper.entityToDtoAttendance(attendance));
        }
        return attendanceDtos;
    }
}
