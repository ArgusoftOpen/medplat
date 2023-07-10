/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.config.requestResponseFilter.dao;

import com.argusoft.medplat.config.requestResponseFilter.dto.RequestResponseDetailsDto;
import com.argusoft.medplat.config.requestResponseFilter.model.RequestResponseDetailsMaster;
import com.argusoft.medplat.database.common.GenericDao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author ashish
 */
public interface RequestResponseDetailsDao extends GenericDao<RequestResponseDetailsMaster, Integer> {

    Integer createOrUpdate(RequestResponseDetailsDto requestResponseDetailsDto);

    void updateEndTime(Integer id, Date endTime);
    
    void updateRequestById(Integer id, Date endDate);

    Integer insertPageTitle(String pageTitle);

    Integer insertUrl(String url);

    Map<String, Integer> getPageTitleMapping();

    Map<String, Integer> getUrlMapping();

    List<Pattern> getApiToBeIgnoredForReqResFilter();
}
