/**
 * Payment columns composable
 * Feature-specific composable for payment columns
 */

import type { PaymentColumn } from '../types'
import { useI18n } from '@/shared/composables/useI18n'

export const usePaymentColumns = (t: (key: string) => string): PaymentColumn[] => {
  return [
    {
      id: 'id',
      label: t('payment.id'),
      accessorKey: 'id',
    },
    {
      id: 'amount',
      label: t('payment.amount'),
      accessorKey: 'amount',
    },
    {
      id: 'status',
      label: t('payment.status'),
      accessorKey: 'status',
    },
    {
      id: 'date',
      label: t('payment.date'),
      accessorKey: 'date',
    },
  ]
}

