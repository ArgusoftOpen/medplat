/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.sewa.android.app.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
/**
 * @author batul
 */
@DatabaseTable
public class MemberAvailableEveningBean extends BaseEntity implements Serializable {

    @DatabaseField(index = true)
    private String familyId;

    @DatabaseField
    private String memberId;

    @DatabaseField
    private Boolean eveningAvailability;

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Boolean getEveningAvailability() {
        return eveningAvailability;
    }

    public void setEveningAvailability(Boolean eveningAvailability) {
        this.eveningAvailability = eveningAvailability;
    }
}
