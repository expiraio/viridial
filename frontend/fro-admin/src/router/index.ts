import { createRouter, createWebHistory } from "vue-router"
import DesignSystem from "@/pages/design-system.vue"

export const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/",
      redirect: "/design-system",
    },
    {
      path: "/design-system",
      component: DesignSystem,
    },
  ],
})
