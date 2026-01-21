# Configuration

This directory contains application configuration that is loaded from environment variables.

## Environment Variables

All configuration values are loaded from environment variables defined in `.env` files.

### Required Variables

- `VITE_API_BASE_URL` - API base URL (default: `/api`)
- `VITE_I18N_DEFAULT_LOCALE` - Default locale (default: `en`)

### Optional Variables

- `VITE_APP_NAME` - Application name (default: `Viridial Admin`)
- `VITE_APP_VERSION` - Application version (default: `1.0.0`)
- `VITE_APP_ENV` - Environment (default: `development`)
- `VITE_API_TIMEOUT` - API timeout in milliseconds (default: `30000`)
- `VITE_I18N_FALLBACK_LOCALE` - Fallback locale (default: `en`)
- `VITE_I18N_SUPPORTED_LOCALES` - Comma-separated list of supported locales (default: `en,fr`)
- `VITE_LOG_LEVEL` - Log level: DEBUG, INFO, WARN, ERROR, NONE (default: `DEBUG` in dev, `INFO` in prod)
- `PORT` - Development server port (default: `5173`) - Note: No `VITE_` prefix as it's used at build time

## Files

- `env.ts` - Environment variable loader with validation
- `app.config.ts` - Application configuration (uses env.ts)
- `constants.ts` - Application constants (uses app.config.ts)

## Usage

```typescript
import { appConfig } from '@/core/config/app.config'
import { env } from '@/core/config/env'

// Access configuration
console.log(appConfig.name) // Viridial Admin
console.log(appConfig.api.baseURL) // http://localhost:8080/api

// Access environment directly
console.log(env.appName)
console.log(env.apiBaseURL)

// Check environment
import { isDev, isProd, isStaging } from '@/core/config/app.config'
if (isDev) {
  // Development only code
}
```

## Environment Files

- `.env.example` - Template with all available variables
- `.env` - Local environment variables (not committed to git)
- `.env.local` - Local overrides (not committed to git)
- `.env.production` - Production environment variables
- `.env.development` - Development environment variables

## Validation

Environment variables are validated on import in development mode. Missing or invalid required variables will log warnings to the console.

