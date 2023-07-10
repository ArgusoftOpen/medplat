package com.argusoft.medplat.internationalization.controller;

import com.argusoft.medplat.internationalization.service.InternationalizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * <p>
 * Define APIs for internationalization.
 * </p>
 *
 * @author dhaval
 * @since 26/08/20 10:19 AM
 */
@RestController
@RequestMapping("/api/internationalization")
public class InternationalizationController {

    @Autowired
    private InternationalizationService internationalizationService;

    /**
     * Update labels.
     */
    @PostMapping(value = "/updateLabelsMap")
    public void updateLabelsMap() {
        internationalizationService.updateLabelsMap();
    }
}
