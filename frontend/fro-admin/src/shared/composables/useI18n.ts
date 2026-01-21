/**
 * i18n composable
 * Wrapper for vue-i18n with additional utilities
 */

import { useI18n as useVueI18n } from 'vue-i18n'

export const useI18n = () => {
  const { t, locale, availableLocales } = useVueI18n()
  
  const changeLocale = (newLocale: string) => {
    locale.value = newLocale
    localStorage.setItem('app-locale', newLocale)
  }
  
  return {
    t,
    locale,
    availableLocales,
    changeLocale,
  }
}

