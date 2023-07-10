
package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.common.dao.SmsDao;
import com.argusoft.medplat.common.model.Sms;
import com.argusoft.medplat.common.service.SmsService;
import com.argusoft.medplat.common.util.ConstantUtil;
import com.argusoft.medplat.event.dto.EventConfigTypeDto;
import com.argusoft.medplat.event.util.EventFunctionUtil;
import com.argusoft.medplat.exception.ImtechoSystemException;
import com.argusoft.medplat.sms_queue.dao.SmsQueueDao;
import com.argusoft.medplat.sms_queue.dto.SmsQueueDto;
import com.argusoft.medplat.sms_queue.model.SmsQueue;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements methods of SmsService
 * @author prateek
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsDao smsDao;

    @Autowired
    private SmsQueueDao smsQueueDao;

//    @Autowired
//    private SmsTypeService smsTypeService;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SmsServiceImpl.class);


    private final Client client = Client.create();

    private static final String SMS_TYPE = "UC"; //unicode

    private static final String DLT_ENTITY_ID = "1001636770000021340";

    private static Map<String, String> smsTypeTemplateIdMap;


    /**
     * {@inheritDoc}
     */
    @Override
    public Integer sendSms(String mobileNumber, String message, Boolean isPriority, String smsType) {
        if (smsType == null) {
            throw new ImtechoSystemException("Sms Type field not found", 503);
        }
        if (mobileNumber == null) {
            throw new ImtechoSystemException("Mobile number is not found", 503);
        }
        Integer id = -1;
        if (Boolean.FALSE.equals(isPriority)) {
            SmsQueue smsQueue = new SmsQueue();
            smsQueue.setMessage(message);
            smsQueue.setMobileNumber(mobileNumber);
            smsQueue.setMessageType(smsType);
            smsQueue.setIsProcessed(false);
            smsQueue.setCreatedOn(new Date());
            smsQueue.setStatus(SmsQueue.STATUS.NEW);
            id = smsQueueDao.create(smsQueue);
        } else {
            try {
                String messageHex = DatatypeConverter.printHexBinary(message.getBytes(StandardCharsets.UTF_16));
                
                        String mobileNo;
                        if (mobileNumber.startsWith("+91") || mobileNumber.startsWith("91")) {
                            mobileNo = mobileNumber;
                        } else {
                            mobileNo = "91" + mobileNumber;
                        }
                        Sms sms = new Sms();
                        sms.setMobileNumbers(mobileNo);
                        sms.setMessage(message);
                        sms.setMessageType(smsType);
                        id=generateResponse(sms,mobileNumber, messageHex,mobileNo);
            } catch (Exception e) {
                LOGGER.info("SMS Sending failed Reason: {}" , e.getMessage());
            }
        }
        return id;
    }

    /**
     * Generates sms response
     * @param sms An instance of Sms
     * @param mobileNumber A mobile number
     * @param messageHex Hex of string message
     * @param mobileNo A mobile number with 91 prefix
     * @return
     */
    private int generateResponse(Sms sms,String mobileNumber, String messageHex, String mobileNo){
        int id;
        try {
            WebResource webResource = client.resource(ConstantUtil.SMS_SERVICE_URL + "?username=" + ConstantUtil.SMS_SERVICE_USERNAME + "&pin=" + ConstantUtil.SMS_SERVICE_PASSWORD + "&signature=" + ConstantUtil.SMS_SERVICE_SIGNATURE + "&message=" + messageHex + "&msgType=" + SMS_TYPE + "&mnumber=" + mobileNo + "&dlt_entity_id=" + DLT_ENTITY_ID);
            ClientResponse response = webResource.get(ClientResponse.class);
            String resposeString = response.getEntity(String.class);
            LOGGER.info("SMS Response String: {}, response status: {}", resposeString, response.getStatus());
            sms.setResponse(resposeString);
            sms.setStatus(Sms.STATUS.SENT);
            sms.setResponseId(resposeString.substring(resposeString.indexOf("=")+1, resposeString.indexOf("~")));
            id = smsDao.create(sms);
            response.close();
        } catch (Exception e) {          // ignore if any failed so other can continue
            sms.setStatus(Sms.STATUS.EXCEPTION);
            sms.setExceptionString(e.getMessage());
            smsDao.create(sms);
            id = smsDao.create(sms);
            LOGGER.info("SMS Sending failed to mobile no:{}, Reason: {}" , mobileNumber, e.getMessage());
        }
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(EventConfigTypeDto notificationConfigTypeDto, List<LinkedHashMap<String, Object>> queryDataLists) {
        String smsTemplate = notificationConfigTypeDto.getTemplate();
        for (LinkedHashMap<String, Object> queryDataList : queryDataLists) {
            if (!queryDataList.containsKey(notificationConfigTypeDto.getSmsConfigJson().getMobileNumberFieldName())) {
                throw new ImtechoSystemException("sms_contacts field not found. Event config id : " + notificationConfigTypeDto.getId(), 503);
            }
            if (notificationConfigTypeDto.getSmsConfigJson().getSmsTypeField() == null) {
                throw new ImtechoSystemException("sms_type field not found. Event config id : " + notificationConfigTypeDto.getId(), 503);
            }
            String mobileNumbers = queryDataList.get(notificationConfigTypeDto.getSmsConfigJson().getMobileNumberFieldName()).toString();
            String smsType;
            if (Boolean.TRUE.equals(notificationConfigTypeDto.getSmsConfigJson().getIsSmsTypeFixed())) {
                smsType = notificationConfigTypeDto.getSmsConfigJson().getSmsTypeField();
            } else {
                smsType = queryDataList.get(notificationConfigTypeDto.getSmsConfigJson().getSmsTypeField()).toString();
            }
            Boolean priority = false;
            if (Boolean.TRUE.equals(notificationConfigTypeDto.getSmsConfigJson().getIsPriority())) {
                priority = notificationConfigTypeDto.getSmsConfigJson().getIsPriority();
            }
            if (mobileNumbers != null) {
                String[] mobileNumbersList = mobileNumbers.split(",");
                for (String mobNum : mobileNumbersList) {
                    sendSms(mobNum,
                            EventFunctionUtil.replaceParameterWithValue(smsTemplate, notificationConfigTypeDto.getTemplateParameter(), queryDataList), priority, smsType);
                }
            }
        }
    }

    /**
     * Load all sms type and store sms type and template id in map
     */
//    public void loadAllSmsTypes() {
//        smsTypeTemplateIdMap = new HashMap<>();
//
//        List<SmsTypeMasterDto> smsTypeMasterDtos = smsTypeService.getAllActiveSmsTypes();
//
//        if (!CollectionUtils.isEmpty(smsTypeMasterDtos)) {
//            for (SmsTypeMasterDto smsTypeMasterDto : smsTypeMasterDtos) {
//                smsTypeTemplateIdMap.put(smsTypeMasterDto.getSmsType(), smsTypeMasterDto.getTemplateId());
//            }
//        }
//    }

//    @Override
//    public void updateSmsTypeMap(String smsType, String templateId, State state) {
//        if (Objects.isNull(smsTypeTemplateIdMap)) {
//            smsTypeTemplateIdMap = new HashMap<>();
//        }
//        if (state.equals(State.INACTIVE)) {
//            smsTypeTemplateIdMap.remove(smsType);
//        } else {
//            smsTypeTemplateIdMap.put(smsType, templateId);
//        }
//    }

    @Override
    public Integer sendSms(SmsQueueDto smsQueueDto) {
        //cheking if sms is blocked
        if(smsQueueDto.getIsBlocked()) {
            Sms sms = new Sms();
            sms.setMobileNumbers(getMobileNo(smsQueueDto.getMobileNumber()));
            sms.setMessage(smsQueueDto.getMessage());
            sms.setMessageType(smsQueueDto.getMessageType());
            sms.setStatus(Sms.STATUS.BLOCKED);
            return smsDao.create(sms);
        }
        return this.sendSms(smsQueueDto.getMobileNumber(), smsQueueDto.getMessage(), true, smsQueueDto.getMessageType());
    }

    /**
     * Return mobile number with adding country code
     * @param mobileNumber string
     * @return String mobile number with country code
     */
    private String getMobileNo(String mobileNumber) {
        if (mobileNumber.startsWith("+91") || mobileNumber.startsWith("91")) {
            return mobileNumber;
        }
        return "91" + mobileNumber;
    }

}
