package com.argusoft.medplat.web.users.dao.impl;

import com.argusoft.medplat.web.users.dao.UserUsageAnalyticsDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *     Implements methods of UserUsageAnalyticsDao
 * </p>
 * @author ashish
 * @since 31/08/2020 4:30
 */

@Repository
public class UserUsageAnalyticsDaoImpl implements UserUsageAnalyticsDao {

    private static final String PRE_STATE_ID="prevStateId";
    private static final String ACTIVE_TAB_TIME="activeTabTime";
    private static final String TOTAL_TIME="totalTime";

    @Autowired
    protected SessionFactory sessionFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer insertOrUpdateUserUsageDetails(String id, Integer pageTitleId, Integer userId, Long activeTabTime, Long totalTime, String nextStateId, String prevStateId, Boolean isBrowserCloseDet) {

        Session session = sessionFactory.getCurrentSession();

        if (Boolean.TRUE.equals(isBrowserCloseDet)) {
            NativeQuery<Integer> q3 = session.createNativeQuery("update request_response_page_wise_time_details set active_tab_time = :activeTabTime,total_time = :totalTime where id = :prevStateId");
            q3.setParameter(PRE_STATE_ID, prevStateId);
            q3.setParameter(ACTIVE_TAB_TIME, activeTabTime);
            q3.setParameter(TOTAL_TIME, totalTime);
            q3.executeUpdate();
            return 1;
        } else {
            NativeQuery<Integer> q1 = session.createNativeQuery("insert into request_response_page_wise_time_details "
                    + "(id,page_title_id,user_id,active_tab_time,total_time,next_page_id,prev_page_id,created_on)"
                    + " values (:currStateId,:pageTitleId,:userId,:activeTabTime,:totalTime,:nextStateId,:prevStateId,now()) "
                    + "ON CONFLICT ON CONSTRAINT request_response_page_wise_time_details_pk "
                    + "DO NOTHING;");
            q1.setParameter("currStateId", id);
            q1.setParameter("pageTitleId", pageTitleId);
            q1.setParameter("userId", userId);
            q1.setParameter(ACTIVE_TAB_TIME, 0);
            q1.setParameter(TOTAL_TIME, 0);
            q1.setParameter("nextStateId", nextStateId);
            q1.setParameter(PRE_STATE_ID, prevStateId);
            Integer rowInserted = q1.executeUpdate();

            if (prevStateId != null && activeTabTime != null && totalTime != null && Boolean.FALSE.equals(isBrowserCloseDet)) {
                NativeQuery<Integer> q2 = session.createNativeQuery("update request_response_page_wise_time_details set next_page_id = :currStateId,active_tab_time = :activeTabTime,total_time = :totalTime where id = :prevStateId");
                q2.setParameter("currStateId", id);
                q2.setParameter(PRE_STATE_ID, prevStateId);
                q2.setParameter(ACTIVE_TAB_TIME, activeTabTime);
                q2.setParameter(TOTAL_TIME, totalTime);
                q2.executeUpdate();
            }
            return rowInserted;
        }
    }
}
