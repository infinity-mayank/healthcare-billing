import { type ReactNode } from 'react';
import { Box } from '@mui/material';

interface MainLayoutProps {
    children: ReactNode;
}

export const MainLayout = ({ children }: MainLayoutProps): ReactNode => {
    return (
        <Box
            sx={{
                height: '100vh',
                width: '100%',
                bgcolor: 'teal.800',
                overflow: 'auto'
            }}
        >
            <Box
                sx={{
                    minHeight: '100%',
                    width: '100%',
                    p: { xs: 2, md: 4 }
                }}
            >
                {children}
            </Box>
        </Box>
    );
};
