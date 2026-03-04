package com.argusoft.medplat.web.healthinfra.model;

import lombok.Data;

@Data
public class HealthInfrastructureMatchDetail {

    private Integer id;
    private Integer type;
    private String address;
    private String name;
    private String nameInEnglish;
    private Integer locationId;
    private String locationName;
    private String hfrFacilityId;

}
