package com.argusoft.medplat.internationalization.service;

/**
 * <p>
 *     Define services for internationalization.
 * </p>
 * @author dhaval
 * @since 25/08/20 4:00 PM
 *
 */
public interface InternationalizationService {

     /**
      * Load labels for all languages.
      */
     void loadAllLanguageLabels();

     /**
      * Get label by key and language code.
      * @param key Key of label.
      * @param languageCode Language code like EN, GU etc.
      * @return Returns label name by key and language code.
      */
     String getLabelByKeyAndLanguageCode(String key, String languageCode);

     /**
      * Update labels.
      */
     void updateLabelsMap();
}
