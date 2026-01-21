<script setup lang="ts">
import { computed } from "vue"
import { cn } from "@/lib/utils"
import { useI18n } from "@/composables/useI18n"

const { t } = useI18n()

const props = defineProps<{
  open: boolean
}>()

defineEmits<{
  (e: "close"): void
}>()

const asideClasses = computed(() =>
  cn(
    "fixed inset-y-0 left-0 z-40",
    "h-screen w-64",
    "border-r bg-sidebar text-sidebar-foreground",
    "transition-transform duration-300 ease-in-out",
    "md:translate-x-0 md:relative md:z-auto",
    props.open ? "translate-x-0" : "-translate-x-full md:translate-x-0"
  )
)
</script>

<template>
  <!-- Mobile Overlay -->
  <div
    v-if="open"
    class="fixed inset-0 z-30 bg-black/50 md:hidden"
    @click="$emit('close')"
  />

  <!-- Sidebar -->
  <aside :class="asideClasses" style="background-color:#01002d">
    <div class="flex items-center justify-between gap-2 px-3 h-12 border-b border-white/10">
        <div class="flex items-center gap-2"><div class="flex-shrink-0 w-7 h-7 bg-[#5955D1] rounded-md flex items-center justify-center"><span class="text-white font-bold text-sm">V</span></div><span class="text-sm font-bold text-white">{{ t('nav.viridial') }}</span></div>
    </div>
    <div class="flex-1 overflow-y-auto overflow-x-hidden">
        <div class="p-2 space-y-1">
            <div class="space-y-0.5 mb-3"><div class="relative group"><a class="block w-full" href="/admin"><div class="flex items-center gap-2 px-3 py-1.5 text-xs font-medium rounded-md transition-all duration-200 hover:bg-white/10 text-white/80" style="color:rgba(255, 255, 255, 0.8)"><span class="flex-shrink-0 w-4 h-4 flex items-center justify-center transition-colors text-white/90"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-layout-dashboard" aria-hidden="true"><rect width="7" height="9" x="3" y="3" rx="1"></rect><rect width="7" height="5" x="14" y="3" rx="1"></rect><rect width="7" height="9" x="14" y="12" rx="1"></rect><rect width="7" height="5" x="3" y="16" rx="1"></rect></svg></span><span class="flex-1 truncate text-xs">{{ t('nav.dashboard') }}</span></div></a></div></div>
        </div>
    </div>
  </aside>
</template>
