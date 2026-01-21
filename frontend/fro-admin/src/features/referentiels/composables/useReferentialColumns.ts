/**
 * Referential Columns Composable
 * Defines column structure for referential table
 */

import type { ColumnDef } from '@tanstack/vue-table'
import type { Referential } from '../types'
import type { Composer } from 'vue-i18n'
import { Badge } from '@/components/ui/badge'
import { h } from 'vue'

export function useReferentialColumns(t: Composer['t']): ColumnDef<Referential>[] {
  return [
    {
      accessorKey: 'parentLabel',
      header: () => t('referentiels.columns.parentLabel'),
      cell: ({ row }) => {
        return h('div', { class: 'font-medium' }, [
          row.original.parentLabel,
          h('small', { class: 'block text-gray-500' }, row.original.parentCode),
        ])
      },
    },
    {
      accessorKey: 'code',
      header: () => t('referentiels.columns.code'),
      cell: ({ row }) => {
        return h('div', { class: 'font-medium' }, row.original.code)
      },
    },
    {
      accessorKey: 'dataType',
      header: () => t('referentiels.columns.dataType'),
      cell: ({ row }) => {
        return h(Badge, { variant: 'secondary' }, () => row.original.dataType)
      },
    },
    {
      accessorKey: 'label',
      header: () => t('referentiels.columns.label'),
      cell: ({ row }) => {
        return h('div', { class: 'max-w-[200px] truncate' }, row.original.label)
      },
    },
    {
      accessorKey: 'description',
      header: () => t('referentiels.columns.description'),
      cell: ({ row }) => {
        const desc = row.original.description
        return desc
          ? h('div', { class: 'max-w-[300px] truncate text-muted-foreground' }, desc)
          : h('span', { class: 'text-muted-foreground' }, '-')
      },
    },
    {
      accessorKey: 'externalCode',
      header: () => t('referentiels.columns.externalCode'),
      cell: ({ row }) => {
        const code = row.original.externalCode
        return code
          ? h('div', { class: 'font-mono text-sm' }, code)
          : h('span', { class: 'text-muted-foreground' }, '-')
      },
    },
    {
      accessorKey: 'displayOrder',
      header: () => t('referentiels.columns.displayOrder'),
      cell: ({ row }) => {
        return h('div', { class: 'text-center' }, row.original.displayOrder)
      },
    },
    {
      accessorKey: 'locale',
      header: () => t('referentiels.columns.locale'),
      cell: ({ row }) => {
        return h(Badge, { variant: 'outline' }, () => row.original.locale.toUpperCase())
      },
    },
    {
      accessorKey: 'createdAt',
      header: () => t('common.createdAt'),
      cell: ({ row }) => {
        return h('div', { class: 'text-center' }, row.original.createdAt ? new Date(row.original.createdAt).toLocaleString() : '-')
      },
    },
    
    {
      accessorKey: 'active',
      header: () => t('referentiels.columns.active'),
      cell: ({ row }) => {
        const isActive = row.original.active
        return h(
          Badge,
          { variant: isActive ? 'default' : 'danger' },
          () => isActive ? t('common.yes') : t('common.no')
        )
      },
    },
  ]
}

