import React from 'react';
import { Paper, Box, Button } from '@mui/material';
import PersonAddIcon from '@mui/icons-material/PersonAdd';
import { PATIENT_LABELS } from "../../constants";

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
                <Box sx={{ display: 'flex', gap: 1 }}>
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
