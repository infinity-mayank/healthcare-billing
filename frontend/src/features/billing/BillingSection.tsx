import React from 'react';
import BillingForm from './BillingForm';
import { useApp } from "../../hooks";

const BillingSection = (): React.ReactNode => {
    const { patientModal, doctorModal } = useApp();

    return (
        <BillingForm
            onOpenPatientModal={patientModal.open}
            onOpenDoctorModal={doctorModal.open}
        />
    );
};

export default BillingSection;

