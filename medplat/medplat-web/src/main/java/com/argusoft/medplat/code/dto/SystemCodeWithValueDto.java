package com.argusoft.medplat.code.dto;

import lombok.Data;

/**
 * <p>
 * Define dto for system code along with its value
 * </p>
 *
 * @author monika
 * @since 11/05/21 4:10 PM
 */
@Data
public class SystemCodeWithValueDto {
    private Integer id;
    private String value;
    private String code;
}
