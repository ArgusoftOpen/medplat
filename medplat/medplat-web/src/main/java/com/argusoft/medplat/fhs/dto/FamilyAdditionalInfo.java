package com.argusoft.medplat.fhs.dto;

/**
 *
 * <p>
 *     Used for family additional info.
 * </p>
 * @author rahul
 * @since 26/08/20 11:00 AM
 *
 */
public class FamilyAdditionalInfo {

    private Long lastFhsDate;
    private Long lastMemberNcdScreeningDate;
    private Long lastIdspScreeningDate;
    private Long lastIdsp2ScreeningDate;
    private String ncdYear;         // Comma separated financial year when all the screening for all member is done

    public Long getLastFhsDate() {
        return lastFhsDate;
    }

    public void setLastFhsDate(Long lastFhsDate) {
        this.lastFhsDate = lastFhsDate;
    }

    public Long getLastMemberNcdScreeningDate() {
        return lastMemberNcdScreeningDate;
    }

    public void setLastMemberNcdScreeningDate(Long lastMemberNcdScreeningDate) {
        this.lastMemberNcdScreeningDate = lastMemberNcdScreeningDate;
    }

    public Long getLastIdspScreeningDate() {
        return lastIdspScreeningDate;
    }

    public void setLastIdspScreeningDate(Long lastIdspScreeningDate) {
        this.lastIdspScreeningDate = lastIdspScreeningDate;
    }

    public Long getLastIdsp2ScreeningDate() {
        return lastIdsp2ScreeningDate;
    }

    public void setLastIdsp2ScreeningDate(Long lastIdsp2ScreeningDate) {
        this.lastIdsp2ScreeningDate = lastIdsp2ScreeningDate;
    }

    public String getNcdYear() {
        return ncdYear;
    }

    public void setNcdYear(String ncdYear) {
        this.ncdYear = ncdYear;
    }
}
