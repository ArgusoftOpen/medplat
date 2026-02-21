package com.argusoft.medplat.web.ddb.dto;

import lombok.Data;
import java.util.Date;

/**
 * Defines fields for dataset master dto
 * @author ashwin
 * @since 23/08/2025 15:30
 */
@Data
public class DatasetMasterDto {
    private Integer id;
    private String datasetName;
    private String sqlQuery;
    private Integer createdBy;
    private Date createdOn;
    private Integer modifiedBy;
    private Date modifiedOn;
}
