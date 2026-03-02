package com.argusoft.medplat.web.healthinfra.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.web.healthinfra.service.HealthInfrastructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health-infra")
@Tag(name = "Health Infrastructure Controller", description = "")
public class HealthInfrastructureController {

    @Autowired
    private HealthInfrastructureService healthInfrastructureService;

    @RequestMapping(value = "/toggleactive/{id}", method = RequestMethod.POST)
    public void toggleActive(@PathVariable Integer id,
                             @RequestParam("state") String state) {
        healthInfrastructureService.toggleActive(id, state);
    }

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