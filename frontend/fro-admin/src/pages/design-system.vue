<script setup lang="ts">
import { useI18n } from '@/composables/useI18n'
import Filter from '@/components/Filter.vue'
import List from '@/components/List.vue'
import Stats from '@/components/Stats.vue'
import { usePaymentColumns } from '@/composables/usePaymentColumns'
import { Button } from '@/components/ui/button'
import { CirclePlus } from 'lucide-vue-next'

const { t } = useI18n()
const columns = usePaymentColumns(t)
const data: any[] = []
</script>

<template>
  <div class="flex flex-col h-full min-h-0 bg-background">
    <!-- Page Header -->
    <header
      class="shrink-0 sticky top-0 z-30 px-4 md:px-6 py-4 border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60"
    >
      <div class="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div class="space-y-1">
          <h1 class="text-2xl md:text-3xl font-bold tracking-tight text-foreground">
            {{ t('designSystem.title') }}
          </h1>
          <p class="text-sm text-muted-foreground">
            {{ t('designSystem.subtitle') }}
          </p>
        </div>
        <div class="flex items-center gap-2">
          <Button size="sm" class="gap-2 font-semibold shadow-sm">
            <CirclePlus class="h-4 w-4" />
            {{ t('designSystem.newUser') }}
          </Button>
        </div>
      </div>
    </header>

    <!-- Page Content -->
    <div class="flex-1 flex flex-col min-h-0 overflow-hidden">
      <!-- Stats Section -->
      <section class="shrink-0 px-4 md:px-6 pt-6">
        <Stats />
      </section>

      <!-- Filter Section -->
      <section class="shrink-0 px-4 md:px-6 pt-4">
        <Filter />
      </section>

      <!-- Main Content Section -->
      <section class="flex-1 flex flex-col min-h-0 px-4 md:px-6 pt-4 pb-6 overflow-hidden">
        <div class="flex-1 flex flex-col min-h-0 overflow-hidden bg-card rounded-lg border shadow-sm">
          <List :data="data" :columns="columns" :enable-row-selection="true" />
        </div>
      </section>
    </div>
  </div>
</template>
