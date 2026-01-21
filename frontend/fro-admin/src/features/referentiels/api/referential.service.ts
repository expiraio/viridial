/**
 * Referential Service
 * API service for referential operations
 */

import { apiClient } from '@/shared/api/client'
import type { Referential, ReferentialSearchForm } from '../types'

export const referentialService = {
  /**
   * Search referentials
   * Note: Backend returns ResponseEntity<List<ReferentialForm>> which is just the array
   * Spring returns the array directly in the response body
   * apiClient.post returns response.data (from axios), which is the array
   */
  async search(searchForm: ReferentialSearchForm): Promise<any> {
    const response: any = await apiClient.post('/referentiels/search', searchForm)
    // Backend returns PaginatedResponse: { items: Referential[], total: number, page: number, size: number }
    // Normalize legacy array responses into the same shape for backward compatibility
    if (response && response.items) return response
    if (Array.isArray(response)) return { items: response, total: response.length, page: 0, size: response.length }
    if (response?.data && response.data.items) return response.data
    if (Array.isArray(response?.data)) return { items: response.data, total: response.data.length, page: 0, size: response.data.length }
    return { items: [], total: 0, page: 0, size: 0 }
  },

  /**
   * Get a single referential by ID
   * @param id The referential ID
   * @returns The referential or null if not found
   */
  async getById(id: number): Promise<Referential | null> {
    const response: any = await apiClient.post<Referential[]>('/referentiels/search', { id })
    const referentials = Array.isArray(response) ? response : response?.data || []
    return referentials.length > 0 ? referentials[0] : null
  },

  /**
   * Bulk update active status for referentials
   * @param ids List of referential IDs to update
   * @param active The active status to set
   * @returns Response with updated count and active status
   */
  async bulkUpdateActive(ids: number[], active: boolean): Promise<{ updatedCount: number; active: boolean }> {
    const response: any = await apiClient.post('/referentiels/bulk-update-active', { ids, active })
    return response?.data || response || { updatedCount: 0, active }
  },
  /**
   * Bulk delete referentials
   * @param ids list of ids to delete
   */
  async bulkDelete(ids: number[]): Promise<{ deletedCount: number }> {
    const response: any = await apiClient.post('/referentiels/bulk-delete', { ids })
    return response?.data || response || { deletedCount: 0 }
  },
}

