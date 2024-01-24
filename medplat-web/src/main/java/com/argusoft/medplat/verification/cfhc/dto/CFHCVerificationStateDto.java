
package com.argusoft.medplat.verification.cfhc.dto;

import java.util.List;

/**
 * <p>
 *     Defines fields for child malnutrition treatment center state
 * </p>
 * @author raj
 * @since 09/09/2020 12:30
 */
public class CFHCVerificationStateDto {

    private List<Integer> locationIds;

    private List<String> states;

    private String familyid;

    public String getFamilyid() {
        return familyid;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CFHCVerificationStateDto{" + "locationIds=" + locationIds + ", states=" + states + '}';
    }

    public List<Integer> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(List<Integer> locationIds) {
        this.locationIds = locationIds;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates(List<String> states) {
        this.states = states;
    }
}
