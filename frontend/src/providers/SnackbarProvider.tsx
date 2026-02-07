import React, { useState } from 'react';
import { Snackbar, Alert } from '@mui/material';
import { SnackbarContext, type SnackbarContextValue } from '../context';

interface SnackbarProviderProps {
    children: React.ReactNode;
}

export const SnackbarProvider = ({ children }: SnackbarProviderProps): React.ReactNode => {
    const [snackbar, setSnackbar] = useState<{
        open: boolean;
        message: string;
        severity: 'success' | 'error';
    }>({
        open: false,
        message: '',
        severity: 'success',
    });

    const showSuccess = (message: string) => {
        setSnackbar({ open: true, message, severity: 'success' });
    };

    const showError = (message: string) => {
        setSnackbar({ open: true, message, severity: 'error' });
    };

    const handleClose = () => {
        setSnackbar({ ...snackbar, open: false });
    };

    const value: SnackbarContextValue = {
        showSuccess,
        showError,
    };

    return (
        <SnackbarContext.Provider value={value}>
            {children}
            <Snackbar
                open={snackbar.open}
                autoHideDuration={6000}
                onClose={handleClose}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
            >
                <Alert
                    onClose={handleClose}
                    severity={snackbar.severity}
                    variant="filled"
                    sx={{ width: '100%' }}
                >
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </SnackbarContext.Provider>
    );
};
