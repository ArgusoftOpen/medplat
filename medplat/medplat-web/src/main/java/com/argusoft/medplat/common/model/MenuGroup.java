package com.argusoft.medplat.common.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *<p>Defines fields related to user</p>
 * @author charmi
 * @since 26/08/2020 5:30
 */
@Entity
@NoArgsConstructor
@Table(name = "menu_group")
public class MenuGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Basic(optional = false)
    private Integer id;
    @Column(name = "group_name")
    @Size(max = 100)
    private String groupName;
    @Column(name = "active")
    private Boolean isActive;
    @Column(name = "parent_group")
    private Integer parentGroup;

    @Column(name = "group_type")
    private String type;

    @Column(name="menu_display_order")
    private Integer displayOrder;
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Integer getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(Integer parentGroup) {
        this.parentGroup = parentGroup;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MenuGroup)) {
            return false;
        }
        MenuGroup other = (MenuGroup) obj;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {
            
        }

        public static final String ID = "id";
        public static final String GROUP_NAME = "groupName";
        public static final String IS_ACTIVE = "isActive";
        public static final String PARENT_GROUP = "parentGroup";
        public static final String TYPE = "type";
        public static final String DISPLAY_ORDER = "displayOrder";
    }
}
