package com.argusoft.medplat.sms_queue.mapper;

import com.argusoft.medplat.sms_queue.dto.SmsQueueDto;
import com.argusoft.medplat.sms_queue.model.SmsQueue;

import java.util.ArrayList;
import java.util.List;

/**
 *<p>
 *     An util class for sms queue to convert dto to modal or modal to dto
 *</p>
 * @author sneha
 * @since 03/09/2020 10:30
 */
public class SmsQueueMapper {

    private SmsQueueMapper(){
    }

    /**
     * Converts sms queue dto to modal
     * @param sms An instance of SmsQueue
     * @return An instance of SmsQueueDto
     */
    public static SmsQueueDto convertSmsQueueToSmsQueueDto(SmsQueue sms) {
        SmsQueueDto dto = new SmsQueueDto();
        if (sms != null) {
            dto.setId(sms.getId());
            dto.setMobileNumber(sms.getMobileNumber());
            dto.setMessage(sms.getMessage());
            dto.setIsProcessed(sms.getIsProcessed());
            dto.setIsSent(sms.getIsSent());
            dto.setProcessedOn(sms.getProcessedOn());
            dto.setCreatedOn(sms.getCreatedOn());
            dto.setCompletedOn(sms.getCompletedOn());
            dto.setStatus(sms.getStatus());
            dto.setExceptionString(sms.getExceptionString());
            dto.setMessageType(sms.getMessageType());
            dto.setSmsId(sms.getSmsId());
        }
        return dto;
    }

    /**
     * Converts sms queue dto to modal
     * @param smsQueues A list of SmsQueue
     * @return A list of SmsQueueDto
     */
    public static List<SmsQueueDto> convertSmsQueuesToSmsQueueDtos(List<SmsQueue> smsQueues) {
        List<SmsQueueDto> dtos = new ArrayList<>();
        for (SmsQueue event : smsQueues) {
            dtos.add(convertSmsQueueToSmsQueueDto(event));
        }
        return dtos;
    }
}
