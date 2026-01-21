/**
 * API Client
 * Centralized HTTP client configuration with error handling and logging
 */

import axios, { type AxiosInstance, type AxiosRequestConfig, type AxiosResponse, type InternalAxiosRequestConfig } from 'axios'
import { appConfig } from '@/core/config/app.config'
import { STORAGE_KEYS, HTTP_STATUS } from '@/core/config/constants'
import type { ApiResponse } from '@/shared/types'
import { logger } from '@/shared/utils/logger'
import { ErrorHandler } from '@/shared/utils/error-handler'
import { useRouter } from 'vue-router'

class ApiClient {
  private client: AxiosInstance

  constructor() {
    this.client = axios.create({
      baseURL: appConfig.api.baseURL,
      timeout: appConfig.api.timeout,
      headers: {
        'Content-Type': 'application/json',
      },
    })

    this.setupInterceptors()
  }

  private setupInterceptors() {
    // Request interceptor
    this.client.interceptors.request.use(
      (config: InternalAxiosRequestConfig) => {
        // Add auth token if available
        const token = localStorage.getItem(STORAGE_KEYS.TOKEN)
        if (token && config.headers) {
          config.headers.Authorization = `Bearer ${token}`
        }

        // Log request
        logger.debug('API Request', {
          method: config.method?.toUpperCase(),
          url: config.url,
          baseURL: config.baseURL,
        }, 'ApiClient')

        return config
      },
      (error: any) => {
        logger.error('Request Error', error, 'ApiClient')
        return Promise.reject(error)
      }
    )

    // Response interceptor
    this.client.interceptors.response.use(
      (response: AxiosResponse<ApiResponse>) => {
        // Log successful response
        logger.debug('API Response', {
          status: response.status,
          url: response.config.url,
          data: response.data,
        }, 'ApiClient')

        return response
      },
      (error: any) => {
        const status = error.response?.status

        // Handle 401 Unauthorized
        if (status === HTTP_STATUS.UNAUTHORIZED) {
          logger.warn('Unauthorized request', { url: error.config?.url }, 'ApiClient')
          localStorage.removeItem(STORAGE_KEYS.TOKEN)
          localStorage.removeItem(STORAGE_KEYS.USER)
          
          // Only redirect if not already on login page
          if (typeof window !== 'undefined' && !window.location.pathname.includes('/login')) {
            window.location.href = '/login'
          }
        }

        // Handle 403 Forbidden
        if (status === HTTP_STATUS.FORBIDDEN) {
          logger.warn('Forbidden request', { url: error.config?.url }, 'ApiClient')
        }

        // Handle 500 Server Error
        if (status === HTTP_STATUS.INTERNAL_SERVER_ERROR) {
          logger.error('Server Error', {
            url: error.config?.url,
            message: error.response?.data?.message,
          }, 'ApiClient')
        }

        // Log all errors
        logger.error('API Error', {
          status,
          url: error.config?.url,
          message: error.message,
          response: error.response?.data,
        }, 'ApiClient')

        return Promise.reject(error)
      }
    )
  }

  async get<T = any>(url: string, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
    const response = await this.client.get<ApiResponse<T>>(url, config)
    return response.data
  }

  async post<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
    const response = await this.client.post<ApiResponse<T>>(url, data, config)
    return response.data
  }

  async put<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
    const response = await this.client.put<ApiResponse<T>>(url, data, config)
    return response.data
  }

  async patch<T = any>(url: string, data?: any, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
    const response = await this.client.patch<ApiResponse<T>>(url, data, config)
    return response.data
  }

  async delete<T = any>(url: string, config?: AxiosRequestConfig): Promise<ApiResponse<T>> {
    const response = await this.client.delete<ApiResponse<T>>(url, config)
    return response.data
  }
}

export const apiClient = new ApiClient()
export default apiClient

