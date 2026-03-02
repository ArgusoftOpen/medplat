/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.users.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.common.util.AESEncryption;
import com.argusoft.medplat.common.util.IJoinEnum;

import javax.persistence.*;
import javax.persistence.criteria.JoinType;
import java.io.Serializable;
import java.util.Date;

/**
 *<p>Defines fields related to user</p>
 * @author vaishali
 * @since 26/08/2020 5:30
 */
@Entity
@Table(name = "um_user",
        indexes = {
            @Index(columnList = "user_name", name = "um_user_user_name_idx"),
            @Index(columnList = "state", name = "um_user_state_idx"),
            @Index(columnList = "aadhar_number", name = "um_user_aadhar_number_idx"),
            @Index(columnList = "role_id", name = "um_user_role_id_idx")
        })
public class UserMaster extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "user_name", nullable = false, length = 30, unique = true)
    private String userName;
    @Column(name = "password", length = 50)
    private String password;
    @Column(name = "email_id", length = 150)
    private String emailId;
    @Column(name = "contact_number", nullable = false, length = 15)
    private String contactNumber;
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    @Column(name = "middle_name", length = 100)
    private String middleName;
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    @Column(name = "gender", length = 15)
    private String gender;
    @Column(name = "address", length = 100)
    private String address;
    @Column(name = "prefered_language", length = 30)
    private String prefferedLanguage;
    @Column(name = "report_preferred_language", length = 30)
    private String reportPreferredLanguage;
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;
    @Column(name = "code", length = 255)
    private String code;
    @Column(name = "aadhar_number", length = 255)
    private String aadharNumber;
    @Column(name = "aadhar_number_encrypted", length = 255)
    private String aadharNumberEncrypted;
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RoleMaster role;
    @Column(name = "role_id")
    private Integer roleId;
    @Column(name = "server_type", length = 10)
    private String serverType;
    @Column(name = "search_text", length = 1000)
    private String searchText;
    @Column(name = "title", length = 10)
    private String title;
    @Column(name = "imei_number", length = 100)
    private String imeiNumber;
    @Column(name = "techo_phone_number", length = 100)
    private String techoPhoneNumber;
    @Column(name = "sdk_version")
    private Integer sdkVersion;
    @Column(name = "free_space_mb")
    private Integer freeSpaceMB;
    @Column(name = "latitude")
    private String latitude;
    @Column(name = "longitude")
    private String longitude;
    @Column(name = "no_of_attempts")
    private Integer noOfAttempts;
    @Column(name = "rch_institution_master_id")
    private Integer rchInstitutionId;
    @Column(name = "infrastructure_id")
    private Integer infrastructureId;

    @Column(name = "login_code")
    private String loginCode;

    @Column(name = "convox_id")
    private String convoxId;
    
    @Column(name = "member_master_id")
    private Integer memberId;
    
    @Column(name = "location_id")
    private Integer locationId;
    
    @Column(name = "pincode")
    private Integer pinCode;

    @Column(name = "pin")
    private String pin;

    @Column(name = "first_time_password_changed")
    private Boolean firstTimePasswordChanged;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrefferedLanguage() {
        return prefferedLanguage;
    }

    public void setPrefferedLanguage(String prefferedLanguage) {
        this.prefferedLanguage = prefferedLanguage;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAadharNumber() {
        return aadharNumberEncrypted != null ? AESEncryption.getInstance().decrypt(aadharNumberEncrypted) : null;
    }

    public void setAadharNumber(String aadharNumber) {
        if (aadharNumber != null) {
            this.aadharNumberEncrypted = AESEncryption.getInstance().encrypt(aadharNumber);
        } else {
            this.aadharNumberEncrypted = null;
        }
        this.aadharNumber = aadharNumber;
    }

    public String getAadharNumberEncrypted() {
        return aadharNumberEncrypted != null ? AESEncryption.getInstance().decrypt(aadharNumberEncrypted) : null;
    }

    public void setAadharNumberEncrypted(String aadharNumberEncrypted) {
        this.aadharNumberEncrypted = AESEncryption.getInstance().encrypt(aadharNumberEncrypted);
    }

    public RoleMaster getRole() {
        return role;
    }

    public void setRole(RoleMaster role) {
        this.role = role;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getTechoPhoneNumber() {
        return techoPhoneNumber;
    }

    public void setTechoPhoneNumber(String techoPhoneNumber) {
        this.techoPhoneNumber = techoPhoneNumber;
    }

    public Integer getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(Integer sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public Integer getFreeSpaceMB() {
        return freeSpaceMB;
    }

    public void setFreeSpaceMB(Integer freeSpaceMB) {
        this.freeSpaceMB = freeSpaceMB;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getNoOfAttempts() {
        return noOfAttempts == null ? 0 : noOfAttempts;
    }

    public void setNoOfAttempts(Integer noOfAttempts) {
        this.noOfAttempts = noOfAttempts;
    }

    public Integer getRchInstitutionId() {
        return rchInstitutionId;
    }

    public void setRchInstitutionId(Integer rchInstitutionId) {
        this.rchInstitutionId = rchInstitutionId;
    }

    public Integer getInfrastructureId() {
        return infrastructureId;
    }

    public void setInfrastructureId(Integer infrastructureId) {
        this.infrastructureId = infrastructureId;
    }

    public String getReportPreferredLanguage() {
        return reportPreferredLanguage;
    }

    public void setReportPreferredLanguage(String reportPreferredLanguage) {
        this.reportPreferredLanguage = reportPreferredLanguage;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public String getConvoxId() {
        return convoxId;
    }

    public void setConvoxId(String convoxId) {
        this.convoxId = convoxId;
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

    public Integer getPinCode() {
        return pinCode;
    }

    public void setPinCode(Integer pinCode) {
        this.pinCode = pinCode;
    }

    public Boolean getFirstTimePasswordChanged() {
        return firstTimePasswordChanged;
    }

    public void setFirstTimePasswordChanged(Boolean firstTimePasswordChanged) {
        this.firstTimePasswordChanged = firstTimePasswordChanged;
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {
            
        }

        public static final String ID = "id";
        public static final String USER_NAME = "userName";
        public static final String LOGIN_CODE = "loginCode";
        public static final String PASSWORDSTR = "password";
        public static final String EMAIL_ID = "emailId";
        public static final String CONTACT_NUMBER = "contactNumber";
        public static final String FIRST_NAME = "firstName";
        public static final String MIDDLE_NAME = "middleName";
        public static final String LAST_NAME = "lastName";
        public static final String GENDER = "gender";
        public static final String ADDRESS = "address";
        public static final String PREFFERED_LANGUAGE = "prefferedLanguage";
        public static final String REPORT_PREFERRED_LANGUAGE = "reportPreferredLanguage";
        public static final String DATE_OF_BIRTH = "dateOfBirth";
        public static final String STATE = "state";
        public static final String CODE = "code";
        public static final String AADHAR_NUMBER = "aadharNumber";
        public static final String ROLE = "role";
        public static final String ROLE_ID = "roleId";
        public static final String SERVER_TYPE = "SERVER_TYPE";
        public static final String SEARCH_TEXT = "searchText";
        public static final String NO_OF_ATTEMPTS = "noOfAttempts";
        public static final String RCH_INSTITUTION_ID = "rchInstitutionId";
        public static final String TECHO_PHONE_NUMBER = "techoPhoneNumber";
        public static final String IMEI_NUMBER = "imeiNumber";
        public static final String SDK_VERSION = "sdkVersion";
        public static final String FREE_SPACE_MB = "freeSpaceMB";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String LOGINCODE = "loginCode";
        public static final String CONVOXID = "convoxId";
        public static final String ACTIVE = "ACTIVE";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "UserMaster{" + "id=" + id + ", userName=" + userName + ", password=" + password + ", emailId=" + emailId + ", convoxId=" + convoxId + ", contactNumber=" + contactNumber + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + ", gender=" + gender + ", address=" + address + ", prefferedLanguage=" + prefferedLanguage + ", dateOfBirth=" + dateOfBirth + ", state=" + state + ", code=" + code + ", aadharNumber=" + aadharNumber + ", aadharNumberEncrypted=" + aadharNumberEncrypted + ", role=" + role + ", roleId=" + roleId + ", SERVER_TYPE=" + serverType + ", searchText=" + searchText + ", title=" + title + ", imeiNumber=" + imeiNumber + ", techoPhoneNumber=" + techoPhoneNumber + ", noOfAttempts=" + noOfAttempts + ", rchInstitutionId=" + rchInstitutionId + ", pin=" + pin +'}';
    }

    /**
     * Defines value of states
     */
    public enum State {

        ACTIVE,
        INACTIVE,
        ARCHIVED,
        UNVERIFIED,
        DISAPPROVED
    }

    /**
     * Defines join entity of user
     */
    public enum UserMasterJoin implements IJoinEnum {

        ROLE(Fields.ROLE, Fields.ROLE, JoinType.LEFT);

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

        UserMasterJoin(String value, String alias, JoinType joinType) {
            this.value = value;
            this.alias = alias;
            this.joinType = joinType;
        }

    }

}
