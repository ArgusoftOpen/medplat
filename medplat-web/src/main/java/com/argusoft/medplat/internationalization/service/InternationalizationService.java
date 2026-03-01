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

     /**
      * Get all labels for a given language and app name as a key-value map.
      * @param language Language code (e.g., EN, HI, GU).
      * @param appName Application name (e.g., WEB).
      * @return Returns a map of label key to translated text.
      */
     java.util.Map<String, String> getLabelsByLanguageAndAppName(String language, String appName);
}
