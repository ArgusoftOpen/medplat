package com.argusoft.medplat.mobile.service;

import com.argusoft.medplat.mobile.dto.MobileLibraryDataBean;

import java.util.List;

/**
 *
 * @author prateek on 13 Feb, 2019
 */
public interface MobileLibraryService {

    List<MobileLibraryDataBean> retrieveMobileLibraryDataBeans(Integer roleId, Long lastUpdateDate);
    
}
