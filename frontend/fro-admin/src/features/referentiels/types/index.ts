/**
 * Referential Types
 */

export interface Referential {
  id: number
  code: string
  dataType: string
  label: string
  description?: string
  externalCode?: string
  iconUrl?: string
  displayOrder: number
  locale: string
  typeId?: number
  typeCode?: string
  typeLabel?: string
  typeDescription?: string
  subTypeId?: number
  subTypeCode?: string
  subTypeLabel?: string
  subTypeDescription?: string
  parentId?: number
  parentCode?: string
  parentLabel?: string
  parentDescription?: string
  active: boolean
  startDate?: Date
  endDate?: Date
  createdAt?: Date
  updatedAt?: Date
}

export interface ReferentialSearchForm {
  id?: number
  ids?: number[]
  code?: string
  dataType?: string
  label?: string
  typeId?: number
  subTypeId?: number
  parentId?: number
  active?: boolean
  locale?: string
  page?: number
  size?: number
  sortBy?: string
  sortDirection?: string
  sorts?: Array<{
    field: string
    direction: string
  }>
  filters?: Array<{
    field: string
    operator: string
    value: any
  }>
  ranges?: Record<string, {
    min?: any
    max?: any
  }>
  logicalOperator?: string
  includeDeleted?: boolean
}

