
package com.argusoft.medplat.common.dao.impl;

import com.argusoft.medplat.common.dao.SmsResponseDao;
import com.argusoft.medplat.common.model.SmsResponseEntity;
import com.argusoft.medplat.database.common.impl.GenericDaoImpl;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *     Implements methods of SmsResponseDao
 * </p>
 * @author ashish
 * @since 31/08/2020 4:30
 */
@Repository
public class SmsResponseDaoImpl extends GenericDaoImpl<SmsResponseEntity, String> implements SmsResponseDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public void createOrUpdate(String a2wackid, String a2wstatus, String carrierstatus, String lastutime, String custref, String submitdt, String mnumber, String acode, String senderid) {
        String sql = "INSERT INTO sms_response "
                + "(a2wackid, a2wstatus, carrierstatus, lastutime, custref, submitdt, mnumber, acode, senderid, created_on) "
                + "VALUES(:a2wackid, :a2wstatus, :carrierstatus, :lastutime, :custref, :submitdt, :mnumber, :acode, :senderid, now()) "
                + "ON CONFLICT ON CONSTRAINT sms_response_pkey "
                + "DO "
                + "UPDATE "
                + "SET a2wstatus=:a2wstatus, carrierstatus=:carrierstatus, lastutime=:lastutime, custref=:custref, submitdt=:submitdt, mnumber=:mnumber, acode=:acode, senderid=:senderid, created_on=now();";

        NativeQuery<Integer> nativeQuery = getCurrentSession().createNativeQuery(sql);
        nativeQuery.setParameter("a2wackid", a2wackid);
        nativeQuery.setParameter("a2wstatus", a2wstatus);
        nativeQuery.setParameter("carrierstatus", carrierstatus);
        nativeQuery.setParameter("lastutime", lastutime);
        nativeQuery.setParameter("custref", custref);
        nativeQuery.setParameter("submitdt", submitdt);
        nativeQuery.setParameter("mnumber", mnumber);
        nativeQuery.setParameter("acode", acode);
        nativeQuery.setParameter("senderid", senderid);
        nativeQuery.executeUpdate();

    }

}
