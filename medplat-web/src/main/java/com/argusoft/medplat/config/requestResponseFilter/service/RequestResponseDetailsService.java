/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.config.requestResponseFilter.service;


import java.util.Date;

/**
 *
 * @author ashish
 */
public interface RequestResponseDetailsService {

    public void checkRequestResponse();

    public void checkUserUsageList();

    public void updateEndTimeToDB(Integer id,Date end_date);

    public Integer insertPageTitle(String pageTitle);

    public Integer insertUrl(String url);

    public void setPageTitleMapping();

    public void setUrlMapping();

    public void setApiToBeIgnoredForReqResFilter();
}
