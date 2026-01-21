import { ref } from 'vue'

export function useQuickView() {
  const open = ref(false)
  const data = ref<any>(null)

  const show = (item: any) => {
    data.value = item
    open.value = true
  }

  const hide = () => {
    open.value = false
    data.value = null
  }

  return { open, data, show, hide }
}

