package com.argusoft.medplat.web.location.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * <p>
 *     Define location_level_hierarchy_master entity and its fields.
 * </p>
 * @author istiyak
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "location_level_hierarchy_master")
public class LocationLevelHierarchy implements Serializable {

    private static final int serialVersionUID = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "location_id")
    private Integer locationId;

    @Column(name = "level7")
    private Integer level7;

    @Column(name = "level8")
    private Integer level8;

    @Column(name = "level6")
    private Integer level6;

    @Column(name = "level5")
    private Integer level5;

    @Column(name = "level4")
    private Integer level4;

    @Column(name = "level3")
    private Integer level3;

    @Column(name = "level2")
    private Integer level2;

    @Column(name = "level1")
    private Integer level1;

    @Column(name = "location_type", nullable = false, length = 10)
    private String locationType;

    @Column(name = "effective_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;

    @Column(name = "expiration_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    public LocationLevelHierarchy() {
    }

    public LocationLevelHierarchy(Integer id, Integer locationId, Integer level8, Integer level7, Integer level6, Integer level5, Integer level4, Integer level3, Integer level2, Integer level1, Date effectiveDate, Date expirationDate, boolean isActive) {
        this.id = id;
        this.locationId = locationId;
        this.level7 = level7;
        this.level8 = level8;
        this.level6 = level6;
        this.level5 = level5;
        this.level4 = level4;
        this.level3 = level3;
        this.level2 = level2;
        this.level1 = level1;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getLevel8() {
        return level8;
    }

    public void setLevel8(Integer level8) {
        this.level8 = level8;
    }

    public Integer getLevel7() {
        return level7;
    }

    public void setLevel7(Integer level7) {
        this.level7 = level7;
    }

    public Integer getLevel6() {
        return level6;
    }

    public void setLevel6(Integer level6) {
        this.level6 = level6;
    }

    public Integer getLevel5() {
        return level5;
    }

    public void setLevel5(Integer level5) {
        this.level5 = level5;
    }

    public Integer getLevel4() {
        return level4;
    }

    public void setLevel4(Integer level4) {
        this.level4 = level4;
    }

    public Integer getLevel3() {
        return level3;
    }

    public void setLevel3(Integer level3) {
        this.level3 = level3;
    }

    public Integer getLevel2() {
        return level2;
    }

    public void setLevel2(Integer level2) {
        this.level2 = level2;
    }

    public Integer getLevel1() {
        return level1;
    }

    public void setLevel1(Integer level1) {
        this.level1 = level1;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LocationLevelHierarchy)) {
            return false;
        }
        LocationLevelHierarchy other = (LocationLevelHierarchy) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * Define fields name for location_level_hierarchy_master.
     */
    public static class Fields {

        private Fields(){
        }

        public static final String SERIAL_VERSION_UID = "serialVersionUID";
        public static final String ID = "id";
        public static final String LOCATION_ID = "locationId";
        public static final String LEVEL8 = "level8";
        public static final String LEVEL7 = "level7";
        public static final String LEVEL6 = "level6";
        public static final String LEVEL5 = "level5";
        public static final String LEVEL4 = "level4";
        public static final String LEVEL3 = "level3";
        public static final String LEVEL2 = "level2";
        public static final String LEVEL1 = "level1";
        public static final String EFFECTIVE_DATE = "effectiveDate";
        public static final String EXPIRATION_DATE = "expirationDate";
        public static final String IS_ACTIVE = "isActive";
        public static final String LOCATION_TYPE = "locationType";
        public static final String USER_ID = "userId";
        public static final String LEVEL = "level";
        public static final String PREFERRED_LANG = "prefLanguage";
        public static final String LOC_TYPE = "locType";
        public static final String PARENT = "parent";
    }

    @Override
    public String toString() {
        return "LocationLevelHierarchy{" + "id=" + id + ", locationId=" + locationId + ", level7=" + level7 + ", level6=" + level6 + ", level5=" + level5 + ", level4=" + level4 + ", level3=" + level3 + ", level2=" + level2 + ", level1=" + level1 + ", effectiveDate=" + effectiveDate + ", expirationDate=" + expirationDate + ", isActive=" + isActive + '}';
    }

}
