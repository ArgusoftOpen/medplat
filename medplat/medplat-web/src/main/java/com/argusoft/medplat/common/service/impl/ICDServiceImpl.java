
package com.argusoft.medplat.common.service.impl;

import com.argusoft.medplat.code.dto.SystemCodeListDto;
import com.argusoft.medplat.code.mapper.SystemCodeListMapper;
import com.argusoft.medplat.code.service.SystemCodeListService;
import com.argusoft.medplat.common.model.SystemConfiguration;
import com.argusoft.medplat.common.service.ICDService;
import com.argusoft.medplat.common.service.SystemConfigurationService;
import com.argusoft.medplat.common.util.ICDAPIclient;
import com.argusoft.medplat.common.util.ImtechoUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements methods of ICDService
 * @author ashish
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class ICDServiceImpl implements ICDService {

    @Autowired
    SystemConfigurationService systemConfigurationService;


    @Autowired
    SystemCodeListService systemCodeListService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ICDAPIclient setConnections() {
//      Every request will have fresh credential
        SystemConfiguration tokenEndpoint = systemConfigurationService.retrieveSystemConfigurationByKey("WHO_ICD_CODE_REST_API_TOKEN_ENPOINT");
        SystemConfiguration clientId = systemConfigurationService.retrieveSystemConfigurationByKey("WHO_ICD_CODE_REST_API_CLIENT_ID");
        SystemConfiguration clientSecret = systemConfigurationService.retrieveSystemConfigurationByKey("WHO_ICD_CODE_REST_API_CLIENT_SECRET");
        SystemConfiguration basreUrl = systemConfigurationService.retrieveSystemConfigurationByKey("WHO_ICD_CODE_REST_API_BASE_URL");
        ICDAPIclient icdapIclient = new ICDAPIclient(tokenEndpoint.getKeyValue(),clientId.getKeyValue(),clientSecret.getKeyValue(),basreUrl.getKeyValue());
        try {
            icdapIclient.setToken(icdapIclient.getToken());
        } catch (Exception ex) {
            Logger.getLogger(ICDServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return icdapIclient;
    }


    /**
     * Executes rest call
     * @param setConnectionObject An instance of ICDAPIclient
     * @param uri A uri string
     * @return A response string
     */
    public String executeRestCall(ICDAPIclient setConnectionObject,String uri) {
        try {
            return (setConnectionObject.getURI(uri));
        } catch (Exception ex) {
            Logger.getLogger(ICDServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Returns child category by code
     * @param setConnectionObject An instance of ICDAPIclient
     * @param version A value for version
     * @param releaseYear A value for release year
     * @param code A value for code
     * @param jsonObj An instance of JsonNode
     * @return A list of SystemCodeListDto
     */
    public List<SystemCodeListDto> getChildCategoryByCode(ICDAPIclient setConnectionObject, Integer version, Integer releaseYear, String code, JsonNode jsonObj) {
        List<SystemCodeListDto> childSystemCodeListDtos = new ArrayList<>();
        try {
            JsonNode jsonUrls = jsonObj.get("child");
            for (int i = 0; i < jsonUrls.size(); i++) {
                String url = jsonUrls.get(i).textValue();
                String childCode = url.split(version + "/" + releaseYear + "/")[1];
                String childObjectString = this.executeRestCall(setConnectionObject, version + "/" + releaseYear + "/" + childCode);
                if (childObjectString != null) {
                    JsonNode childJsonObj = ImtechoUtil.convertStringToJson(childObjectString);
                    SystemCodeListDto systemCodeListDto = SystemCodeListMapper.convertJSONObjectToSystemCodeListDto(childJsonObj, version, code, childCode, releaseYear);
                    childSystemCodeListDtos.add(systemCodeListDto);
                }
            }
            return childSystemCodeListDtos;

        } catch (Exception err) {
            return childSystemCodeListDtos;
        }

    }



    /**
     * {@inheritDoc}
     */
    @Override
    public String insertJsonDetailsByCodeRange(Integer version,Integer releaseYear,String startingCode, String endingCode) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode summuryJson = mapper.createObjectNode();

        Date startDate = new Date();
        ICDAPIclient setConnectionObject = this.setConnections();

        List<String> codeArray = new ArrayList<>();
        List<SystemCodeListDto> systemCodeListDtos = new ArrayList<>();
//      For Generating Series A-Z
        for (int alphabetIndex = 65; alphabetIndex <= 90; alphabetIndex++) {
//          As Code will be upto 99 only,Generate the Series
            for (int numberIndexFirstDigit = 0; numberIndexFirstDigit <=9; numberIndexFirstDigit++) {
                for (int numberIndexSecondDigit = 0; numberIndexSecondDigit <=9; numberIndexSecondDigit++) {
//                  Will create Code based on Iteration value
                    String code  = ((char) alphabetIndex) + "" + numberIndexFirstDigit + "" + numberIndexSecondDigit;
                    codeArray.add(code);
                }
            }
        }

//      Get between code List

        List<String> requiredCodeList;

        requiredCodeList = codeArray.subList(codeArray.indexOf(startingCode), codeArray.indexOf(endingCode) + 1);

        for (String code : requiredCodeList) {
            String responseObjectString = this.executeRestCall(setConnectionObject,version + "/" + releaseYear + "/" + code);

            if (responseObjectString != null) {
                JsonNode jsonObj = ImtechoUtil.convertStringToJson(responseObjectString);
                SystemCodeListDto systemCodeListDto = SystemCodeListMapper.convertJSONObjectToSystemCodeListDto(jsonObj, version, code, null, releaseYear);
                systemCodeListDtos.add(systemCodeListDto);
//                      Get Child Configuration
                List<SystemCodeListDto> childSystemCodeDtos = this.getChildCategoryByCode(setConnectionObject,version, releaseYear, code, jsonObj);
                if (!childSystemCodeDtos.isEmpty()) {
                    systemCodeListDtos.addAll(systemCodeListDtos.size(), childSystemCodeDtos);
                }
//
            }
        }

//      saving to DB
        systemCodeListService.saveOrUpdate(systemCodeListDtos);
        Date endDate = new Date();
        ((ObjectNode) summuryJson).put("Total Processed", systemCodeListDtos.size());
        ((ObjectNode) summuryJson).put("Time Taken", (endDate.getTime() - startDate.getTime())/1000 +" Second");
        systemCodeListDtos.clear();
        return ImtechoUtil.convertJsonToString(summuryJson);

    }


}
