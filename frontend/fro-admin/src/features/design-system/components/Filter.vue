<script setup lang="ts">
import { ref, computed } from 'vue'
import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet"
import { RotateCcw, SlidersHorizontal, Plus, X } from 'lucide-vue-next'
import { useI18n } from '@/shared/composables/useI18n'

const { t } = useI18n()

interface Props {
  resultCount?: number
  totalCount?: number
}

const props = withDefaults(defineProps<Props>(), {
  resultCount: undefined,
  totalCount: undefined,
})

const emit = defineEmits<{
  (e: 'update:status', value: string | undefined): void
  (e: 'update:email', value: string): void
  (e: 'update:amount', value: string): void
  (e: 'apply'): void
  (e: 'reset'): void
}>()

const statusOptions = computed(() => [
  { value: 'pending', label: t('status.pending') },
  { value: 'processing', label: t('status.processing') },
  { value: 'success', label: t('status.success') },
  { value: 'failed', label: t('status.failed') },
])

const statusFilter = ref<string | undefined>(undefined)
const emailFilter = ref<string>('')
const amountFilter = ref<string>('')

interface AdditionalFilter {
  id: string
  field: string
  value: string
  type: 'text' | 'number' | 'email' | 'select'
}

const additionalFilters = ref<AdditionalFilter[]>([])
const sheetOpen = ref(false)

const availableFields = computed(() => [
  { value: 'id', label: 'ID', type: 'text' as const },
  { value: 'email', label: t('filter.email'), type: 'email' as const },
  { value: 'amount', label: t('filter.amount'), type: 'number' as const },
  { value: 'status', label: t('filter.status'), type: 'select' as const },
])
</script>

<template>
  <Card class="shadow-sm border-border/50">
    <CardContent class="p-5">
      <!-- Main Filters -->
      <div class="grid grid-cols-1 sm:grid-cols-3 lg:grid-cols-5 gap-4 items-end">
        <!-- Status Filter -->
        <div class="space-y-2">
          <label class="text-sm font-medium text-foreground leading-none">
            {{ t('filter.status') }}
          </label>
          <Select
            v-model="statusFilter"
            @update:model-value="(value) => {
              emit('update:status', typeof value === 'string' ? value : undefined)
            }"
          >
            <SelectTrigger class="h-10">
              <SelectValue :placeholder="t('filter.allStatuses')" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem
                v-for="option in statusOptions"
                :key="option.value"
                :value="option.value"
              >
                {{ option.label }}
              </SelectItem>
            </SelectContent>
          </Select>
        </div>

        <!-- Email Filter -->
        <div class="space-y-2">
          <label class="text-sm font-medium text-foreground leading-none">
            {{ t('filter.email') }}
          </label>
          <Input
            v-model="emailFilter"
            type="email"
            :placeholder="t('filter.filterByEmail')"
            class="h-10"
            @update:model-value="(value) => emit('update:email', String(value))"
          />
        </div>

        <!-- Amount Filter -->
        <div class="space-y-2">
          <label class="text-sm font-medium text-foreground leading-none">
            {{ t('filter.amount') }}
          </label>
          <Input
            v-model="amountFilter"
            type="number"
            :placeholder="t('filter.filterByAmount')"
            class="h-10"
            @update:model-value="(value) => emit('update:amount', String(value))"
          />
        </div>

        <!-- Action Buttons -->
        <div class="flex items-end gap-2 sm:col-span-3 lg:col-span-2">
          <Sheet :open="sheetOpen" @update:open="sheetOpen = $event">
            <SheetTrigger as-child>
              <Button variant="outline" size="sm" class="h-10 gap-2">
                <SlidersHorizontal class="h-4 w-4" />
                {{ t('common.moreFilters') }}
              </Button>
            </SheetTrigger>
            <SheetContent side="right" class="w-full sm:w-[450px] p-6">
              <SheetHeader class="space-y-3 mb-6">
                <SheetTitle class="text-xl">{{ t('filter.advancedFilters') }}sss</SheetTitle>
                <SheetDescription class="text-sm">
                  {{ t('filter.advancedFiltersDescription') }}
                </SheetDescription>
              </SheetHeader>

              <div class="space-y-4">
                <!-- Additional Filters List -->
                <div v-if="additionalFilters.length > 0" class="space-y-3">
                  <div
                    v-for="filter in additionalFilters"
                    :key="filter.id"
                    class="flex items-end gap-2 p-3 bg-muted/50 rounded-lg border"
                  >
                    <div class="flex-1 space-y-2">
                      <label class="text-xs font-medium text-muted-foreground">
                        {{ t('filter.field') }}
                      </label>
                      <Select
                        :model-value="filter.field"
                        @update:model-value="(value) => {
                          if (typeof value === 'string') {
                            filter.field = value
                            filter.value = ''
                          }
                        }"
                      >
                        <SelectTrigger class="h-9">
                          <SelectValue />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem
                            v-for="field in availableFields"
                            :key="field.value"
                            :value="field.value"
                          >
                            {{ field.label }}
                          </SelectItem>
                        </SelectContent>
                      </Select>
                    </div>

                    <div class="flex-1 space-y-2">
                      <label class="text-xs font-medium text-muted-foreground">
                        {{ t('filter.value') }}
                      </label>
                      <Input
                        v-if="filter.type !== 'select'"
                        v-model="filter.value"
                        :type="filter.type"
                        :placeholder="t('filter.filterByEmail')"
                        class="h-9"
                      />
                      <Select
                        v-else
                        :model-value="filter.value"
                        @update:model-value="(value) => {
                          if (typeof value === 'string') {
                            filter.value = value
                          }
                        }"
                      >
                        <SelectTrigger class="h-9">
                          <SelectValue :placeholder="t('filter.selectStatus')" />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem
                            v-for="option in statusOptions"
                            :key="option.value"
                            :value="option.value"
                          >
                            {{ option.label }}
                          </SelectItem>
                        </SelectContent>
                      </Select>
                    </div>

                    <Button
                      variant="ghost"
                      size="sm"
                      class="h-9 w-9 p-0 text-destructive hover:text-destructive"
                      @click="additionalFilters = additionalFilters.filter(f => f.id !== filter.id)"
                    >
                      <X class="h-4 w-4" />
                    </Button>
                  </div>
                </div>

                <!-- Add Filter Button -->
                <Button
                  variant="outline"
                  size="sm"
                  class="w-full h-10 gap-2"
                  @click="() => {
                    const firstField = availableFields[0]
                    if (firstField) {
                      additionalFilters.push({
                        id: Date.now().toString(),
                        field: firstField.value,
                        value: '',
                        type: firstField.type
                      })
                    }
                  }"
                >
                  <Plus class="h-4 w-4" />
                  {{ t('filter.addFilter') }}
                </Button>

                <!-- Action Buttons -->
                <div class="flex items-center gap-3 pt-4 border-t">
                  <Button
                    variant="outline"
                    size="sm"
                    class="flex-1 h-10 gap-2"
                    @click="emit('reset')"
                  >
                    <RotateCcw class="h-4 w-4" />
                    {{ t('common.reset') }}
                  </Button>
                  <Button
                    variant="default"
                    size="sm"
                    class="flex-1 h-10 gap-2"
                    @click="emit('apply')"
                  >
                    {{ t('common.apply') }}
                  </Button>
                </div>
              </div>
            </SheetContent>
          </Sheet>

          <Button
            variant="outline"
            size="sm"
            class="h-10 gap-2"
            @click="emit('reset')"
          >
            <RotateCcw class="h-4 w-4" />
            {{ t('common.reset') }}
          </Button>
          <Button
            variant="default"
            size="sm"
            class="h-10 gap-2 shadow-sm"
            @click="emit('apply')"
          >
            {{ t('common.apply') }}
          </Button>
        </div>
      </div>
    </CardContent>
  </Card>
</template>
