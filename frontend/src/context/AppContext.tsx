import { createContext } from 'react';

export interface AppContextValue {
    patientModal: {
        isOpen: boolean;
        open: () => void;
        close: () => void;
    };
}

export const AppContext = createContext<AppContextValue | undefined>(undefined);
