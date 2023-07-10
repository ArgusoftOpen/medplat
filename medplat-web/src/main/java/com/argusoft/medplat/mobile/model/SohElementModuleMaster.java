package com.argusoft.medplat.mobile.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "soh_element_module_master")
public class SohElementModuleMaster extends EntityAuditInfo implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "module")
    private String module;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "module_order")
    private Integer moduleOrder;

    @Column(name = "state")
    private String state;

    @Column(name="footer_description")
    private String footerDescription;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Integer getModuleOrder() {
        return moduleOrder;
    }

    public void setModuleOrder(Integer moduleOrder) {
        this.moduleOrder = moduleOrder;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFooterDescription() {
        return footerDescription;
    }

    public void setFooterDescription(String footerDescription) {
        this.footerDescription = footerDescription;
    }
}
