package com.argusoft.medplat.web.users.dao;

/**
 * <p>Defines database method for user analytics</p>
 * @author ashish
 * @since 31/08/2020 10:30
 */
public interface UserUsageAnalyticsDao {
    /**
     * Insert user usage analytics based on given parameter
     * @param id An id
     * @param pageTitleId A title of page
     * @param userId An id of user
     * @param activeTabTime A time of active tab
     * @param totalTime A total tab time
     * @param nextStateId An id of next state
     * @param prevStateId An id of previous state
     * @param isBrowserCloseDet Is browser close or not
     * @return An id of created row
     */
    Integer insertOrUpdateUserUsageDetails(String id, Integer pageTitleId, Integer userId, Long activeTabTime, Long totalTime, String nextStateId, String prevStateId,Boolean isBrowserCloseDet);
}
