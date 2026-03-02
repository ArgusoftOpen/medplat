package com.argusoft.medplat.training.service;

import com.argusoft.medplat.training.dto.QrAttendanceMarkRequest;
import com.argusoft.medplat.training.dto.QrAttendanceTokenDto;

/**
 * Prototype in-memory service for QR-based LMS attendance.
 *
 * This implementation is intentionally stateless from a persistence point of view
 * and is meant only for prototyping the dynamic QR attendance flow.
 */
public interface LmsQrAttendanceService {

    /**
     * Generate a short-lived QR attendance token for a given training session.
     *
     * @param trainingId Training identifier.
     * @param sessionId  Logical session identifier within the training.
     * @param latitude   Center latitude for geo-fencing (optional).
     * @param longitude  Center longitude for geo-fencing (optional).
     * @param radiusInMeters Geo-fence radius in meters (optional).
     * @param requestedByUserId User id of the instructor/requestor.
     * @param requestIp  IP address of the requestor.
     * @return DTO containing token metadata without QR image bytes.
     */
    QrAttendanceTokenDto generateToken(Integer trainingId,
                                       String sessionId,
                                       Double latitude,
                                       Double longitude,
                                       Double radiusInMeters,
                                       Integer requestedByUserId,
                                       String requestIp);

    /**
     * Validate and consume a QR attendance token for the currently authenticated learner.
     *
     * @param requestPayload Request containing the token and optional geo-coordinates.
     * @param learnerUserId  Authenticated learner user id.
     * @param requestIp      IP address from which the token is being consumed.
     */
    void validateAndMarkAttendance(QrAttendanceMarkRequest requestPayload,
                                   Integer learnerUserId,
                                   String requestIp);
}

