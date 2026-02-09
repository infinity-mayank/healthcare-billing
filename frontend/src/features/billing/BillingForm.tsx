import React from 'react';
import { Paper, Box, Button, Typography } from '@mui/material';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import { RiBillFill } from "react-icons/ri";
import { BILLING_LABELS, PATIENT_LABELS } from "../../constants";

interface BillingFormProps {
    onOpenPatientModal: () => void;
}

const BillingForm = ({
    onOpenPatientModal,
}: BillingFormProps): React.ReactNode => {
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
                        <RiBillFill size={50} />
                    </Box>

                    <Box>
                        <Typography variant="h5" fontWeight={700} color="teal.800">
                            {BILLING_LABELS.GENERATE_BILL}
                        </Typography>
                    </Box>
                </Box>

                <Box sx={{ display: 'flex' }}>
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
                </Box>
            </Box>
        </Paper>
    );
};

export default BillingForm;
