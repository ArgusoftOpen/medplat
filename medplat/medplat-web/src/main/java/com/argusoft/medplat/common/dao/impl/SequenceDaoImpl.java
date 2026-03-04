
package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.SequenceDao;
import com.argusoft.medplat.common.databean.OtpDataBean;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 *     Implements methods of SequenceDao
 * </p>
 * @author kunjan
 * @since 31/08/2020 4:30
 */
@Repository
public class SequenceDaoImpl implements SequenceDao {
    
    private static final String MOBILE_NUMBER="mobileNumber";

    @Autowired
    protected SessionFactory sessionFactory;

//    @Autowired
//    @Qualifier(value = "mobileMaxRefreshCount")
//    private TenantCacheProvider<Integer> tenantCacheProviderForMobileMaxRefreshCount;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getNextValueBySequenceName(String sequenceName) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<BigInteger> q = session.createNativeQuery("select nextval('" + sequenceName + "');");
        BigInteger result = q.uniqueResult();
        return result.intValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkNumberOfResponsesInLast2Hours() {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<BigInteger> q = session.createNativeQuery("select count(*) from app_version_response where created_on > current_timestamp - interval '1 hour' ");
        BigInteger result = q.uniqueResult();
        if (result.longValue() < 250) {
            NativeQuery<Integer> q2 = session.createNativeQuery("insert into app_version_response(created_on) values (now());");
            q2.executeUpdate();
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertOtp(String mobileNumber, String otp) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q1 = session.createNativeQuery("update otp_master set state = 'INACTIVE' where mobile_number = :mobileNumber ;");
        q1.setParameter(MOBILE_NUMBER, mobileNumber);
        q1.executeUpdate();
        NativeQuery<Integer> q = session.createNativeQuery("insert into otp_master(mobile_number,otp,expiry,state) "
                + "values (:mobileNumber,:otp,now() + interval '15 minutes','ACTIVE');");
        q.setParameter(MOBILE_NUMBER, mobileNumber);
        q.setParameter("otp", otp);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OtpDataBean retrieveOtp(String mobileNumber) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<OtpDataBean> q = session.createNativeQuery("select mobile_number as \"mobileNumber\",otp,expiry as \"expiryDate\",state,count from otp_master  "
                + "where mobile_number = :mobileNumber and expiry > now() and state = 'ACTIVE' order by expiry desc;")
                .addScalar(MOBILE_NUMBER, StandardBasicTypes.STRING)
                .addScalar("otp", StandardBasicTypes.STRING)
                .addScalar("state", StandardBasicTypes.STRING)
                .addScalar("expiryDate", StandardBasicTypes.TIMESTAMP)
                .addScalar("count", StandardBasicTypes.INTEGER);
        q.setParameter(MOBILE_NUMBER, mobileNumber);

        List<OtpDataBean> otpDataBeans = q.setResultTransformer(Transformers.aliasToBean(OtpDataBean.class)).list();
        return CollectionUtils.isEmpty(otpDataBeans) ? null : otpDataBeans.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void invalidateOtp(String mobileNumber) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery("update otp_master set state = 'INACTIVE' where mobile_number = :mobileNumber ;");
        q.setParameter(MOBILE_NUMBER, mobileNumber);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logNotificationPush(String token, String heading, String message, String exception, String response) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery("INSERT INTO mytecho_push_notification_log(token, user_id, heading, message, response, exception, created_on)\n"
                + "VALUES (:token,(select id from mytecho_user where device_id = :token limit 1), :heading , :message , :response , :exception , now());");
        q.setParameter("heading", heading);
        q.setParameter("message", message);
        q.setParameter("token", token);
        q.setParameter("exception", exception);
        q.setParameter("response", response);
        q.executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTryCount(String mobileNumber, Integer count) {
        Session session = sessionFactory.getCurrentSession();
        NativeQuery<Integer> q = session.createNativeQuery("update otp_master set count = :count where mobile_number = :mobileNumber and state = 'ACTIVE' and expiry > now()");
        q.setParameter(MOBILE_NUMBER, mobileNumber);
        q.setParameter("count", count);
        q.executeUpdate();
    }

}
