package com.argusoft.medplat.ncddnhdd.dto;

import com.argusoft.medplat.fhs.dto.MemberDto;
import lombok.Getter;
import lombok.Setter;

public class MemberRegistrationDto {
    private Integer locationId;
    private Integer areaId;
    private Integer referredFromInfraId;
    private Boolean isHof;
    private MemberDto hof;
    private MemberDto member;
    private String address;

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getReferredFromInfraId() {
        return referredFromInfraId;
    }

    public void setReferredFromInfraId(Integer referredFromInfraId) {
        this.referredFromInfraId = referredFromInfraId;
    }

    public MemberDto getHof() {
        return hof;
    }

    public void setHof(MemberDto hof) {
        this.hof = hof;
    }

    public MemberDto getMember() {
        return member;
    }

    public void setMember(MemberDto member) {
        this.member = member;
    }

    public Boolean getIsHof() {
        return isHof;
    }

    public void setIsHof(Boolean isHof) { this.isHof = isHof; }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
