<script setup lang="ts" generic="TData extends Record<string, any>">
import type {
  ColumnDef,
  ColumnFiltersState,
  ExpandedState,
  SortingState,
  VisibilityState,
} from '@tanstack/vue-table'

import {
  FlexRender,
  getCoreRowModel,
  getExpandedRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useVueTable,
} from '@tanstack/vue-table'

import { computed, h, ref, useSlots } from 'vue'
import { useI18n } from '@/composables/useI18n'
import { createReusableTemplate } from '@vueuse/core'

import {
  MoreHorizontal,
  Info,
  Eye,
  CheckCircle,
  XCircle,
  Loader2,
  Trash2
} from 'lucide-vue-next'

import { Button } from '@/components/ui/button'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card'
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Checkbox } from '@/components/ui/checkbox'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { ScrollArea } from '@/components/ui/scroll-area'
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from '@/components/ui/tooltip'
import Pagination from '@/components/pagination.vue'
import { valueUpdater } from '@/lib/utils'

/* ---------------- PROPS ---------------- */

interface Props {
  data: any[]
  columns: ColumnDef<any>[]
  enableRowSelection?: boolean
  enableExpansion?: boolean
  defaultPageSize?: number
  emptyMessage?: string
  loading?: boolean
  totalItems?: number
}

const props = withDefaults(defineProps<Props>(), {
  enableRowSelection: false,
  enableExpansion: false,
  defaultPageSize: 10,
  emptyMessage: 'No results.',
  loading: false,
  totalItems: 0,
})

const emit = defineEmits<{
  (e: 'page-change', payload: { pageIndex: number; pageSize: number }): void
  (e: 'activate-bulk', ids: any[]): void
  (e: 'deactivate-bulk', ids: any[]): void
  (e: 'delete-bulk', ids: any[]): void
}>()

const { t } = useI18n()
const slots = useSlots()

/* ---------------- REUSABLE ACTIONS TEMPLATE ---------------- */

const [DefineActionsTemplate, ReuseActionsTemplate] =
  createReusableTemplate<{
    row: any
    onQuickView?: () => void
    onViewDetail?: () => void
    onActivate?: () => void
    onDeactivate?: () => void
  }>()

/* ---------------- TABLE STATE ---------------- */

const sorting = ref<SortingState>([])
const columnFilters = ref<ColumnFiltersState>([])
const columnVisibility = ref<VisibilityState>({})
const rowSelection = ref({})
const expanded = ref<ExpandedState>({})
const clientPageIndex = ref(0)
const serverPageIndex = ref(0)
const pageSize = ref(props.defaultPageSize)

/* ---------------- COLUMNS ---------------- */

const columns = computed<ColumnDef<any>[]>(() => {
  const cols = [...props.columns]

  if (props.enableRowSelection) {
    cols.unshift({
      id: 'select',
      header: ({ table }) =>
        h(Checkbox, {
          modelValue: table.getIsAllPageRowsSelected(),
          'onUpdate:modelValue': v =>
            table.toggleAllPageRowsSelected(!!v),
        }),
      cell: ({ row }) =>
        h(Checkbox, {
          modelValue: row.getIsSelected(),
          'onUpdate:modelValue': v => row.toggleSelected(!!v),
        }),
      enableSorting: false,
    })
  }

  return cols
})

/* ---------------- TABLE INSTANCE ---------------- */

const table = useVueTable({
  data: computed(() => props.data),
  columns: columns.value,
  getCoreRowModel: getCoreRowModel(),
  getPaginationRowModel: getPaginationRowModel(),
  getSortedRowModel: getSortedRowModel(),
  getFilteredRowModel: getFilteredRowModel(),
  getExpandedRowModel: props.enableExpansion
    ? getExpandedRowModel()
    : undefined,
  onSortingChange: u => valueUpdater(u, sorting),
  onColumnFiltersChange: u => valueUpdater(u, columnFilters),
  onColumnVisibilityChange: u => valueUpdater(u, columnVisibility),
  onRowSelectionChange: u => valueUpdater(u, rowSelection),
  state: {
    get pagination() {
      // If server-side pagination is enabled (totalItems provided), keep table pageIndex at 0
      // so the client-side paginator doesn't attempt to paginate the already-paginated data.
      const pageIndexUsed = props.totalItems ? 0 : clientPageIndex.value
      return { pageIndex: pageIndexUsed, pageSize: pageSize.value }
    },
    get sorting() {
      return sorting.value
    },
    get columnFilters() {
      return columnFilters.value
    },
    get columnVisibility() {
      return columnVisibility.value
    },
    get rowSelection() {
      return rowSelection.value
    },
    get expanded() {
      return expanded.value
    },
  },
})

/* ---------------- ACTION EMITTERS ---------------- */

const emitEvent = (name: string, payload: any) => {
  window.dispatchEvent(
    new CustomEvent(name, {
      detail: payload,
      bubbles: true,
    }),
  )
}

const selectedRowIds = () => table.getRowModel().rows.filter(r => r.getIsSelected && r.getIsSelected()).map(r => r.original?.id)
const selectedCount = () => table.getRowModel().rows.filter(r => r.getIsSelected && r.getIsSelected()).length

// Confirmation dialog state for bulk actions
const confirmDialogOpen = ref(false)
const confirmAction = ref<'activate' | 'deactivate' | 'delete' | null>(null)

const openConfirmDialog = (action: 'activate' | 'deactivate' | 'delete') => {
  confirmAction.value = action
  confirmDialogOpen.value = true
}

const performConfirmedBulk = () => {
  const ids = selectedRowIds()
  if (!ids.length) {
    confirmDialogOpen.value = false
    return
  }
  if (confirmAction.value === 'activate') emit('activate-bulk', ids)
  if (confirmAction.value === 'deactivate') emit('deactivate-bulk', ids)
  if (confirmAction.value === 'delete') emit('delete-bulk', ids)
  confirmDialogOpen.value = false
  confirmAction.value = null
}

const handleBulkActivate = () => openConfirmDialog('activate')
const handleBulkDeactivate = () => openConfirmDialog('deactivate')
const handleBulkDelete = () => openConfirmDialog('delete')

const showColumnMenu = ref(false)
const toggleColumnVisibility = (colId: string) => {
  const col = table.getAllLeafColumns().find(c => c.id === colId)
  if (col && typeof col.toggleVisibility === 'function') col.toggleVisibility(!col.getIsVisible())
}

// Pagination handlers that emit a unified page-change event for parent
const onUpdatePageIndex = (v: number) => {
  if (props.totalItems) {
    serverPageIndex.value = v
    emit('page-change', { pageIndex: serverPageIndex.value, pageSize: pageSize.value })
  } else {
    clientPageIndex.value = v
    emit('page-change', { pageIndex: clientPageIndex.value, pageSize: pageSize.value })
  }
}

const onUpdatePageSize = (v: number) => {
  pageSize.value = v
  if (props.totalItems) {
    serverPageIndex.value = 0
    emit('page-change', { pageIndex: serverPageIndex.value, pageSize: pageSize.value })
  } else {
    clientPageIndex.value = 0
    emit('page-change', { pageIndex: clientPageIndex.value, pageSize: pageSize.value })
  }
}

const onPreviousPage = () => {
  if (props.totalItems) {
    serverPageIndex.value = Math.max(0, serverPageIndex.value - 1)
    emit('page-change', { pageIndex: serverPageIndex.value, pageSize: pageSize.value })
  } else {
    clientPageIndex.value = Math.max(0, clientPageIndex.value - 1)
    emit('page-change', { pageIndex: clientPageIndex.value, pageSize: pageSize.value })
  }
}

const onNextPage = () => {
  if (props.totalItems) {
    const maxIndex = Math.max(0, Math.ceil(props.totalItems / pageSize.value) - 1)
    serverPageIndex.value = Math.min(maxIndex, serverPageIndex.value + 1)
    emit('page-change', { pageIndex: serverPageIndex.value, pageSize: pageSize.value })
  } else {
    const maxIndex = Math.max(0, table.getPageCount() - 1)
    clientPageIndex.value = Math.min(maxIndex, clientPageIndex.value + 1)
    emit('page-change', { pageIndex: clientPageIndex.value, pageSize: pageSize.value })
  }
}

const onFirstPage = () => {
  if (props.totalItems) {
    serverPageIndex.value = 0
    emit('page-change', { pageIndex: serverPageIndex.value, pageSize: pageSize.value })
  } else {
    clientPageIndex.value = 0
    emit('page-change', { pageIndex: clientPageIndex.value, pageSize: pageSize.value })
  }
}

const onLastPage = () => {
  if (props.totalItems) {
    serverPageIndex.value = Math.max(0, Math.ceil(props.totalItems / pageSize.value) - 1)
    emit('page-change', { pageIndex: serverPageIndex.value, pageSize: pageSize.value })
  } else {
    clientPageIndex.value = Math.max(0, table.getPageCount() - 1)
    emit('page-change', { pageIndex: clientPageIndex.value, pageSize: pageSize.value })
  }
}
</script>

<template>
  <Card class="flex flex-col h-full border-none shadow-none">

    <!-- ================= ACTIONS TEMPLATE ================= -->
    <DefineActionsTemplate
      v-slot="{ onQuickView, onViewDetail, onActivate, onDeactivate }"
    >
      <div class="flex gap-1">
        <Button
          v-if="onQuickView"
          size="sm"
          variant="ghost"
          @click="onQuickView"
        >
          <Info class="h-4 w-4" />
        </Button>

        <Button
          v-if="onViewDetail"
          size="sm"
          variant="ghost"
          @click="onViewDetail"
        >
          <Eye class="h-4 w-4" />
        </Button>

        <Button
          v-if="onActivate"
          size="sm"
          variant="ghost"
          @click="onActivate"
        >
          <CheckCircle class="h-4 w-4" />
        </Button>

        <Button
          v-if="onDeactivate"
          size="sm"
          variant="ghost"
          @click="onDeactivate"
        >
          <XCircle class="h-4 w-4" />
        </Button>
      </div>
    </DefineActionsTemplate>

    <!-- ================= HEADER: Bulk actions + Column chooser ================= -->
    <CardHeader class="flex items-center justify-between gap-4 px-4 py-2">
      <div class="flex items-center gap-3">
        <div v-if="props.enableRowSelection" class="flex items-center gap-2">
          
          <Button size="sm" variant="outline" :disabled="selectedCount() === 0" @click="handleBulkActivate">
            <CheckCircle class="h-4 w-4 mr-1" /> {{ t('common.activate') }}
          </Button>
          <Button size="sm" variant="outline" :disabled="selectedCount() === 0" @click="handleBulkDeactivate">
            <XCircle class="h-4 w-4 mr-1" /> {{ t('common.deactivate') }}
          </Button>
          <Button size="sm" variant="danger" :disabled="selectedCount() === 0" @click="handleBulkDelete">
            <Trash2 class="h-4 w-4 mr-1" />{{ t('common.delete') }}
          </Button>
          <span class="text-sm text-muted-foreground">{{ selectedCount() }} {{ t('common.selected') }}</span>
        </div>
      </div>

      <div class="relative">
        <Button size="sm" variant="outline" @click="showColumnMenu = !showColumnMenu">Columns</Button>
        <div v-if="showColumnMenu" class="absolute right-0 mt-2 w-48 bg-white border rounded shadow-lg z-40 p-2">
          <div v-for="col in table.getAllLeafColumns()" :key="col.id" class="flex items-center gap-2 py-1">
            <input type="checkbox" :checked="col.getIsVisible()" @change="() => toggleColumnVisibility(col.id)" />
            <label class="text-sm">{{ col.columnDef.header ? (typeof col.columnDef.header === 'string' ? col.columnDef.header : col.id) : col.id }}</label>
          </div>
        </div>
      </div>
    </CardHeader>

    <!-- Confirmation dialog for bulk actions -->
    <Dialog :open="confirmDialogOpen" @update:open="(v) => confirmDialogOpen = v">
      <DialogContent>
        <DialogHeader>
          <DialogTitle>
            {{ confirmAction === 'delete' ? 'Confirm delete' : confirmAction === 'activate' ? 'Confirm activation' : 'Confirm deactivation' }}
          </DialogTitle>
          <DialogDescription>
            <span v-if="confirmAction === 'delete'">Are you sure you want to delete the selected items?</span>
            <span v-else-if="confirmAction === 'activate'">Are you sure you want to activate the selected items?</span>
            <span v-else>Are you sure you want to deactivate the selected items?</span>
          </DialogDescription>
        </DialogHeader>
        <div class="mt-4 flex justify-end gap-2">
          <Button size="sm" variant="ghost" @click="() => (confirmDialogOpen = false)">Cancel</Button>
          <Button size="sm" variant="destructive" @click="performConfirmedBulk">Confirm</Button>
        </div>
      </DialogContent>
    </Dialog>

    <!-- ================= TABLE ================= -->

    <CardContent class="p-0 flex-1 min-h-0 relative">
      <ScrollArea class="h-full">
        <Table>
          <TableHeader>
            <TableRow v-for="hg in table.getHeaderGroups()" :key="hg.id">
              <TableHead v-for="h in hg.headers" :key="h.id">
                <FlexRender
                  v-if="!h.isPlaceholder"
                  :render="h.column.columnDef.header"
                  :props="h.getContext()"
                />
              </TableHead>
              <TableHead class="w-10" />
            </TableRow>
          </TableHeader>

          <TableBody>
            <TableRow
              v-for="row in table.getRowModel().rows"
              :key="row.id"
              class="relative group"
            >
              <TableCell
                v-for="cell in row.getVisibleCells()"
                :key="cell.id"
              >
                <FlexRender
                  :render="cell.column.columnDef.cell"
                  :props="cell.getContext()"
                />
              </TableCell>

              <!-- FLOATING ACTIONS -->
              <TableCell class="w-10">
                <TooltipProvider>
                  <Tooltip>
                    <TooltipTrigger as-child>
                      <Button
                        variant="ghost"
                        size="sm"
                        class="opacity-0 group-hover:opacity-100 transition"
                      >
                        <MoreHorizontal class="h-4 w-4" />
                      </Button>
                    </TooltipTrigger>

                    <TooltipContent side="left">
                      <template v-if="slots.actions">
                        <slot name="actions" :row="row.original" />
                      </template>
                      <template v-else>
                        <ReuseActionsTemplate
                          :row="row.original"
                          :onQuickView="() =>
                            emitEvent('quick-view-item', { item: row.original })"
                          :onViewDetail="() =>
                            emitEvent('view-detail-item', { item: row.original })"
                          :onActivate="() =>
                            emitEvent('activate-referential', { referential: row.original })"
                          :onDeactivate="() =>
                            emitEvent('deactivate-referential', { referential: row.original })"
                        />
                      </template>
                    </TooltipContent>
                  </Tooltip>
                </TooltipProvider>
              </TableCell>
            </TableRow>

            <TableRow v-if="!table.getRowModel().rows.length">
              <TableCell :colspan="columns.length + 1" class="text-center h-24">
                {{ emptyMessage }}
              </TableCell>
            </TableRow>
          </TableBody>
        </Table>
      </ScrollArea>
      <!-- Loading overlay -->
      <div
        v-if="props.loading"
        class="absolute inset-0 z-50 flex items-center justify-center bg-white/60"
      >
        <Loader2 class="animate-spin h-8 w-8 text-primary" />
      </div>
    </CardContent>

    <!-- ================= PAGINATION ================= -->

    <CardFooter class="border-t-1 pt-2 pb-2">
      <Pagination
        :page-index="(props.totalItems ? serverPageIndex : clientPageIndex)"
        :page-size="pageSize"
        :page-count="props.totalItems ? Math.max(1, Math.ceil(props.totalItems / pageSize)) : table.getPageCount()"
        :total-items="props.totalItems"
        @update:pageIndex="onUpdatePageIndex"
        @update:pageSize="onUpdatePageSize"
        @previousPage="onPreviousPage"
        @nextPage="onNextPage"
        @firstPage="onFirstPage"
        @lastPage="onLastPage"
      />
    </CardFooter>
  </Card>
</template>
