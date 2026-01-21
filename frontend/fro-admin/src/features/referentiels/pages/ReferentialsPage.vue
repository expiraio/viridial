<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useI18n } from '@/composables/useI18n'
import ReferentialFilter from '../components/ReferentialFilter.vue'
import List from '@/components/List.vue'
import { useReferentialColumns } from '../composables/useReferentialColumns'
import { useReferentials } from '../composables/useReferentials'
import { useReferentialActions } from '../composables/useReferentialActions'
import { referentialService } from '../api/referential.service'
import { useQuickView } from '../composables/useQuickView'
import { mapOperatorToBackend } from '../utils/operatorMapper'
import type { ReferentialSearchForm } from '../types'
import { Button } from '@/components/ui/button'
import { CirclePlus, Eye, Trash2 } from 'lucide-vue-next'
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { DropdownMenuItem } from '@/components/ui/dropdown-menu'
import { useRouter } from 'vue-router'
import { toast } from 'vue-sonner'

const { t } = useI18n()
const router = useRouter()
const columns = useReferentialColumns(t)
const { referentials, loading, totalCount, searchReferentials } = useReferentials()
const pageIndex = ref(0)
const pageSize = ref(10)
const quickView = useQuickView()

// Filter state
const filters = ref({
  code: '',
  dataType: undefined as string | undefined,
  label: '',
  active: undefined as boolean | undefined,
  additional: [] as any[],
})

const performSearch = async () => {
  const searchForm: ReferentialSearchForm = {
    code: filters.value.code || undefined,
    dataType: filters.value.dataType,
    label: filters.value.label || undefined,
    active: filters.value.active,
    page: pageIndex.value,
    size: pageSize.value,
    filters: filters.value.additional.map(f => ({
      field: f.field,
      operator: mapOperatorToBackend(f.operator),
      value: ['isEmpty', 'isNotEmpty'].includes(f.operator) ? null : f.value,
    })),
  }
  await searchReferentials(searchForm)
}

const handleReset = () => {
  filters.value = { code: '', dataType: undefined, label: '', active: undefined, additional: [] }
  pageIndex.value = 0
  pageSize.value = 10
  performSearch()
}

const { handleIndividualAction } = useReferentialActions(performSearch)

// Bulk action modal state
const bulkDialogOpen = ref(false)
const bulkLoading = ref(false)
const bulkMessage = ref('')
const bulkActionName = ref<'activate' | 'deactivate' | 'delete' | null>(null)

const runBulkAction = async (ids: number[], action: 'activate' | 'deactivate' | 'delete') => {
  if (!ids || !ids.length) return
  bulkDialogOpen.value = true
  bulkLoading.value = true
  bulkMessage.value = ''
  bulkActionName.value = action

  try {
    if (action === 'delete') {
      const res = await referentialService.bulkDelete(ids)
      bulkMessage.value = `${res.deletedCount} item(s) deleted successfully`
    } else {
      const res = await referentialService.bulkUpdateActive(ids, action === 'activate')
      bulkMessage.value = `${res.updatedCount} item(s) ${action === 'activate' ? 'activated' : 'deactivated'} successfully`
    }
    await performSearch()
  } catch (err) {
    bulkMessage.value = err instanceof Error ? err.message : 'Operation failed'
  } finally {
    bulkLoading.value = false
  }
}

const handleQuickView = (e: Event) => {
  const item = (e as CustomEvent).detail.item || (e as CustomEvent).detail.referential
  if (item) quickView.show(item)
}

const handleViewDetail = (e: Event) => {
  const item = (e as CustomEvent).detail.item || (e as CustomEvent).detail.referential
  if (item?.id) {
    router.push(`/referentiels/${item.id}`)
  }
}

const handleAction = (e: Event, active: boolean) => {
  handleIndividualAction((e as CustomEvent).detail.referential, active)
}

const onPageChange = (p: { pageIndex: number; pageSize: number }) => {
  pageIndex.value = p.pageIndex
  pageSize.value = p.pageSize
  performSearch()
}

const dispatchEvent = (name: string, data: any) => {
  window.dispatchEvent(new CustomEvent(name, { detail: data, bubbles: true }))
}

const events = [
  ['quick-view-item', handleQuickView],
  ['quick-view-referential', handleQuickView],
  ['view-detail-item', handleViewDetail],
  ['activate-referential', (e: Event) => handleAction(e, true)],
  ['deactivate-referential', (e: Event) => handleAction(e, false)],
] as const

onMounted(() => {
  events.forEach(([name, handler]) => window.addEventListener(name, handler))
  performSearch()
})

onUnmounted(() => {
  events.forEach(([name, handler]) => window.removeEventListener(name, handler))
})
</script>

<template>
  <div class="flex flex-col h-full min-h-0 ">

    <header class="shrink-0 sticky top-0 z-30 px-4 md:px-6 py-4 border-b  backdrop-blur bg-white">
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div class="space-y-1">
          <h1 class="text-2xl font-bold tracking-tight text-foreground-title">{{ t('referentiels.title') }}</h1>
          <p class="text-sm text-muted-foreground">{{ t('referentiels.subtitle') }}</p>
        </div>
        <Button size="sm" class="gap-2 font-semibold shadow-sm">
          <CirclePlus class="h-4 w-4" />
          {{ t('referentiels.newReferential') }}
        </Button>
      </div>
    </header>

    <div class="flex-1 flex flex-col min-h-0 overflow-hidden">
      <section class="shrink-0 px-4 md:px-6 pt-4">
        <ReferentialFilter
          :result-count="totalCount"
          :total-count="totalCount"
          @update:code="filters.code = $event"
          @update:dataType="filters.dataType = $event"
          @update:label="filters.label = $event"
          @update:active="filters.active = $event"
          @update:additionalFilters="filters.additional = $event"
          @apply="performSearch"
          @reset="handleReset"
        />
      </section>

      <section class="flex-1 flex flex-col min-h-0 px-4 md:px-6 pt-4 pb-6 overflow-hidden">
        <div class="flex-1 flex flex-col min-h-0 overflow-hidden bg-card rounded-lg border shadow-sm border-border">
          <List
            :data="referentials"
            :columns="columns"
            :enable-row-selection="true"
            :loading="loading"
            :result-count="totalCount"
            :total-items="totalCount"
            @activate-bulk="(ids) => runBulkAction(ids, 'activate')"
            @deactivate-bulk="(ids) => runBulkAction(ids, 'deactivate')"
            @delete-bulk="(ids) => runBulkAction(ids, 'delete')"
            @page-change="onPageChange"
          >
            <template #actions="{ row }">
              <DropdownMenuItem @click="dispatchEvent('view-referential', { referential: row })">
                <Eye class="mr-2 h-4 w-4" />
                {{ t('common.view') }}
              </DropdownMenuItem>
              <DropdownMenuItem @click="dispatchEvent('delete-referential', { referential: row })" class="text-destructive">
                <Trash2 class="mr-2 h-4 w-4" />
                {{ t('common.delete') }}
              </DropdownMenuItem>
            </template>
          </List>
        </div>
      </section>
    </div>

    <Dialog :open="quickView.open.value" @update:open="quickView.open.value = $event">
      <DialogContent class="quick-view-modal max-w-4xl">
        <DialogHeader class="pb-3">
          <DialogTitle class="text-xl font-bold">{{ t('common.quickView') }}</DialogTitle>
          <DialogDescription>{{ t('common.quickViewDescription') }}</DialogDescription>
        </DialogHeader>
        <div v-if="quickView.data.value" class="quick-view-content mt-3">
          <div v-for="(label, key) in {
            id: t('referentiels.columns.id'),
            code: t('referentiels.columns.code'),
            dataType: t('referentiels.columns.dataType'),
            label: t('referentiels.columns.label'),
            description: t('referentiels.columns.description'),
            externalCode: t('referentiels.columns.externalCode'),
            displayOrder: t('referentiels.columns.displayOrder'),
            locale: t('referentiels.columns.locale'),
            active: t('referentiels.columns.active'),
          }" :key="key" v-show="quickView.data.value[key] !== undefined && quickView.data.value[key] !== null" class="quick-view-item">
            <div class="quick-view-label">{{ label }}</div>
            <div class="quick-view-value">
              {{ key === 'active' ? (quickView.data.value[key] ? t('common.yes') : t('common.no')) : quickView.data.value[key] }}
            </div>
          </div>
        </div>
      </DialogContent>
    </Dialog>
    <!-- Bulk action result modal -->
    <Dialog :open="bulkDialogOpen" @update:open="bulkDialogOpen = $event">
      <DialogContent class="max-w-lg">
        <DialogHeader class="pb-2">
          <DialogTitle>{{ bulkActionName === 'delete' ? t('referentiels.delete') : bulkActionName === 'activate' ? t('common.activate') : t('common.deactivate') }}</DialogTitle>
        </DialogHeader>

        <div class="py-4">
          <div v-if="bulkLoading" class="flex flex-col items-center gap-3">
            <div class="animate-spin border-4 border-primary w-10 h-10 rounded-full border-t-transparent"></div>
            <div class="text-sm text-muted-foreground">Processing...</div>
          </div>

          <div v-else class="text-center">
            <div class="text-base font-medium">{{ bulkMessage }}</div>
          </div>
        </div>

        <div class="flex justify-end gap-2">
          <Button size="sm" variant="ghost" @click="bulkDialogOpen = false">Close</Button>
        </div>
      </DialogContent>
    </Dialog>
  </div>
</template>
