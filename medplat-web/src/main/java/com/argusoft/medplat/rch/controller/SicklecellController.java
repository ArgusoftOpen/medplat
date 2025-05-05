package com.argusoft.medplat.rch.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.rch.dto.SicklecellScreeningDto;
import com.argusoft.medplat.rch.service.SicklecellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Define APIs for sickle cell.
 * </p>
 *
 * @author smeet
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/sicklecell")
@Tag(name = "Sicklecell Controller", description = "")
public class SicklecellController {

    @Autowired
    SicklecellService sicklecellService;

    /**
     * Add sickle cell details.
     *
     * @param sicklecellScreeningDto Details of sickle cell.
     */
    @PostMapping(value = "")
    public void create(@RequestBody SicklecellScreeningDto sicklecellScreeningDto) {
        sicklecellService.create(sicklecellScreeningDto);
    }
}