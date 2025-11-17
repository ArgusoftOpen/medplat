package com.argusoft.medplat.web.ddb.dto;

import lombok.Data;
import java.util.Date;

/**
 * Defines fields for indicator master dto
 * @author ashwin
 * @since 23/08/2025 15:30
 */
@Data
public class IndicatorMasterDto {
    private Integer id;
    private String indicatorName;
    private String description;
    private String sqlQuery;
    private Integer queryResult;
    private Integer createdBy;
    private Date createdOn;
    private Integer modifiedBy;
    private Date modifiedOn;
}
