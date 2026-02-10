import { useModal } from "../hooks";
import type { ReactNode } from "react";
import { AppContext, type AppContextValue } from "../context";
import { useState } from "react";

interface AppProviderProps {
    children: ReactNode;
}

export const AppProvider = ({ children }: AppProviderProps): ReactNode => {
    const patientModal = useModal();
    const doctorModal = useModal();
    const [patientsTrigger, setPatientsTrigger] = useState(0);

    const triggerPatientsReload = () => {
        setPatientsTrigger(prev => prev + 1);
    };

    const value: AppContextValue = {
        patientModal: {
            isOpen: patientModal.isOpen,
            open: patientModal.open,
            close: patientModal.close,
        },
        doctorModal: {
            isOpen: doctorModal.isOpen,
            open: doctorModal.open,
            close: doctorModal.close
        },
        refreshCounter: patientsTrigger,
        refreshData: triggerPatientsReload,
    };

    return <AppContext.Provider value={value}>{children}</AppContext.Provider>;
};