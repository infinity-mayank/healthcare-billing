import React from 'react';
import {
    Box,
    FormControl,
    InputLabel,
    Select,
    MenuItem
} from '@mui/material';
import Grid from '@mui/material/Grid';
import { DOCTOR_LABELS, PATIENT_LABELS } from '../../constants';
import type { Doctor, Patient } from '../../shared/types';

interface BillingSelectorsProps {
    patients: Patient[];
    doctors: Doctor[];
    selectedPatientId: string;
    selectedDoctorId: string;
    onPatientChange: (patientId: string) => void;
    onDoctorChange: (doctorId: string) => void;
}

const Selectors = ({
    patients,
    doctors,
    selectedPatientId,
    selectedDoctorId,
    onPatientChange,
    onDoctorChange
}: BillingSelectorsProps): React.ReactNode => {
    return (
        <Box component="form">
            <Grid sx={{ display: 'flex', gap: 2 }}>
                <FormControl fullWidth required>
                    <InputLabel id="patient-label">
                        {PATIENT_LABELS.SELECT_PATIENT}
                    </InputLabel>
                    <Select
                        labelId="patient-label"
                        id="patient"
                        value={selectedPatientId}
                        onChange={(e) => onPatientChange(e.target.value)}
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
                    <InputLabel id="doctor-label">
                        {DOCTOR_LABELS.SELECT_DOCTOR}
                    </InputLabel>
                    <Select
                        labelId="doctor-label"
                        id="doctor"
                        value={selectedDoctorId}
                        onChange={(e) => onDoctorChange(e.target.value)}
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
    );
};

export default Selectors;

