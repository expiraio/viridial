const OPERATOR_MAP: Record<string, string> = {
  equals: 'EQUALS',
  notEquals: 'NOT_EQUALS',
  contains: 'CONTAINS',
  notContains: 'NOT_CONTAINS',
  startsWith: 'STARTS_WITH',
  endsWith: 'ENDS_WITH',
  greaterThan: 'GREATER_THAN',
  lessThan: 'LESS_THAN',
  greaterThanOrEqual: 'GREATER_THAN_OR_EQUAL',
  lessThanOrEqual: 'LESS_THAN_OR_EQUAL',
  isEmpty: 'IS_NULL',
  isNotEmpty: 'IS_NOT_NULL',
}

export const mapOperatorToBackend = (operator: string): string => 
  OPERATOR_MAP[operator] || operator.toUpperCase()

