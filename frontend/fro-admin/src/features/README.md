# Features Directory

This directory contains feature-based modules. Each feature is self-contained with its own components, composables, stores, types, and API services.

## Feature Structure

```
features/
  {feature-name}/
    components/       # Feature-specific components
    composables/     # Feature-specific composables
    pages/          # Feature pages/views
    stores/         # Feature-specific stores (if using Pinia)
    api/            # Feature-specific API services
    types/          # Feature-specific TypeScript types
    index.ts        # Feature exports
```

## Example Feature

```
features/
  users/
    components/
      UserCard.vue
      UserForm.vue
    composables/
      useUsers.ts
    pages/
      UsersListPage.vue
      UserDetailPage.vue
    stores/
      users.store.ts
    api/
      users.service.ts
    types/
      index.ts
    index.ts
```

## Best Practices

1. **Self-contained**: Each feature should be as independent as possible
2. **Shared resources**: Use `@/shared` for common utilities, types, and components
3. **UI components**: Use `@/components/ui` for reusable UI primitives
4. **Routing**: Define routes in `core/router` but keep feature pages in features
5. **Exports**: Use `index.ts` files for clean imports

## Adding a New Feature

1. Create the feature directory structure
2. Add feature routes to `core/router/index.ts`
3. Export feature components/types from `features/{feature}/index.ts`
4. Use lazy loading for feature pages in routes

