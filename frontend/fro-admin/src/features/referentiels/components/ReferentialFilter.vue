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
  (e: 'update:code', value: string): void
  (e: 'update:dataType', value: string | undefined): void
  (e: 'update:label', value: string): void
  (e: 'update:active', value: boolean | undefined): void
  (e: 'update:additionalFilters', value: AdditionalFilter[]): void
  (e: 'apply'): void
  (e: 'reset'): void
}>()

const dataTypeOptions = computed(() => [
  { value: 'LANGUAGE', label: t('referentiels.dataTypes.language') },
  { value: 'REGION', label: t('referentiels.dataTypes.region') },
  { value: 'SUB_REGION', label: t('referentiels.dataTypes.subRegion') },
  { value: 'TEAM_TYPE', label: t('referentiels.dataTypes.teamType') },
  { value: 'INDUSTRY', label: t('referentiels.dataTypes.industry') },
  { value: 'ADDRESS_TYPE', label: t('referentiels.dataTypes.addressType') },
  { value: 'PHONE_TYPE', label: t('referentiels.dataTypes.phoneType') },
  { value: 'REFERENTIAL_TYPE', label: t('referentiels.dataTypes.referentialType') },
])

const activeOptions = computed(() => [
  { value: undefined, label: t('common.all') },
  { value: true, label: t('common.yes') },
  { value: false, label: t('common.no') },
])

const codeFilter = ref<string>('')
const dataTypeFilter = ref<string | undefined>(undefined)
const labelFilter = ref<string>('')
const activeFilter = ref<boolean | undefined>(undefined)

type FilterOperator = 'equals' | 'contains' | 'startsWith' | 'endsWith' | 'greaterThan' | 'lessThan' | 'greaterThanOrEqual' | 'lessThanOrEqual' | 'notEquals' | 'notContains' | 'isEmpty' | 'isNotEmpty'

interface AdditionalFilter {
  id: string
  field: string
  operator: FilterOperator
  value: string
  type: 'text' | 'number' | 'select' | 'boolean'
}

const additionalFilters = ref<AdditionalFilter[]>([])
const sheetOpen = ref(false)

const availableFields = computed(() => [
  { value: 'id', label: t('referentiels.columns.id'), type: 'number' as const },
  { value: 'code', label: t('referentiels.columns.code'), type: 'text' as const },
  { value: 'dataType', label: t('referentiels.columns.dataType'), type: 'select' as const },
  { value: 'label', label: t('referentiels.columns.label'), type: 'text' as const },
  { value: 'description', label: t('referentiels.columns.description'), type: 'text' as const },
  { value: 'externalCode', label: t('referentiels.fields.externalCode'), type: 'text' as const },
  { value: 'locale', label: t('referentiels.fields.locale'), type: 'text' as const },
  { value: 'displayOrder', label: t('referentiels.fields.displayOrder'), type: 'number' as const },
  { value: 'active', label: t('referentiels.columns.active'), type: 'select' as const },
])

const filterOperators = computed(() => [
  { value: 'equals', label: t('filter.operator.equals'), applicableTypes: ['text', 'number', 'select', 'boolean'] },
  { value: 'notEquals', label: t('filter.operator.notEquals'), applicableTypes: ['text', 'number', 'select', 'boolean'] },
  { value: 'contains', label: t('filter.operator.contains'), applicableTypes: ['text'] },
  { value: 'notContains', label: t('filter.operator.notContains'), applicableTypes: ['text'] },
  { value: 'startsWith', label: t('filter.operator.startsWith'), applicableTypes: ['text'] },
  { value: 'endsWith', label: t('filter.operator.endsWith'), applicableTypes: ['text'] },
  { value: 'greaterThan', label: t('filter.operator.greaterThan'), applicableTypes: ['number'] },
  { value: 'lessThan', label: t('filter.operator.lessThan'), applicableTypes: ['number'] },
  { value: 'greaterThanOrEqual', label: t('filter.operator.greaterThanOrEqual'), applicableTypes: ['number'] },
  { value: 'lessThanOrEqual', label: t('filter.operator.lessThanOrEqual'), applicableTypes: ['number'] },
  { value: 'isEmpty', label: t('filter.operator.isEmpty'), applicableTypes: ['text', 'number', 'select', 'boolean'] },
  { value: 'isNotEmpty', label: t('filter.operator.isNotEmpty'), applicableTypes: ['text', 'number', 'select', 'boolean'] },
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

const addFilter = () => {
  const firstField = availableFields.value[0]
  if (firstField) {
    const availableOps = getOperatorsForField(firstField.type)
    const firstOp = availableOps[0]
    if (firstOp) {
      additionalFilters.value.push({
        id: Date.now().toString(),
        field: firstField.value,
        operator: firstOp.value as FilterOperator,
        value: '',
        type: firstField.type,
      })
    }
  }
}

const removeFilter = (id: string) => {
  additionalFilters.value = additionalFilters.value.filter(f => f.id !== id)
}

const handleApply = () => {
  emit('update:code', codeFilter.value)
  emit('update:dataType', dataTypeFilter.value ?? undefined)
  emit('update:label', labelFilter.value)
  emit('update:active', activeFilter.value)
  emit('update:additionalFilters', additionalFilters.value)
  emit('apply')
}

const handleApplyAdvancedFilters = () => {
  emit('update:additionalFilters', additionalFilters.value)
  emit('apply')
}

const handleDataTypeChange = (value: unknown) => {
  if (value === 'all' || value === null || value === undefined) {
    dataTypeFilter.value = undefined
  } else if (typeof value === 'string') {
    dataTypeFilter.value = value
  }
}

const handleReset = () => {
  // Reset local state
  codeFilter.value = ''
  dataTypeFilter.value = undefined
  labelFilter.value = ''
  activeFilter.value = undefined
  additionalFilters.value = []
  // Emit updates to sync with parent component
  emit('update:code', '')
  emit('update:dataType', undefined)
  emit('update:label', '')
  emit('update:active', undefined)
  emit('update:additionalFilters', [])
  // Emit reset event to trigger parent's reset handler
  emit('reset')
}

const handleResetAdvancedFilters = () => {
  additionalFilters.value = []
  emit('update:additionalFilters', [])
  // Trigger apply to refresh results with cleared filters
  emit('apply')
}
</script>

<template>
  <Card class="shadow-sm border-border ">
    <CardContent class="p-5">
      <!-- Main Filters -->
      <div class="flex flex-col sm:flex-row items-stretch sm:items-end gap-4">
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 flex-1">
          <!-- Code Filter -->
          <div class="space-y-2">
            <label class="text-sm font-medium text-foreground leading-none">
              {{ t('referentiels.filters.code') }}
            </label>
            <Input
              v-model="codeFilter"
              type="text"
              :placeholder="t('referentiels.filters.filterByCode')"
              class="h-10"
            />
          </div>

          <!-- Data Type Filter -->
          <div class="space-y-2">
            <label class="text-sm font-medium text-foreground leading-none">
              {{ t('referentiels.filters.dataType') }}
            </label>
            <Select 
              :model-value="dataTypeFilter === undefined ? 'all' : dataTypeFilter"
              @update:model-value="handleDataTypeChange"
            >
              <SelectTrigger class="h-10">
                <SelectValue :placeholder="t('referentiels.filters.allDataTypes')" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">
                  {{ t('referentiels.filters.allDataTypes') }}
                </SelectItem>
                <SelectItem
                  v-for="option in dataTypeOptions"
                  :key="option.value"
                  :value="option.value"
                >
                  {{ option.label }}
                </SelectItem>
              </SelectContent>
            </Select>
          </div>

          <!-- Label Filter -->
          <div class="space-y-2">
            <label class="text-sm font-medium text-foreground leading-none">
              {{ t('referentiels.filters.label') }}
            </label>
            <Input
              v-model="labelFilter"
              type="text"
              :placeholder="t('referentiels.filters.filterByLabel')"
              class="h-10"
            />
          </div>

          <!-- Active Filter -->
          <div class="space-y-2">
            <label class="text-sm font-medium text-foreground leading-none">
              {{ t('referentiels.filters.active') }}
            </label>
            <Select 
              :model-value="activeFilter === undefined ? 'all' : String(activeFilter)"
              @update:model-value="(value) => {
                if (value === 'all') {
                  activeFilter = undefined
                } else {
                  activeFilter = value === 'true'
                }
              }"
            >
              <SelectTrigger class="h-10">
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem
                  v-for="option in activeOptions"
                  :key="String(option.value ?? 'all')"
                  :value="option.value === undefined ? 'all' : String(option.value)"
                >
                  {{ option.label }}
                </SelectItem>
              </SelectContent>
            </Select>
          </div>
        </div>

        <!-- Action Buttons -->
        <div class="flex items-end gap-2 shrink-0">
          <Sheet :open="sheetOpen" @update:open="sheetOpen = $event">
            <SheetTrigger as-child>
              <Button variant="outline" size="sm" class="h-10 gap-2">
                <SlidersHorizontal class="h-4 w-4" />
                {{ t('common.moreFilters') }}
              </Button>
            </SheetTrigger>
            <SheetContent side="right" :transparent-overlay="true" class="w-full sm:w-[700px] lg:w-[900px] xl:w-[1000px] p-6 bg-[var(--bg-filter)]">
              <SheetHeader class="space-y-3 mb-6">
                <SheetTitle class="text-xl">{{ t('filter.advancedFilters') }}</SheetTitle>
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
                        @click="removeFilter(filter.id)"
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
                        :type="filter.type === 'number' ? 'number' : 'text'"
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
                          <SelectValue :placeholder="t('filter.enterValue')" />
                        </SelectTrigger>
                        <SelectContent>
                          <template v-if="filter.field === 'dataType'">
                            <SelectItem
                              v-for="option in dataTypeOptions"
                              :key="option.value"
                              :value="option.value"
                            >
                              {{ option.label }}
                            </SelectItem>
                          </template>
                          <template v-else-if="filter.field === 'active'">
                            <SelectItem
                              v-for="option in activeOptions"
                              :key="String(option.value ?? 'all')"
                              :value="option.value === undefined ? 'all' : String(option.value)"
                            >
                              {{ option.label }}
                            </SelectItem>
                          </template>
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
                  @click="addFilter"
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
                    @click="handleResetAdvancedFilters"
                  >
                    <RotateCcw class="h-4 w-4" />
                    {{ t('common.reset') }}
                  </Button>
                  <Button
                    variant="default"
                    size="sm"
                    class="flex-1 h-10 gap-2"
                    @click="handleApplyAdvancedFilters"
                  >
                    {{ t('common.apply') }}
                  </Button>
                </div>
              </div>
            </SheetContent>
          </Sheet>

          <Button variant="outline" size="sm" class="h-10 gap-2" @click="handleReset">
            <RotateCcw class="h-4 w-4" />
            {{ t('common.reset') }}
          </Button>
          <Button size="sm" class="h-10 gap-2" @click="handleApply">
            {{ t('common.apply') }}
          </Button>
        </div>
      </div>
    </CardContent>
  </Card>
</template>

