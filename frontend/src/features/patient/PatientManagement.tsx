import React from 'react';
import PatientModal from './PatientModal';
import {useApp} from "../../hooks";

const PatientManagement = (): React.ReactNode => {
    const { patientModal, refreshData } = useApp();

    return (
        <PatientModal
            isOpen={patientModal.isOpen}
            onClose={patientModal.close}
            onSuccess={refreshData}
        />
    );
};

export default PatientManagement;

