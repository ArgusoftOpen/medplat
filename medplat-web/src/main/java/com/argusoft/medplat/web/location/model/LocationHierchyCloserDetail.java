/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.argusoft.medplat.web.location.model;

import com.argusoft.medplat.common.util.IJoinEnum;

import javax.persistence.*;
import javax.persistence.criteria.JoinType;
import java.io.Serializable;

/**
 *
 * <p>
 *     Define location_hierchy_closer_det entity and its fields.
 * </p>
 * @author Harshit
 * @since 26/08/20 11:00 AM
 *
 */
@Entity
@Table(name = "location_hierchy_closer_det")
public class LocationHierchyCloserDetail implements Serializable {

    private static final int serialVersionUID = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "parent_id", nullable = false)
    private Integer parentId;
    @Column(name = "child_id", nullable = false)
    private Integer childId;
    @Column(name = "parent_loc_type", nullable = false)
    private String parentLocationType;
    @Column(name = "child_loc_type", nullable = false)
    private String childLocationType;
    @Column(name = "depth", nullable = false)
    private Integer depth;
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LocationMaster parentLocationDetail;
    @JoinColumn(name = "child_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private LocationMaster childLocationDetail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public String getParentLocationType() {
        return parentLocationType;
    }

    public void setParentLocationType(String parentLocationType) {
        this.parentLocationType = parentLocationType;
    }

    public String getChildLocationType() {
        return childLocationType;
    }

    public void setChildLocationType(String childLocationType) {
        this.childLocationType = childLocationType;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public LocationMaster getParentLocationDetail() {
        return parentLocationDetail;
    }

    public void setParentLocationDetail(LocationMaster parentLocationDetail) {
        this.parentLocationDetail = parentLocationDetail;
    }

    public LocationMaster getChildLocationDetail() {
        return childLocationDetail;
    }

    public void setChildLocationDetail(LocationMaster childLocationDetail) {
        this.childLocationDetail = childLocationDetail;
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
        if (!(object instanceof LocationHierchyCloserDetail)) {
            return false;
        }
        LocationHierchyCloserDetail other = (LocationHierchyCloserDetail) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    /**
     * Define fields name for location_hierchy_closer_det.
     */
    public static class Fields {

        private Fields(){
        }

        public static final String SERIAL_VERSION_UID = "serialVersionUID";
        public static final String ID = "id";
        public static final String PARENT_ID = "parentId";
        public static final String CHILD_ID = "childId";
        public static final String PARENT_LOCATION_TYPE = "parentLocationType";
        public static final String CHILD_LOCATION_TYPE = "childLocationType";
        public static final String DEPTH = "depth";
        public static final String PARENT_LOCATION_DETAIL = "parentLocationDetail";
        public static final String CHILD_LOCATION_DETAIL = "childLocationDetail";
    }

    public enum LocationHierchyCloserDetailJoin implements IJoinEnum {

        PARENT_LOCATION_DETAIL(Fields.PARENT_LOCATION_DETAIL, Fields.PARENT_LOCATION_DETAIL, JoinType.LEFT),
        CHILD_LOCATION_DETAIL(Fields.CHILD_LOCATION_DETAIL, Fields.CHILD_LOCATION_DETAIL, JoinType.LEFT);
        
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

        LocationHierchyCloserDetailJoin(String value, String alias, JoinType joinType) {
            this.value = value;
            this.alias = alias;
            this.joinType = joinType;
        }
    }
    @Override
    public String toString() {
        return "com.argusoft.sewa.model.Location[id=" + id + "]";
    }

}
