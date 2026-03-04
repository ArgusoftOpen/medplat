package com.argusoft.medplat.rch.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 * Define sickle_cell_screening entity and its fields.
 * </p>
 *
 * @author prateek
 * @since 26/08/20 11:00 AM
 */
@Entity
@Table(name = "sickle_cell_screening")
public class SicklecellScreening extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "anemia_test_done")
    private Boolean anemiaTestDone;

    @Column(name = "dtt_test_result")
    private String dttTestResult;

    @Column(name = "hplc_test_done")
    private Boolean hplcTestDone;

    @Column(name = "hplc_test_result")
    private String hplcTestResult;

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

    public Boolean getAnemiaTestDone() {
        return anemiaTestDone;
    }

    public void setAnemiaTestDone(Boolean anemiaTestDone) {
        this.anemiaTestDone = anemiaTestDone;
    }

    public String getDttTestResult() {
        return dttTestResult;
    }

    public void setDttTestResult(String dttTestResult) {
        this.dttTestResult = dttTestResult;
    }

    public Boolean getHplcTestDone() {
        return hplcTestDone;
    }

    public void setHplcTestDone(Boolean hplcTestDone) {
        this.hplcTestDone = hplcTestDone;
    }

    public String getHplcTestResult() {
        return hplcTestResult;
    }

    public void setHplcTestResult(String hplcTestResult) {
        this.hplcTestResult = hplcTestResult;
    }

    @Override
    public String toString() {
        return "SicklecellScreening{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", locationId=" + locationId +
                ", anemiaTestDone=" + anemiaTestDone +
                ", dttTestResult='" + dttTestResult + '\'' +
                ", hplcTestDone=" + hplcTestDone +
                ", hplcTestResult='" + hplcTestResult + '\'' +
                '}';
    }
}
