import React, { useCallback, useEffect, useState } from 'react';
import { Paper } from '@mui/material';
import type { Bill, Doctor, Patient } from "../../shared/types";
import { patientAPI } from "../patient/patientApi";
import { useSnackbar, useApp } from "../../hooks";
import { doctorAPI } from "../doctor/doctorApi";
import { billingAPI } from "./billingApi";
import Header from './Header.tsx';
import Selectors from './Selectors.tsx';
import BillBreakdown from './BillBreakdown';

interface BillingFormProps {
    onOpenPatientModal: () => void;
    onOpenDoctorModal: () => void;
}

const GenerateBill = ({
    onOpenPatientModal,
    onOpenDoctorModal
}: BillingFormProps): React.ReactNode => {
    const { refreshCounter } = useApp();
    const [patients, setPatients] = useState<Patient[]>([]);
    const [doctors, setDoctors] = useState<Doctor[]>([]);
    const [bill, setBill] = useState<Bill | null>(null);
    const [selectedPatientId, setSelectedPatientId] = useState('');
    const [selectedDoctorId, setSelectedDoctorId] = useState('');
    const { showError } = useSnackbar();

    const loadPatients = useCallback(async () => {
        try {
            const patients: Patient[] = await patientAPI.getAll() ?? [];
            setPatients(patients);
        } catch (error) {
            console.error('Failed to load patients:', error);
            showError('Failed to load patients. Please try again.');
        }
    }, [showError]);

    const loadDoctors = useCallback(async () => {
        try {
            const doctors: Doctor[] = await doctorAPI.getAll() ?? [];
            setDoctors(doctors);
        } catch (error) {
            console.error('Failed to load doctors:', error);
            showError('Failed to load doctors. Please try again.');
        }
    }, [showError]);

    useEffect(() => {
        const loadData = async () => {
            await loadPatients();
            await loadDoctors();
        }

        loadData();
    }, [loadPatients, refreshCounter, loadDoctors]);

    const generateBill = useCallback(async () => {
        try {
            const bill: Bill = await billingAPI.generateBill(selectedDoctorId, selectedPatientId);
            setBill(bill);
        } catch (error) {
            console.error('Failed to generate bill:', error);
            showError('Failed to generate bill. Please try again.');
        }
    }, [selectedPatientId, selectedDoctorId, showError]);

    useEffect(() => {
        const loadBillData = async () => {
            if (selectedPatientId && selectedDoctorId) {
                await generateBill();
            }
        }

        loadBillData();
    }, [selectedDoctorId, selectedPatientId, generateBill]);

    return (
        <Paper
            elevation={3}
            sx={{
                borderRadius: 3,
                p: 4
            }}
        >
            <Header
                onOpenPatientModal={onOpenPatientModal}
                onOpenDoctorModal={onOpenDoctorModal}
            />

            <Selectors
                patients={patients}
                doctors={doctors}
                selectedPatientId={selectedPatientId}
                selectedDoctorId={selectedDoctorId}
                onPatientChange={setSelectedPatientId}
                onDoctorChange={setSelectedDoctorId}
            />

            {bill && <BillBreakdown bill={bill} />}
        </Paper>
    );
};

export default GenerateBill;
