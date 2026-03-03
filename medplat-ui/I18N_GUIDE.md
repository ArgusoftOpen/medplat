# MEDplat Multilingual (i18n) Implementation Guide

## Overview

This document provides comprehensive guidance on the MEDplat platform's new multilingual (i18n) system. The implementation supports dynamic language switching with full offline capability, fallback languages, and easy language addition.

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Supported Languages](#supported-languages)
3. [File Structure](#file-structure)
4. [Features](#features)
5. [Usage Guidelines](#usage-guidelines)
6. [Adding New Languages](#adding-new-languages)
7. [Translation Management](#translation-management)
8. [Offline Support](#offline-support)
9. [API Reference](#api-reference)
10. [Troubleshooting](#troubleshooting)

---

## Architecture Overview

The MEDplat i18n system is built on:

- **Angular Translate** - Industry-standard AngularJS translation library
- **Custom Language Service** - Manages language selection, persistence, and switching
- **Translation Cache Service** - Provides offline support via localStorage
- **Translation Loader Factory** - Handles dynamic translation file loading with fallback

### System Flow

```
User Action (Language Switch)
    ↓
Language Service
    ↓
Angular Translate
    ↓
Translation Loader Factory
    ↓
Cache Check → File Load → Cache Update
    ↓
DOM Update (Instant)
```

---

## Supported Languages

Currently, MEDplat supports the following languages:

| Code | Language | Native Name | Flag |
|------|----------|-------------|------|
| `en` | English | English | 🇬🇧 |
| `hi` | Hindi | हिंदी | 🇮🇳 |
| `gu` | Gujarati *(deprecated)* | ગુજરાતી | 🇮🇳 |

### Default Language
- **Primary**: English (`en`)
- **Fallback**: English (`en`)

---

## File Structure

```
medplat-ui/
├── app/
│   ├── locales/              # Translation files
│   │   ├── en.json          # English translations
│   │   ├── hi.json          # Hindi translations
│   │   └── gu.json          # Gujarati translations (deprecated, file may be removed)
│   │
│   ├── common/
│   │   ├── services/
│   │   │   └── i18n/        # i18n services
│   │   │       ├── language.service.js           # Language management
│   │   │       ├── translation-cache.service.js  # Offline support
│   │   │       └── translation-loader.factory.js # Dynamic loading
│   │   │
│   │   ├── controllers/
│   │   │   └── language-switcher.controller.js   # UI Controller
│   │   │
│   │   ├── directives/
│   │   │   └── language-switcher.html            # Language selector UI
│   │   │
│   │   └── views/
│   │       ├── layout.html                       # Navigation with i18n
│   │       └── update-user-profile.html          # Profile with i18n
│   │
│   ├── login/
│   │   └── views/
│   │       └── login.html                        # Login page with i18n
│   │
│   ├── app.js                # Angular app config with i18n
│   └── config.lazyload.js    # Lazy load i18n services
│
└── styles/
    └── css/
        └── language-switcher.css  # Styling for language switcher
```

---

## Features

### 1. Dynamic Language Switching
- Switch languages without page reload
- Instant UI update
- Language preference persisted in localStorage
- Broadcasting of language change events

### 2. Offline Support
- Translation files cached in localStorage
- Works seamlessly without internet connectivity
- Automatic fallback to cached translations if API fails
- Cache version management

### 3. Fallback Language Mechanism
- Primary: User's selected language
- Secondary: System default (English)
- Tertiary: Cached version if available
- Graceful degradation if translation is missing

### 4. Component Coverage
- Login & Authentication pages
- Navigation & Layout
- User profile & preferences
- All form elements and buttons
- Dialog & Modal messages
- Error & Success notifications

### 5. RTL Language Support
- Automatic direction (dir) attribute setting
- Ready for Arabic, Hebrew, Urdu (future expansion)

---

## Usage Guidelines

### In Templates (HTML)

#### Basic Translation
```html
<!-- Simple string translation -->
<h3>{{ 'LOGIN.TITLE' | translate }}</h3>

<!-- With placeholders -->
<p>{{ 'WELCOME_MESSAGE' | translate: {name: userFullName} }}</p>

<!-- Attribute translation -->
<input placeholder="{{ 'LOGIN.USERNAME_PLACEHOLDER' | translate }}">
```

#### Conditional Translation
```html
<div ng-if="true">
    {{ 'PROFILE.NOT_AVAILABLE' | translate }}
</div>
```

#### Directive Translation
```html
<span ng-click="validate" title="{{ 'FORM_COMMON.REQUIRED' | translate }}">
    {{ 'FORM_COMMON.SAVE' | translate }}
</span>
```

### In Controllers (JavaScript)

#### Get Current Language
```javascript
var currentLang = LanguageService.getCurrentLanguage(); // 'en'
```

#### Switch Language
```javascript
LanguageService.setLanguage('hi').then(function(lang) {
    console.log('Language switched to:', lang);
}).catch(function(err) {
    console.error('Error switching language:', err);
});
```

#### Get Supported Languages
```javascript
var languages = LanguageService.getSupportedLanguages();
// Returns: [{code: 'en', name: 'English', flag: '🇬🇧'}, ...]
```

#### Get Language Info
```javascript
var info = LanguageService.getLanguageInfo('hi');
// Returns: {code: 'hi', name: 'हिंदी', flag: '🇮🇳'}
```

#### Listen to Language Changes
```javascript
$scope.$on('languageChanged', function(event, data) {
    console.log('Language changed to:', data.language);
    console.log('Language name:', data.name);
});
```

#### Check RTL Language
```javascript
if (LanguageService.isRTL('ar')) {
    // Handle RTL layout
}
```

#### Translate in Code
```javascript
function TranslateInCode($translate) {
    $translate('LOGIN.TITLE').then(function(translation) {
        console.log('Translated text:', translation);
    });
}
```

---

## Adding New Languages

### Step 1: Create Translation File

Create a new JSON file in `app/locales/` named after the language code (e.g., `app/locales/ta.json` for Tamil):

```json
{
  "APP": {
    "NAME": "MEDplat",
    "DESCRIPTION": "Translation in new language"
  },
  "LOGIN": {
    "TITLE": "Login in new language",
    "USERNAME_PLACEHOLDER": "Username in new language",
    "PASSWORD_PLACEHOLDER": "Password in new language",
    ...
  }
  // Copy all keys from en.json and translate the values
}
```

**Important**: Ensure all keys from `en.json` are present in the new translation file. Missing keys will display the English fallback.

### Step 2: Register Language in Language Service

Edit `app/common/services/i18n/language.service.js` and add to `SUPPORTED_LANGUAGES`:

```javascript
const SUPPORTED_LANGUAGES = {
    'en': { code: 'en', name: 'English', flag: '🇬🇧' },
    'hi': { code: 'hi', name: 'हिंदी', flag: '🇮🇳' },
    'gu': { code: 'gu', name: 'ગુજરાતી', flag: '🇮🇳' },
    'ta': { code: 'ta', name: 'தமிழ்', flag: '🇮🇳' }  // Add new language
};
```

### Step 3: Register in Angular Translate Configuration

Edit `app/app.js` and update the `registerAvailableLanguageKeys` configuration:

```javascript
$translateProvider.registerAvailableLanguageKeys(['en', 'hi', 'gu', 'ta'], {
    'en': 'en',
    'en_US': 'en',
    'en_GB': 'en',
    'hi': 'hi',
    'hi_IN': 'hi',
    'gu': 'gu',
    'gu_IN': 'gu',
    'ta': 'ta',          // Add new language
    'ta_IN': 'ta'        // Add region variant
});
```

### Step 4: Clear Cache (Optional)

If testing locally, clear the translation cache to force reload:

```javascript
TranslationCacheService.clearCache('ta');
```

### Step 5: Test

1. Restart the application
2. Switch to the new language using the language switcher
3. Verify all UI elements display the translations correctly
4. Check browser console for any missing translation warnings

---

## Translation Management

### Translation File Structure

Every translation file (`en.json`, `hi.json`, etc.) should follow this hierarchy:

```json
{
  "APP": {
    "NAME": "App name",
    "DESCRIPTION": "App description"
  },
  "LOGIN": {
    "TITLE": "",
    "USERNAME_PLACEHOLDER": "",
    "PASSWORD_PLACEHOLDER": "",
    "LOGIN_BUTTON": "",
    "LOST_PASSWORD": "",
    "USERNAME_REQUIRED": "",
    "PASSWORD_REQUIRED": ""
  },
  "NAVIGATION": {
    "SEARCH_MENU": "",
    "NO_DATA_FOUND": "",
    "ACTIONS": "",
    ...
  },
  "PROFILE": {
    "PREFERRED_LANGUAGE": "",
    "ENGLISH": "",
    "HINDI": "",
    ...
  },
  "FORM_COMMON": {
    "SAVE": "",
    "CANCEL": "",
    "DELETE": "",
    ...
  },
  "MESSAGES": {
    "SUCCESS": "",
    "ERROR": "",
    "WARNING": "",
    ...
  }
}
```

### Best Practices

1. **Use Hierarchical Keys**: Group related translations (e.g., `LOGIN.TITLE`, `LOGIN.BUTTON`)
2. **Use UPPERCASE for Keys**: Maintains consistency and makes keys stand out in templates
3. **Keep Keys Consistent**: Same key should have the same meaning across all translations
4. **Avoid Dynamic Keys**: Always use static keys. Don't generate key names at runtime
5. **Test All Keys**: Ensure every translation key in the template exists in all language files
6. **Use Comments**: Add comments for context-specific translations
7. **Version Your Translations**: Keep track of translation versions for updates

### Translation Workflow

1. **Add New Feature** → Add translation keys to `en.json`
2. **Update Templates** → Use the new translation keys
3. **Translate** → Add translations for all supported languages
4. **Review** → Verify translations for correctness and context
5. **Deploy** → Push translations with the code

---

## Offline Support

### How It Works

1. **First Load**: Translation files are loaded from the server
2. **Caching**: Translations are automatically cached in localStorage
3. **Offline Access**: Cached translations are used when offline
4. **Cache Update**: Cache is refreshed whenever a new translation is loaded from server

### Cache Details

- **Storage**: Browser's localStorage
- **Cache Key**: `medplat_translation_cache_{language}` (e.g., `medplat_translation_cache_en`)
- **Size**: Typically 20-50 KB per language (well within localStorage limits)
- **Duration**: Persistent until manually cleared

### Manual Cache Management

```javascript
// Clear cache for specific language
TranslationCacheService.clearCache('en');

// Clear all language caches
TranslationCacheService.clearCache();

// Check if language is cached
var isCached = TranslationCacheService.isCacheValid('hi');

// Get list of cached languages
var cachedLangs = TranslationCacheService.getCachedLanguages();

// Get cached translation directly
var translations = TranslationCacheService.getFromCache('en');
```

### Storage Quota

Modern browsers typically allow 5-10 MB per origin. With MEDplat's translations (~40 KB per language), you can cache hundreds of languages safely.

---

## API Reference

### LanguageService

#### `init()`
Initializes the language service. Called automatically on app startup.
```javascript
LanguageService.init();
```

#### `getCurrentLanguage(): string`
Returns the current language code.
```javascript
var lang = LanguageService.getCurrentLanguage(); // 'en'
```

#### `getBrowserLanguage(): string | null`
Returns the browser's preferred language code or null if unsupported.
```javascript
var browserLang = LanguageService.getBrowserLanguage(); // 'hi'
```

#### `setLanguage(languageCode): Promise`
Sets the language and persists preference. Returns a promise.
```javascript
LanguageService.setLanguage('hi').then(function(lang) {
    console.log('Now using:', lang);
});
```

#### `getSupportedLanguages(): Array`
Returns array of all supported language objects.
```javascript
var langs = LanguageService.getSupportedLanguages();
// [{code: 'en', name: 'English', flag: '🇬🇧'}, ...]
```

#### `getLanguageInfo(languageCode): Object`
Returns metadata for a specific language.
```javascript
var info = LanguageService.getLanguageInfo('hi');
// {code: 'hi', name: 'हिंदी', flag: '🇮🇳'}
```

#### `updateUserLanguagePreference(languageCode): void`
Updates user's language preference in backend.
```javascript
LanguageService.updateUserLanguagePreference('hi');
```

#### `isRTL(languageCode): boolean`
Checks if language is right-to-left.
```javascript
if (LanguageService.isRTL('ar')) { /* ... */ }
```

#### `setDirection(languageCode): void`
Sets document direction (dir attribute) for RTL languages.
```javascript
LanguageService.setDirection('ar'); // Sets dir="rtl"
```

### TranslationCacheService

#### `getFromCache(language): Object | null`
Returns cached translation or null if not found.

#### `saveToCache(language, translations): boolean`
Saves translation to cache. Returns success status.

#### `isCacheValid(language): boolean`
Checks if translation is cached.

#### `clearCache(language): boolean`
Clears cache. If language specified, clears only that language.

#### `getCachedLanguages(): Array`
Returns array of all cached language codes.

### TranslationLoaderFactory

Automatically used by Angular Translate. Loads translations with offline fallback.

---

## Troubleshooting

### Issue: Translations not loading

**Symptoms**: UI shows translation keys instead of translated text (e.g., `LOGIN.TITLE`)

**Solutions**:
1. Check browser console for errors
2. Verify translation file exists in `app/locales/{language}.json`
3. Check that language is registered in `language.service.js`
4. Clear browser cache and localStorage
5. Check network tab to see if translation file loads successfully

### Issue: Language won't switch

**Symptoms**: Switching language doesn't update UI

**Solutions**:
1. Ensure `LanguageService.setLanguage()` promise resolves
2. Check if controller initializes `LanguageService`
3. Verify `angular-translate` library is loaded
4. Check for JavaScript errors in console
5. Try refreshing the page

### Issue: Offline translation not working

**Symptoms**: App can't translate when offline

**Solutions**:
1. Ensure you've loaded the language once online (to cache it)
2. Check localStorage quota isn't exceeded
3. Verify browser allows localStorage (not in private mode)
4. Clear cache and reload online to recache
5. Check browser console for storage errors

### Issue: Missing translation keys

**Symptoms**: Some translations appear in English even when translating to another language

**Solutions**:
1. Check translation file has all keys from `en.json`
2. Verify key spelling matches exactly (case-sensitive)
3. Check JSON syntax is valid (use JSONLint)
4. Add missing key to translation file
5. Clear cache and reload

### Issue: Performance degradation

**Symptoms**: App loading slowly, especially on language switch

**Solutions**:
1. Check translation files aren't excessively large (target < 100 KB)
2. Verify network requests complete quickly
3. Clear old caches: `TranslationCacheService.clearCache()`
4. Check localStorage usage
5. Profile app performance with DevTools

### Issue: RTL languages display incorrectly

**Symptoms**: Arabic/Hebrew text appears left-aligned

**Solutions**:
1. Ensure `LanguageService.setDirection()` is called
2. Add RTL language to `isRTL()` check in language.service.js
3. CSS should use `start`/`end` instead of `left`/`right` for RTL compatibility
4. Check browser DevTools for correct `dir="rtl"` attribute

---

## Events

### languageChanged

Fired when language is successfully changed.

```javascript
$scope.$on('languageChanged', function(event, data) {
    console.log('Language: ' + data.language);
    console.log('Name: ' + data.name);
});
```

### languageSwitched

Fired by language switcher component when user switches language.

```javascript
$scope.$on('languageSwitched', function(event, data) {
    console.log('User switched to: ' + data.language);
});
```

---

## Performance Considerations

1. **Translation File Size**: Keep under 100 KB per language
2. **Caching**: Initial load caches translations automatically
3. **Lazy Loading**: Services are lazy-loaded on demand
4. **Memory**: Cached translations use minimal memory
5. **Network**: Translation files are cached after first load

---

## Security Considerations

1. **XSS Prevention**: Use `sanitize` strategy in Angular Translate
2. **Translation Injection**: Always use `| translate` filter, never `ng-bind-html`
3. **User Preferences**: Language preference is stored in localStorage (not secure for sensitive data)
4. **Backend Integration**: Language preference can be synced with user profile

---

## Future Enhancements

- [ ] Remove Gujarati (`gu.json`) translations (completed)- [ ] Add support for more languages (Tamil, Marathi, etc.)
- [ ] Implement translation management dashboard
- [ ] Add RTL language support (Arabic, Hebrew)
- [ ] Integrate with backend translation API
- [ ] Implement plural form support
- [ ] Add date/time localization
- [ ] Add currency localization

---

## Support

For issues or questions:
1. Check this documentation
2. Review browser console for errors
3. Check GitHub issues: https://github.com/ArgusoftOpen/medplat/issues/111
4. Create a new issue with detailed reproduction steps

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2026-03-03 | Initial implementation with English and Hindi support |

---

**Last Updated**: March 3, 2026  
**Maintained By**: MEDplat Development Team  
**License**: ISC
