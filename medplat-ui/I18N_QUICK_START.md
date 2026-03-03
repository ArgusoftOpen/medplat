# MEDplat i18n Quick Start Guide

## What's New?

The MEDplat platform now supports dynamic multilingual functionality with full offline capability. Users can switch languages instantly without page reload, and all translations are cached locally for offline access.

## Supported Languages

- **English** (en) 🇬🇧 - Default
- **Hindi** (hi) 🇮🇳
- **Gujarati** (gu) 🇮🇳 *(removed – not currently supported)*

## For End Users

### Switching Languages

1. Look for the **language switcher** in the top navigation bar (next to user menu) or on the login page
2. Click on the current language name or flag
3. Select your preferred language from the dropdown
4. Your preference is automatically saved and will persist on next visit

### Supported Features

- ✅ Switch languages without page reload
- ✅ All UI text updates instantly
- ✅ Works offline (after first load)
- ✅ Language preference persists across sessions
- ✅ Automatic fallback to English if translation is missing

## For Developers

### Using Translations in Templates

```html
<!-- Simple translation -->
<h3>{{ 'LOGIN.TITLE' | translate }}</h3>

<!-- With dynamic placeholders -->
<p>{{ 'WELCOME' | translate: {user: userName} }}</p>

<!-- In attributes -->
<input placeholder="{{ 'FORM.NAME' | translate }}">
```

### Using Translations in Controllers

```javascript
// Inject LanguageService
function MyController(LanguageService, $translate) {
    
    // Get current language
    var lang = LanguageService.getCurrentLanguage();
    
    // Switch language
    LanguageService.setLanguage('hi').then(function(lang) {
        console.log('Switched to: ' + lang);
    });
    
    // Get list of supported languages
    var languages = LanguageService.getSupportedLanguages();
    
    // Translate strings in code
    $translate('SUCCESS_MESSAGE').then(function(text) {
        console.log(text);
    });
    
    // Listen for language changes
    $rootScope.$on('languageChanged', function(event, data) {
        console.log('Language changed to: ' + data.language);
    });
}
```

### Adding a New Language

1. **Create Translation File**
   - Create `app/locales/xx.json` (where `xx` is the language code)
   - Copy structure from `app/locales/en.json`
   - Translate all values (keep keys unchanged)

2. **Register Language**
   - Edit `app/common/services/i18n/language.service.js`
   - Add to `SUPPORTED_LANGUAGES` object:
     ```javascript
     'xx': { code: 'xx', name: 'Language Name', flag: '🏳️' }
     ```

3. **Register in Angular Translate**
   - Edit `app/app.js`
   - Add language code to `registerAvailableLanguageKeys()`

4. **Test**
   - Reload the app
   - Switch to new language using the switcher
   - Verify translations display correctly

### Files Added/Modified

#### New Files
- `app/locales/en.json` - English translations
- `app/locales/hi.json` - Hindi translations
- `app/common/services/i18n/language.service.js` - Language management
- `app/common/services/i18n/translation-cache.service.js` - Offline support
- `app/common/services/i18n/translation-loader.factory.js` - Translation loading
- `app/common/controllers/language-switcher.controller.js` - Switcher UI logic
- `app/common/directives/language-switcher.html` - Switcher HTML template
- `styles/css/language-switcher.css` - Switcher styles
- `I18N_GUIDE.md` - Comprehensive documentation

#### Modified Files
- `app/app.js` - Added Angular Translate configuration
- `app/config.lazyload.js` - Added i18n services to lazy load config
- `app/login/views/login.html` - Replaced hardcoded strings with translation keys
- `app/common/views/layout.html` - Updated navigation with translations
- `app/common/views/update-user-profile.html` - Updated profile with translations
- `index.html` - Added Angular Translate library and i18n services

### Translation Keys Structure

All translations are grouped hierarchically for easy management:

```
APP.*                  - Application name and description
LOGIN.*                - Login page strings
FORGOT_PASSWORD.*      - Forgot password page strings
NAVIGATION.*           - Navigation menu items
PROFILE.*              - User profile strings
USER_MANAGEMENT.*      - User management forms
COURSE.*               - Course management
FORM_COMMON.*          - Common form elements
MESSAGES.*             - Success/error/warning messages
```

See `app/locales/en.json` for the complete structure.

## Offline Support

The system automatically caches translations in browser's localStorage:

- **First load**: Downloads translation files from server and caches them
- **Offline**: Uses cached translations from localStorage
- **Online**: Automatically updates cache when fresh translations are available
- **Storage**: ~30-50 KB per language (safe within browser limits)

### Managing Cache (Advanced)

```javascript
// Clear cache for specific language
TranslationCacheService.clearCache('en');

// Clear all caches
TranslationCacheService.clearCache();

// Check if language is cached
var isCached = TranslationCacheService.isCacheValid('hi');

// Get list of cached languages
var cached = TranslationCacheService.getCachedLanguages();
```

## Troubleshooting

### Translations showing as keys (e.g., "LOGIN.TITLE")

1. Check browser console for errors
2. Verify translation file exists: `app/locales/{language}.json`
3. Ensure language is registered in `language.service.js`
4. Clear browser cache and localStorage
5. Restart the application

### Language won't switch

1. Check that Angular Translate library loaded (look for errors in console)
2. Verify `LanguageService` initialized: Look for init message in console
3. Try hard refresh (Ctrl+F5 or Cmd+Shift+R)
4. Check if translations file loads in Network tab

### Offline not working

1. Ensure you loaded the language once online (to cache it)
2. Check browser allows localStorage (not in private/incognito mode)
3. Check localStorage quota in DevTools
4. Try clearing cache: `TranslationCacheService.clearCache()`

## Best Practices

1. **Always use translation keys** - Don't hardcode UI strings
2. **Keep keys hierarchical** - Group related translations (e.g., `LOGIN.*`)
3. **Test all languages** - Verify every string displays correctly
4. **Use consistent keys** - Same string should always use same key
5. **Document new keys** - Add comments for context-specific translations
6. **Keep files updated** - Ensure all language files have all keys

## Performance Impact

- **Initial load**: +1-2 translated strings while loading translations
- **Language switch**: Instant (< 100ms) due to caching
- **Memory**: Minimal (~40 KB per cached language)
- **Storage**: Uses localStorage (supports 5-10 MB typical browser limit)

## Next Steps

1. (Gujarati support has been removed; no action required)
2. Translate remaining components (forms, tables, dialogs)
3. Integrate with user profile backend
4. Add more languages as needed
5. Consider implementing translation management dashboard

## Documentation

For comprehensive documentation, see `I18N_GUIDE.md` in the project root.

---

**Last Updated**: March 3, 2026  
**Implemented By**: MEDplat Development Team
