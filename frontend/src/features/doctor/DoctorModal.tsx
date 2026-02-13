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
    MenuItem,
    Select,
    FormControl,
    InputLabel,
    FormHelperText,
} from '@mui/material';
import type { SelectChangeEvent } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import type { DoctorFormData, FormErrors, Specialty } from "../../shared/types.ts";
import { validateRequired } from "../../utils/helpers.ts";
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { doctorAPI } from "./doctorApi.ts";
import { DOCTOR_LABELS, EMPTY_STRING } from "../../constants";
import { useSnackbar } from "../../hooks";
import { format } from 'date-fns';

const SPECIALTY_OPTIONS: { label: string; value: Specialty }[] = [
    { label: 'Orthopedics', value: 'ORTHO' },
    { label: 'Cardiology', value: 'CARDIO' },
];

interface DoctorModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSuccess: () => void;
}

const DoctorModal = ({
    isOpen,
    onClose,
    onSuccess
}: DoctorModalProps): React.ReactNode => {
    const { showSuccess, showError } = useSnackbar();

    const [formData, setFormData] = useState<DoctorFormData>({
        firstName: EMPTY_STRING,
        lastName: EMPTY_STRING,
        npiNumber: EMPTY_STRING,
        specialty: EMPTY_STRING,
        practiceStartDate: EMPTY_STRING,
    });

    const [practiceStartDate, setPracticeStartDate] = useState<Date | null>(null);

    const [errors, setErrors] = useState<FormErrors>({});
    const [isSubmitting, setIsSubmitting] = useState(false);

    const resetForm = () => {
        setFormData({
            firstName: EMPTY_STRING,
            lastName: EMPTY_STRING,
            npiNumber: EMPTY_STRING,
            specialty: EMPTY_STRING,
            practiceStartDate: EMPTY_STRING,
        });
        setPracticeStartDate(null);
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

    const handleSpecialtyChange = (e: SelectChangeEvent<Specialty | ''>) => {
        const value = e.target.value as Specialty | '';
        setFormData(prev => ({ ...prev, specialty: value }));

        if (errors.specialty) {
            setErrors(prev => ({ ...prev, specialty: null }));
        }
    };

    const handlePracticeStartDateChange = (value: Date | null) => {
        setPracticeStartDate(value);

        setFormData(prev => ({
            ...prev,
            practiceStartDate: value ? format(value, 'MM/dd/yyyy') : EMPTY_STRING,
        }));

        if (errors.practiceStartDate) {
            setErrors(prev => ({ ...prev, practiceStartDate: null }));
        }
    };

    const validate = (): FormErrors => {
        const newErrors: FormErrors = {};

        const firstNameError = validateRequired(formData.firstName, 'First name');
        if (firstNameError) newErrors.firstName = firstNameError;

        const lastNameError = validateRequired(formData.lastName, 'Last name');
        if (lastNameError) newErrors.lastName = lastNameError;

        const practiceStartDateError = validateRequired(formData.practiceStartDate, 'Practice start date');
        if (practiceStartDateError) {
            newErrors.practiceStartDate = practiceStartDateError;
        } else if (practiceStartDate) {
            const picked = practiceStartDate;
            const today = new Date();
            if (picked > today) {
                newErrors.practiceStartDate = "Practice start date can't be in the future";
            }
        }

        const npiNumberError = validateRequired(formData.npiNumber, 'NPI Number');
        if (npiNumberError) {
            newErrors.npiNumber = npiNumberError;
        } else {
            const npi = formData.npiNumber.trim();
            if (!/^\d{10}$/.test(npi)) {
                newErrors.npiNumber = 'NPI Number must be exactly 10 digits';
            }
        }

        const specialtyError = validateRequired(formData.specialty, 'Specialty');
        if (specialtyError) newErrors.specialty = specialtyError;

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
            await doctorAPI.register(formData);

            showSuccess('Doctor registered successfully!');

            resetForm();

            if (onSuccess) {
                onSuccess();
            }
            onClose();
        } catch (error) {
            console.error('Failed to register doctor:', error);
            showError('Failed to register doctor. Please try again.');
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
                    {DOCTOR_LABELS.REGISTER_DOCTOR}
                </Typography>

                <IconButton onClick={handleClose} size="small">
                    <CloseIcon />
                </IconButton>
            </DialogTitle>

            <Box component="form" onSubmit={handleSubmit} noValidate sx={{ display: 'flex', flexDirection: 'column', flex: 1, overflow: 'hidden' }}>
                <DialogContent sx={{ flex: 1, overflow: 'auto' }}>
                    <Stack spacing={2}>
                        <Stack direction={{ md: 'row', sm: 'column' }} spacing={2}>
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
                        <Stack direction={{  md: 'row', sm: 'column' }} spacing={2}>
                            <TextField
                                label="NPI Number"
                                name="npiNumber"
                                value={formData.npiNumber}
                                onChange={handleChange}
                                fullWidth
                                required
                                error={!!errors.npiNumber}
                                helperText={errors.npiNumber}
                            />

                            <FormControl
                                fullWidth
                                required
                                error={!!errors.specialty}
                            >
                                <InputLabel id="specialty-label">Specialty</InputLabel>
                                <Select
                                    labelId="specialty-label"
                                    id="specialty"
                                    name="specialty"
                                    value={formData.specialty}
                                    onChange={handleSpecialtyChange}
                                    label="Specialty"
                                >
                                    {SPECIALTY_OPTIONS.map((specialty) => (
                                        <MenuItem key={specialty.value} value={specialty.value}>
                                            {specialty.label}
                                        </MenuItem>
                                    ))}
                                </Select>
                                {errors.specialty && (
                                    <FormHelperText>{errors.specialty}</FormHelperText>
                                )}
                            </FormControl>
                        </Stack>

                        <LocalizationProvider dateAdapter={AdapterDateFns}>
                            <DatePicker
                                label="Practice Start Date"
                                value={practiceStartDate}
                                onChange={handlePracticeStartDateChange}
                                disableFuture
                                slotProps={{
                                    textField: {
                                        fullWidth: true,
                                        required: true,
                                        error: !!errors.practiceStartDate,
                                        helperText: errors.practiceStartDate,
                                        inputProps: {
                                            'aria-label': 'Practice Start Date',
                                        },
                                    },
                                }}
                            />
                        </LocalizationProvider>
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
                        {isSubmitting ? DOCTOR_LABELS.REGISTERING_DOCTOR : DOCTOR_LABELS.REGISTER_DOCTOR}
                    </Button>
                </DialogActions>
            </Box>
        </Dialog>
    );
};

export default DoctorModal;