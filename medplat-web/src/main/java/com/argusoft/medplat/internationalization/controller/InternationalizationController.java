package com.argusoft.medplat.internationalization.controller;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.argusoft.medplat.internationalization.service.InternationalizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
@Tag(name = "Internationalization Controller", description = "")
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

    /**
     * Get all labels for a given language and app name.
     *
     * @param language Language code (e.g., EN, HI, GU).
     * @param appName Application name (e.g., WEB). Defaults to WEB.
     * @return Map of label key to translated text.
     */
    @GetMapping(value = "/labels")
    public Map<String, String> getLabelsByLanguage(
            @RequestParam("language") String language,
            @RequestParam(value = "appName", defaultValue = "WEB") String appName) {
        return internationalizationService.getLabelsByLanguageAndAppName(language, appName);
    }
}