<script setup lang="ts">
import { computed } from 'vue'
import { Card, CardContent } from "@/components/ui/card"
import { Users, UserCheck, UserX, Clock } from 'lucide-vue-next'
import { useI18n } from '@/shared/composables/useI18n'

const { t } = useI18n()

interface Props {
  total?: number
  active?: number
  inactive?: number
  pending?: number
}

const props = withDefaults(defineProps<Props>(), {
  total: 0,
  active: 0,
  inactive: 0,
  pending: 0,
})

const statItems = computed(() => [
  {
    label: t('stats.users'),
    value: props.total,
    icon: Users,
    color: 'text-blue-600 dark:text-blue-400',
    bgColor: 'bg-blue-50 dark:bg-blue-950/50',
    borderClass: 'border-r border-b'
  },
  {
    label: t('stats.active'),
    value: props.active,
    icon: UserCheck,
    color: 'text-green-600 dark:text-green-400',
    bgColor: 'bg-green-50 dark:bg-green-950/50',
    borderClass: 'border-r border-b'
  },
  {
    label: t('stats.inactive'),
    value: props.inactive,
    icon: UserX,
    color: 'text-gray-600 dark:text-gray-400',
    bgColor: 'bg-gray-50 dark:bg-gray-900/50',
    borderClass: 'border-r border-b'
  },
  {
    label: t('stats.pending'),
    value: props.pending,
    icon: Clock,
    color: 'text-orange-600 dark:text-orange-400',
    bgColor: 'bg-orange-50 dark:bg-orange-950/50',
    borderClass: 'border-b'
  },
])
</script>

<template>
  <Card class="shadow-sm border-border/50 overflow-hidden">
    <CardContent class="p-0">
      <div class="grid grid-cols-2 sm:grid-cols-4 gap-0 divide-x divide-y divide-border">
        <div
          v-for="item in statItems"
          :key="item.label"
          class="p-5 hover:bg-muted/30 transition-colors duration-200"
        >
          <div class="flex items-center gap-4">
            <div :class="[item.bgColor, item.color, 'p-3 rounded-lg shadow-sm']">
              <component :is="item.icon" class="h-5 w-5" />
            </div>
            <div class="flex-1 min-w-0 space-y-1">
              <p class="text-xs font-medium text-muted-foreground uppercase tracking-wide">
                {{ item.label }}
              </p>
              <p class="text-2xl md:text-3xl font-bold text-foreground tabular-nums">
                {{ item.value.toLocaleString() }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </CardContent>
  </Card>
</template>
