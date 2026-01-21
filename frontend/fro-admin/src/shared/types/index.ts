/**
 * Shared types and interfaces
 * Common types used across the application
 */

export interface ApiResponse<T = any> {
  data: T
  message?: string
  status: number
  success: boolean
}

export interface PaginatedResponse<T = any> {
  data: T[]
  total: number
  page: number
  pageSize: number
  totalPages: number
}

export interface BaseEntity {
  id: string | number
  createdAt?: string
  updatedAt?: string
}

export type Nullable<T> = T | null
export type Optional<T> = T | undefined

