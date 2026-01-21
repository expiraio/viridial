# Project Structure

This document describes the structure of the Viridial Admin frontend application, organized for scalability and maintainability.

## Directory Structure

```
src/
├── app/                    # App-level configuration (deprecated, use common/)
│   └── providers/         # Global providers (Theme, etc.)
│
├── assets/                # Static assets (images, fonts, etc.)
│   └── style.css         # Global styles
│
├── common/                # Common/shared components and layouts
│   ├── components/       # Shared components used across features
│   └── layouts/          # Layout components (AppShell, AppHeader, AppAside)
│
├── components/            # UI component library (shadcn/ui style)
│   └── ui/               # Reusable UI primitives (Button, Card, Table, etc.)
│
├── core/                  # Core application logic
│   ├── config/           # Application configuration
│   │   ├── app.config.ts # Main app config
│   │   └── constants.ts  # Application constants
│   ├── i18n/             # Internationalization setup
│   ├── middleware/       # Route guards and middleware
│   ├── plugins/          # Plugin registration
│   └── router/           # Router configuration
│
├── features/              # Feature-based modules
│   └── design-system/    # Example feature
│       ├── components/   # Feature-specific components
│       ├── composables/  # Feature-specific composables
│       ├── pages/        # Feature pages/views
│       ├── types/        # Feature-specific types
│       └── index.ts      # Feature exports
│
├── lib/                   # Library utilities
│   └── utils.ts          # Utility functions (cn, valueUpdater, etc.)
│
├── shared/                # Shared resources
│   ├── api/              # API client and services
│   │   └── client.ts     # Axios-based HTTP client
│   ├── composables/      # Shared composables
│   │   └── useI18n.ts    # i18n composable
│   ├── i18n/             # i18n resources
│   │   └── locales/      # Translation files
│   ├── stores/           # Shared Pinia stores
│   ├── types/            # Shared TypeScript types
│   └── utils/            # Shared utility functions
│
├── App.vue               # Root component
└── main.ts              # Application entry point
```

## Key Concepts

### Core (`/core`)
Contains the foundational application setup:
- **config/**: Centralized configuration and constants
- **i18n/**: Internationalization setup
- **middleware/**: Route guards (auth, guest, etc.)
- **plugins/**: Plugin registration (router, i18n, etc.)
- **router/**: Router configuration with lazy loading

### Features (`/features`)
Feature-based modules following the principle of feature isolation:
- Each feature is self-contained
- Contains components, composables, pages, types, and API services
- Promotes code organization and reusability

### Shared (`/shared`)
Shared resources used across multiple features:
- **api/**: HTTP client with interceptors
- **composables/**: Reusable Vue composables
- **types/**: Shared TypeScript types
- **utils/**: Shared utility functions
- **stores/**: Shared Pinia stores

### Common (`/common`)
Common components and layouts:
- **components/**: Shared components used across features
- **layouts/**: Layout components (shell, header, sidebar)

### Components (`/components`)
UI component library:
- **ui/**: Reusable UI primitives (Button, Card, Table, etc.)
- These are design system components, not feature-specific

## Import Paths

Use the `@/` alias for imports:

```typescript
// Core
import { appConfig } from '@/core/config/app.config'
import { router } from '@/core/router'

// Shared
import { apiClient } from '@/shared/api/client'
import { useI18n } from '@/shared/composables/useI18n'
import type { ApiResponse } from '@/shared/types'

// Features
import { DesignSystemPage } from '@/features/design-system'

// Common
import { AppShell } from '@/common/layouts'

// Components
import { Button } from '@/components/ui/button'

// Utils
import { cn } from '@/lib/utils'
```

## Adding a New Feature

1. Create feature directory: `src/features/{feature-name}/`
2. Add subdirectories: `components/`, `composables/`, `pages/`, `types/`, `api/`
3. Create feature pages in `pages/`
4. Add routes to `core/router/index.ts` with lazy loading
5. Export from `features/{feature-name}/index.ts`

## Best Practices

1. **Feature Isolation**: Keep features independent and self-contained
2. **Shared Resources**: Use `@/shared` for common utilities
3. **Type Safety**: Define types in feature `types/` or `shared/types/`
4. **Lazy Loading**: Use lazy loading for routes to improve performance
5. **Consistent Naming**: Use consistent naming conventions across features
6. **Documentation**: Document complex features and utilities

## Migration Notes

The project has been restructured from a flat structure to a feature-based structure:
- Old `pages/` → `features/{feature}/pages/`
- Old `composables/` → `features/{feature}/composables/` or `shared/composables/`
- Old `i18n/` → `shared/i18n/`
- Old `app/layouts/` → `common/layouts/`
- Old `router/` → `core/router/`

