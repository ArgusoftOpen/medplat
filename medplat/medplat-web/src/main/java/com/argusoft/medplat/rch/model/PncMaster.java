/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.util.ImtechoUtil;
import com.argusoft.medplat.exception.ImtechoMobileException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Define rch_pnc_master entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "rch_pnc_master")
public class PncMaster extends VisitCommonFields implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "member_status")
    private String memberStatus;

    @Column(name = "pregnancy_reg_det_id")
    private Integer pregnancyRegDetId;

    @Column(name = "pnc_no")
    private String pncNo;

    @Column(name = "is_from_web")
    private Boolean isFromWeb;

    @Column(name = "service_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Column(name = "delivery_place")
    private String deliveryPlace;

    @Column(name = "type_of_hospital")
    private Integer typeOfHospital;

    @Column(name = "health_infrastructure_id")
    private Integer healthInfrastructureId;

    @Column(name = "delivery_done_by")
    private String deliveryDoneBy;

    @Column(name = "delivery_person")
    private Integer deliveryPerson;

    @Column(name = "delivery_person_name")
    private String deliveryPersonName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Integer getPregnancyRegDetId() {
        return pregnancyRegDetId;
    }

    public void setPregnancyRegDetId(Integer pregnancyRegDetId) {
        this.pregnancyRegDetId = pregnancyRegDetId;
    }

    public String getPncNo() {
        return pncNo;
    }

    public void setPncNo(String pncNo) {
        this.pncNo = pncNo;
    }

    public Boolean getIsFromWeb() {
        return isFromWeb;
    }

    public void setIsFromWeb(Boolean isFromWeb) {
        this.isFromWeb = isFromWeb;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        if (serviceDate != null && ImtechoUtil.clearTimeFromDate(serviceDate).after(new Date())) {
            throw new ImtechoMobileException("Service date cannot be future", 100);
        }
        this.serviceDate = ImtechoUtil.clearTimeFromDate(serviceDate);
    }

    public String getDeliveryPlace() {
        return deliveryPlace;
    }

    public void setDeliveryPlace(String deliveryPlace) {
        this.deliveryPlace = deliveryPlace;
    }

    public Integer getTypeOfHospital() {
        return typeOfHospital;
    }

    public void setTypeOfHospital(Integer typeOfHospital) {
        this.typeOfHospital = typeOfHospital;
    }

    public Integer getHealthInfrastructureId() {
        return healthInfrastructureId;
    }

    public void setHealthInfrastructureId(Integer healthInfrastructureId) {
        this.healthInfrastructureId = healthInfrastructureId;
    }

    public String getDeliveryDoneBy() {
        return deliveryDoneBy;
    }

    public void setDeliveryDoneBy(String deliveryDoneBy) {
        this.deliveryDoneBy = deliveryDoneBy;
    }

    public Integer getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(Integer deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public String getDeliveryPersonName() {
        return deliveryPersonName;
    }

    public void setDeliveryPersonName(String deliveryPersonName) {
        this.deliveryPersonName = deliveryPersonName;
    }

    @Override
    public String toString() {
        return "PncMaster{" + "id=" + id + ", memberStatus=" + memberStatus + ", pregnancyRegDetId=" + pregnancyRegDetId + ", pncNo=" + pncNo + ", isFromWeb=" + isFromWeb + ", serviceDate=" + serviceDate + ", deliveryPlace=" + deliveryPlace + ", typeOfHospital=" + typeOfHospital + ", healthInfrastructureId=" + healthInfrastructureId + ", deliveryDoneBy=" + deliveryDoneBy + ", deliveryPerson=" + deliveryPerson + ", deliveryPersonName=" + deliveryPersonName + '}';
    }
}
