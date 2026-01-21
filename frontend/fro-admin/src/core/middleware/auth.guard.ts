/**
 * Authentication Guard
 * Middleware for protecting routes that require authentication
 */

import type { NavigationGuardNext, RouteLocationNormalized } from 'vue-router'
import { STORAGE_KEYS } from '@/core/config/constants'
import { ROUTES } from '@/core/config/constants'
import { logger } from '@/shared/utils/logger'

export function authGuard(
  to: RouteLocationNormalized,
  _from: RouteLocationNormalized,
  next: NavigationGuardNext
) {
  const token = localStorage.getItem(STORAGE_KEYS.TOKEN)
  
  if (!token) {
    logger.warn('Unauthorized access attempt', { path: to.fullPath }, 'AuthGuard')
    // Redirect to login if not authenticated
    next({ 
      path: ROUTES.LOGIN, 
      query: { redirect: to.fullPath } 
    })
  } else {
    next()
  }
}

