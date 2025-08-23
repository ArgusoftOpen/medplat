package com.argusoft.medplat.web.ddb.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "indicator_master")
@Getter
@Setter
public class IndicatorMaster extends EntityAuditInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "indicator_name", columnDefinition = "TEXT")
    private String indicatorName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "sql_query", columnDefinition = "TEXT")
    private String sqlQuery;

    @Column(name = "query_result")
    private Integer queryResult;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IndicatorMaster)) {
            return false;
        }
        IndicatorMaster other = (IndicatorMaster) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    public static class IndicatorMasterFields {
        private IndicatorMasterFields() {}
        public static final String ID = "id";
        public static final String INDICATOR_NAME = "indicatorName";
        public static final String DESCRIPTION = "description";
        public static final String SQL_QUERY = "sqlQuery";
        public static final String QUERY_RESULT = "queryResult";
    }
}
