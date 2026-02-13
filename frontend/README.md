# Healthcare Billing - Frontend

A modern React-based frontend application for healthcare billing management system. This application provides an intuitive interface for managing patients, doctors, and billing operations.

## Architecture

This project follows a **Feature-Based Architecture** pattern, organizing code by domain features rather than technical layers. This approach promotes:

- **Better Scalability**: Each feature is self-contained and can grow independently
- **Improved Maintainability**: Related code is co-located, making it easier to understand and modify
- **Team Collaboration**: Teams can work on different features without conflicts
- **Code Reusability**: Shared components and utilities are separated from feature-specific code

### Project Structure

```
src/
├── features/              # Feature modules (domain-driven)
│   ├── billing/          # Billing management feature
│   │   ├── BillingSection.tsx
│   │   ├── GenerateBill.tsx
│   │   ├── BillBreakdown.tsx
│   │   ├── Selectors.tsx
│   │   ├── Header.tsx
│   │   ├── billingApi.ts
│   │   └── *.test.tsx
│   ├── doctor/           # Doctor management feature
│   │   ├── DoctorManagement.tsx
│   │   ├── DoctorModal.tsx
│   │   ├── doctorApi.ts
│   │   └── *.test.tsx
│   └── patient/          # Patient management feature
│       ├── PatientManagement.tsx
│       ├── PatientModal.tsx
│       ├── patientApi.ts
│       └── *.test.tsx
├── shared/               # Shared components and utilities
│   ├── components/       # Reusable UI components
│   └── types.ts          # Common type definitions
├── services/             # Core services (API client)
│   └── apiClient.ts
├── context/              # React Context providers
│   ├── AppContext.tsx
│   └── SnackbarContext.tsx
├── providers/            # Provider components
│   ├── AppProvider.tsx
│   └── SnackbarProvider.tsx
├── hooks/                # Custom React hooks
│   ├── useApp.ts
│   ├── useModal.ts
│   └── useSnackbar.ts
├── layouts/              # Layout components
│   └── MainLayout.tsx
├── constants/            # Application constants
├── utils/                # Utility functions
├── theme.ts              # MUI theme configuration
└── App.tsx               # Root application component
```

## Tech Stack

### Core
- **React 19.2.0** - UI library
- **TypeScript 5.9.3** - Type-safe JavaScript
- **Vite 7.2.4** - Fast build tool and dev server

### UI Framework
- **Material-UI (MUI) 7.3.7** - Component library
  - `@mui/material` - Core components
  - `@mui/icons-material` - Icon library
  - `@mui/x-date-pickers` - Date picker components

### Development Tools
- **ESLint 9.39.1** - Code linting
- **Vitest 4.0.18** - Unit testing framework
- **Testing Library** - React component testing
  - `@testing-library/react`
  - `@testing-library/jest-dom`

## Getting Started

### Prerequisites
- Node.js (v18 or higher recommended)
- npm package manager

### Installation

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

### Development

Start the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:5173` (default Vite port).

### Building for Production

Build the application:
```bash
npm run build
```

The optimized production build will be created in the `dist/` directory.

### Preview Production Build

Preview the production build locally:
```bash
npm run preview
```

## Testing

Run unit tests:
```bash
npm test
```

## 🔍 Code Quality

### Linting

Run ESLint to check code quality:
```bash
npm run lint
```

Auto-fix linting issues:
```bash
npm run lint -- --fix
```

## Features

### Patient Management
- Add patient records
- Patient information includes name, date of birth, and insurance information(BIN Number, PCN Number and Member ID)

### Doctor Management
- Add doctor records
- Doctor information includes name, specialty, NPI number, and practice start date

### Billing Operations
- Generate bills for patient consultations
- View detailed bill breakdowns

## Feature-Based Architecture Benefits

### 1. **Domain-Driven Organization**
Each feature folder (`billing`, `doctor`, `patient`) contains all related code:
- Components
- API integration
- Tests

### 2. **Clear Boundaries**
Features have well-defined boundaries, making it easy to:
- Understand the scope of each module
- Identify dependencies between features
- Refactor or remove features independently

### 3. **Shared Resources**
Common code is organized in dedicated folders:
- `shared/` - Reusable components and types
- `services/` - Core services used across features
- `hooks/` - Custom hooks for cross-cutting concerns
- `context/` - Application-wide state management

### 4. **Testability**
Each feature includes co-located tests, making it easy to:
- Find and run feature-specific tests
- Ensure feature quality independently

## Configuration Files

- `vite.config.ts` - Vite build configuration
- `vitest.config.ts` - Vitest testing configuration
- `tsconfig.json` - TypeScript base configuration
- `tsconfig.app.json` - TypeScript configuration for application code
- `tsconfig.node.json` - TypeScript configuration for Node.js code
- `eslint.config.js` - ESLint configuration

## API Integration

The application communicates with a backend API using the centralized `apiClient` service located in `src/services/apiClient.ts`. Each feature has its own API module:
- `features/billing/billingApi.ts`
- `features/doctor/doctorApi.ts`
- `features/patient/patientApi.ts`
