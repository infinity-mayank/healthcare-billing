import React from 'react';
import { Box, Button, Typography } from '@mui/material';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import ReceiptIcon from '@mui/icons-material/Receipt';
import { BILLING_LABELS, DOCTOR_LABELS, PATIENT_LABELS } from '../../constants';

interface BillingFormHeaderProps {
    onOpenPatientModal: () => void;
    onOpenDoctorModal: () => void;
}

const Header = ({
    onOpenPatientModal,
    onOpenDoctorModal
}: BillingFormHeaderProps): React.ReactNode => {
    return (
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
    );
};

export default Header;

