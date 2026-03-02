package com.argusoft.medplat.training.controller;

import com.argusoft.medplat.common.service.QRCodeGeneratorService;
import com.argusoft.medplat.config.security.ImtechoSecurityUser;
import com.argusoft.medplat.training.dto.QrAttendanceMarkRequest;
import com.argusoft.medplat.training.dto.QrAttendanceTokenDto;
import com.argusoft.medplat.training.service.LmsQrAttendanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Prototype controller exposing APIs for QR-based LMS attendance.
 *
 * These endpoints are intended for experimentation only and do not persist state.
 */
@RestController
@RequestMapping("/api/lms/qr-attendance")
@Tag(name = "LMS QR Attendance (Prototype)", description = "Prototype APIs for QR based attendance marking")
public class LmsQrAttendanceController {

    @Autowired
    private LmsQrAttendanceService lmsQrAttendanceService;

    @Autowired
    private QRCodeGeneratorService qrCodeGeneratorService;

    @Autowired
    private ImtechoSecurityUser securityUser;

    /**
     * Generate a short-lived QR attendance token and corresponding QR image (as Base64 PNG).
     *
     * This is expected to be called by instructors from the LMS UI and refreshed every 2 minutes.
     */
    @PostMapping("/generate")
    public QrAttendanceTokenDto generateAttendanceQr(@RequestParam("trainingId") Integer trainingId,
                                                     @RequestParam("sessionId") String sessionId,
                                                     @RequestParam(value = "latitude", required = false) Double latitude,
                                                     @RequestParam(value = "longitude", required = false) Double longitude,
                                                     @RequestParam(value = "radiusInMeters", required = false) Double radiusInMeters,
                                                     HttpServletRequest request) {

        Integer userId = securityUser.getId();
        String requesterIp = request.getRemoteAddr();

        QrAttendanceTokenDto dto = lmsQrAttendanceService.generateToken(
                trainingId,
                sessionId,
                latitude,
                longitude,
                radiusInMeters,
                userId,
                requesterIp);

        String attendanceUrl = "/api/lms/qr-attendance/scan?token=" + dto.getToken();
        byte[] qrPng = qrCodeGeneratorService.generateQRCode(attendanceUrl, 256, 256, null);

        dto.setQrImageBase64(Base64Utils.encodeToString(qrPng));

        return dto;
    }

    /**
     * Endpoint that is hit when a learner scans the QR code.
     *
     * In a real implementation this would likely redirect to a UI route; for the prototype
     * the token is just passed back to the client so it can call the mark API.
     */
    @GetMapping("/scan")
    public QrAttendanceTokenDto handleScan(@RequestParam("token") String token) {
        QrAttendanceTokenDto dto = new QrAttendanceTokenDto();
        dto.setToken(token);
        return dto;
    }

    /**
     * Mark attendance for the authenticated learner using a previously scanned token.
     */
    @PostMapping("/mark")
    public void markAttendance(@RequestBody QrAttendanceMarkRequest requestPayload,
                               HttpServletRequest request) {
        Integer learnerUserId = securityUser.getId();
        String requestIp = request.getRemoteAddr();
        lmsQrAttendanceService.validateAndMarkAttendance(requestPayload, learnerUserId, requestIp);
    }
}

