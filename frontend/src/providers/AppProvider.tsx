import { useModal } from "../hooks";
import type { ReactNode } from "react";
import { AppContext, type AppContextValue } from "../context";

interface AppProviderProps {
    children: ReactNode;
}

export const AppProvider = ({ children }: AppProviderProps): ReactNode => {
    const patientModal = useModal();

    const value: AppContextValue = {
        patientModal: {
            isOpen: patientModal.isOpen,
            open: patientModal.open,
            close: patientModal.close,
        }
    };

    return <AppContext.Provider value={value}>{children}</AppContext.Provider>;
};