/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.common.util;

import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author prateek
 */
@Service
    public class WebServiceUtil extends SpringBeanAutowiringSupport {
    
    private Map<String, Integer> userSessionMap = new LinkedHashMap<>();
    private Map<String, Integer> sessionUsageMap = new LinkedHashMap<>();
    private Map<String, String> webserviceInputdataMap = new LinkedHashMap<>();

    public Map<String, Integer> getUserSessionMap() {
        return userSessionMap;
    }

    public void setUserSessionMap(Map<String, Integer> userSessionMap) {
        this.userSessionMap = userSessionMap;
    }

    public Map<String, Integer> getSessionUsageMap() {
        return sessionUsageMap;
    }

    public void setSessionUsageMap(Map<String, Integer> sessionUsageMap) {
        this.sessionUsageMap = sessionUsageMap;
    }

    public Map<String, String> getWebserviceInputdataMap() {
        return webserviceInputdataMap;
    }

    public void setWebserviceInputdataMap(Map<String, String> webserviceInputdataMap) {
        this.webserviceInputdataMap = webserviceInputdataMap;
    }
    
}
