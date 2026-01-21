# Stores Structure

This directory contains shared stores that can be used across multiple features.

## Structure

For feature-specific stores, create them within the feature directory:
```
features/
  my-feature/
    stores/
      my-feature.store.ts
```

## Example Store

```typescript
import { defineStore } from 'pinia'

export const useMyStore = defineStore('myStore', {
  state: () => ({
    // state
  }),
  getters: {
    // getters
  },
  actions: {
    // actions
  },
})
```

