/**
 * Application configuration
 * Centralized configuration for the application
 * All values are loaded from environment variables via env.ts
 */

import { env, isDev, isProd, isStaging } from './env'

export const appConfig = {
  name: env.appName,
  version: env.appVersion,
  environment: env.appEnv,
  isDev,
  isProd,
  isStaging,
  api: {
    baseURL: env.apiBaseURL,
    timeout: env.apiTimeout,
  },
  i18n: {
    defaultLocale: env.i18nDefaultLocale,
    fallbackLocale: env.i18nFallbackLocale,
    supportedLocales: env.i18nSupportedLocales,
  },
  logging: {
    level: env.logLevel,
  },
  storage: {
    keys: {
      theme: env.storageKeyTheme,
      locale: env.storageKeyLocale,
      token: env.storageKeyToken,
      user: env.storageKeyUser,
    },
  },
  features: env.features,
} as const

export type AppConfig = typeof appConfig

// Export env utilities for direct access if needed
export { env, isDev, isProd, isStaging } from './env'

