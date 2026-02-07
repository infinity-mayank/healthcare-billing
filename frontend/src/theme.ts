import { createTheme } from '@mui/material/styles';

// Tailwind teal color palette
// https://tailwindcss.com/docs/customizing-colors
export const theme = createTheme({
    palette: {
        primary: {
            main: '#0d9488', // teal-600
            dark: '#115e59', // teal-700
            light: '#99f6e4', // teal-200 (lighter for backgrounds)
        },
        teal: {
            50: '#f0fdfa',
            100: '#ccfbf1',
            200: '#99f6e4',
            300: '#5eead4',
            400: '#2dd4bf',
            500: '#14b8a6',
            600: '#0d9488',
            700: '#115e59',
            800: '#115e59', // teal-800 (same as 700 in newer Tailwind)
            900: '#134e4a',
            main: '#0d9488', // teal-600
            dark: '#115e59', // teal-700
            light: '#14b8a6', // teal-500
        } as any,
    },
    typography: {
        fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
    },
    shape: {
        borderRadius: 8,
    },
});

// Type augmentation for custom palette
declare module '@mui/material/styles' {
    interface Palette {
        teal: {
            50: string;
            100: string;
            200: string;
            300: string;
            400: string;
            500: string;
            600: string;
            700: string;
            800: string;
            900: string;
            main: string;
            dark: string;
            light: string;
        };
    }
    interface PaletteOptions {
        teal?: {
            50?: string;
            100?: string;
            200?: string;
            300?: string;
            400?: string;
            500?: string;
            600?: string;
            700?: string;
            800?: string;
            900?: string;
            main?: string;
            dark?: string;
            light?: string;
        };
    }
}


