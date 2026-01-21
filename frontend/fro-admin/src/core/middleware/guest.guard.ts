/**
 * Guest Guard
 * Middleware for protecting routes that should only be accessible to unauthenticated users
 */

import type { NavigationGuardNext, RouteLocationNormalized } from 'vue-router'
import { STORAGE_KEYS } from '@/core/config/constants'
import { ROUTES } from '@/core/config/constants'
import { logger } from '@/shared/utils/logger'

export function guestGuard(
  to: RouteLocationNormalized,
  _from: RouteLocationNormalized,
  next: NavigationGuardNext
) {
  const token = localStorage.getItem(STORAGE_KEYS.TOKEN)
  
  if (token) {
    logger.info('Authenticated user accessing guest route', { path: to.fullPath }, 'GuestGuard')
    // Redirect to home if already authenticated
    next({ path: ROUTES.HOME })
  } else {
    next()
  }
}

