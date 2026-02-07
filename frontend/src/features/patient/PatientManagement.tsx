import React from 'react';
import PatientModal from './PatientModal';
import {useApp} from "../../hooks/useApp.ts";

const PatientManagement = (): React.ReactNode => {
    const { patientModal } = useApp();

    return (
        <PatientModal
            isOpen={patientModal.isOpen}
            onClose={patientModal.close}
        />
    );
};

export default PatientManagement;

