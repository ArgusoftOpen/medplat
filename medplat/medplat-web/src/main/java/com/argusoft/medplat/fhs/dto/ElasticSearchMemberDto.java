package com.argusoft.medplat.fhs.dto;

/**
 *
 * <p>
 *     Used for elastic search.
 * </p>
 * @author jay
 * @since 26/08/20 11:00 AM
 *
 */
public class ElasticSearchMemberDto {

    private Integer id;
    private String uniquehealthid;
    private String firstname;
    private String middlename;
    private String lastname;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUniquehealthid() {
        return uniquehealthid;
    }

    public void setUniquehealthid(String uniquehealthid) {
        this.uniquehealthid = uniquehealthid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
