import React from 'react';
import { useApp } from "../../hooks";
import DoctorModal from "./DoctorModal.tsx";

const DoctorManagement = (): React.ReactNode => {
    const { doctorModal, refreshData } = useApp();

    return (
        <DoctorModal
            isOpen={doctorModal.isOpen}
            onClose={doctorModal.close}
            onSuccess={refreshData}
        />
    );
};

export default DoctorManagement;

