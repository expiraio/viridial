import { useI18n } from '@/composables/useI18n'
import { useToast } from '@/shared/composables/useToast'
import { referentialService } from '../api/referential.service'

export function useReferentialActions(refresh: () => Promise<void>) {
  const { t } = useI18n()
  const toast = useToast()

  const extractIds = (rows: any[]) =>
    (rows || [])
      .map(r => (r && typeof r === 'object' && 'id' in r ? (r as any).id : r))
      .filter(id => id != null)

  const handleBulkAction = async (rows: any[], active: boolean) => {
    const ids = extractIds(rows)
    if (!ids.length) {
      toast.warning(t('referentiels.noValidIds'))
      return
    }
    
    try {
      const response = await referentialService.bulkUpdateActive(ids, active)
      await refresh()
        {
          const rawTitle = t(active ? 'referentiels.bulkActivateSuccess' : 'referentiels.bulkDeactivateSuccess', { count: response.updatedCount })
          const titleHtml = rawTitle.replace(String(response.updatedCount), `<strong>${response.updatedCount}</strong>`)
          const desc = t(active ? 'referentiels.bulkActivateSuccessDescription' : 'referentiels.bulkDeactivateSuccessDescription', { count: response.updatedCount })
          toast.successHtml ? toast.successHtml(titleHtml, desc) : toast.success(rawTitle, desc)
        }
    } catch (error) {
      toast.error(
        t(active ? 'referentiels.bulkActivateError' : 'referentiels.bulkDeactivateError'),
        error instanceof Error ? error.message : t('common.errorOccurred')
      )
    }
  }

  const handleIndividualAction = async (referential: any, active: boolean) => {
    if (!referential?.id) {
      toast.warning(t('referentiels.noValidId'))
      return
    }
    
    try {
      await referentialService.bulkUpdateActive([referential.id], active)
      await refresh()
        {
          const rawTitle = t(active ? 'referentiels.activateSuccess' : 'referentiels.deactivateSuccess')
          const titleHtml = rawTitle
          const desc = t(active ? 'referentiels.activateSuccessDescription' : 'referentiels.deactivateSuccessDescription', { label: referential.label || referential.code })
          toast.successHtml ? toast.successHtml(titleHtml, desc) : toast.success(rawTitle, desc)
        }
    } catch (error) {
      toast.error(
        t(active ? 'referentiels.activateError' : 'referentiels.deactivateError'),
        error instanceof Error ? error.message : t('common.errorOccurred')
      )
    }
  }

  const handleBulkDelete = async (rows: any[]) => {
    const ids = extractIds(rows)
    if (!ids.length) {
      toast.warning(t('referentiels.noValidIds'))
      return
    }

    try {
      const response = await referentialService.bulkDelete(ids)
      await refresh()
        {
          const rawTitle = t('referentiels.bulkDeleteSuccess', { count: response.deletedCount })
          const titleHtml = rawTitle.replace(String(response.deletedCount), `<strong>${response.deletedCount}</strong>`)
          const desc = t('referentiels.bulkDeleteSuccessDescription', { count: response.deletedCount })
          toast.successHtml ? toast.successHtml(titleHtml, desc) : toast.success(rawTitle, desc)
        }
    } catch (error) {
      toast.error(
        t('referentiels.bulkDeleteError'),
        error instanceof Error ? error.message : t('common.errorOccurred')
      )
    }
  }

  return { handleBulkAction, handleIndividualAction, handleBulkDelete }
}

