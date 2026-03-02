package com.argusoft.medplat.ncddnhdd.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ncd_cbac_nutrition_master")
public class MemberCbacAndNutritionMaster extends EntityAuditInfo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "service_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceDate;

    @Column(name = "family_id")
    private Integer familyId;

    @Column(name = "cbac_table_id")
    private String cbacTableId;

    @Column(name = "child_nutrition_table_id")
    private String childNutritionTableId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Integer getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Integer familyId) {
        this.familyId = familyId;
    }

    public String getCbacTableId() {
        return cbacTableId;
    }

    public void setCbacTableId(String cbacTableId) {
        this.cbacTableId = cbacTableId;
    }

    public String getChildNutritionTableId() {
        return childNutritionTableId;
    }

    public void setChildNutritionTableId(String childNutritionTableId) {
        this.childNutritionTableId = childNutritionTableId;
    }
}
