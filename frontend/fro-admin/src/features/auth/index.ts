/**
 * Authentication Feature
 * Main export for the authentication feature module
 */

export { default as LoginPage } from './pages/LoginPage.vue'
export { useAuth } from './composables/useAuth'
export { authService } from './api/auth.service'
export * from './types'

