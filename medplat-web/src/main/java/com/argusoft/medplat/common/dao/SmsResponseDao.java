package com.argusoft.medplat.common.dao;

import com.argusoft.medplat.common.model.SmsResponseEntity;
import com.argusoft.medplat.database.common.GenericDao;


/**
 * <p>Defines database method for sms response</p>
 * @author ashish
 * @since 31/08/2020 10:30
 */
public interface SmsResponseDao extends GenericDao<SmsResponseEntity, String> {
    /**
     * Sends sms to given mobile numbers
     * @param a2wackid An acknowledgement id
     * @param a2wstatus An acknowledgement status
     * @param carrierstatus A carrier status
     * @param lastutime A last updated time
     * @param custref A customer reference
     * @param submitdt A date of submit
     * @param mnumber A mobile numbers
     * @param acode An account code
     * @param senderid Sender Id
     */
    void createOrUpdate(String a2wackid, String a2wstatus, String carrierstatus, String lastutime, String custref, String submitdt, String mnumber, String acode, String senderid);
}
