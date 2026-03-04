package com.argusoft.medplat.training.service.impl;

import com.argusoft.medplat.training.dao.AttendanceDao;
import com.argusoft.medplat.training.dto.AttendanceDto;
import com.argusoft.medplat.training.mapper.AttendanceMapper;
import com.argusoft.medplat.training.model.Attendance;
import com.argusoft.medplat.training.service.AttendanceService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * <p>
 *     Define services for attendance.
 * </p>
 * @author akshar
 * @since 26/08/20 11:00 AM
 *
 */
@Service("attendanceService")
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    AttendanceDao attendanceDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAttendance(AttendanceDto attendanceDto) {
        attendanceDao.create(AttendanceMapper.dtoToEntityAttendance(attendanceDto, null));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAttendance(AttendanceDto attendanceDto) {
        Attendance attendance = attendanceDao.retrieveById(attendanceDto.getId());
        attendanceDao.createOrUpdate(AttendanceMapper.dtoToEntityAttendance(attendanceDto, attendance));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAttendance(Integer attendanceId) {
        attendanceDao.deleteById(attendanceId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AttendanceDto> getAttendanceByTrainingOnDate(Integer trainingId, Date onDate) {
        return AttendanceMapper.entityToDtoAttendanceList(attendanceDao.getAttendanceByTrainingOnDate(trainingId, onDate));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AttendanceDto> getAttendancesByTraining(Integer trainingId) {
        return AttendanceMapper.entityToDtoAttendanceList(attendanceDao.getAttendancesByTraining(trainingId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AttendanceDto> getAttendanceByTrainingForDate(Integer trainingId, Date forDate) {
        return AttendanceMapper.entityToDtoAttendanceList(attendanceDao.getAttendanceByTrainingForDate(trainingId, forDate));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AttendanceDto> getAttendancesByTrainingIds(List<Integer> trainingIds) {
        return AttendanceMapper.entityToDtoAttendanceList(attendanceDao.getAttendancesByTrainingIds(trainingIds));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AttendanceDto> searchForAttendances(Integer topicId, Integer userId, Integer trainingId, Long onDateLong, Long forDateLong) {
        Date onDate = null;
        Date forDate = null;

        if (onDateLong != null) {
            onDate = new Date(onDateLong);
        }

        if (forDateLong != null) {
            forDate = new Date(forDateLong);
        }

        if (trainingId != null && onDate != null) {
            return this.getAttendanceByTrainingOnDate(trainingId, onDate);
        }

        if (trainingId != null && forDate != null) {
            return this.getAttendanceByTrainingForDate(trainingId, forDate);
        }

        if (trainingId != null) {
            return this.getAttendancesByTraining(trainingId);
        }

        if (userId != null) {
            return this.getAttendancesByTraining(userId);
        }

        if (topicId != null) {
            return this.getAttendancesByTraining(topicId);
        }
        return new ArrayList<>();
    }
}
