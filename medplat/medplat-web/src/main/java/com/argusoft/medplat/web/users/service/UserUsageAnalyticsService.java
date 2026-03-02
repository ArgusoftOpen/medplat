package com.argusoft.medplat.web.users.service;

/**
 * <p>
 *     Define methods for user usage analytic
 * </p>
 * @author ashish
 * @since 27/08/2020 4:30
 */
public interface UserUsageAnalyticsService {

    /**
     * Insert user usage analytics based on given parameter
     * @param id An id
     * @param pageTitle A title of page
     * @param userId An id of user
     * @param activeTabTime A time of active tab
     * @param totalTime A total tab time
     * @param nextStateId An id of next state
     * @param prevStateId An id of previous state
     * @param isBrowserCloseDet Is browser close or not
     * @return An id of created row
     */
    Integer insertUserUsageDetails(String id, String pageTitle, Integer userId, Long activeTabTime, Long totalTime, String nextStateId, String prevStateId,Boolean isBrowserCloseDet);

//    void upload();
}
