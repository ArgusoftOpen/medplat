package com.argusoft.medplat.training.controller;

import com.argusoft.medplat.training.dto.AttendanceDto;
import com.argusoft.medplat.training.service.AttendanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * <p>
 * Define APIs for attendance.
 * </p>
 *
 * @author akshar
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    /**
     * Update attendance in training.
     * @param attendanceDtos Attendance details.
     */
    @PutMapping(value = "/update")
    public void updateAttendance(@RequestBody List<AttendanceDto> attendanceDtos) {
        for (AttendanceDto attendance : attendanceDtos) {
            attendanceService.updateAttendance(attendance);
        }
    }

    /**
     * Retrieves attendances based on training id and date.
     * @param topicId Topic id.
     * @param userId User id.
     * @param trainingId Training id.
     * @param onDate Training effective date.
     * @param forDate Training date.
     * @return Returns list of attendances based on defined criteria.
     */
    @GetMapping(value = "/retrieve")
    public List<AttendanceDto> searchForAttendances(@RequestParam(value = "topicId", required = false) Integer topicId,
                                                    @RequestParam(value = "personId", required = false) Integer userId,
                                                    @RequestParam(value = "trainingId") Integer trainingId,
                                                    @RequestParam(value = "onDate", required = false) Long onDate,
                                                    @RequestParam(value = "forDate", required = false) Long forDate) {
        return attendanceService.searchForAttendances(topicId, userId, trainingId, onDate, forDate);
    }
}
