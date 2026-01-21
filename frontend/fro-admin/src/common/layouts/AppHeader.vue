<script setup lang="ts">
import { Button } from "@/components/ui/button"

defineEmits<{
  (e: "toggle-sidebar"): void
}>()
</script>

<template>
  <header
    class="sticky top-0 z-30 border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60"
  >
    <div class="flex items-center justify-between w-full px-3 md:px-4 h-14">
      <!-- Left -->
      <div class="flex items-center gap-2 md:gap-4 flex-1">
        <!-- Mobile menu -->
        <Button
          variant="ghost"
          size="icon"
          class="md:hidden"
          @click="$emit('toggle-sidebar')"
        >
          ☰
        </Button>

        <span class="text-sm md:text-base font-semibold tracking-tight">
          Viridial
        </span>
      </div>

      <!-- Right -->
      <div class="flex items-center gap-1 md:gap-2">
        <Button variant="ghost" size="icon" class="h-9 w-9">
          🌙
        </Button>
      </div>
    </div>
  </header>
</template>
