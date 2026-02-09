import React from 'react';
import BillingForm from './BillingForm';
import { useApp } from "../../hooks/useApp.ts";

const BillingSection: React.FC = () => {
    const { patientModal } = useApp();

    return (
        <BillingForm
            onOpenPatientModal={patientModal.open}
        />
    );
};

export default BillingSection;

