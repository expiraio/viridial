<script setup lang="ts" generic="TData extends Record<string, any>">
// Types
import type {
  ColumnDef,
  ColumnFiltersState,
  ExpandedState,
  SortingState,
  VisibilityState,
} from '@tanstack/vue-table'

// TanStack Table
import {
  FlexRender,
  getCoreRowModel,
  getExpandedRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useVueTable,
} from '@tanstack/vue-table'

// Vue
import { computed, h, ref, useSlots } from 'vue'
import { useI18n } from '@/shared/composables/useI18n'

// Composables
import { createReusableTemplate } from '@vueuse/core'

// Icons
import { ChevronDown, MoreHorizontal, Trash2, CheckCircle, Loader2 } from 'lucide-vue-next'

// UI Components
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardFooter, CardHeader } from '@/components/ui/card'
import { Checkbox } from '@/components/ui/checkbox'
import {
  DropdownMenu,
  DropdownMenuCheckboxItem,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
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

// Utils
import { valueUpdater } from '@/lib/utils'

interface Props {
  data: any[]
  columns: ColumnDef<any>[]
  enableRowSelection?: boolean
  enableColumnVisibility?: boolean
  enableExpansion?: boolean
  defaultPageSize?: number
  pageSizeOptions?: number[]
  emptyMessage?: string
  idKey?: string
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  enableRowSelection: false,
  enableColumnVisibility: true,
  enableExpansion: false,
  defaultPageSize: 5,
  pageSizeOptions: () => [5, 10, 20, 30],
  emptyMessage: 'No results.',
  idKey: 'id',
  loading: false,
})

// i18n
const { t } = useI18n()

// Slots
const slots = useSlots()

// Reusable Template for Actions
const [DefineActionsTemplate, ReuseActionsTemplate] = createReusableTemplate<{
  row: any
  onExpand?: () => void
}>()

// Table State
const sorting = ref<SortingState>([])
const columnFilters = ref<ColumnFiltersState>([])
const columnVisibility = ref<VisibilityState>({})
const rowSelection = ref({})
const expanded = ref<ExpandedState>({})
const pageSize = ref(props.defaultPageSize)
const pageIndex = ref(0)

// Build columns with optional selection column
const buildColumns = (): ColumnDef<any>[] => {
  const cols: ColumnDef<any>[] = [...props.columns]

  // Add selection column if enabled
  if (props.enableRowSelection) {
    cols.unshift({
      id: 'select',
      header: ({ table }) => h(Checkbox, {
        'modelValue': table.getIsAllPageRowsSelected() || (table.getIsSomePageRowsSelected() && 'indeterminate'),
        'onUpdate:modelValue': value => table.toggleAllPageRowsSelected(!!value),
        'ariaLabel': 'Select all',
      }),
      cell: ({ row }) => h(Checkbox, {
        'modelValue': row.getIsSelected(),
        'onUpdate:modelValue': value => row.toggleSelected(!!value),
        'ariaLabel': 'Select row',
      }),
      enableSorting: false,
      enableHiding: false,
    })
  }

  // Add actions column if actions slot is provided
  if (slots.actions) {
    cols.push({
      id: 'actions',
      enableHiding: false,
      size: 50,
      minSize: 50,
      maxSize: 50,
      cell: ({ row }) => {
        return h(ReuseActionsTemplate, {
          row: row.original,
          onExpand: props.enableExpansion ? row.toggleExpanded : undefined,
        })
      },
    })
  }

  return cols
}

const columns = computed(() => buildColumns())

// Table Configuration
const table = useVueTable({
  data: props.data,
  columns: columns.value,
  getCoreRowModel: getCoreRowModel(),
  getPaginationRowModel: getPaginationRowModel(),
  getSortedRowModel: getSortedRowModel(),
  getFilteredRowModel: getFilteredRowModel(),
  getExpandedRowModel: props.enableExpansion ? getExpandedRowModel() : undefined,
  onSortingChange: updaterOrValue => valueUpdater(updaterOrValue, sorting),
  onColumnFiltersChange: updaterOrValue => valueUpdater(updaterOrValue, columnFilters),
  onColumnVisibilityChange: updaterOrValue => valueUpdater(updaterOrValue, columnVisibility),
  onRowSelectionChange: updaterOrValue => valueUpdater(updaterOrValue, rowSelection),
  onExpandedChange: props.enableExpansion ? (updaterOrValue => valueUpdater(updaterOrValue, expanded)) : undefined,
  state: {
    get pagination() {
      return {
        pageIndex: pageIndex.value,
        pageSize: pageSize.value,
      }
    },
    get sorting() { return sorting.value },
    get columnFilters() { return columnFilters.value },
    get columnVisibility() { return columnVisibility.value },
    get rowSelection() { return rowSelection.value },
    get expanded() { return expanded.value },
  },
  onPaginationChange: (updaterOrValue) => {
    const value = typeof updaterOrValue === 'function' ? updaterOrValue({ pageIndex: pageIndex.value, pageSize: pageSize.value }) : updaterOrValue
    pageIndex.value = value.pageIndex
    pageSize.value = value.pageSize
  },
  enableRowSelection: props.enableRowSelection,
  enableExpanding: props.enableExpansion,
  columnResizeMode: 'onChange',
})

// Computed Properties
const selectedRowsCount = computed(() => {
  return table.getSelectedRowModel().rows.length
})

const hasSelectedRows = computed(() => selectedRowsCount.value > 0)

const selectedRows = computed(() => {
  return table.getSelectedRowModel().rows.map(row => row.original)
})

// Emits
const emits = defineEmits<{
  (e: 'selection-action', rows: any[]): void
  (e: 'delete-bulk', rows: any[]): void
  (e: 'activate-bulk', rows: any[]): void
  (e: 'view-payment', payment: any): void
  (e: 'delete-payment', payment: any): void
}>()

// Handle custom events from action buttons
const handleViewPaymentEvent = (e: Event) => {
  const customEvent = e as CustomEvent
  emits('view-payment', customEvent.detail.payment)
}

const handleDeletePaymentEvent = (e: Event) => {
  const customEvent = e as CustomEvent
  emits('delete-payment', customEvent.detail.payment)
}

// Expose table instance for external filtering
defineExpose({
  table,
})

</script>

<template>
  <Card class="flex flex-col h-full min-h-0 overflow-hidden shadow-sm border-border/50">
    <DefineActionsTemplate v-slot="{ row, onExpand }">
      <DropdownMenu v-if="slots.actions">
        <DropdownMenuTrigger as-child>
          <Button variant="ghost" class="h-8 w-8 p-0">
            <span class="sr-only">Open menu</span>
            <MoreHorizontal class="h-4 w-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end">
          <DropdownMenuLabel>Actions</DropdownMenuLabel>
          <slot name="actions" :row="row" />
          <DropdownMenuSeparator v-if="enableExpansion && onExpand" />
          <DropdownMenuItem v-if="enableExpansion && onExpand" @click="onExpand">
            Expand
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </DefineActionsTemplate>

    <CardHeader class="flex flex-col sm:flex-row items-stretch sm:items-center justify-between gap-2 sm:gap-0 py-4 shrink-0">
      <!-- Left Side: Actions and Controls -->
      <div class="flex items-center gap-2 flex-wrap">
        <TooltipProvider>
          <!-- Bulk Activate Button -->
          <Tooltip>
            <TooltipTrigger as-child>
              <span class="inline-block">
                <Button 
                  variant="default" 
                  size="sm"
                  :disabled="!hasSelectedRows"
                  class="h-8 w-8 p-0"
                  @click="emits('activate-bulk', selectedRows)"
                >
                  <CheckCircle class="h-4 w-4" />
                </Button>
              </span>
            </TooltipTrigger>
            <TooltipContent>
              <p>{{ hasSelectedRows ? t('list.activateBulk', { count: selectedRowsCount }) : t('list.activateNoSelection') }}</p>
            </TooltipContent>
          </Tooltip>

          <!-- Bulk Delete Button -->
          <Tooltip>
            <TooltipTrigger as-child>
              <span class="inline-block">
                <Button 
                  variant="outline" 
                  size="sm"
                  :disabled="!hasSelectedRows"
                  class="h-8 w-8 p-0"
                  @click="emits('delete-bulk', selectedRows)"
                >
                  <Trash2 class="h-4 w-4" />
                </Button>
              </span>
            </TooltipTrigger>
            <TooltipContent>
              <p>{{ hasSelectedRows ? t('list.deleteBulk', { count: selectedRowsCount }) : t('list.deleteNoSelection') }}</p>
            </TooltipContent>
          </Tooltip>
        </TooltipProvider>
      </div>

      <!-- Right Side: Column Visibility Dropdown -->
      <div v-if="enableColumnVisibility" class="flex items-center">
        <DropdownMenu>
          <DropdownMenuTrigger as-child>
            <Button variant="outline" size="sm">
              Columns
              <ChevronDown class="ml-2 h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            <DropdownMenuCheckboxItem v-for="column in table.getAllColumns().filter((column) => column.getCanHide())"
              :key="column.id" class="capitalize" :model-value="column.getIsVisible()" @update:model-value="(value) => {
                column.toggleVisibility(!!value)
              }">
              {{ column.id }}
            </DropdownMenuCheckboxItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>

    </CardHeader>

    <CardContent class="flex-1 min-h-0 p-0">
      <ScrollArea class="h-full w-full">
        <div 
          class="table-wrapper [&>div]:!overflow-visible min-w-full"
          @view-payment="handleViewPaymentEvent"
          @delete-payment="handleDeletePaymentEvent"
        >
          <Table>
            <TableHeader class="sticky top-0 z-20 bg-background shadow-sm">
              <TableRow v-for="headerGroup in table.getHeaderGroups()" :key="headerGroup.id">
                <TableHead 
                  v-for="header in headerGroup.headers" 
                  :key="header.id"
                  :style="header.column.columnDef.size ? {
                    width: `${header.column.columnDef.size}px`,
                    minWidth: `${header.column.columnDef.size}px`,
                    maxWidth: `${header.column.columnDef.size}px`,
                  } : {}"
                >
                  <FlexRender v-if="!header.isPlaceholder" :render="header.column.columnDef.header"
                    :props="header.getContext()" />
                </TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              <template v-if="loading">
                <TableRow>
                  <TableCell :colspan="table.getAllColumns().length" class="h-24 text-center">
                    <div class="flex items-center justify-center gap-2">
                      <Loader2 class="h-5 w-5 animate-spin text-muted-foreground" />
                      <span class="text-sm text-muted-foreground">{{ t('common.loading') }}</span>
                    </div>
                  </TableCell>
                </TableRow>
              </template>

              <template v-else-if="table.getRowModel().rows?.length">
                <template v-for="row in table.getRowModel().rows" :key="row.id">
                  <TableRow :data-state="row.getIsSelected() && 'selected'">
                    <TableCell 
                      v-for="cell in row.getVisibleCells()" 
                      :key="cell.id"
                      :style="cell.column.columnDef.size ? {
                        width: `${cell.column.columnDef.size}px`,
                        minWidth: `${cell.column.columnDef.size}px`,
                        maxWidth: `${cell.column.columnDef.size}px`,
                      } : {}"
                    >
                      <FlexRender :render="cell.column.columnDef.cell" :props="cell.getContext()" />
                    </TableCell>
                  </TableRow>
                  <TableRow v-if="enableExpansion && row.getIsExpanded()">
                    <TableCell :colspan="row.getAllCells().length">
                      <slot name="expanded" :row="row.original" />
                    </TableCell>
                  </TableRow>
                </template>
              </template>

              <TableRow v-else>
                <TableCell :colspan="table.getAllColumns().length" class="h-24 text-center">
                  {{ emptyMessage || t('common.noResults') }}
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </div>
      </ScrollArea>
    </CardContent>
        <CardFooter class="shrink-0 px-6 py-4 border-t bg-muted/30">
          <div class="flex flex-col sm:flex-row items-stretch sm:items-center justify-between gap-4 w-full">
        <div class="flex items-center justify-between sm:justify-start space-x-2">
          <span class="text-sm text-muted-foreground whitespace-nowrap">Rows per page:</span>
          <select v-model.number="pageSize" class="border rounded px-2 py-1 text-sm min-w-[70px]">
            <option v-for="size in pageSizeOptions" :key="size" :value="size">
              {{ size }}
            </option>
          </select>
        </div>

            <div class="flex items-center gap-2">
              <Button
                variant="outline"
                size="sm"
                class="h-9 gap-2"
                :disabled="!table.getCanPreviousPage()"
                @click="table.previousPage()"
              >
                {{ t('table.previous') }}
              </Button>
              <Button
                variant="outline"
                size="sm"
                class="h-9 gap-2"
                :disabled="!table.getCanNextPage()"
                @click="table.nextPage()"
              >
                {{ t('table.next') }}
              </Button>
            </div>
      </div>
    </CardFooter>
  </Card>
</template>
