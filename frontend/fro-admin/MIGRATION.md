# Migration Guide

This document outlines the changes made to restructure the project for scalability.

## What Changed

### Directory Structure

**Before:**
```
src/
├── app/
│   └── layouts/
├── components/
├── composables/
├── i18n/
├── pages/
├── router/
└── stores/
```

**After:**
```
src/
├── core/              # Core application logic
├── features/          # Feature-based modules
├── shared/           # Shared resources
├── common/           # Common components & layouts
└── components/       # UI component library
```

### Key Changes

1. **Core Directory** (`/core`)
   - Centralized configuration (`config/`)
   - Router setup with lazy loading (`router/`)
   - Middleware/guards (`middleware/`)
   - Plugin registration (`plugins/`)
   - i18n setup (`i18n/`)

2. **Features Directory** (`/features`)
   - Feature-based organization
   - Each feature is self-contained
   - Example: `design-system/` feature with components, composables, pages, types

3. **Shared Directory** (`/shared`)
   - API client (`api/client.ts`)
   - Shared composables (`composables/`)
   - Shared types (`types/`)
   - Shared utilities (`utils/`)
   - i18n locales (`i18n/locales/`)
   - Shared stores (`stores/`)

4. **Common Directory** (`/common`)
   - Common layouts (`layouts/`)
   - Common components (`components/`)

## Import Path Updates

### Updated Imports

**Before:**
```typescript
import { router } from './router'
import i18n from './i18n'
import { useI18n } from '@/composables/useI18n'
import AppShell from '@/app/layouts/AppShell.vue'
import DesignSystem from '@/pages/design-system.vue'
```

**After:**
```typescript
import { router } from '@/core/router'
import i18n from '@/core/i18n'
import { useI18n } from '@/shared/composables/useI18n'
import { AppShell } from '@/common/layouts'
import DesignSystemPage from '@/features/design-system/pages/DesignSystemPage.vue'
```

## File Moves

- `src/i18n/` → `src/shared/i18n/`
- `src/app/layouts/` → `src/common/layouts/`
- `src/router/` → `src/core/router/`
- `src/pages/design-system.vue` → `src/features/design-system/pages/DesignSystemPage.vue`
- `src/composables/useI18n.ts` → `src/shared/composables/useI18n.ts`
- `src/composables/usePaymentColumns.ts` → `src/features/design-system/composables/usePaymentColumns.ts`
- `src/components/Filter.vue` → `src/features/design-system/components/Filter.vue`
- `src/components/List.vue` → `src/features/design-system/components/List.vue`
- `src/components/Stats.vue` → `src/features/design-system/components/Stats.vue`

## New Files Created

### Core
- `core/config/app.config.ts` - Application configuration
- `core/config/constants.ts` - Application constants
- `core/plugins/index.ts` - Plugin registration
- `core/middleware/auth.guard.ts` - Authentication guard
- `core/middleware/guest.guard.ts` - Guest guard

### Shared
- `shared/api/client.ts` - HTTP client with interceptors
- `shared/types/index.ts` - Shared TypeScript types
- `shared/utils/index.ts` - Shared utilities

### Features
- `features/design-system/types/index.ts` - Feature types
- `features/design-system/index.ts` - Feature exports

## Breaking Changes

1. **Import paths** - All imports need to be updated to use new paths
2. **Router** - Router now uses lazy loading for routes
3. **Main.ts** - Uses centralized plugin registration

## Next Steps

1. Update any remaining imports in your codebase
2. Create new features following the feature structure
3. Move shared components to `common/components/` if used across features
4. Add feature-specific API services in `features/{feature}/api/`
5. Add feature-specific stores in `features/{feature}/stores/` if needed

## Benefits

1. **Scalability** - Easy to add new features without cluttering
2. **Maintainability** - Clear separation of concerns
3. **Reusability** - Shared resources are clearly identified
4. **Performance** - Lazy loading for routes
5. **Type Safety** - Better organization of types
6. **Team Collaboration** - Features can be worked on independently

