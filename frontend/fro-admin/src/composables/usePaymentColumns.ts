import type { ColumnDef } from '@tanstack/vue-table'
import type { Composer } from 'vue-i18n'

export interface Payment {
  id: string
  amount: number
  status: 'pending' | 'processing' | 'success' | 'failed'
  email: string
  naaa: number
}

export const usePaymentColumns = (t: Composer['t']): ColumnDef<Payment>[] => {
  return []
}
