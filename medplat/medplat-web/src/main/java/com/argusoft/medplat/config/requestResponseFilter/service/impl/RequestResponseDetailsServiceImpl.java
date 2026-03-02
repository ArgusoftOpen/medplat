/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.config.requestResponseFilter.service.impl;

import com.argusoft.medplat.config.requestResponseFilter.constant.RequestResponseConstant;
import com.argusoft.medplat.config.requestResponseFilter.dao.RequestResponseDetailsDao;
import com.argusoft.medplat.config.requestResponseFilter.dto.RequestResponseDetailsDto;
import com.argusoft.medplat.config.requestResponseFilter.service.RequestResponseDetailsService;
import com.argusoft.medplat.web.users.controller.UserUsageAnalyticsController;
import com.argusoft.medplat.web.users.dao.UserUsageAnalyticsDao;
import com.argusoft.medplat.web.users.dto.UserUsageAnalyticsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author ashish
 */
@Service
public class RequestResponseDetailsServiceImpl implements RequestResponseDetailsService {

    public static Map<String, RequestResponseDetailsDto> hm
            = new ConcurrentHashMap<>();

    public static Map<String, Integer> page_title_map
            = new ConcurrentHashMap<>();

    public static Map<String, Integer> url_map
            = new ConcurrentHashMap<>();

    private boolean thresholdFlag = false;

    private final Integer responseThreshold = 0; // 5 min
    public static Integer listRemoveTime = 60000; // 10 min

    private static Logger log = LoggerFactory.getLogger(RequestResponseDetailsServiceImpl.class);

    @Autowired
    RequestResponseDetailsDao requestResponseDetailsDao;

    @Autowired
    private UserUsageAnalyticsDao userUsageAnalyticsDao;

    @Override
    public void checkRequestResponse() {

        Iterator<Map.Entry<String, RequestResponseDetailsDto>> itr = hm.entrySet().iterator();

        while (itr.hasNext()) {
            Map.Entry<String, RequestResponseDetailsDto> entry = itr.next();
            RequestResponseDetailsDto dto = entry.getValue();

            if (thresholdFlag) {
                if ((dto.getEndTime() == null && (new Date().getTime() - dto.getStartTime().getTime() > responseThreshold))
                        || (dto.getEndTime() != null && dto.getEndTime().getTime() - dto.getStartTime().getTime() > responseThreshold)) {

                    if (dto.getId() != null && dto.getEndTime() != null) {
//                    Update the entries
                        requestResponseDetailsDao.updateEndTime(dto.getId(), dto.getEndTime());
                    } else if (dto.getId() == null) {
//                    Insert the entries
                        try {
                            Integer id = requestResponseDetailsDao.createOrUpdate(dto);
                            if (id != null) {
                                dto.setId(id);
                            }
                        }catch(Exception e) {
                            log.info("Exception: when thresholdFlag is on " + e);
                            log.info("Exception details URL Id" + dto.getUrlId() + " Exception details param " + dto.getParam() + "Exception details Body " + dto.getBody() + "Remote IP" + dto.getRemoteIp() + "Page title " + dto.getPageTitleId());
                            itr.remove();

                        }
                    }
                }

                if (dto.getEndTime() != null && new Date().getTime() - dto.getStartTime().getTime() < listRemoveTime) {
                    itr.remove();
                    continue;
                }
            } else {
//                wait for request gets completed
                if (dto.getEndTime() != null && dto.getId() == null) {
                    try {
                        Integer id = requestResponseDetailsDao.createOrUpdate(dto);
                        if (id != null) {
                            dto.setId(id);
                        }
                        itr.remove();
                        continue;
                    }
                    catch(Exception e) {
                        log.info("Exception: when thresholdFlag is off " + e);
                        log.info("Exception details URL Id" + dto.getUrlId() + " Exception details param " + dto.getParam() + "Exception details Body " + dto.getBody() + "Remote IP" + dto.getRemoteIp() + "Page title ID" + dto.getPageTitleId());

                        itr.remove();
                    }

                }

            }
//          If request doesn't gets completed then will remove after certain time
            if (dto.getEndTime() == null && new Date().getTime() - dto.getStartTime().getTime() > listRemoveTime) {
                if (dto.getId() != null) {
                    itr.remove();
                } else {
                    try {
                        requestResponseDetailsDao.createOrUpdate(dto);
                        itr.remove();
                    }
                    catch (Exception e) {
                        log.info("Exception: when Request doesn't get completed after certain time " + e);
                        log.info("Exception details URL Id" + dto.getUrlId() + " Exception details param " + dto.getParam() + "Exception details Body " + dto.getBody() + "Remote IP" + dto.getRemoteIp() + "Page title " + dto.getPageTitleId());

                        itr.remove();
                    }
                }
            }
        }

    }

    @Override
    public void updateEndTimeToDB(Integer id, Date end_date) {
        requestResponseDetailsDao.updateRequestById(id, end_date);
    }

    @Override
    public Integer insertPageTitle(String pageTitle) {
        Integer pageTitleId = requestResponseDetailsDao.insertPageTitle(pageTitle);
        RequestResponseDetailsServiceImpl.page_title_map.put(pageTitle, pageTitleId);
        return pageTitleId;
    }

    @Override
    public void setPageTitleMapping() {
        page_title_map = requestResponseDetailsDao.getPageTitleMapping();

    }

    @Override
    public void setUrlMapping() {
        url_map = requestResponseDetailsDao.getUrlMapping();
    }

    @Override
    public void setApiToBeIgnoredForReqResFilter() {
        RequestResponseConstant.API_TO_BE_IGNORED_REGEX_ARRAY = requestResponseDetailsDao.getApiToBeIgnoredForReqResFilter();
    }

    @Override
    public Integer insertUrl(String url) {
        Integer urlId = requestResponseDetailsDao.insertUrl(url);
        RequestResponseDetailsServiceImpl.url_map.put(url, urlId);
        return urlId;

    }

    @Override
    public void checkUserUsageList() {
        //      For inserting data of state and their time to db
        Iterator<UserUsageAnalyticsDto> userUsageItr = UserUsageAnalyticsController.USER_USAGE_ANALYTICS_DB_API_LIST.iterator();
        while (userUsageItr.hasNext()) {
            UserUsageAnalyticsDto userUsageAnalyticsDto = userUsageItr.next();
            try {
                insertUserUsageDetails(userUsageAnalyticsDto.getCurrStateId(), userUsageAnalyticsDto.getPageTitle(), userUsageAnalyticsDto.getUserId(), userUsageAnalyticsDto.getActiveTabTime(), userUsageAnalyticsDto.getTotalTime(), userUsageAnalyticsDto.getNextStateId(), userUsageAnalyticsDto.getPrevStateId(), userUsageAnalyticsDto.isBrowserCloseDet());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                userUsageItr.remove();
            }
        }
    }
    private Integer insertUserUsageDetails(String id, String pageTitle, Integer userId, Long activeTabTime, Long totalTime, String nextStateId, String prevStateId, Boolean isBrowserCloseDet) {
        Integer pageTitleId = getPageTitleId(pageTitle);
        return userUsageAnalyticsDao.insertOrUpdateUserUsageDetails(id, pageTitleId, userId, activeTabTime, totalTime, nextStateId, prevStateId, isBrowserCloseDet);
    }
    private Integer getPageTitleId(String pageTitle) {
        if (pageTitle == null) {
            return -1;
        }
        Integer pageTitleId = getPageTitleId(pageTitle);
        if (pageTitleId == null) {
            pageTitleId = insertPageTitle(pageTitle);
        }
        return pageTitleId;
    }
}
