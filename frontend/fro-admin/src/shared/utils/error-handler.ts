/**
 * Error Handler Utility
 * Centralized error handling with toast notifications
 */

import { toast } from 'vue-sonner'
import { logger } from './logger'
import type { AxiosError } from 'axios'
import { HTTP_STATUS } from '@/core/config/constants'
import { useI18n } from '@/shared/composables/useI18n'

export interface AppError {
  message: string
  code?: string | number
  status?: number
  details?: any
  timestamp: string
}

export class ErrorHandler {
  /**
   * Handle API errors
   */
  static handleApiError(error: unknown, showToast = true): AppError {
    const appError = this.parseError(error)
    
    // Log error
    logger.error('API Error', appError, 'ErrorHandler')

    // Show toast notification
    if (showToast) {
      this.showErrorToast(appError)
    }

    return appError
  }

  /**
   * Parse error into AppError format
   */
  static parseError(error: unknown): AppError {
    const timestamp = new Date().toISOString()

    // Axios error
    if (this.isAxiosError(error)) {
      const axiosError = error as AxiosError<any>
      const status = axiosError.response?.status
      const data = axiosError.response?.data

      return {
        message: data?.message || axiosError.message || 'An error occurred',
        code: data?.code || status,
        status,
        details: data?.errors || data,
        timestamp,
      }
    }

    // Standard Error
    if (error instanceof Error) {
      return {
        message: error.message,
        code: 'UNKNOWN_ERROR',
        details: { stack: error.stack },
        timestamp,
      }
    }

    // String error
    if (typeof error === 'string') {
      return {
        message: error,
        code: 'STRING_ERROR',
        timestamp,
      }
    }

    // Unknown error
    return {
      message: 'An unknown error occurred',
      code: 'UNKNOWN_ERROR',
      details: error,
      timestamp,
    }
  }

  /**
   * Check if error is Axios error
   */
  static isAxiosError(error: unknown): boolean {
    return (
      typeof error === 'object' &&
      error !== null &&
      'isAxiosError' in error &&
      (error as any).isAxiosError === true
    )
  }

  /**
   * Show error toast notification
   */
  static showErrorToast(error: AppError): void {
    const { t } = useI18n()
    
    let message = error.message

    // Translate common error messages
    if (error.status) {
      switch (error.status) {
        case HTTP_STATUS.UNAUTHORIZED:
          message = t('errors.unauthorized') || 'Unauthorized access'
          break
        case HTTP_STATUS.FORBIDDEN:
          message = t('errors.forbidden') || 'Access forbidden'
          break
        case HTTP_STATUS.NOT_FOUND:
          message = t('errors.notFound') || 'Resource not found'
          break
        case HTTP_STATUS.BAD_REQUEST:
          message = t('errors.badRequest') || 'Invalid request'
          break
        case HTTP_STATUS.INTERNAL_SERVER_ERROR:
          message = t('errors.serverError') || 'Server error occurred'
          break
      }
    }

    toast.error(message, {
      description: error.details ? JSON.stringify(error.details) : undefined,
      duration: 5000,
    })
  }

  /**
   * Show success toast
   */
  static showSuccess(message: string, description?: string): void {
    toast.success(message, {
      description,
      duration: 3000,
    })
    logger.info('Success', { message, description }, 'ErrorHandler')
  }

  /**
   * Show warning toast
   */
  static showWarning(message: string, description?: string): void {
    toast.warning(message, {
      description,
      duration: 4000,
    })
    logger.warn('Warning', { message, description }, 'ErrorHandler')
  }

  /**
   * Show info toast
   */
  static showInfo(message: string, description?: string): void {
    toast.info(message, {
      description,
      duration: 3000,
    })
    logger.info('Info', { message, description }, 'ErrorHandler')
  }

  /**
   * Handle network errors
   */
  static handleNetworkError(error: unknown): AppError {
    const appError = this.parseError(error)
    
    if (!this.isAxiosError(error)) {
      appError.message = 'Network error. Please check your connection.'
      appError.code = 'NETWORK_ERROR'
    } else {
      const axiosError = error as AxiosError
      if (!axiosError.response) {
        appError.message = 'Network error. Unable to reach server.'
        appError.code = 'NETWORK_ERROR'
      }
    }

    logger.error('Network Error', appError, 'ErrorHandler')
    this.showErrorToast(appError)

    return appError
  }

  /**
   * Handle validation errors
   */
  static handleValidationError(errors: Record<string, string[]>): void {
    const errorMessages = Object.entries(errors)
      .map(([field, messages]) => `${field}: ${messages.join(', ')}`)
      .join('\n')

    toast.error('Validation Error', {
      description: errorMessages,
      duration: 5000,
    })

    logger.warn('Validation Error', errors, 'ErrorHandler')
  }
}

export default ErrorHandler

