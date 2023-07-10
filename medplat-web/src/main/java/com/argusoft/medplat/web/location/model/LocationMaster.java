/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.model;

import com.argusoft.medplat.common.util.IJoinEnum;

import javax.persistence.*;
import javax.persistence.criteria.JoinType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * <p>
 *     Define location_master entity and its fields.
 * </p>
 * @author mmodi
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "location_master", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "parent"})})
public class LocationMaster implements Serializable {

    private static final int serialVersionUID = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 4000)
    private String name;

    @Basic(optional = false)
    @Column(name = "type", nullable = false, length = 10)
    private String type;

    @Column(name = "unique_id", length = 50)
    private String uniqueId;

    @Column(name = "address", length = 300)
    private String address;

    @Column(name = "pin_code", length = 15)
    private String pinCode;

    @Column(name = "contact1_phone", length = 15)
    private String contact1Phone;

    @Column(name = "contact1_name", length = 50)
    private String contact1Name;

    @Column(name = "contact1_email", length = 50)
    private String contact1Email;

    @Column(name = "contact2_phone", length = 20)
    private String contact2Phone;

    @Column(name = "contact2_name", length = 50)
    private String contact2Name;

    @Column(name = "contact2_email", length = 50)
    private String contact2Email;

    @Basic(optional = false)
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Basic(optional = false)
    @Column(name = "is_archive", nullable = false)
    private boolean isArchive;

    @Basic(optional = false)
    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Basic(optional = false)
    @Column(name = "created_on", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "modified_by", length = 50)
    private String modifiedBy;

    @Column(name = "associated_user", length = 50)
    private String associatedUser;

    @Column(name = "max_users")
    private Short maxUsers;

    @Column(name = "modified_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(name = "parent", insertable = false, updatable = false)
    private Integer parent;

    @JoinColumn(name = "parent", referencedColumnName = "id", insertable = true, updatable = true)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private LocationMaster parentMaster;

    @Column(name = "is_tba_avaiable")
    private Boolean isTbaAvailable;

    @Column(name = "total_population")
    private Integer totalPopulation;

    @JoinColumn(name = "type", referencedColumnName = "type", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LocationTypeMaster hierarchyType;

    @Column(name = "location_code")
    private Long locationCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Column(name = "location_flag")
    private String locationFlag;

    @Column(name = "census_population")
    private Integer censusPopulation;

    @Column(name = "is_cmtc_present")
    private Boolean containsCmtcCenter;

    @Column(name = "is_nrc_present")
    private Boolean containsNrcCenter;

    @Column(name = "cerebral_palsy_module")
    private Boolean cerebralPalsyModule;

    @Column(name = "geo_fencing")
    private Boolean geoFencing;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "lgd_code")
    private String lgdCode;

    @Column(name = "mdds_code")
    private String mddsCode;

    @Column(name = "is_taaho")
    private Boolean isTaaho;

    public Integer getTotalPopulation() {
        return totalPopulation;
    }

    public void setTotalPopulation(Integer totalPopulation) {
        this.totalPopulation = totalPopulation;
    }

    public Boolean getIsTbaAvailable() {
        return isTbaAvailable;
    }

    public void setIsTbaAvailable(Boolean isTbaAvailable) {
        this.isTbaAvailable = isTbaAvailable;
    }

    public LocationMaster() {
    }

    public LocationMaster(Integer id) {
        this.id = id;
    }

    public LocationMaster(Integer id, String name, String type, boolean isActive, boolean isArchive, String createdBy, Date createdOn, String locationFlag) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isActive = isActive;
        this.isArchive = isArchive;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
        this.locationFlag = locationFlag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getContact1Phone() {
        return contact1Phone;
    }

    public void setContact1Phone(String contact1Phone) {
        this.contact1Phone = contact1Phone;
    }

    public String getContact1Name() {
        return contact1Name;
    }

    public void setContact1Name(String contact1Name) {
        this.contact1Name = contact1Name;
    }

    public String getContact1Email() {
        return contact1Email;
    }

    public void setContact1Email(String contact1Email) {
        this.contact1Email = contact1Email;
    }

    public String getContact2Phone() {
        return contact2Phone;
    }

    public void setContact2Phone(String contact2Phone) {
        this.contact2Phone = contact2Phone;
    }

    public String getContact2Name() {
        return contact2Name;
    }

    public void setContact2Name(String contact2Name) {
        this.contact2Name = contact2Name;
    }

    public String getContact2Email() {
        return contact2Email;
    }

    public void setContact2Email(String contact2Email) {
        this.contact2Email = contact2Email;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsArchive() {
        return isArchive;
    }

    public void setIsArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

//    public LocationMaster getParentMasterForImport() {
//
//        if (parentMaster != null && LocationConstants.LocationType.STATE.getType().equalsIgnoreCase(parentMaster.getType())) {
//            return null;
//        }
//        return parentMaster;
//    }

    public LocationMaster getParentMaster() {
        return parentMaster;
    }

    public void setParentMaster(LocationMaster parentMaster) {
        this.parentMaster = parentMaster;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Short getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Short maxUsers) {
        this.maxUsers = maxUsers;
    }

    public String getAssociatedUser() {
        return associatedUser;
    }

    public void setAssociatedUser(String associatedUser) {
        this.associatedUser = associatedUser;
    }

    public Long getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(Long locationCode) {
        this.locationCode = locationCode;
    }

    public LocationTypeMaster getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(LocationTypeMaster hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public String getLocationFlag() {
        return locationFlag;
    }

    public void setLocationFlag(String locationFlag) {
        this.locationFlag = locationFlag;
    }

    public Integer getCensusPopulation() {
        return censusPopulation;
    }

    public void setCensusPopulation(Integer censusPopulation) {
        this.censusPopulation = censusPopulation;
    }

    public Boolean getContainsCmtcCenter() {
        return containsCmtcCenter;
    }

    public void setContainsCmtcCenter(Boolean containsCmtcCenter) {
        this.containsCmtcCenter = containsCmtcCenter;
    }

    public Boolean getContainsNrcCenter() {
        return containsNrcCenter;
    }

    public void setContainsNrcCenter(Boolean containsNrcCenter) {
        this.containsNrcCenter = containsNrcCenter;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getLgdCode() {
        return lgdCode;
    }

    public void setLgdCode(String lgdCode) {
        this.lgdCode = lgdCode;
    }

    public String getMddsCode() {
        return mddsCode;
    }

    public void setMddsCode(String mddsCode) {
        this.mddsCode = mddsCode;
    }

    public Boolean getCerebralPalsyModule() {
        return cerebralPalsyModule == null ? null : cerebralPalsyModule;
    }

    public void setCerebralPalsyModule(Boolean cerebralPalsyModule) {
        this.cerebralPalsyModule = cerebralPalsyModule;
    }

    public Boolean getGeoFencing() {
        return geoFencing == null ? null : geoFencing;
    }

    public void setGeoFencing(Boolean geoFencing) {
        this.geoFencing = geoFencing;
    }

    public Boolean getIsTaaho() {
        return isTaaho;
    }

    public void setIsTaaho(Boolean isTaaho) {
        this.isTaaho = isTaaho;
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
        if (!(object instanceof LocationMaster)) {
            return false;
        }
        LocationMaster other = (LocationMaster) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "LocationMaster{" + "id=" + id + ", name=" + name + ", type=" + type + ", uniqueId=" + uniqueId + ", address=" + address + ", pinCode=" + pinCode + ", contact1Phone=" + contact1Phone + ", contact1Name=" + contact1Name + ", contact1Email=" + contact1Email + ", contact2Phone=" + contact2Phone + ", contact2Name=" + contact2Name + ", contact2Email=" + contact2Email + ", isActive=" + isActive + ", isArchive=" + isArchive + ", createdBy=" + createdBy + ", createdOn=" + createdOn + ", modifiedBy=" + modifiedBy + ", associatedUser=" + associatedUser + ", maxUsers=" + maxUsers + ", modifiedOn=" + modifiedOn + ", parent=" + parent + ", parentMaster=" + parentMaster + ", isTbaAvailable=" + isTbaAvailable + ", total_population=" + totalPopulation + ", hierarchyType=" + hierarchyType + ", locationCode=" + locationCode + ", state=" + state + '}';
    }

    /**
     * Define fields name for location_master.
     */
    public static class Fields {

        private Fields(){
        }

        public static final String SERIAL_VERSION_UID = "serialVersionUID";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String UNIQUE_ID = "uniqueId";
        public static final String ADDRESS = "address";
        public static final String PIN_CODE = "pinCode";
        public static final String CONTACT1_PHONE = "contact1Phone";
        public static final String CONTACT1_NAME = "contact1Name";
        public static final String CONTACT1_EMAIL = "contact1Email";
        public static final String CONTACT2_PHONE = "contact2Phone";
        public static final String CONTACT2_NAME = "contact2Name";
        public static final String CONTACT2_EMAIL = "contact2Email";
        public static final String IS_ACTIVE = "isActive";
        public static final String IS_ARCHIVE = "isArchive";
        public static final String CREATED_BY = "createdBy";
        public static final String CREATED_ON = "createdOn";
        public static final String MODIFIED_BY = "modifiedBy";
        public static final String ASSOCIATED_USER = "associatedUser";
        public static final String MAX_USERS = "maxUsers";
        public static final String MODIFIED_ON = "modifiedOn";
        public static final String PARENT = "parent";
        public static final String IS_TBA_AVAILABLE = "isTbaAvailable";
        public static final String HIERARCHY_TYPE = "hierarchyType";
        public static final String LOCATION_CODE = "locationCode";
        public static final String STATE = "state";
        public static final String PARENT_MASTER = "parentMaster";
        public static final String IS_TAAHO = "isTaaho";
    }

    public enum LocationMasterJoin implements IJoinEnum {

        PARENT_MASTER(Fields.PARENT_MASTER, Fields.PARENT_MASTER, JoinType.LEFT),
        HIERARCHY_TYPE(Fields.HIERARCHY_TYPE, Fields.HIERARCHY_TYPE, JoinType.LEFT);

        private String value;
        private String alias;
        private JoinType joinType;

        public String getValue() {
            return value;
        }

        public String getAlias() {
            return alias;
        }

        public JoinType getJoinType() {
            return joinType;
        }

        LocationMasterJoin(String value, String alias, JoinType joinType) {
            this.value = value;
            this.alias = alias;
            this.joinType = joinType;
        }
    }

    public enum State {

        ACTIVE,
        INACTIVE
    }

}
