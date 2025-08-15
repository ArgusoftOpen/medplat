package com.argusoft.medplat.web.BackendController.entity;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import javax.persistence.*;

@Entity
@Table(name = "dataset_master")
public class DatasetMaster extends EntityAuditInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dataset_name", nullable = false)
    private String datasetName;

    @Column(name = "sql_query", nullable = false)
    private String sqlQuery;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }
}