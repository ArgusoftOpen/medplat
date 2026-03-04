package com.argusoft.medplat.fcm.service;

import javax.naming.OperationNotSupportedException;

public interface MedplatMessagingService {

    public String sendNotification(String type, String title, String message, String body, String token, Integer image, String eventId, String messageEvent, String headingEvent) throws OperationNotSupportedException;
}
