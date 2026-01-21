<script setup lang="ts">
import { ref, onErrorCaptured, h } from 'vue'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { logger } from '@/shared/utils/logger'
import { ErrorHandler } from '@/shared/utils/error-handler'

const error = ref<Error | null>(null)
const errorInfo = ref<any>(null)

onErrorCaptured((err: Error, instance, info) => {
  error.value = err
  errorInfo.value = info

  const appError = ErrorHandler.parseError(err)
  logger.error('Error Boundary Caught Error', {
    error: appError,
    componentStack: info,
  }, 'ErrorBoundary')

  // Prevent the error from propagating
  return false
})

const resetError = () => {
  error.value = null
  errorInfo.value = null
}
</script>

<template>
  <div v-if="error" class="flex min-h-screen items-center justify-center p-4">
    <Card class="w-full max-w-2xl">
      <CardHeader>
        <CardTitle class="text-destructive">Something went wrong</CardTitle>
        <CardDescription>
          An unexpected error occurred. Please try refreshing the page.
        </CardDescription>
      </CardHeader>
      <CardContent class="space-y-4">
        <div class="rounded-md bg-destructive/15 p-4">
          <p class="font-mono text-sm text-destructive">
            {{ error.message }}
          </p>
        </div>

        <details v-if="errorInfo" class="rounded-md border p-4">
          <summary class="cursor-pointer text-sm font-medium">
            Error Details
          </summary>
          <pre class="mt-2 overflow-auto text-xs">{{ JSON.stringify(errorInfo, null, 2) }}</pre>
        </details>

        <div class="flex gap-2">
          <Button @click="resetError">Try Again</Button>
          <Button variant="outline" @click="window.location.reload()">
            Refresh Page
          </Button>
        </div>
      </CardContent>
    </Card>
  </div>
  <slot v-else />
</template>

