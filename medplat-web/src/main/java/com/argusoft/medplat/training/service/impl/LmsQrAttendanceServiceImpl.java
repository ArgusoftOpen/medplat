package com.argusoft.medplat.training.service.impl;

import com.argusoft.medplat.exception.ImtechoUserException;
import com.argusoft.medplat.training.dto.QrAttendanceMarkRequest;
import com.argusoft.medplat.training.dto.QrAttendanceTokenDto;
import com.argusoft.medplat.training.service.LmsQrAttendanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Prototype in-memory implementation of {@link LmsQrAttendanceService}.
 *
 * This implementation keeps all token state in memory and is NOT suitable for production use.
 */
@Service
@Transactional
public class LmsQrAttendanceServiceImpl implements LmsQrAttendanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LmsQrAttendanceServiceImpl.class);

    /**
     * Default token validity in milliseconds (2 minutes).
     */
    private static final long DEFAULT_TOKEN_TTL_MILLIS = 2 * 60 * 1000L;

    /**
     * Simple in-memory token store keyed by token string.
     */
    private final Map<String, TokenInfo> tokenStore = new ConcurrentHashMap<>();

    @Override
    public QrAttendanceTokenDto generateToken(Integer trainingId,
                                              String sessionId,
                                              Double latitude,
                                              Double longitude,
                                              Double radiusInMeters,
                                              Integer requestedByUserId,
                                              String requestIp) {
        if (trainingId == null) {
            throw new ImtechoUserException("trainingId is required for QR attendance token generation", 400);
        }
        if (sessionId == null || sessionId.isEmpty()) {
            throw new ImtechoUserException("sessionId is required for QR attendance token generation", 400);
        }

        long now = Instant.now().toEpochMilli();
        long expiresAt = now + DEFAULT_TOKEN_TTL_MILLIS;

        String token = UUID.randomUUID().toString();
        TokenInfo info = new TokenInfo();
        info.trainingId = trainingId;
        info.sessionId = sessionId;
        info.latitude = latitude;
        info.longitude = longitude;
        info.radiusInMeters = radiusInMeters;
        info.expiresAt = expiresAt;
        info.createdByUserId = requestedByUserId;
        info.createdFromIp = requestIp;

        tokenStore.put(token, info);

        QrAttendanceTokenDto dto = new QrAttendanceTokenDto();
        dto.setTrainingId(trainingId);
        dto.setSessionId(sessionId);
        dto.setToken(token);
        dto.setExpiresAt(expiresAt);
        dto.setQrImageBase64(null);

        return dto;
    }

    @Override
    public void validateAndMarkAttendance(QrAttendanceMarkRequest requestPayload,
                                          Integer learnerUserId,
                                          String requestIp) {
        if (requestPayload == null || requestPayload.getToken() == null || requestPayload.getToken().isEmpty()) {
            throw new ImtechoUserException("Token is required", 400);
        }
        if (learnerUserId == null) {
            throw new ImtechoUserException("Authenticated learner user id is required", 401);
        }

        TokenInfo info = tokenStore.get(requestPayload.getToken());
        if (info == null) {
            throw new ImtechoUserException("Invalid or already used attendance token", 400);
        }

        long now = Instant.now().toEpochMilli();
        if (info.expiresAt < now) {
            tokenStore.remove(requestPayload.getToken());
            throw new ImtechoUserException("Attendance token has expired", 400);
        }

        if (info.consumedByUserId != null || info.consumedFromIp != null) {
            throw new ImtechoUserException("Attendance token already consumed", 400);
        }

        if (requestIp != null && info.createdFromIp != null && !Objects.equals(requestIp, info.createdFromIp)) {
            LOGGER.warn("Prototype IP validation failed for token {}. expectedIp={}, actualIp={}",
                    requestPayload.getToken(), info.createdFromIp, requestIp);
        }

        if (info.latitude != null && info.longitude != null
                && requestPayload.getLatitude() != null && requestPayload.getLongitude() != null
                && info.radiusInMeters != null && info.radiusInMeters > 0) {
            double distance = distanceInMeters(
                    info.latitude,
                    info.longitude,
                    requestPayload.getLatitude(),
                    requestPayload.getLongitude());
            if (distance > info.radiusInMeters) {
                throw new ImtechoUserException("Geo-fence validation failed for attendance token", 400);
            }
        }

        info.consumedByUserId = learnerUserId;
        info.consumedFromIp = requestIp;
        info.consumedAt = now;

        tokenStore.put(requestPayload.getToken(), info);

        LOGGER.info("Prototype QR attendance marked. trainingId={}, sessionId={}, learnerUserId={}, ip={}",
                info.trainingId, info.sessionId, learnerUserId, requestIp);
    }

    private static double distanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_METERS = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_METERS * c;
    }

    /**
     * Internal in-memory representation of token metadata.
     */
    private static class TokenInfo {
        private Integer trainingId;
        private String sessionId;
        private Double latitude;
        private Double longitude;
        private Double radiusInMeters;
        private Long expiresAt;
        private Integer createdByUserId;
        private String createdFromIp;
        private Integer consumedByUserId;
        private String consumedFromIp;
        private Long consumedAt;
    }
}

