/**
 * Authentication Types
 */

export interface LoginCredentials {
  email: string
  password: string
  rememberMe?: boolean
}

export interface RegisterData {
  email: string
  password: string
  confirmPassword: string
  firstName?: string
  lastName?: string
}

export interface User {
  id: string
  email: string
  firstName?: string
  lastName?: string
  avatar?: string
  roles?: string[]
  permissions?: string[]
}

export interface AuthResponse {
  user: User
  token: string
  refreshToken?: string
  expiresIn?: number
}

export interface AuthState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  isLoading: boolean
}

