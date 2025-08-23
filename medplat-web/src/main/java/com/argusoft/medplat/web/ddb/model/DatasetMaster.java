package com.argusoft.medplat.web.ddb.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dataset_master")
@Getter
@Setter
public class DatasetMaster extends EntityAuditInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;

    @Column(name = "dataset_name", columnDefinition = "TEXT")
    private String datasetName;

    @Column(name = "sql_query", columnDefinition = "TEXT")
    private String sqlQuery;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DatasetMaster)) {
            return false;
        }
        DatasetMaster other = (DatasetMaster) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    public static class DatasetMasterFields {
        private DatasetMasterFields() {}
        public static final String ID = "id";
        public static final String DATASET_NAME = "datasetName";
        public static final String SQL_QUERY = "sqlQuery";
    }
}
