# Improvements: Logger, Error Handling, Authentication & Toast

This document describes the improvements made to the application including logger, error handling, authentication, and toast notifications.

## 🎯 Overview

The following improvements have been implemented:

1. **Logger System** - Centralized logging with different log levels
2. **Error Handling** - Comprehensive error handling with toast integration
3. **Authentication** - Complete authentication feature with login
4. **Toast Notifications** - Sonner toast integration for user feedback

## 📝 Logger System

### Location
`src/shared/utils/logger.ts`

### Features
- Multiple log levels: DEBUG, INFO, WARN, ERROR, NONE
- Log storage (last 100 logs)
- Context-aware logging
- Environment-based log level configuration
- Console output with appropriate methods

### Usage

```typescript
import { logger } from '@/shared/utils/logger'

// Different log levels
logger.debug('Debug message', { data: 'value' }, 'Context')
logger.info('Info message', { data: 'value' }, 'Context')
logger.warn('Warning message', { data: 'value' }, 'Context')
logger.error('Error message', { data: 'value' }, 'Context')

// Get logs
const recentErrors = logger.getLogs(LogLevel.ERROR, 10)

// Set log level
logger.setLogLevel(LogLevel.DEBUG)
```

### Configuration

Set log level via environment variable:
```env
VITE_LOG_LEVEL=DEBUG  # DEBUG, INFO, WARN, ERROR, NONE
```

Default: `DEBUG` in development, `INFO` in production

## 🚨 Error Handling

### Location
`src/shared/utils/error-handler.ts`

### Features
- Automatic error parsing (Axios, Error, String)
- Toast notifications for errors
- Network error detection
- Validation error handling
- Success/Warning/Info toast helpers

### Usage

```typescript
import { ErrorHandler } from '@/shared/utils/error-handler'

// Handle API errors
try {
  await apiCall()
} catch (error) {
  const appError = ErrorHandler.handleApiError(error)
  // Error is automatically logged and shown in toast
}

// Show success toast
ErrorHandler.showSuccess('Operation completed', 'Details here')

// Show warning toast
ErrorHandler.showWarning('Warning message', 'Details here')

// Show info toast
ErrorHandler.showInfo('Information', 'Details here')

// Handle validation errors
ErrorHandler.handleValidationError({
  email: ['Email is required'],
  password: ['Password must be at least 8 characters']
})
```

## 🔐 Authentication

### Location
`src/features/auth/`

### Structure
```
auth/
├── api/
│   └── auth.service.ts      # API service for auth operations
├── composables/
│   └── useAuth.ts           # Authentication composable
├── pages/
│   └── LoginPage.vue        # Login page component
├── types/
│   └── index.ts             # TypeScript types
└── index.ts                  # Feature exports
```

### Features
- Login/Register functionality
- Token management
- User state management
- Permission and role checking
- Automatic token refresh
- Logout functionality

### Usage

```typescript
import { useAuth } from '@/features/auth'

const {
  user,              // Computed: Current user
  token,              // Computed: Auth token
  isAuthenticated,    // Computed: Is user authenticated
  isLoading,         // Computed: Is loading
  login,             // Function: Login user
  register,          // Function: Register user
  logout,            // Function: Logout user
  refreshUser,       // Function: Refresh user data
  hasPermission,     // Function: Check permission
  hasRole,           // Function: Check role
} = useAuth()

// Login
await login({
  email: 'user@example.com',
  password: 'password',
  rememberMe: true
})

// Check permissions
if (hasPermission('users.create')) {
  // User can create users
}

// Check roles
if (hasRole('admin')) {
  // User is admin
}
```

### API Endpoints

The auth service expects the following endpoints:
- `POST /auth/login` - Login
- `POST /auth/register` - Register
- `POST /auth/logout` - Logout
- `GET /auth/me` - Get current user
- `POST /auth/refresh` - Refresh token
- `POST /auth/forgot-password` - Forgot password
- `POST /auth/reset-password` - Reset password
- `POST /auth/change-password` - Change password

## 🍞 Toast Notifications

### Location
`src/shared/composables/useToast.ts`

### Features
- Success, Error, Warning, Info toasts
- Loading toasts
- Promise-based toasts
- Integrated with Sonner

### Usage

```typescript
import { useToast } from '@/shared/composables/useToast'

const toast = useToast()

// Simple toasts
toast.success('Success!', 'Operation completed')
toast.error('Error!', 'Something went wrong')
toast.warning('Warning!', 'Please check this')
toast.info('Info', 'Here is some information')

// Loading toast
const toastId = toast.loading('Processing...')
// Later: toast.success('Done!') or toast.error('Failed!')

// Promise toast
toast.promise(
  fetchData(),
  {
    loading: 'Loading data...',
    success: 'Data loaded successfully!',
    error: 'Failed to load data'
  }
)
```

### Integration

The Toaster component is already integrated in `App.vue`:

```vue
<template>
  <AppShell>
    <RouterView />
  </AppShell>
  <Toaster />
</template>
```

## 🔧 API Client Integration

The API client has been updated to include:
- Request/Response logging
- Automatic error handling
- Token management
- Network error detection

### Features
- Automatic token injection
- Request/Response logging
- Error logging
- 401 auto-redirect to login
- 403/500 error handling

## 🛡️ Error Boundary

### Location
`src/common/components/ErrorBoundary.vue`

### Features
- Catches Vue component errors
- Displays user-friendly error message
- Error details in collapsible section
- Retry functionality

### Usage

```vue
<template>
  <ErrorBoundary>
    <YourComponent />
  </ErrorBoundary>
</template>
```

## 📚 Router Guards

### Auth Guard
Protects routes that require authentication:
```typescript
{
  path: '/protected',
  meta: { requiresAuth: true }
}
```

### Guest Guard
Protects routes for unauthenticated users only:
```typescript
{
  path: '/login',
  meta: { requiresGuest: true }
}
```

## 🌐 Internationalization

Error messages and auth labels are available in:
- English (`en.json`)
- French (`fr.json`)

### Error Messages
- `errors.unauthorized`
- `errors.forbidden`
- `errors.notFound`
- `errors.badRequest`
- `errors.serverError`
- `errors.networkError`
- `errors.unknownError`
- `errors.validationError`

### Auth Messages
- `auth.login`
- `auth.logout`
- `auth.email`
- `auth.password`
- `auth.rememberMe`
- `auth.forgotPassword`
- `auth.signIn`
- `auth.loginSuccess`
- `auth.loginFailed`

## 🎨 UI Components

### Label Component
Created `src/components/ui/label/` for form labels.

## 📦 Dependencies

All required dependencies are already installed:
- `vue-sonner` - Toast notifications
- `axios` - HTTP client

## 🚀 Getting Started

1. **Use Logger**
   ```typescript
   import { logger } from '@/shared/utils/logger'
   logger.info('Application started')
   ```

2. **Handle Errors**
   ```typescript
   import { ErrorHandler } from '@/shared/utils/error-handler'
   try {
     // Your code
   } catch (error) {
     ErrorHandler.handleApiError(error)
   }
   ```

3. **Use Authentication**
   ```typescript
   import { useAuth } from '@/features/auth'
   const { login, isAuthenticated } = useAuth()
   ```

4. **Show Toasts**
   ```typescript
   import { useToast } from '@/shared/composables/useToast'
   const toast = useToast()
   toast.success('Success!')
   ```

## 🔍 Best Practices

1. **Logging**: Use appropriate log levels
   - DEBUG: Development debugging
   - INFO: General information
   - WARN: Warnings that don't break functionality
   - ERROR: Errors that need attention

2. **Error Handling**: Always handle errors in try-catch blocks
   ```typescript
   try {
     const data = await apiCall()
   } catch (error) {
     ErrorHandler.handleApiError(error)
   }
   ```

3. **Authentication**: Check auth state before protected operations
   ```typescript
   if (!isAuthenticated.value) {
     router.push('/login')
     return
   }
   ```

4. **Toasts**: Use toasts for user feedback
   - Success: Operations completed
   - Error: Operations failed
   - Warning: Important notices
   - Info: General information

## 📝 Notes

- Logger stores last 100 logs in memory
- Error handler automatically shows toasts (can be disabled)
- Auth state persists in localStorage
- Toast notifications are automatically styled with Sonner
- All errors are logged to console and can be retrieved via logger

