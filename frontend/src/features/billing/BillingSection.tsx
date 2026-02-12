import React from 'react';
import GenerateBill from './GenerateBill.tsx';
import { useApp } from "../../hooks";

const BillingSection = (): React.ReactNode => {
    const { patientModal, doctorModal } = useApp();

    return (
        <GenerateBill
            onOpenPatientModal={patientModal.open}
            onOpenDoctorModal={doctorModal.open}
        />
    );
};

export default BillingSection;

