import { createContext } from 'react';

export interface AppContextValue {
    patientModal: {
        isOpen: boolean;
        open: () => void;
        close: () => void;
    };
    refreshCounter: number;
    refreshData: () => void;
}

export const AppContext = createContext<AppContextValue | undefined>(undefined);
