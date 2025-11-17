package com.argusoft.medplat.web.ddb.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Defines fields for indicator master
 * @author ashwin
 * @since 23/08/2025 15:30
 */
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

    /**
     * An util class for string constants of indicator master
     */
    public static class IndicatorMasterFields {
        private IndicatorMasterFields() {}

        public static final String ID = "id";
        public static final String INDICATOR_NAME = "indicatorName";
        public static final String DESCRIPTION = "description";
        public static final String SQL_QUERY = "sqlQuery";
        public static final String QUERY_RESULT = "queryResult";
    }
}
