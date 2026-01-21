/**
 * Authentication Composable
 * Manages authentication state and operations
 */

import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { authService } from '../api/auth.service'
import type { LoginCredentials, RegisterData, User } from '../types'
import { STORAGE_KEYS } from '@/core/config/constants'
import { ErrorHandler } from '@/shared/utils/error-handler'
import { logger } from '@/shared/utils/logger'
import { useToast } from '@/shared/composables/useToast'

const user = ref<User | null>(null)
const token = ref<string | null>(localStorage.getItem(STORAGE_KEYS.TOKEN))
const isLoading = ref(false)

export const useAuth = () => {
  const router = useRouter()
  const toast = useToast()

  const isAuthenticated = computed(() => {
    return !!token.value && !!user.value
  })

  /**
   * Initialize auth state from storage
   */
  const initAuth = async () => {
    try {
      const storedToken = localStorage.getItem(STORAGE_KEYS.TOKEN)
      const storedUser = localStorage.getItem(STORAGE_KEYS.USER)

      if (storedToken && storedUser) {
        token.value = storedToken
        user.value = JSON.parse(storedUser)

        // Verify token is still valid by fetching current user
        try {
          const currentUser = await authService.getCurrentUser()
          user.value = currentUser
          logger.info('Auth initialized', { user: currentUser }, 'useAuth')
        } catch (error) {
          // Token is invalid, clear auth
          logger.warn('Token validation failed', error, 'useAuth')
          clearAuth()
        }
      }
    } catch (error) {
      logger.error('Failed to initialize auth', error, 'useAuth')
      clearAuth()
    }
  }

  /**
   * Login user
   */
  const login = async (credentials: LoginCredentials) => {
    try {
      isLoading.value = true
      logger.info('Attempting login', { email: credentials.email }, 'useAuth')

      const response = await authService.login(credentials)

      // Store auth data
      token.value = response.token
      user.value = response.user

      localStorage.setItem(STORAGE_KEYS.TOKEN, response.token)
      localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(response.user))

      if (response.refreshToken) {
        localStorage.setItem('refreshToken', response.refreshToken)
      }

      logger.info('Login successful', { user: response.user }, 'useAuth')
      toast.success('Login successful', 'Welcome back!')

      return response
    } catch (error) {
      const appError = ErrorHandler.handleApiError(error)
      logger.error('Login failed', appError, 'useAuth')
      throw appError
    } finally {
      isLoading.value = false
    }
  }

  /**
   * Register new user
   */
  const register = async (data: RegisterData) => {
    try {
      isLoading.value = true
      logger.info('Attempting registration', { email: data.email }, 'useAuth')

      const response = await authService.register(data)

      // Store auth data
      token.value = response.token
      user.value = response.user

      localStorage.setItem(STORAGE_KEYS.TOKEN, response.token)
      localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(response.user))

      logger.info('Registration successful', { user: response.user }, 'useAuth')
      toast.success('Registration successful', 'Welcome!')

      return response
    } catch (error) {
      const appError = ErrorHandler.handleApiError(error)
      logger.error('Registration failed', appError, 'useAuth')
      throw appError
    } finally {
      isLoading.value = false
    }
  }

  /**
   * Logout user
   */
  const logout = async () => {
    try {
      isLoading.value = true
      logger.info('Logging out', {}, 'useAuth')

      await authService.logout()
    } catch (error) {
      logger.warn('Logout API call failed', error, 'useAuth')
      // Continue with local logout even if API call fails
    } finally {
      clearAuth()
      toast.info('Logged out', 'You have been logged out')
      router.push('/login')
      isLoading.value = false
    }
  }

  /**
   * Clear authentication data
   */
  const clearAuth = () => {
    token.value = null
    user.value = null
    localStorage.removeItem(STORAGE_KEYS.TOKEN)
    localStorage.removeItem(STORAGE_KEYS.USER)
    localStorage.removeItem('refreshToken')
    logger.info('Auth cleared', {}, 'useAuth')
  }

  /**
   * Refresh user data
   */
  const refreshUser = async () => {
    try {
      const currentUser = await authService.getCurrentUser()
      user.value = currentUser
      localStorage.setItem(STORAGE_KEYS.USER, JSON.stringify(currentUser))
      logger.info('User data refreshed', { user: currentUser }, 'useAuth')
    } catch (error) {
      const appError = ErrorHandler.handleApiError(error, false)
      logger.error('Failed to refresh user', appError, 'useAuth')
      throw appError
    }
  }

  /**
   * Check if user has permission
   */
  const hasPermission = (permission: string): boolean => {
    if (!user.value?.permissions) return false
    return user.value.permissions.includes(permission)
  }

  /**
   * Check if user has role
   */
  const hasRole = (role: string): boolean => {
    if (!user.value?.roles) return false
    return user.value.roles.includes(role)
  }

  return {
    // State
    user: computed(() => user.value),
    token: computed(() => token.value),
    isAuthenticated,
    isLoading: computed(() => isLoading.value),

    // Methods
    initAuth,
    login,
    register,
    logout,
    clearAuth,
    refreshUser,
    hasPermission,
    hasRole,
  }
}

