<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from '@/composables/useI18n'
import { referentialService } from '../api/referential.service'
import type { Referential } from '../types'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { ArrowLeft, Loader2, Edit, CirclePlus } from 'lucide-vue-next'
import { useToast } from '@/shared/composables/useToast'

const { t } = useI18n()
const route = useRoute()
const router = useRouter()
const toast = useToast()

const loading = ref(true)
const referential = ref<Referential | null>(null)
const error = ref<string | null>(null)

const referentialId = computed(() => {
  const id = route.params.id
  return typeof id === 'string' ? parseInt(id, 10) : Number(id)
})

const fetchReferential = async () => {
  try {
    loading.value = true
    error.value = null
    const id = referentialId.value
    
    if (!id || isNaN(id)) {
      error.value = t('referentiels.columns.invalidId')
      return
    }

    const data = await referentialService.getById(id)
    if (!data) {
      error.value = t('referentiels.columns.notFound')
      return
    }
    
    referential.value = data
  } catch (err: any) {
    error.value = err?.message || t('common.errorOccurred')
    toast.error(t('common.errorOccurred'), err?.message || t('referentiels.columns.fetchError'))
  } finally {
    loading.value = false
  }
}

const goBack = () => {
  router.push('/referentiels')
}

const goEdit = () => {
  const id = referentialId.value
  if (id && !isNaN(id)) router.push(`/referentiels/${id}/edit`)
}

const goNew = () => {
  router.push('/referentiels/new')
}

onMounted(() => {
  fetchReferential()
})
</script>

<template>
  <div class="flex flex-col h-full min-h-0 bg-background">
    <!-- Header -->
    <header class="shrink-0 sticky top-0 z-30 px-4 md:px-6 py-4 border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div class="flex items-center gap-4">
        <Button variant="ghost" size="icon" @click="goBack" class="shrink-0">
          <ArrowLeft class="h-4 w-4" />
        </Button>
        <div class="flex-1 min-w-0">
          <h1 class="text-2xl font-bold tracking-tight text-foreground-title">
            {{ t('referentiels.detail.title') }}
          </h1>
          <p class="text-sm text-muted-foreground">{{ t('referentiels.detail.subtitle') }}</p>
        </div>
        <div class="flex items-center gap-2">
          <Button variant="outline" size="sm" class="gap-2" @click="goEdit" :disabled="!referential">
            <Edit class="h-4 w-4" />
            {{ t('common.edit') }}
          </Button>
          <Button size="sm" class="gap-2" @click="goNew">
            <CirclePlus class="h-4 w-4" />
            {{ t('referentiels.newReferential') }}
          </Button>
        </div>
      </div>
    </header>

    <!-- Content -->
    <div class="flex-1 overflow-y-auto p-4 md:p-6">
      <!-- Loading State -->
      <div v-if="loading" class="flex items-center justify-center h-full">
        <div class="flex flex-col items-center gap-4">
          <Loader2 class="h-8 w-8 animate-spin text-muted-foreground" />
          <p class="text-sm text-muted-foreground">{{ t('common.loading') }}</p>
        </div>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="flex items-center justify-center h-full">
        <Card class="max-w-md">
          <CardHeader>
            <CardTitle class="text-destructive">{{ t('common.error') }}</CardTitle>
            <CardDescription>{{ error }}</CardDescription>
          </CardHeader>
          <CardContent>
            <Button @click="fetchReferential" variant="outline" class="w-full">
              {{ t('common.retry') }}
            </Button>
          </CardContent>
        </Card>
      </div>

      <!-- Referential Details -->
      <div v-else-if="referential" class="mx-auto space-y-6">
        <!-- Main Info Card -->
        <Card>
          <CardHeader>
            <div class="flex items-start justify-between">
              <div class="space-y-1">
                <CardTitle class="text-2xl font-semibold">{{ referential.label }}</CardTitle>
                <CardDescription>{{ referential.code }}</CardDescription>
              </div>
              <Badge :variant="referential.active ? 'default' : 'secondary'">
                {{ referential.active ? t('common.active') : t('common.inactive') }}
              </Badge>
            </div>
          </CardHeader>
          <CardContent class="space-y-6">
            <!-- Basic Information -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div class="space-y-1">
                <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.columns.code') }}</p>
                <p class="text-sm">{{ referential.code }}</p>
              </div>
              <div class="space-y-1">
                <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.columns.dataType') }}</p>
                <p class="text-sm">{{ referential.dataType }}</p>
              </div>
              <div class="space-y-1">
                <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.columns.label') }}</p>
                <p class="text-sm">{{ referential.label }}</p>
              </div>
              <div class="space-y-1">
                <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.columns.locale') }}</p>
                <p class="text-sm">{{ referential.locale || '-' }}</p>
              </div>
              <div class="space-y-1">
                <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.columns.displayOrder') }}</p>
                <p class="text-sm">{{ referential.displayOrder }}</p>
              </div>
              <div class="space-y-1">
                <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.status') }}</p>
                <Badge :variant="referential.active ? 'default' : 'secondary'">
                  {{ referential.active ? t('common.active') : t('common.inactive') }}
                </Badge>
              </div>
            </div>

            <!-- Description -->
            <div v-if="referential.description" class="space-y-1">
              <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.columns.description') }}</p>
              <p class="text-sm">{{ referential.description }}</p>
            </div>

            <!-- External Code -->
            <div v-if="referential.externalCode" class="space-y-1">
              <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.columns.externalCode') }}</p>
              <p class="text-sm">{{ referential.externalCode }}</p>
            </div>

            <!-- Icon URL -->
            <div v-if="referential.iconUrl" class="space-y-1">
              <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.columns.iconUrl') }}</p>
              <a :href="referential.iconUrl" target="_blank" class="text-sm text-primary hover:underline">
                {{ referential.iconUrl }}
              </a>
            </div>

            <!-- Dates -->
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6 pt-4 border-t">
              <div class="space-y-1">
                <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.startDate') }}</p>
                <p class="text-sm" v-if="referential.startDate" >{{ new Date(referential.startDate).toLocaleDateString() }}</p><p v-else> -- </p>
              </div>
              <div class="space-y-1">
                <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.endDate') }}</p>
                <p class="text-sm" v-if="referential.endDate">{{ new Date(referential.endDate).toLocaleDateString() }}</p><p v-else> -- </p>
              </div>
              <div class="space-y-1">
                <p class="text-sm font-semibold text-muted-foreground">{{ t('common.createdAt') }}</p>
                <p class="text-sm" v-if="referential.createdAt">{{ new Date(referential.createdAt).toLocaleString() }}</p><p v-else> -- </p>
              </div>
              <div class="space-y-1">
                <p class="text-sm font-semibold text-muted-foreground">{{ t('common.updatedAt') }}</p>
                <p class="text-sm" v-if="referential.updatedAt">{{ new Date(referential.updatedAt).toLocaleString() }}</p><p v-else> -- </p>
              </div>
            </div>
          </CardContent>
        </Card>

        <!-- Relations Card -->
        <Card v-if="referential.typeId || referential.subTypeId || referential.parentId">
          <CardHeader>
            <CardTitle>{{ t('referentiels.relations') }}</CardTitle>
            <CardDescription>{{ t('referentiels.relationsDescription') }}</CardDescription>
          </CardHeader>
          <CardContent class="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div class="space-y-1">
              <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.typeLabel') }}</p>
              <p class="text-sm" v-if="referential.typeId" >{{ referential.typeLabel }}</p><p v-else> -- </p>
              <p class="text-sm" v-if="referential.typeId" >{{ referential.typeCode }}</p><p v-else> -- </p>
            </div>
            <div class="space-y-1">
              <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.subTypeLabel') }}</p>
              <p class="text-sm" v-if="referential.subTypeId">{{ referential.subTypeLabel }}</p> <p v-else> -- </p>
              <p class="text-sm" v-if="referential.subTypeId">{{ referential.subTypeCode }}</p> <p v-else> -- </p>
            </div>
            <div class="space-y-1">
              <p class="text-sm font-semibold text-muted-foreground">{{ t('referentiels.parentLabel') }}</p>
              <p class="text-sm"  v-if="referential.parentId">{{ referential.parentLabel }}</p><p v-else> -- </p>
              <p class="text-sm"  v-if="referential.parentId">{{ referential.parentCode }}</p><p v-else> -- </p>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  </div>
</template>

