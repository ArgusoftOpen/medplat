
package com.argusoft.medplat.common.service;

/**
 * <p>
 *     Define methods for National Informatics Centre
 * </p>
 * @author ashish
 * @since 27/08/2020 4:30
 */
public interface NicSmsService {
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
    void smsResponse(String a2wackid,
            String a2wstatus,
            String carrierstatus,
            String lastutime,
            String custref,
            String submitdt,
            String mnumber,
            String acode,
            String senderid);
}
