/**
 * Router configuration
 * Main router setup with lazy loading
 */

import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { ROUTES } from '@/core/config/constants'
import { authGuard, guestGuard } from '@/core/middleware'

// Lazy load route components
const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: ROUTES.DESIGN_SYSTEM,
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/features/auth/pages/LoginPage.vue'),
    meta: {
      title: 'Login',
      requiresAuth: false,
      requiresGuest: true,
    },
  },
  {
    path: ROUTES.DESIGN_SYSTEM,
    name: 'DesignSystem',
    component: () => import('@/features/design-system/pages/DesignSystemPage.vue'),
    meta: {
      title: 'Design System',
      requiresAuth: false,
    },
  },
  {
    path: ROUTES.REFERENTIELS,
    name: 'Referentiels',
    component: () => import('@/features/referentiels/pages/ReferentialsPage.vue'),
    meta: {
      title: 'Referentials',
      requiresAuth: false,
    },
  },
  {
    path: ROUTES.REFERENTIEL_DETAIL,
    name: 'ReferentialDetail',
    component: () => import('@/features/referentiels/pages/ReferentialDetailPage.vue'),
    meta: {
      title: 'Referential Detail',
      requiresAuth: false,
    },
  },
]

export const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  },
})

// Router guards
router.beforeEach((to, from, next) => {
  // Guest guard - redirect authenticated users away from guest-only pages
  if (to.meta.requiresGuest) {
    guestGuard(to, from, next)
    return
  }

  // Auth guard - protect routes that require authentication
  if (to.meta.requiresAuth) {
    authGuard(to, from, next)
    return
  }

  next()
})

export default router

