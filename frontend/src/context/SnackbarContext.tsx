import { createContext } from 'react';

export interface SnackbarContextValue {
    showSuccess: (message: string) => void;
    showError: (message: string) => void;
}

export const SnackbarContext = createContext<SnackbarContextValue | undefined>(undefined);
