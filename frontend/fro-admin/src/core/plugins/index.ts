/**
 * Core plugins initialization
 * Centralized plugin registration
 */

import type { App } from 'vue'
import { router } from '@/core/router'
import i18n from '@/i18n'

export function registerPlugins(app: App) {
  app.use(router)
  app.use(i18n)
}

