# Environment Variables Setup

This document explains how to set up and use environment variables in the application.

## Quick Start

1. Copy `.env.example` to `.env`:
   ```bash
   cp .env.example .env
   ```

2. Update `.env` with your configuration values

3. Restart your development server

## Environment Files

### `.env.example`
Template file with all available environment variables and their descriptions. This file is committed to version control.

### `.env`
Your local environment variables. This file is **NOT** committed to version control (see `.gitignore`).

### `.env.local`
Local overrides that take precedence over `.env`. Also not committed.

### `.env.production` / `.env.development`
Environment-specific files that can be used for different build modes.

## Available Variables

### Application Info
- `VITE_APP_NAME` - Application name (default: `Viridial Admin`)
- `VITE_APP_VERSION` - Application version (default: `1.0.0`)
- `VITE_APP_ENV` - Environment: `development`, `staging`, or `production` (default: `development`)

### API Configuration
- `VITE_API_BASE_URL` - Base URL for API requests (default: `/api`)
  - Example: `http://localhost:8080/api`
  - Example: `https://api.example.com/v1`
- `VITE_API_TIMEOUT` - Request timeout in milliseconds (default: `30000`)

### Development Server
- `PORT` - Development server port (default: `5173`)
  - Used by Vite dev server
  - Preview server uses port 4173 by default
  - Note: This variable doesn't need `VITE_` prefix as it's used at build time

### Internationalization
- `VITE_I18N_DEFAULT_LOCALE` - Default locale (default: `en`)
- `VITE_I18N_FALLBACK_LOCALE` - Fallback locale if translation missing (default: `en`)
- `VITE_I18N_SUPPORTED_LOCALES` - Comma-separated list of supported locales (default: `en,fr`)
  - Example: `en,fr,es,de`

### Logging
- `VITE_LOG_LEVEL` - Logging level (default: `DEBUG` in dev, `INFO` in prod)
  - Options: `DEBUG`, `INFO`, `WARN`, `ERROR`, `NONE`
  - `DEBUG`: Show all logs (development)
  - `INFO`: Show info, warnings, and errors (default)
  - `WARN`: Show warnings and errors only
  - `ERROR`: Show errors only
  - `NONE`: Disable all logging

### Storage Keys (Advanced)
Only change these if you need custom storage keys:
- `VITE_STORAGE_KEY_THEME` - Theme storage key (default: `app-theme`)
- `VITE_STORAGE_KEY_LOCALE` - Locale storage key (default: `app-locale`)
- `VITE_STORAGE_KEY_TOKEN` - Auth token storage key (default: `auth-token`)
- `VITE_STORAGE_KEY_USER` - User data storage key (default: `user-data`)

## Usage in Code

### Accessing Configuration

```typescript
import { appConfig } from '@/core/config/app.config'

// Application info
console.log(appConfig.name) // Viridial Admin
console.log(appConfig.version) // 1.0.0
console.log(appConfig.environment) // development

// API configuration
console.log(appConfig.api.baseURL) // http://localhost:8080/api
console.log(appConfig.api.timeout) // 30000

// i18n configuration
console.log(appConfig.i18n.defaultLocale) // en
console.log(appConfig.i18n.supportedLocales) // ['en', 'fr']

// Environment checks
if (appConfig.isDev) {
  // Development only code
}

if (appConfig.isProd) {
  // Production only code
}
```

### Direct Environment Access

```typescript
import { env } from '@/core/config/env'

// Direct access to environment values
console.log(env.appName)
console.log(env.apiBaseURL)
```

### Feature Flags

```typescript
import { getEnvBoolean } from '@/core/config/env'

// In env.ts, add feature flags:
features: {
  newFeature: getEnvBoolean('VITE_FEATURE_NEW_FEATURE_ENABLED', false),
}

// Then use in code:
if (appConfig.features.newFeature) {
  // Feature enabled
}
```

## Environment-Specific Examples

### Development
```env
VITE_APP_ENV=development
VITE_API_BASE_URL=http://localhost:8080/api
VITE_LOG_LEVEL=DEBUG
PORT=5173
```

### Staging
```env
VITE_APP_ENV=staging
VITE_API_BASE_URL=https://staging-api.example.com/api
VITE_LOG_LEVEL=INFO
```

### Production
```env
VITE_APP_ENV=production
VITE_API_BASE_URL=https://api.example.com/v1
VITE_LOG_LEVEL=WARN
```

## Important Notes

1. **Vite Prefix**: All environment variables must be prefixed with `VITE_` to be accessible in the browser.

2. **Restart Required**: After changing `.env` files, you must restart the development server for changes to take effect.

3. **Build Time**: Environment variables are embedded at build time, not runtime. Different builds can have different values.

4. **Security**: Never commit sensitive data (API keys, secrets) to version control. Use `.env.local` for sensitive values.

5. **Type Safety**: Configuration is type-safe through TypeScript. Use `appConfig` instead of direct `import.meta.env` access when possible.

## Validation

The configuration system validates required environment variables in development mode. Missing or invalid values will log warnings to the console.

## Troubleshooting

### Variables not working?
1. Check that variables are prefixed with `VITE_`
2. Restart the development server
3. Check for typos in variable names
4. Verify `.env` file is in the project root

### Build issues?
1. Ensure all required variables are set
2. Check for syntax errors in `.env` files
3. Verify no quotes around values (unless needed)

## Best Practices

1. **Use `.env.example`** as documentation for available variables
2. **Never commit `.env`** - it's in `.gitignore`
3. **Use defaults** - provide sensible defaults in code
4. **Document** - add comments in `.env.example` for clarity
5. **Validate** - use the validation system to catch missing variables early

