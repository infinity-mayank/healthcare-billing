import React, { useState } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    Box,
    Stack,
    Typography,
    IconButton,
    CircularProgress,
    Paper,
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import type {FormErrors, PatientFormData} from "../../shared/types.ts";
import { validateRequired } from "../../utils/helpers.ts";
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { patientAPI } from "./patientApi.ts";
import { EMPTY_STRING, PATIENT_LABELS } from "../../constants";

interface PatientModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSuccess?: () => void;
}

const formatMMDDYYYY = (date: Date): string => {
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');
    const yyyy = String(date.getFullYear());
    return `${mm}/${dd}/${yyyy}`;
};

const stripTime = (date: Date): Date => {
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    return d;
};

const PatientModal = ({ isOpen, onClose, onSuccess }: PatientModalProps): React.ReactNode => {
    const [formData, setFormData] = useState<PatientFormData>({
        firstName: EMPTY_STRING,
        lastName: EMPTY_STRING,
        dateOfBirth: EMPTY_STRING,
        insuranceBIN: EMPTY_STRING,
        insurancePCN: EMPTY_STRING,
        insuranceMemberID: EMPTY_STRING,
    });

    const [dobDate, setDobDate] = useState<Date | null>(null);

    const [errors, setErrors] = useState<FormErrors>({});
    const [isSubmitting, setIsSubmitting] = useState(false);

    const resetForm = () => {
        setFormData({
            firstName: EMPTY_STRING,
            lastName: EMPTY_STRING,
            dateOfBirth: EMPTY_STRING,
            insuranceBIN: EMPTY_STRING,
            insurancePCN: EMPTY_STRING,
            insuranceMemberID: EMPTY_STRING,
        });
        setDobDate(null);
        setErrors({});
    };

    const handleClose = () => {
        resetForm();
        onClose();
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;

        setFormData(prev => ({ ...prev, [name]: value }));

        if (errors[name]) {
            setErrors(prev => ({ ...prev, [name]: null }));
        }
    };

    const handleDobChange = (value: Date | null) => {
        setDobDate(value);

        setFormData(prev => ({
            ...prev,
            dateOfBirth: value ? formatMMDDYYYY(value) : EMPTY_STRING,
        }));

        if (errors.dateOfBirth) {
            setErrors(prev => ({ ...prev, dateOfBirth: null }));
        }
    };

    const validate = (): FormErrors => {
        const newErrors: FormErrors = {};

        const firstNameError = validateRequired(formData.firstName, 'First name');
        if (firstNameError) newErrors.firstName = firstNameError;

        const lastNameError = validateRequired(formData.lastName, 'Last name');
        if (lastNameError) newErrors.lastName = lastNameError;

        const dobError = validateRequired(formData.dateOfBirth, 'Date of birth');
        if (dobError) {
            newErrors.dateOfBirth = dobError;
        } else if (dobDate) {
            const picked = stripTime(dobDate);
            const today = stripTime(new Date());
            if (picked > today) {
                newErrors.dateOfBirth = "Date of birth can't be in the future";
            }
        }

        const binError = validateRequired(formData.insuranceBIN, 'Insurance BIN');
        if (binError) newErrors.insuranceBIN = binError;

        const pcnError = validateRequired(formData.insurancePCN, 'Insurance PCN');
        if (pcnError) newErrors.insurancePCN = pcnError;

        const memberError = validateRequired(formData.insuranceMemberID, 'Insurance Member ID');
        if (memberError) newErrors.insuranceMemberID = memberError;

        return newErrors;
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            return;
        }

        setIsSubmitting(true);
        try {
            await patientAPI.register(formData);

            resetForm();

            if (onSuccess) {
                onSuccess();
            }
            onClose();
        } catch (error) {
            console.error('Failed to register patient:', error);
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <Dialog
            open={isOpen}
            onClose={handleClose}
            maxWidth="md"
            fullWidth
            slotProps={{
                paper: {
                    sx: {
                        height: '80vh',
                        display: 'flex',
                        flexDirection: 'column',
                    },
                },
            }}
        >
            <DialogTitle
                sx={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    borderBottom: 1,
                    borderColor: 'divider',
                    flexShrink: 0,
                }}
            >
                <Typography fontWeight="bold">
                    {PATIENT_LABELS.REGISTER_PATIENT}
                </Typography>

                <IconButton onClick={handleClose} size="small">
                    <CloseIcon />
                </IconButton>
            </DialogTitle>

            <Box component="form" onSubmit={handleSubmit} noValidate sx={{ display: 'flex', flexDirection: 'column', flex: 1, overflow: 'hidden' }}>
                <DialogContent sx={{ flex: 1, overflow: 'auto' }}>
                    <Stack spacing={2}>
                        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
                            <TextField
                                label="First Name"
                                name="firstName"
                                value={formData.firstName}
                                onChange={handleChange}
                                fullWidth
                                required
                                error={!!errors.firstName}
                                helperText={errors.firstName}
                            />

                            <TextField
                                label="Last Name"
                                name="lastName"
                                value={formData.lastName}
                                onChange={handleChange}
                                fullWidth
                                required
                                error={!!errors.lastName}
                                helperText={errors.lastName}
                            />
                        </Stack>

                        <LocalizationProvider dateAdapter={AdapterDateFns}>
                            <DatePicker
                                label="Date of Birth"
                                value={dobDate}
                                onChange={handleDobChange}
                                disableFuture
                                slotProps={{
                                    textField: {
                                        fullWidth: true,
                                        required: true,
                                        error: !!errors.dateOfBirth,
                                        helperText: errors.dateOfBirth,
                                        inputProps: {
                                            'aria-label': 'Date of Birth',
                                        },
                                    },
                                }}
                            />
                        </LocalizationProvider>

                        <Paper sx={{ p: 2, borderRadius: 2, border: 1, borderColor: 'teal.main' }}>
                            <Typography fontWeight="bold" mb={1}>
                                Insurance Information
                            </Typography>

                            <Stack spacing={2}>
                                <TextField
                                    label="BIN Number"
                                    name="insuranceBIN"
                                    value={formData.insuranceBIN}
                                    onChange={handleChange}
                                    fullWidth
                                    required
                                    error={!!errors.insuranceBIN}
                                    helperText={errors.insuranceBIN}
                                />

                                <TextField
                                    label="PCN Number"
                                    name="insurancePCN"
                                    value={formData.insurancePCN}
                                    onChange={handleChange}
                                    fullWidth
                                    required
                                    error={!!errors.insurancePCN}
                                    helperText={errors.insurancePCN}
                                />

                                <TextField
                                    label="Member ID"
                                    name="insuranceMemberID"
                                    value={formData.insuranceMemberID}
                                    onChange={handleChange}
                                    fullWidth
                                    required
                                    error={!!errors.insuranceMemberID}
                                    helperText={errors.insuranceMemberID}
                                />
                            </Stack>
                        </Paper>
                    </Stack>
                </DialogContent>

                <DialogActions sx={{ px: 3, pb: 3, flexShrink: 0, borderTop: 1, borderColor: 'divider' }}>
                    <Button onClick={handleClose} variant="outlined" fullWidth>
                        Cancel
                    </Button>

                    <Button
                        type="submit"
                        variant="contained"
                        fullWidth
                        disabled={isSubmitting}
                        startIcon={
                            isSubmitting ? (
                                <CircularProgress size={20} color="inherit" />
                            ) : (
                                <PersonAddIcon />
                            )
                        }
                    >
                        {isSubmitting ? PATIENT_LABELS.REGISTERING_PATIENT : PATIENT_LABELS.REGISTER_PATIENT}
                    </Button>
                </DialogActions>
            </Box>
        </Dialog>
    );
};

export default PatientModal;