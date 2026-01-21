<template>
  <ErrorBoundary>
    <AppShell>
      <RouterView />
    </AppShell>
    <Toaster />
  </ErrorBoundary>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { AppShell } from '@/common/layouts'
import { Toaster } from '@/components/ui/sonner'
import { ErrorBoundary } from '@/common/components'
import { useAuth } from '@/features/auth/composables/useAuth'
import { logger } from '@/shared/utils/logger'

// Initialize authentication on app mount
const { initAuth } = useAuth()

onMounted(async () => {
  try {
    await initAuth()
    logger.info('Auth initialized on app mount', {}, 'App')
  } catch (error) {
    logger.error('Failed to initialize auth on app mount', error, 'App')
  }
})
</script>
