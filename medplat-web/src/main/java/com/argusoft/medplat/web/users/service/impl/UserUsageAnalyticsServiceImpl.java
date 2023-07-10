package com.argusoft.medplat.web.users.service.impl;

import com.argusoft.medplat.config.RequestResponseLoggingFilter;
import com.argusoft.medplat.web.users.dao.UserUsageAnalyticsDao;
import com.argusoft.medplat.web.users.service.UserUsageAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements methods of UserUsageAnalyticsService
 * @author ashish
 * @since 28/08/2020 4:30
 */
@Service
@Transactional
public class UserUsageAnalyticsServiceImpl implements UserUsageAnalyticsService {

    @Autowired
    private UserUsageAnalyticsDao userUsageAnalyticsDao;
    
    @Autowired
    private RequestResponseLoggingFilter requestResponseLoggingFilter;

//    @Autowired
//    @Qualifier(value = "userUsageAnalyticsDbApiList")
//    private TenantCacheProvider<List<UserUsageAnalyticsDto>> tenantCacheProviderForUserUsageAnalyticsDbApiList;
//
//    @Autowired
//    @Qualifier(value = "isUserUsageAnalyticsActive")
//    private TenantCacheProvider<Boolean> tenantCacheProviderForIsUserUsageAnalyticsActive;
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insertUserUsageDetails(String id,String pageTitle, Integer userId, Long activeTabTime, Long totalTime, String nextStateId, String prevStateId, Boolean isBrowserCloseDet) {
        Integer pageTitleId =  requestResponseLoggingFilter.getPageTitleId(pageTitle);
        return userUsageAnalyticsDao.insertOrUpdateUserUsageDetails(id,pageTitleId,userId,activeTabTime,totalTime,nextStateId,prevStateId,isBrowserCloseDet);
    }

//    public void upload() {
//        if(tenantCacheProviderForIsUserUsageAnalyticsActive.get()) {
//            if (tenantCacheProviderForUserUsageAnalyticsDbApiList.get().size() > 0) {
//                for (UserUsageAnalyticsDto userUsageAnalyticsDto : tenantCacheProviderForUserUsageAnalyticsDbApiList.get()
//                ) {
//                    this.insertUserUsageDetails(userUsageAnalyticsDto.getCurrStateId(), userUsageAnalyticsDto.getPageTitle(), userUsageAnalyticsDto.getUserId(), userUsageAnalyticsDto.getActiveTabTime(), userUsageAnalyticsDto.getTotalTime(), userUsageAnalyticsDto.getNextStateId(), userUsageAnalyticsDto.getPrevStateId(), userUsageAnalyticsDto.isBrowserCloseDet());
//                }
//            }
//            tenantCacheProviderForUserUsageAnalyticsDbApiList.get().clear();
//        }
//    }
}
