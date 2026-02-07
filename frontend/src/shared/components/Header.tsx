import React from 'react';
import { Box, Typography } from '@mui/material';
import { BiHealth } from "react-icons/bi";
import { APP_LABELS } from "../../constants";

export const Header = (): React.ReactNode => {
    return (
        <Box
            component="header"
            sx={{
                textAlign: 'center',
                mb: 6
            }}
        >
            <Box
                sx={{
                    display: 'inline-flex',
                    alignItems: 'center',
                    gap: 2,
                    mb: 1
                }}
            >
                <BiHealth size={40} />
                <Typography
                    variant="h3"
                    component="h1"
                    sx={{
                        fontWeight: 'bold',
                        color: 'white'
                    }}
                >
                    {APP_LABELS.TITLE}
                </Typography>
            </Box>
        </Box>
    );
};
