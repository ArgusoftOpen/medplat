
package com.argusoft.medplat.nutrition.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>
 *     Defines fields for Child Malnutrition Treatment Center (CMTC) and Nutrition Rehabilitation Center (NRC) mo verification
 * </p>
 * @author kunjan
 * @since 09/09/2020 5:30
 */
@Entity
@Table(name = "child_cmtc_nrc_mo_verification")
public class ChildCmtcNrcMoVerification extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "child_id")
    private Integer childId;

    @Column(name = "weight", columnDefinition = "numeric", precision = 7, scale = 3)
    private Float weight;

    @Column(name = "height")
    private Integer height;

    @Column(name = "mid_upper_arm_circumference")
    private Integer midUpperArmCircumference;

    @Column(name = "sd_score")
    private String sdScore;

    @Column(name = "bilateral_pitting_oedema")
    private String bilateralPittingOedema;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getMidUpperArmCircumference() {
        return midUpperArmCircumference;
    }

    public void setMidUpperArmCircumference(Integer midUpperArmCircumference) {
        this.midUpperArmCircumference = midUpperArmCircumference;
    }

    public String getSdScore() {
        return sdScore;
    }

    public void setSdScore(String sdScore) {
        this.sdScore = sdScore;
    }

    public String getBilateralPittingOedema() {
        return bilateralPittingOedema;
    }

    public void setBilateralPittingOedema(String bilateralPittingOedema) {
        this.bilateralPittingOedema = bilateralPittingOedema;
    }

}
