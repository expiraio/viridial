/**
 * Application constants
 * Shared constants used across the application
 * Storage keys are now loaded from environment variables
 */

import { appConfig } from './app.config'

export const ROUTES = {
  HOME: '/',
  LOGIN: '/login',
  DESIGN_SYSTEM: '/design-system',
  REFERENTIELS: '/referentiels',
  REFERENTIEL_DETAIL: '/referentiels/:id',
} as const

export const STORAGE_KEYS = {
  THEME: appConfig.storage.keys.theme,
  LOCALE: appConfig.storage.keys.locale,
  TOKEN: appConfig.storage.keys.token,
  USER: appConfig.storage.keys.user,
} as const

export const HTTP_STATUS = {
  OK: 200,
  CREATED: 201,
  NO_CONTENT: 204,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  INTERNAL_SERVER_ERROR: 500,
} as const

