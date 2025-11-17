package com.argusoft.medplat.web.ddb.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Defines fields for dataset master
 * @author ashwin
 * @since 23/08/2025 15:30
 */
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

    /**
     * An util class for string constants of dataset master
     */
    public static class DatasetMasterFields {
        private DatasetMasterFields() {}

        public static final String ID = "id";
        public static final String DATASET_NAME = "datasetName";
        public static final String SQL_QUERY = "sqlQuery";
    }
}
