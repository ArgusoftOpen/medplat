package com.argusoft.medplat.web.users.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
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
@Table(name = "user_menu_item",
        indexes = {
            @Index(columnList = "user_id", name = "user_menu_item_user_id_idx"),
            @Index(columnList = "role_id", name = "user_menu_item_role_id_idx")
        })
public class UserMenuItem extends EntityAuditInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_menu_id")
    private Integer id;
    @Column(name = "menu_config_id")
    private Integer menuConfigId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "role_id")
    private Integer roleId;
    @Size(max = 5000)
    @Column(name = "feature_json")
    private String featureJson;

    @Override
    public String toString() {
        return "UserMenuItem{" + "id=" + id + ", menuConfigId=" + menuConfigId + ", userId=" + userId + ", roleId=" + roleId + "}";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMenuConfigId() {
        return menuConfigId;
    }

    public void setMenuConfigId(Integer menuConfigId) {
        this.menuConfigId = menuConfigId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer designationId) {
        this.roleId = designationId;
    }

    public String getFeatureJson() {
        return featureJson;
    }

    public void setFeatureJson(String featureJson) {
        this.featureJson = featureJson;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (null != id ? id.hashCode() : 0);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UserMenuItem)) {
            return false;
        }
        UserMenuItem other = (UserMenuItem) obj;
        return !((null == this.id && null != other.id) || (null != this.id && !this.id.equals(other.id)));
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {
            
        }

        public static final String ID = "id";
        public static final String MENU_CONFIG_ID = "menuConfigId";
        public static final String USER_ID = "userId";
        public static final String ROLE_ID = "roleId";
        public static final String FEATURE_JSON = "featureJson";
    }
}
