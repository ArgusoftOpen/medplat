package com.argusoft.medplat.common.model;

import com.argusoft.medplat.common.util.IJoinEnum;
import com.argusoft.medplat.web.users.model.UserMenuItem;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.criteria.JoinType;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 *<p>Defines fields related to user</p>
 * @author charmi
 * @since 26/08/2020 5:30
 */
@Entity
@NoArgsConstructor
@Table(name = "menu_config")
@NamedQuery(name = "MenuConfig.findAll", query = "SELECT m FROM MenuConfig m")
public class MenuConfig implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Basic(optional = false)
    private Integer id;

//    menu_type_uuid
    @Column(name = "uuid")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID menuTypeUUID;

    @Column(name = "group_name_uuid")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID groupNameUUID;

    @Column(name = "sub_group_uuid")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID subGroupUUID;

    @Column(name = "menu_name")
    @Size(max = 200)
    private String name;
    @Column(name = "menu_type")
    @Size(max = 100)
    private String type;
    @Column(name = "group_id")
    private Integer groupId;
    @Column(name = "sub_group_id")
    private Integer subGroupId;
    @Column(name = "active")
    private Boolean isActive;
    @Column(name = "navigation_state")
    private String navigationState;

    @Column(name = "is_dynamic_report")
    private Boolean isDynamicReport;

    @Column(name = "feature_json")
    @Size(max = 100)
    private String featureJson;

    @Column(name = "only_admin")
    private Boolean onlyAdmin;

    @Column(name = "menu_display_order")
    private Integer displayOrder;

    @OneToMany(mappedBy = "menuConfigId", fetch = FetchType.LAZY)
    private List<UserMenuItem> userMenuItemList;
    @JoinColumn(name = "group_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;
    @JoinColumn(name = "sub_group_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup subGroup;

    @Column(name = "description")
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getMenuTypeUUID() {
        return menuTypeUUID;
    }

    public void setMenuTypeUUID(UUID menuTypeUUID) {
        this.menuTypeUUID = menuTypeUUID;
    }

    public UUID getGroupNameUUID() {
        return groupNameUUID;
    }

    public void setGroupNameUUID(UUID groupNameUUID) {
        this.groupNameUUID = groupNameUUID;
    }

    public UUID getSubGroupUUID() {
        return subGroupUUID;
    }

    public void setSubGroupUUID(UUID subGroupUUID) {
        this.subGroupUUID = subGroupUUID;
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

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getNavigationState() {
        return navigationState;
    }

    public void setNavigationState(String navigationState) {
        this.navigationState = navigationState;
    }

    public Integer getSubGroupId() {
        return subGroupId;
    }

    public void setSubGroupId(Integer subGroupId) {
        this.subGroupId = subGroupId;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public MenuGroup getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(MenuGroup subGroup) {
        this.subGroup = subGroup;
    }

    public List<UserMenuItem> getUserMenuItemList() {
        return userMenuItemList;
    }

    public void setUserMenuItemList(List<UserMenuItem> userMenuItemList) {
        this.userMenuItemList = userMenuItemList;
    }

    public Boolean getIsDynamicReport() {
        return isDynamicReport;
    }

    public void setIsDynamicReport(Boolean isDynamicReport) {
        this.isDynamicReport = isDynamicReport;
    }

    public String getFeatureJson() {
        return featureJson;
    }

    public void setFeatureJson(String featureJson) {
        this.featureJson = featureJson;
    }

    public Boolean getOnlyAdmin() {
        return onlyAdmin;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

        if (!(obj instanceof MenuConfig)) {
            return false;
        }
        MenuConfig other = (MenuConfig) obj;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    /**
     * An util class defines string constant
     */
    public static class Fields {

        private Fields() {
            
        }

        public static final String ID = "id";
        public static final String MENU_TYPE_UUID = "menuTypeUUID";
        public static final String GROUP_NAME_UUID = "groupNameUUID";
        public static final String SUB_GROUP_UUID = "subGroupUUID";
        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String GROUP_ID = "groupId";
        public static final String SUB_GROUP_ID = "subGroupId";
        public static final String IS_ACTIVE = "isActive";
        public static final String NAVIGATION_STATE = "navigationState";
        public static final String IS_DYNAMIC_REPORT = "isDynamicReport";
        public static final String FEATURE_JSON = "featureJson";
        public static final String USER_MENU_ITEM_LIST = "userMenuItemList";
        public static final String USER_MENU_ITEM_LIST_USER = "userMenuItemList.userId";
        public static final String MENU_GROUP = "menuGroup";
        public static final String SUB_GROUP = "subGroup";
        public static final String ONLY_ADMIN = "onlyAdmin";
        public static final String DESCRIPTIO = "description";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "MenuConfig{" + "id=" + id + ", menuTypeUUID=" + menuTypeUUID + ", groupNameUUID=" + groupNameUUID + ", subGroupUUID=" + subGroupUUID + ", name=" + name + ", type=" + type + ", groupId=" + groupId + ", subGroupId=" + subGroupId + ", isActive=" + isActive + ", navigationState=" + navigationState + ", isDynamicReport=" + isDynamicReport + ", featureJson=" + featureJson + ", onlyAdmin=" + onlyAdmin + ", displayOrder=" + displayOrder + ", userMenuItemList=" + userMenuItemList + ",description=" + description + ",menuGroup=" + menuGroup + ", subGroup=" + subGroup + '}';
    }

    /**
     * Defines join entity of menu config
     */
    public enum MenuConfigJoin implements IJoinEnum {

        USER_MENU_ENTITY(Fields.USER_MENU_ITEM_LIST, Fields.USER_MENU_ITEM_LIST, JoinType.LEFT),
        USER_ENTITY(Fields.USER_MENU_ITEM_LIST_USER, "userId", JoinType.LEFT),
        MENU_GROUP(Fields.MENU_GROUP, Fields.MENU_GROUP, JoinType.LEFT),
        SUB_GROUP(Fields.SUB_GROUP, Fields.SUB_GROUP, JoinType.LEFT);

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

        MenuConfigJoin(String value, String alias, JoinType joinType) {
            this.value = value;
            this.alias = alias;
            this.joinType = joinType;
        }
    }
}
