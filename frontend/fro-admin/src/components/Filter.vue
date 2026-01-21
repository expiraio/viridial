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
import { useI18n } from '@/composables/useI18n'

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

type FilterOperator = 'equals' | 'contains' | 'startsWith' | 'endsWith' | 'greaterThan' | 'lessThan' | 'greaterThanOrEqual' | 'lessThanOrEqual' | 'notEquals' | 'notContains' | 'isEmpty' | 'isNotEmpty'

interface AdditionalFilter {
  id: string
  field: string
  operator: FilterOperator
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

const filterOperators = computed(() => [
  { value: 'equals', label: t('filter.operator.equals'), applicableTypes: ['text', 'email', 'number', 'select'] },
  { value: 'notEquals', label: t('filter.operator.notEquals'), applicableTypes: ['text', 'email', 'number', 'select'] },
  { value: 'contains', label: t('filter.operator.contains'), applicableTypes: ['text', 'email'] },
  { value: 'notContains', label: t('filter.operator.notContains'), applicableTypes: ['text', 'email'] },
  { value: 'startsWith', label: t('filter.operator.startsWith'), applicableTypes: ['text', 'email'] },
  { value: 'endsWith', label: t('filter.operator.endsWith'), applicableTypes: ['text', 'email'] },
  { value: 'greaterThan', label: t('filter.operator.greaterThan'), applicableTypes: ['number'] },
  { value: 'lessThan', label: t('filter.operator.lessThan'), applicableTypes: ['number'] },
  { value: 'greaterThanOrEqual', label: t('filter.operator.greaterThanOrEqual'), applicableTypes: ['number'] },
  { value: 'lessThanOrEqual', label: t('filter.operator.lessThanOrEqual'), applicableTypes: ['number'] },
  { value: 'isEmpty', label: t('filter.operator.isEmpty'), applicableTypes: ['text', 'email', 'number', 'select'] },
  { value: 'isNotEmpty', label: t('filter.operator.isNotEmpty'), applicableTypes: ['text', 'email', 'number', 'select'] },
])

const getOperatorsForField = (fieldType: string) => {
  return filterOperators.value.filter(op => op.applicableTypes.includes(fieldType))
}

const getFieldType = (fieldValue: string) => {
  const field = availableFields.value.find(f => f.value === fieldValue)
  return field?.type || 'text'
}

const setOperatorForFilter = (filter: AdditionalFilter, operatorValue: string) => {
  filter.operator = operatorValue as FilterOperator
  if (operatorValue === 'isEmpty' || operatorValue === 'isNotEmpty') {
    filter.value = ''
  }
}

const setFieldForFilter = (filter: AdditionalFilter, fieldValue: string) => {
  const fieldType = getFieldType(fieldValue)
  filter.field = fieldValue
  filter.type = fieldType
  filter.value = ''
  // Reset operator to first available for new field type
  const availableOps = getOperatorsForField(fieldType)
  const firstOp = availableOps[0]
  if (firstOp) {
    filter.operator = firstOp.value as FilterOperator
  }
}

const createNewFilter = (): AdditionalFilter | null => {
  const firstField = availableFields.value[0]
  if (firstField) {
    const availableOps = getOperatorsForField(firstField.type)
    const firstOp = availableOps[0]
    if (firstOp) {
      return {
        id: Date.now().toString(),
        field: firstField.value,
        operator: firstOp.value as FilterOperator,
        value: '',
        type: firstField.type
      }
    }
  }
  return null
}
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
                {{ t('common.moreFilters') }}dddd
              </Button>
            </SheetTrigger>
            <SheetContent side="right" class="w-full sm:w-[450px] p-6">
              <SheetHeader class="space-y-3 mb-6">
                <SheetTitle class="text-xl">{{ t('filter.advancedFilters') }}ssss</SheetTitle>
                <SheetDescription class="text-sm">
                  {{ t('filter.advancedFiltersDescription') }}dddd
                </SheetDescription>
              </SheetHeader>

              <div class="space-y-4">
                <!-- Additional Filters List -->
                <div v-if="additionalFilters.length > 0" class="space-y-3">
                  <div
                    v-for="filter in additionalFilters"
                    :key="filter.id"
                    class="flex flex-col gap-2 p-3 bg-muted/50 rounded-lg border"
                  >
                    <div class="flex items-end gap-2">
                      <!-- Field Selection -->
                      <div class="flex-1 space-y-2">
                        <label class="text-xs font-medium text-muted-foreground">
                          {{ t('filter.field') }}
                        </label>
                        <Select
                          :model-value="filter.field"
                          @update:model-value="(value) => {
                            if (typeof value === 'string') {
                              setFieldForFilter(filter, value)
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

                      <!-- Operator Selection -->
                      <div class="flex-1 space-y-2">
                        <label class="text-xs font-medium text-muted-foreground">
                          {{ t('filter.operator.label') }}
                        </label>
                        <Select
                          :model-value="filter.operator"
                          @update:model-value="(value) => {
                            if (typeof value === 'string') {
                              setOperatorForFilter(filter, value)
                            }
                          }"
                        >
                          <SelectTrigger class="h-9">
                            <SelectValue :placeholder="t('filter.operator.label')">
                              {{ filterOperators.find(op => op.value === filter.operator)?.label || filter.operator }}
                            </SelectValue>
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem
                              v-for="op in getOperatorsForField(filter.type)"
                              :key="op.value"
                              :value="op.value"
                            >
                              {{ op.label }}
                            </SelectItem>
                          </SelectContent>
                        </Select>
                      </div>

                      <!-- Remove Button -->
                      <Button
                        variant="ghost"
                        size="sm"
                        class="h-9 w-9 p-0 text-destructive hover:text-destructive shrink-0"
                        @click="additionalFilters = additionalFilters.filter(f => f.id !== filter.id)"
                      >
                        <X class="h-4 w-4" />
                      </Button>
                    </div>

                    <!-- Value Input (hidden for isEmpty/isNotEmpty) -->
                    <div v-if="filter.operator !== 'isEmpty' && filter.operator !== 'isNotEmpty'" class="space-y-2">
                      <label class="text-xs font-medium text-muted-foreground">
                        {{ t('filter.value') }}
                      </label>
                      <Input
                        v-if="filter.type !== 'select'"
                        v-model="filter.value"
                        :type="filter.type"
                        :placeholder="t('filter.enterValue')"
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
                  </div>
                </div>

                <!-- Add Filter Button -->
                <Button
                  variant="outline"
                  size="sm"
                  class="w-full h-10 gap-2"
                  @click="() => {
                    const newFilter = createNewFilter()
                    if (newFilter) {
                      additionalFilters.push(newFilter)
                    }
                  }"
                >
                  <Plus class="h-4 w-4" />
                  {{ t('filter.addFilter') }}sssss
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
