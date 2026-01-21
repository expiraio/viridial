/**
 * Referentials Composable
 * Manages referential data fetching and state
 */

import { ref, computed } from 'vue'
import { referentialService } from '../api/referential.service'
import type { Referential, ReferentialSearchForm } from '../types'

export function useReferentials() {
  const referentials = ref<Referential[]>([])
  const loading = ref(false)
  const error = ref<Error | null>(null)
  const totalCount = ref(0)

  const searchReferentials = async (searchForm: ReferentialSearchForm) => {
    loading.value = true
    error.value = null
    
    try {
       searchForm.sorts = [
        {
          field: 'code',
          direction: 'ASC'
        },
        {
          field: 'label',
          direction: 'ASC'
        }
      ]
      const response = await referentialService.search(searchForm)
      // Expecting PaginatedResponse: { items: Referential[], total: number, page, size }
      const items = response?.items ?? (Array.isArray(response) ? response : response?.data ?? [])
      const total = response?.total ?? (Array.isArray(response) ? response.length : (response?.data ? response.data.length : 0))
      console.log('Search response:', response)
      referentials.value = items
      totalCount.value = total
      console.log('Referentials after assignment:', referentials.value)
      console.log('Total count:', totalCount.value)
    } catch (err) {
      console.error('Error in searchReferentials:', err)
      error.value = err instanceof Error ? err : new Error('Failed to search referentials')
      referentials.value = []
      totalCount.value = 0
      throw err
    } finally {
      loading.value = false
    }
  }

  const reset = () => {
    referentials.value = []
    totalCount.value = 0
    error.value = null
    loading.value = false
  }

  return {
    referentials, // Return the ref directly instead of computed
    loading: computed(() => loading.value),
    error: computed(() => error.value),
    totalCount: computed(() => totalCount.value),
    searchReferentials,
    reset,
  }
}

