/**
 * Toast Composable
 * Convenient wrapper for toast notifications
 */

import { toast } from 'vue-sonner'
import { h } from 'vue'
import { ErrorHandler } from '@/shared/utils/error-handler'
import { logger } from '@/shared/utils/logger'

export const useToast = () => {
  const success = (message: string, description?: string) => {
    ErrorHandler.showSuccess(message, description)
  }

  const error = (message: string, description?: string) => {
    toast.error(message, {
      description,
      duration: 5000,
    })
    logger.error('Toast Error', { message, description }, 'useToast')
  }

  const warning = (message: string, description?: string) => {
    ErrorHandler.showWarning(message, description)
  }

  // Render HTML content by creating a VNode with innerHTML
  const successHtml = (html: string, description?: string) => {
    try {
      const vnode = h('div', { innerHTML: html })
      toast.success(vnode, { description, duration: 3000 })
      logger.info('Success', { html, description }, 'useToast')
    } catch (e) {
      logger.error('Toast HTML render error', e, 'useToast')
      success(html, description)
    }
  }

  const errorHtml = (html: string, description?: string) => {
    try {
      const vnode = h('div', { innerHTML: html })
      toast.error(vnode, { description, duration: 5000 })
      logger.error('Error', { html, description }, 'useToast')
    } catch (e) {
      logger.error('Toast HTML render error', e, 'useToast')
      error(html, description)
    }
  }

  const warningHtml = (html: string, description?: string) => {
    try {
      const vnode = h('div', { innerHTML: html })
      toast.warning(vnode, { description, duration: 4000 })
      logger.warn('Warning', { html, description }, 'useToast')
    } catch (e) {
      logger.error('Toast HTML render error', e, 'useToast')
      warning(html, description)
    }
  }

  const info = (message: string, description?: string) => {
    ErrorHandler.showInfo(message, description)
  }

  const loading = (message: string) => {
    return toast.loading(message)
  }

  const promise = <T>(
    promise: Promise<T>,
    options: {
      loading: string
      success: string | ((data: T) => string)
      error: string | ((error: any) => string)
    }
  ) => {
    return toast.promise(promise, options)
  }

  return {
    success,
    error,
    warning,
    info,
    loading,
    promise,
    successHtml,
    errorHtml,
    warningHtml,
  }
}

