# Referentiels Feature

This feature provides a complete interface for searching and managing referential data.

## Structure

```
referentiels/
├── api/
│   └── referential.service.ts    # API service for referential operations
├── composables/
│   ├── useReferentialColumns.ts  # Column definitions for the table
│   └── useReferentials.ts        # Data fetching and state management
├── components/
│   └── ReferentialFilter.vue     # Custom filter component
├── pages/
│   └── ReferentialsPage.vue      # Main page component
├── types/
│   └── index.ts                  # TypeScript types
└── index.ts                      # Feature exports
```

## Features

- ✅ Search referentials with filters (code, dataType, label, active)
- ✅ List view with pagination and sorting
- ✅ Bulk actions (activate, delete)
- ✅ Row selection
- ✅ Column visibility control
- ✅ View and delete actions per row
- ✅ Loading states
- ✅ i18n support (English/French)

## API Endpoint

- **POST** `/referentiels/search` - Search referentials

## Usage

The page is accessible at `/referentiels` route.

## Configuration

Make sure `VITE_API_BASE_URL` is set to your backend URL (default: `/api`).
For local development with backend on port 8081, set:
```
VITE_API_BASE_URL=http://localhost:8081
```
