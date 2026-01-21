<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../composables/useAuth'
import { useI18n } from '@/shared/composables/useI18n'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card'
import { Checkbox } from '@/components/ui/checkbox'
import { Label } from '@/components/ui/label'
import type { LoginCredentials } from '../types'

const router = useRouter()
const { login, isLoading } = useAuth()
const { t } = useI18n()

const email = ref('')
const password = ref('')
const rememberMe = ref(false)
const error = ref<string | null>(null)

const handleSubmit = async (e: Event) => {
  e.preventDefault()
  error.value = null

  if (!email.value || !password.value) {
    error.value = 'Please fill in all fields'
    return
  }

  try {
    const credentials: LoginCredentials = {
      email: email.value,
      password: password.value,
      rememberMe: rememberMe.value,
    }

    await login(credentials)
    router.push('/')
  } catch (err: any) {
    error.value = err.message || 'Login failed. Please try again.'
  }
}
</script>

<template>
  <div class="flex min-h-screen items-center justify-center bg-background p-4">
    <Card class="w-full max-w-md">
      <CardHeader class="space-y-1">
        <CardTitle class="text-2xl font-bold">Sign in</CardTitle>
        <CardDescription>
          Enter your credentials to access your account
        </CardDescription>
      </CardHeader>
      <CardContent>
        <form @submit="handleSubmit" class="space-y-4">
          <div v-if="error" class="rounded-md bg-destructive/15 p-3 text-sm text-destructive">
            {{ error }}
          </div>

          <div class="space-y-2">
            <Label for="email">Email</Label>
            <Input
              id="email"
              v-model="email"
              type="email"
              placeholder="name@example.com"
              required
              :disabled="isLoading"
            />
          </div>

          <div class="space-y-2">
            <Label for="password">Password</Label>
            <Input
              id="password"
              v-model="password"
              type="password"
              placeholder="••••••••"
              required
              :disabled="isLoading"
            />
          </div>

          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-2">
              <Checkbox id="remember" v-model="rememberMe" :disabled="isLoading" />
              <Label for="remember" class="text-sm font-normal cursor-pointer">
                Remember me
              </Label>
            </div>
            <a href="/forgot-password" class="text-sm text-primary hover:underline">
              Forgot password?
            </a>
          </div>

          <Button type="submit" class="w-full" :disabled="isLoading">
            <span v-if="isLoading">Signing in...</span>
            <span v-else>Sign in</span>
          </Button>
        </form>
      </CardContent>
    </Card>
  </div>
</template>

