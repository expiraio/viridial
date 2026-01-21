<script setup lang="ts">
import { computed } from 'vue'
import { Button } from '@/components/ui/button'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from 'lucide-vue-next'

interface Props {
  pageIndex: number
  pageSize: number
  pageCount: number
  totalItems?: number
  canPreviousPage?: boolean
  canNextPage?: boolean
}

interface Emits {
  (e: 'update:pageIndex', value: number): void
  (e: 'update:pageSize', value: number): void
  (e: 'previousPage'): void
  (e: 'nextPage'): void
  (e: 'firstPage'): void
  (e: 'lastPage'): void
}

const props = withDefaults(defineProps<Props>(), {
  totalItems: 0,
  canPreviousPage: false,
  canNextPage: false,
})

const emit = defineEmits<Emits>()

const pageSizeOptions = [5, 10, 20, 30, 50, 100]

const pageInfo = computed(() => {
  const start = props.pageIndex * props.pageSize + 1
  const end = Math.min((props.pageIndex + 1) * props.pageSize, props.totalItems || 0)
  const total = props.totalItems || 0
  return { start, end, total }
})

const handlePageSizeChange = (value: any) => {
  if (value === null || value === undefined) return
  const str = String(value)
  emit('update:pageSize', Number.parseInt(str))
  emit('update:pageIndex', 0) // Reset to first page when page size changes
}

const handlePageChange = (page: number) => {
  emit('update:pageIndex', Math.max(0, Math.min(page, props.pageCount - 1)))
}

// Generate page numbers to display
const visiblePages = computed(() => {
  const currentPage = props.pageIndex + 1
  const totalPages = props.pageCount || 1
  const pages: (number | string)[] = []
  
  if (totalPages <= 5) {
    // Show all pages if 7 or fewer
    for (let i = 1; i <= totalPages; i++) {
      pages.push(i)
    }
  } else {
    // Always show first page
    pages.push(1)
    
    if (currentPage <= 4) {
      // Near the beginning
      for (let i = 2; i <= 5; i++) {
        pages.push(i)
      }
      pages.push('ellipsis')
      pages.push(totalPages)
    } else if (currentPage >= totalPages - 3) {
      // Near the end
      pages.push('ellipsis')
      for (let i = totalPages - 4; i <= totalPages; i++) {
        pages.push(i)
      }
    } else {
      // In the middle
      pages.push('ellipsis')
      for (let i = currentPage - 1; i <= currentPage + 1; i++) {
        pages.push(i)
      }
      pages.push('ellipsis')
      pages.push(totalPages)
    }
  }
  
  return pages
})
</script>

<template>
  <div class="flex items-center justify-between w-full">
    <!-- Left: Rows per page selector and info -->
    <div class="flex items-center gap-4">
      <div class="flex items-center space-x-2">
        <span class="text-sm text-muted-foreground whitespace-nowrap">Rows per page:</span>
        <Select :model-value="String(pageSize)" @update:model-value="handlePageSizeChange">
          <SelectTrigger class="h-8 w-[70px]">
            <SelectValue :placeholder="String(pageSize)" />
          </SelectTrigger>
          <SelectContent side="top">
            <SelectItem
              v-for="size in pageSizeOptions"
              :key="size"
              :value="String(size)"
            >
              {{ size }}
            </SelectItem>
          </SelectContent>
        </Select>
      </div>

      <div class="text-sm text-muted-foreground">
        <span v-if="totalItems > 0">
          {{ pageInfo.start }}-{{ pageInfo.end }} of {{ pageInfo.total }}
        </span>
        <span v-else>
          Page {{ pageIndex + 1 }} of {{ pageCount || 1 }}
        </span>
      </div>
    </div>

    <!-- Right: Pagination controls -->
    <div class="flex items-center justify-center sm:justify-end gap-1 sm:gap-2 flex-wrap">
      <Button
        variant="outline"
        size="sm"
        class="hidden h-8 w-8 p-0 lg:flex"
          :disabled="pageIndex <= 0"
        @click="emit('firstPage')"
      >
        <span class="sr-only">Go to first page</span>
        <ChevronsLeft class="h-4 w-4" />
      </Button>

      <Button
        variant="outline"
        size="sm"
        class="h-8 w-8 p-0"
        :disabled="pageIndex <= 0"
        @click="emit('previousPage')"
      >
        <span class="sr-only">Go to previous page</span>
        <ChevronLeft class="h-4 w-4" />
      </Button>

      <!-- Page Numbers -->
      <div class="flex items-center gap-1">
        <template v-for="(page, index) in visiblePages" :key="index">
          <Button
            v-if="page !== 'ellipsis'"
            variant="outline"
            size="sm"
            class="h-8 w-8 p-0 text-xs sm:text-sm"
            :class="{
              'bg-primary text-primary-foreground hover:bg-primary hover:text-primary-foreground': page === pageIndex + 1,
            }"
            :disabled="page === pageIndex + 1"
            @click="handlePageChange((page as number) - 1)"
          >
            {{ page }}
          </Button>
          <span
            v-else
            class="px-1 sm:px-2 text-xs sm:text-sm text-muted-foreground"
          >
            ...
          </span>
        </template>
      </div>

      <Button
        variant="outline"
        size="sm"
        class="h-8 w-8 p-0"
        :disabled="pageIndex >= (pageCount - 1)"
        @click="emit('nextPage')"
      >
        <span class="sr-only">Go to next page</span>
        <ChevronRight class="h-4 w-4" />
      </Button>

      <Button
        variant="outline"
        size="sm"
        class="hidden h-8 w-8 p-0 lg:flex"
        :disabled="pageIndex >= (pageCount - 1)"
        @click="emit('lastPage')"
      >
        <span class="sr-only">Go to last page</span>
        <ChevronsRight class="h-4 w-4" />
      </Button>
    </div>
  </div>
</template>
