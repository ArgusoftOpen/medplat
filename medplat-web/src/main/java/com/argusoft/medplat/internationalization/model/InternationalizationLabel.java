package com.argusoft.medplat.internationalization.model;

import com.argusoft.medplat.common.model.EntityAuditInfo;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 * <p>
 *     Define internationalization_label_master entity and its fields.
 * </p>
 * @author dhaval
 * @since 26/08/20 11:00 AM
 *
 */
@Embeddable
@Table(name = "internationalization_label_master")
public class InternationalizationLabel extends EntityAuditInfo implements Serializable {

    @Column(name = "country")
    private String country;
    @Column(name = "key")
    private String key;
    @Column(name = "language")
    private String language;
    @Column(name = "custom3b")
    private Boolean custom3b;
    @Column(name = "text")
    private String text;
    @Column(name = "translation_pending")
    private String translationPending;
    @Column(name = "app_name")
    private String appName;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getCustom3b() {
        return custom3b;
    }

    public void setCustom3b(Boolean custom3b) {
        this.custom3b = custom3b;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslationPending() {
        return translationPending;
    }

    public void setTranslationPending(String translationPending) {
        this.translationPending = translationPending;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "InternationalizationLabel{" +
                "country='" + country + '\'' +
                ", key='" + key + '\'' +
                ", language='" + language + '\'' +
                ", custom3b=" + custom3b +
                ", text='" + text + '\'' +
                ", translationPending='" + translationPending + '\'' +
                ", appName='" + appName + '\'' +
                '}';
    }

    public static class Fields {
        public static final String COUNTRY = "country";
        public static final String KEY = "key";
        public static final String LANGUAGE = "language";
        public static final String CUSTOM_3B = "custom3b";
        public static final String TEXT = "text";
        public static final String TRANSLATION_PENDING = "translationPending";
        public static final String APP_NAME = "appName";
    }
}
