# i18n - Internationalization Guide

MEDplat uses [angular-translate](https://angular-translate.github.io/) for client-side multilingual support.

## Supported Languages

| Code | Language | File |
|------|----------|------|
| `en` | English (default/fallback) | `locale-en.json` |
| `hi` | Hindi | `locale-hi.json` |
| `gu` | Gujarati | `locale-gu.json` |

## How It Works

1. Translation JSON files are bundled as static assets in `app/i18n/`
2. `angular-translate` loads the appropriate file based on user selection
3. User's language preference is persisted in `localStorage`
4. If a key is missing in the selected language, English is used as fallback

## Adding a New Language

### 1. Create the locale file

Copy `locale-en.json` and translate all values:

```bash
cp app/i18n/locale-en.json app/i18n/locale-ta.json
```

Edit `locale-ta.json` and translate all string values (keep the keys unchanged).

### 2. Register in the language switcher

Edit `app/common/directives/language-switcher.directive.js` and add your language to the `languages` array:

```javascript
scope.languages = [
    { code: 'en', label: 'English' },
    { code: 'hi', label: 'हिंदी' },
    { code: 'gu', label: 'ગુજરાતી' },
    { code: 'ta', label: 'தமிழ்' }  // <-- add new language here
];
```

### 3. Add to USER constant (for admin UI)

Edit `app/manage/constants/user.constants.js`:

```javascript
prefferedLanguage: {
    gujarati: 'GU',
    english: 'EN',
    hindi: 'HI',
    tamil: 'TA'  // <-- add here
}
```

### 4. Add to language name filter

Edit `app/common/filters/languagename.filter.js` and add a new condition:

```javascript
} else if (input === USER.prefferedLanguage.tamil) {
    out = "Tamil";
}
```

### 5. Seed database labels (optional)

Create a Flyway migration to insert translations into `internationalization_label_master`:

```sql
INSERT INTO internationalization_label_master (country, "key", "language", created_by, created_on, custom3b, "text", translation_pending, app_name)
VALUES ('IN', 'LOGIN.TITLE', 'TA', -1, now(), false, 'உள்நுழைவு', false, 'WEB')
ON CONFLICT DO NOTHING;
```

## Key Naming Conventions

Keys use flat, dot-separated namespaces:

| Prefix | Scope |
|--------|-------|
| `LOGIN.*` | Login page |
| `FORGOT_PASSWORD.*` | Forgot password page |
| `NAV.*` | Navigation/layout |
| `COMMON.*` | Shared UI elements (buttons, labels) |
| `TOAST.*` | Toast notification messages |
| `USERS.*` | Users management module |
| `FHS.*` | FHS module |
| `STATES.*` | Navigation state labels |

## Usage in Templates (HTML)

Use the `translate` filter:

```html
<button>{{ 'LOGIN.SUBMIT' | translate }}</button>
<input placeholder="{{ 'LOGIN.USERNAME_PLACEHOLDER' | translate }}">
```

## Usage in Controllers (JS)

Inject `$translate` and use `instant()`:

```javascript
function MyController($translate, toaster) {
    toaster.pop('success', $translate.instant('TOAST.DATA_UPDATED'));
}
```

## Offline Support

Translations are cached in `localStorage` by angular-translate. Once loaded, they work offline without server connectivity.

## Translation Sync API

An optional backend API is available to fetch the latest translations from the database:

```
GET /api/internationalization/labels?language=HI&appName=WEB
```

This returns a JSON map of `{ "key": "translated text" }`.
