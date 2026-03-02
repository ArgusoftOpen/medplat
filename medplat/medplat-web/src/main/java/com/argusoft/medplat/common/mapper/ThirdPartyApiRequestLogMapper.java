/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.mapper;

import com.argusoft.medplat.common.dto.ThirdPartyApiRequestLogDto;
import com.argusoft.medplat.common.model.ThirdPartyRequestLogModel;

import java.util.Date;

/**
 *
 * @author ashish
 */
public class ThirdPartyApiRequestLogMapper {
    
    public static ThirdPartyApiRequestLogDto convertDatatoDto(String responseString, String reqParam, String reqBody, String reqRemoteIP, String thirtPartyRequestedType, String errorMsg, String reqStatus) {
        ThirdPartyApiRequestLogDto dto = new ThirdPartyApiRequestLogDto();
        
        dto.setResponseString(responseString);
        dto.setReqParam(reqParam);
        dto.setReqBody(reqBody);
        dto.setReqRemoteIP(reqRemoteIP);
        dto.setThirtPartyRequestedType(thirtPartyRequestedType);
        dto.setErrorMsg(errorMsg);
        dto.setReqStatus(reqStatus);
        dto.setReqTime(new Date());        
        return dto;
    }
    
    
    public static ThirdPartyRequestLogModel convertDtoToModel(ThirdPartyApiRequestLogDto dto) {
        ThirdPartyRequestLogModel model = new ThirdPartyRequestLogModel();
        model.setResBody(dto.getResponseString());
        model.setReqParam(dto.getReqParam());
        model.setReqBody(dto.getReqBody());
        model.setReqRemoteIp(dto.getReqRemoteIP());
        model.setThirdPartyType(dto.getThirtPartyRequestedType());
        model.setReqError(dto.getErrorMsg());
        model.setReqState(dto.getReqStatus());
        model.setReqTime(dto.getReqTime());
        return model;
    }
    
}
