/**
 * Environment Configuration
 * Centralized environment variable access with validation and defaults
 */

/**
 * Get environment variable with fallback
 */
function getEnv(key: string, defaultValue: string = ''): string {
  return import.meta.env[key] ?? defaultValue
}

/**
 * Get environment variable as number with fallback
 */
function getEnvNumber(key: string, defaultValue: number): number {
  const value = import.meta.env[key]
  if (value === undefined) return defaultValue
  const parsed = Number.parseInt(value, 10)
  return Number.isNaN(parsed) ? defaultValue : parsed
}

/**
 * Get environment variable as boolean
 * Exported for use in feature flags
 */
export function getEnvBoolean(key: string, defaultValue: boolean = false): boolean {
  const value = import.meta.env[key]
  if (value === undefined) return defaultValue
  return value === 'true' || value === '1'
}

/**
 * Get environment variable as array (comma-separated)
 */
function getEnvArray(key: string, defaultValue: string[] = []): string[] {
  const value = import.meta.env[key]
  if (!value) return defaultValue
  return value.split(',').map((item: string) => item.trim()).filter(Boolean)
}

/**
 * Application environment
 */
export const env = {
  // App Info
  appName: getEnv('VITE_APP_NAME', 'Viridial Admin'),
  appVersion: getEnv('VITE_APP_VERSION', '1.0.0'),
  appEnv: getEnv('VITE_APP_ENV', import.meta.env.MODE || 'development'),

  // API
  // Use '/api' for development (Vite proxy) or full URL for production
  apiBaseURL: getEnv('VITE_API_BASE_URL', '/api'),
  apiTimeout: getEnvNumber('VITE_API_TIMEOUT', 30000),

  // i18n
  i18nDefaultLocale: getEnv('VITE_I18N_DEFAULT_LOCALE', 'en'),
  i18nFallbackLocale: getEnv('VITE_I18N_FALLBACK_LOCALE', 'en'),
  i18nSupportedLocales: getEnvArray('VITE_I18N_SUPPORTED_LOCALES', ['en', 'fr']),

  // Logging
  logLevel: getEnv('VITE_LOG_LEVEL', import.meta.env.DEV ? 'DEBUG' : 'INFO'),

  // Storage Keys
  storageKeyTheme: getEnv('VITE_STORAGE_KEY_THEME', 'app-theme'),
  storageKeyLocale: getEnv('VITE_STORAGE_KEY_LOCALE', 'app-locale'),
  storageKeyToken: getEnv('VITE_STORAGE_KEY_TOKEN', 'auth-token'),
  storageKeyUser: getEnv('VITE_STORAGE_KEY_USER', 'user-data'),

  // Feature Flags
  // Use getEnvBoolean('VITE_FEATURE_EXAMPLE_ENABLED', false) for feature flags
  features: {
    // Add feature flags here as needed
    // Example: exampleFeature: getEnvBoolean('VITE_FEATURE_EXAMPLE_ENABLED', false),
  },
} as const

/**
 * Check if running in development
 */
export const isDev = env.appEnv === 'development' || import.meta.env.DEV

/**
 * Check if running in production
 */
export const isProd = env.appEnv === 'production' || import.meta.env.PROD

/**
 * Check if running in staging
 */
export const isStaging = env.appEnv === 'staging'

/**
 * Validate environment configuration
 */
export function validateEnv(): void {
  const required: Array<{ key: string; value: any; name: string }> = [
    { key: 'VITE_API_BASE_URL', value: env.apiBaseURL, name: 'API Base URL' },
    { key: 'VITE_I18N_DEFAULT_LOCALE', value: env.i18nDefaultLocale, name: 'Default Locale' },
  ]

  const missing = required.filter((item) => !item.value)

  if (missing.length > 0) {
    console.warn(
      '⚠️  Missing or invalid environment variables:',
      missing.map((item) => `${item.name} (${item.key})`).join(', ')
    )
  }
}

// Validate on import in development
if (isDev) {
  validateEnv()
}

export default env

