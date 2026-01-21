/**
 * i18n configuration
 * Internationalization setup
 * Configuration loaded from environment variables
 */

import { createI18n } from 'vue-i18n'
import en from '@/shared/i18n/locales/en.json'
import fr from '@/shared/i18n/locales/fr.json'
import { appConfig } from '@/core/config/app.config'

export type MessageLanguages = keyof typeof messages

export const messages = {
  en,
  fr,
}

// Get locale from localStorage or use default
const savedLocale = localStorage.getItem(appConfig.storage.keys.locale)
const initialLocale = savedLocale && appConfig.i18n.supportedLocales.includes(savedLocale)
  ? savedLocale
  : appConfig.i18n.defaultLocale

const i18n = createI18n({
  legacy: false,
  locale: initialLocale,
  fallbackLocale: appConfig.i18n.fallbackLocale,
  messages,
})

export default i18n

