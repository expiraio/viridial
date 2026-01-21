# API Layer

This directory contains the API client and service layer.

## Structure

- `client.ts` - Axios-based HTTP client with interceptors
- `services/` - Feature-specific API services (create as needed)

## Usage

```typescript
import { apiClient } from '@/shared/api/client'

// GET request
const response = await apiClient.get<User[]>('/users')

// POST request
const newUser = await apiClient.post<User>('/users', userData)

// PUT request
const updated = await apiClient.put<User>('/users/1', userData)

// DELETE request
await apiClient.delete('/users/1')
```

## Creating Feature Services

Create feature-specific services in `features/{feature-name}/api/`:

```typescript
import { apiClient } from '@/shared/api/client'
import type { User } from '../types'

export const userService = {
  getAll: () => apiClient.get<User[]>('/users'),
  getById: (id: string) => apiClient.get<User>(`/users/${id}`),
  create: (user: Partial<User>) => apiClient.post<User>('/users', user),
  update: (id: string, user: Partial<User>) => apiClient.put<User>(`/users/${id}`, user),
  delete: (id: string) => apiClient.delete(`/users/${id}`),
}
```

