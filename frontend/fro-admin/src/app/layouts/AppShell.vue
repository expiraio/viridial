<script setup lang="ts">
import { ref } from "vue"
import AppHeader from "./AppHeader.vue"
import AppAside from "./AppAside.vue"

const sidebarOpen = ref(false)
</script>

<template>
  <div class="flex h-screen dark:bg-gray-950">
    <!-- Fixed Header -->
    

    <!-- Fixed Sidebar -->
    <AppAside
      :open="sidebarOpen"
      @close="sidebarOpen = false"
    />

    <!-- Scroll Container -->
    <main
      class="flex-1 flex flex-col overflow-hidden w-full md:w-auto"
    >
    <AppHeader @toggle-sidebar="sidebarOpen = !sidebarOpen" />
    <div class="flex-1 flex flex-col overflow-hidden bg-[var(--bg-main-color)]">
      <slot />
      </div>

     
    </main>
  </div>
</template>
