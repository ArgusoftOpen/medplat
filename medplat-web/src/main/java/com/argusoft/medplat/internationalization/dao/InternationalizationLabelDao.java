package com.argusoft.medplat.internationalization.dao;

import com.argusoft.medplat.database.common.GenericDao;
import com.argusoft.medplat.internationalization.model.InternationalizationLabel;

import java.util.List;

/**
 * <p>
 * Define methods for internationalization.
 * </p>
 *
 * @author dhaval
 * @since 26/08/20 10:19 AM
 */
public interface InternationalizationLabelDao extends GenericDao<InternationalizationLabel, Long> {
    /**
     * Retrieves labels for all languages.
     *
     * @return Returns list of all labels.
     */
    List<InternationalizationLabel> getAllLanguageLabels();

    /**
     * Retrieves all labels after given date.
     *
     * @param labelsMapLastUpdatedAt Last updated date.
     * @return Returns list of labels.
     */
    List<InternationalizationLabel> getAllLanguageLabelsAfterGivenDate(String labelsMapLastUpdatedAt);

    InternationalizationLabel getLabelByKeyLanguageAndCountry(String key, String language, String country, String appName);

    void createOrUpdateLabel(String key, String value, String language, String country, String appName);
}
