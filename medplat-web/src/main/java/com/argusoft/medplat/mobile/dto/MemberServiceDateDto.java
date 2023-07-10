package com.argusoft.medplat.mobile.dto;

import java.util.Date;

/**
 *
 * @author prateek on 12 Mar, 2019
 */
public class MemberServiceDateDto {

    private Integer memberId;
    
    private Date serviceDate;

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }
}
