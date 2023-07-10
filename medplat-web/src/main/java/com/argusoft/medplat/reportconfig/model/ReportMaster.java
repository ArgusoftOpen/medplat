/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.reportconfig.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import com.argusoft.medplat.common.model.MenuConfig;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 *
 * <p>
 *     Define report_master entity and its fields.
 * </p>
 * @author prateek
 * @since 26/08/20 11:00 AM
 *
 */

 
@Entity
@Table(name = "report_master")
@NamedQueries({
    @NamedQuery(name = "ReportMaster.findAll", query = "SELECT r FROM ReportMaster r")})
public class ReportMaster extends EntityAuditInfo implements Serializable {

    private static final int serialVersionUID = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @OneToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private MenuConfig menuConfig;

    @Size(max = 200)
    @Column(name = "report_name")
    private String reportName;

    @Size(max = 500)
    @Column(name = "file_name")
    private String fileName;

    @Size(max = 15)
    @Column(name = "report_type")
    private String reportType;

    @Column(name = "code",length = 20)
    private String code; 
    
    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @OneToMany(mappedBy = "reportMasterId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("id")
    private List<ReportParameterMaster> reportParameterMasterList;
    
    @Basic
    @Column(name = "config_json")
    private String configJson;
    
    
    @Column(name = "UUID")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    private UUID uuid;
    

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }  
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MenuConfig getMenuConfig() {
        return menuConfig;
    }

    public void setMenuConfig(MenuConfig menuConfig) {
        this.menuConfig = menuConfig;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<ReportParameterMaster> getReportParameterMasterList() {
        return reportParameterMasterList;
    }

    public void setReportParameterMasterList(List<ReportParameterMaster> reportParameterMasterList) {
        this.reportParameterMasterList = reportParameterMasterList;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
     

}
