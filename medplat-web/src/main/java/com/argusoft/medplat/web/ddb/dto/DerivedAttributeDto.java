package com.argusoft.medplat.web.ddb.dto;

import lombok.Data;
import java.util.Date;

/**
 * Defines fields for derived attribute dto
 * @author ashwin
 * @since 23/08/2025 15:30
 */
@Data
public class DerivedAttributeDto {
    private Integer id;
    private String derivedName;
    private String formula;
    private Double result;
    private Integer createdBy;
    private Date createdOn;
    private Integer modifiedBy;
    private Date modifiedOn;
}
