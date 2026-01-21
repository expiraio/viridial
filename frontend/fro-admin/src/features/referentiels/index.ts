/**
 * Referentiels Feature
 * Main export file for referentiels feature
 */

export { default as ReferentialsPage } from './pages/ReferentialsPage.vue'
export { referentialService } from './api/referential.service'
export { useReferentials } from './composables/useReferentials'
export { useReferentialColumns } from './composables/useReferentialColumns'
export type { Referential, ReferentialSearchForm } from './types'

