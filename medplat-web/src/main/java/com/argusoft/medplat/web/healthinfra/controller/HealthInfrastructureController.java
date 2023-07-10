package com.argusoft.medplat.web.healthinfra.controller;

import com.argusoft.medplat.web.healthinfra.service.HealthInfrastructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health-infra")
public class HealthInfrastructureController {

    @Autowired
    private HealthInfrastructureService healthInfrastructureService;

//    @GetMapping(value = "/get-health-infra-by-id/{id}")
//    public HealthInfrastructureHFRDetails getHealthInfrastructureById(@PathVariable Integer id) {
//        return healthInfrastructureService.getHealthInfrastructureById(id);
//    }

//    @PostMapping(value = "/store-link-hfr-id/{id}")
//    public HealthInfrastructureMatchDetail saveAndLinkHFRId(@PathVariable Integer id,
//                                                            @RequestParam("hfrFacilityId") String hfrFacilityId) {
//        return healthInfrastructureService.saveAndLinkHFRId(id, hfrFacilityId);
//    }
}
