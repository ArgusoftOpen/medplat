/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.config.requestResponseFilter.dao.impl;

import com.argusoft.medplat.config.requestResponseFilter.dao.RequestResponseDetailsDao;
import com.argusoft.medplat.config.requestResponseFilter.dto.RequestResponseDetailsDto;
import com.argusoft.medplat.config.requestResponseFilter.model.RequestResponseDetailsMaster;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * @author ashish
 */
@Repository
@Transactional
public class RequestResponseDetailsDaoImpl extends GenericDaoImpl<RequestResponseDetailsMaster, Integer> implements RequestResponseDetailsDao {

    @Override
    public Integer createOrUpdate(RequestResponseDetailsDto requestResponseDetailsDto) {
        String sql;

        sql = "INSERT INTO request_response_details_master "
                + "(username,url,param,body,start_time,end_time,remote_ip,page_title) "
                + "VALUES(:username, :url, :param, :body, :startTime, :endTime, :remoteIp, :pageTitleId)"
                + " RETURNING id";

        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(sql);
        sQLQuery.setParameter("username", requestResponseDetailsDto.getUsername());
        sQLQuery.setParameter("url", requestResponseDetailsDto.getUrlId());
        sQLQuery.setParameter("param", requestResponseDetailsDto.getParam());
        sQLQuery.setParameter("body", requestResponseDetailsDto.getBody());
        sQLQuery.setParameter("startTime", requestResponseDetailsDto.getStartTime());
        sQLQuery.setParameter("endTime", requestResponseDetailsDto.getEndTime());
        sQLQuery.setParameter("remoteIp", requestResponseDetailsDto.getRemoteIp());
        sQLQuery.setParameter("pageTitleId", requestResponseDetailsDto.getPageTitleId());
        return sQLQuery.uniqueResult();
    }

    @Override
    public void updateRequestById(Integer id, Date endDate) {
        String sql;
        sql = "update request_response_details_master set end_time = :end_Date where id = :id";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(sql);
        q.setParameter("id", id);
        q.setParameter("end_Date", endDate);
        q.executeUpdate();
    }

    @Override
    public Integer insertPageTitle(String pageTitle) {
        String sql = "INSERT INTO request_response_navigation_state_mapping "
                + "(navigation_state) "
                + "VALUES(:pageTitle) "
                + "ON CONFLICT ON CONSTRAINT request_response_navigation_state_mapping_navigation_state_key "
                + "Do update set navigation_state = EXCLUDED.navigation_state "
                + "RETURNING id";

        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(sql);
        sQLQuery.setParameter("pageTitle", pageTitle);
        return sQLQuery.uniqueResult();
    }

    @Override
    public Map<String, Integer> getPageTitleMapping() {
        Map<String, Integer> pageTitleMap = new ConcurrentHashMap<>();
        //String sql = "select id,page_title from request_response_page_title_mapping";
        String sql = "select id,navigation_state from request_response_navigation_state_mapping";

        NativeQuery<Object> sQLQuery = getCurrentSession().createNativeQuery(sql);
        List<Object> list = sQLQuery.list();
        for (Object object : list) {
            Object[] resultArray = (Object[]) object;
            pageTitleMap.put((String) resultArray[1], (Integer) resultArray[0]);
        }
        return pageTitleMap;
    }

    @Override
    public Map<String, Integer> getUrlMapping() {
        Map<String, Integer> urlMap = new ConcurrentHashMap<>();
        String sql = "select id,url from request_response_url_mapping";

        NativeQuery<Object> sQLQuery = getCurrentSession().createNativeQuery(sql);
        List<Object> list = sQLQuery.list();
        for (Object object : list) {
            Object[] resultArray = (Object[]) object;
            urlMap.put((String) resultArray[1], (Integer) resultArray[0]);
        }
        return urlMap;
    }

    @Override
    public ArrayList<Pattern> getApiToBeIgnoredForReqResFilter() {
        ArrayList<Pattern> regexList = new ArrayList<>();
        String sql = "select regex_pattern from request_response_regex_list_to_be_ignored";
        NativeQuery<Object> sQLQuery = getCurrentSession().createNativeQuery(sql);
        List<Object> list = sQLQuery.list();
        for (Object regex : list) {
            regexList.add(Pattern.compile((String) regex));
        }
        return regexList;
    }

    @Override
    public Integer insertUrl(String url) {
        String sql = "INSERT INTO request_response_url_mapping "
                + "(url) "
                + "VALUES(:url) "
                + "ON CONFLICT ON CONSTRAINT request_response_url_mapping_url_key "
                + "Do update set url = EXCLUDED.url "
                + "RETURNING id";

        NativeQuery<Integer> sQLQuery = getCurrentSession().createNativeQuery(sql);
        sQLQuery.setParameter("url", url);
        return sQLQuery.uniqueResult();
    }

    @Override
    public void updateEndTime(Integer id, Date endTime) {
        String sql = "update request_response_details_master set end_time = :end_Date where id = :id";
        NativeQuery<Integer> q = getCurrentSession().createNativeQuery(sql);
        q.setParameter("id", id);
        q.setParameter("end_Date", endTime);
        q.executeUpdate();
    }

}
