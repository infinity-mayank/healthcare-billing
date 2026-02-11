import React, { useCallback, useEffect, useState } from 'react';
import {
    Paper,
    Box,
    Button,
    Typography,
    MenuItem,
    FormControl,
    InputLabel,
    Select,
    Stack,
    Divider
} from '@mui/material';
import Grid from '@mui/material/Grid';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import ReceiptIcon from '@mui/icons-material/Receipt';
import { BILLING_LABELS, DOCTOR_LABELS, PATIENT_LABELS } from "../../constants";
import type { Bill, Doctor, Patient } from "../../shared/types.ts";
import { patientAPI } from "../patient/patientApi.ts";
import { useSnackbar, useApp } from "../../hooks";
import { doctorAPI } from "../doctor/doctorApi.ts";
import { formatCurrency } from "../../utils/helpers.ts";
import { billingAPI } from "./billingApi.ts";

interface BillingFormProps {
    onOpenPatientModal: () => void;
    onOpenDoctorModal: () => void;
}

const BillingForm = ({
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
            <Box
                sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    mb: 4
                }}
            >
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    <Box
                        sx={{
                            display: 'flex',
                            alignItems: 'center',
                            justifyContent: 'center',
                            color: 'teal.800'
                        }}
                    >
                        <ReceiptIcon fontSize="large" />
                    </Box>

                    <Box>
                        <Typography variant="h5" fontWeight={700} color="teal.800">
                            {BILLING_LABELS.GENERATE_BILL}
                        </Typography>
                    </Box>
                </Box>

                <Box sx={{ display: 'flex', gap: 2 }}>
                    <Button
                        variant="contained"
                        onClick={onOpenPatientModal}
                        startIcon={<PersonAddIcon />}
                        sx={{
                            bgcolor: 'teal.main',
                            '&:hover': {
                                bgcolor: 'teal.dark',
                                boxShadow: 3
                            },
                            borderRadius: 2,
                            textTransform: 'none',
                            fontWeight: 600,
                            px: 2,
                            py: 1
                        }}
                    >
                        {PATIENT_LABELS.NEW_PATIENT}
                    </Button>
                    <Button
                        variant="contained"
                        onClick={onOpenDoctorModal}
                        startIcon={<PersonAddIcon />}
                        sx={{
                            bgcolor: 'teal.main',
                            '&:hover': {
                                bgcolor: 'teal.dark',
                                boxShadow: 3
                            },
                            borderRadius: 2,
                            textTransform: 'none',
                            fontWeight: 600,
                            px: 2,
                            py: 1
                        }}
                    >
                        {DOCTOR_LABELS.NEW_DOCTOR}
                    </Button>
                </Box>
            </Box>
            <Box component="form">
                <Grid sx={{ display: 'flex', gap: 2 }}>
                    <FormControl fullWidth required>
                        <InputLabel id="patient-label">{PATIENT_LABELS.SELECT_PATIENT}</InputLabel>
                        <Select
                            labelId="patient-label"
                            id="patient"
                            value={selectedPatientId}
                            onChange={(e) => setSelectedPatientId(e.target.value)}
                            label="Select Patient"
                        >
                            {patients.map((patient) => (
                                <MenuItem key={patient.id} value={patient.id}>
                                    {patient.firstName} {patient.lastName}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <FormControl fullWidth required>
                        <InputLabel id="doctor-label">{DOCTOR_LABELS.SELECT_DOCTOR}</InputLabel>
                        <Select
                            labelId="doctor-label"
                            id="doctor"
                            value={selectedDoctorId}
                            onChange={(e) => setSelectedDoctorId(e.target.value)}
                            label="Select Doctor"
                        >
                            {doctors.map((doctor) => (
                                <MenuItem key={doctor.npiNumber} value={doctor.npiNumber}>
                                    {doctor.firstName} {doctor.lastName} - {doctor.specialty}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </Grid>
            </Box>

            {bill && (
                <Paper
                    sx={{
                        p: 3,
                        border: '1px solid',
                        borderColor: 'teal.800',
                        borderRadius: 3,
                        mt: 4
                    }}
                >
                    <Typography fontWeight={700} fontSize={20} mb={2}>
                        {BILLING_LABELS.BILL_BREAKDOWN}
                    </Typography>

                    <Stack spacing={1.5}>
                        <Stack direction="row" justifyContent="space-between">
                            <Typography color="text.secondary">
                                {BILLING_LABELS.CONSULTATION_FEE}
                            </Typography>
                            <Typography fontWeight={600}>
                                {formatCurrency(bill.consultationFee)}
                            </Typography>
                        </Stack>

                        <Stack direction="row" justifyContent="space-between">
                            <Typography color="text.secondary">
                                {BILLING_LABELS.GST} ({bill.taxRatePercentage}%)
                            </Typography>
                            <Typography fontWeight={600}>
                                {formatCurrency(bill.taxAmount)}
                            </Typography>
                        </Stack>

                        <Divider />

                        <Stack direction="row" justifyContent="space-between">
                            <Typography color="text.secondary">
                                {BILLING_LABELS.SUBTOTAL}
                            </Typography>
                            <Typography fontWeight={600}>
                                {formatCurrency(bill.totalAmount)}
                            </Typography>
                        </Stack>

                        <Stack direction="row" justifyContent="space-between">
                            <Typography color="text.secondary">
                                {BILLING_LABELS.INSURANCE_COVERAGE}
                            </Typography>
                            <Typography fontWeight={600} color="info.main">
                                -{formatCurrency(bill.insurancePayableAmount)}
                            </Typography>
                        </Stack>

                        <Stack direction="row" justifyContent="space-between">
                            <Typography color="text.secondary">
                                {BILLING_LABELS.CO_PAY} ({bill.coPayRatePercentage}%)
                            </Typography>
                            <Typography fontWeight={600}>
                                {formatCurrency(bill.coPayAmount)}
                            </Typography>
                        </Stack>
                    </Stack>
                </Paper>
            )}

        </Paper>
    );
};

export default BillingForm;
