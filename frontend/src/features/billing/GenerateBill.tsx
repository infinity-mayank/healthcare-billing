import React, { useCallback, useEffect, useState } from 'react';
import { Box, Button, Paper } from '@mui/material';
import type { Bill, Doctor, Patient } from "../../shared/types";
import { patientAPI } from "../patient/patientApi";
import { useSnackbar, useApp } from "../../hooks";
import { doctorAPI } from "../doctor/doctorApi";
import { billingAPI } from "./billingApi";
import Header from './Header.tsx';
import Selectors from './Selectors.tsx';
import BillBreakdown from './BillBreakdown';
import { BILLING_LABELS, EMPTY_STRING } from "../../constants";

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
    const [selectedPatientId, setSelectedPatientId] = useState(EMPTY_STRING);
    const [selectedDoctorId, setSelectedDoctorId] = useState(EMPTY_STRING);
    const { showError, showSuccess } = useSnackbar();

    const loadPatients = useCallback(async () => {
        try {
            const patients: Patient[] = await patientAPI.getAll() ?? [];
            setPatients(patients);
        } catch (error) {
            console.error('Failed to load patients:', error);
            showError('Failed to load patients. Please try again.');
        }
    }, []);

    const loadDoctors = useCallback(async () => {
        try {
            const doctors: Doctor[] = await doctorAPI.getAll() ?? [];
            setDoctors(doctors);
        } catch (error) {
            console.error('Failed to load doctors:', error);
            showError('Failed to load doctors. Please try again.');
        }
    }, []);

    useEffect(() => {
        const loadData = async () => {
            await loadPatients();
            await loadDoctors();
        }

        loadData();
    }, [refreshCounter]);

    const generateBill = useCallback(async () => {
        try {
            const bill: Bill = await billingAPI.generateBill(selectedDoctorId, selectedPatientId);
            setBill(bill);
        } catch (error) {
            console.error('Failed to generate bill:', error);
            showError('Failed to generate bill. Please try again.');
        }
    }, [selectedPatientId, selectedDoctorId]);

    useEffect(() => {
        const loadBillData = async () => {
            if (selectedPatientId && selectedDoctorId) {
                await generateBill();
            }
        }

        loadBillData();
    }, [selectedDoctorId, selectedPatientId]);

    const resetSelections = useCallback(() => {
        setSelectedPatientId(EMPTY_STRING);
        setSelectedDoctorId(EMPTY_STRING);
        setBill(null);
    }, []);

    const handleSaveBill = async () => {
        if (!bill) return;
        try {
            await billingAPI.saveBill(bill);
            resetSelections();
        } catch (error) {
            console.error('Failed to save bill:', error);
            showError('Failed to save bill. Please try again.');
        } finally {
            showSuccess('Bill saved successfully!');
        }
    }

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

            {
                bill &&
                    <>
                        <BillBreakdown bill={bill} />
                        <Box display="flex" justifyContent="flex-end" mt={2}>
                            <Button
                                variant="contained"
                                onClick={handleSaveBill}
                                sx={{ textTransform: 'none' }}
                            >
                                {BILLING_LABELS.SAVE_BILL}
                            </Button>
                        </Box>
                    </>
            }
        </Paper>
    );
};

export default GenerateBill;
