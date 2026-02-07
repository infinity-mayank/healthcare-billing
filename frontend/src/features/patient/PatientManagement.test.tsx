import { render, screen, fireEvent } from '@testing-library/react';
import {describe, it, expect, vi, type Mock} from 'vitest';
import PatientManagement from './PatientManagement';
import { useApp } from '../../hooks/useApp';

vi.mock('../../hooks/useApp');
vi.mock('./PatientModal', () => ({
    default: ({ isOpen, onClose }: { isOpen: boolean; onClose: () => void }) => (
        isOpen ? (
            <div data-testid="patient-modal">
                <button onClick={onClose} data-testid="close-button">Close</button>
            </div>
        ) : null
    ),
}));

const mockUseApp = useApp as Mock;

describe('PatientManagement', () => {
    it('render PatientModal with closed state', () => {
        const mockClose = vi.fn();
        mockUseApp.mockReturnValue({
            patientModal: {
                isOpen: false,
                close: mockClose,
            },
        });

        render(<PatientManagement />);

        expect(screen.queryByTestId('patient-modal')).not.toBeInTheDocument();
    });

    it('render PatientModal with open state', () => {
        const mockClose = vi.fn();
        mockUseApp.mockReturnValue({
            patientModal: {
                isOpen: true,
                close: mockClose,
            },
        });

        render(<PatientManagement />);

        expect(screen.getByTestId('patient-modal')).toBeInTheDocument();
    });

    it('pass close function to PatientModal and close method clicked', () => {
        const mockClose = vi.fn();
        mockUseApp.mockReturnValue({
            patientModal: {
                isOpen: true,
                close: mockClose,
            },
        });

        render(<PatientManagement />);

        const closeButton = screen.getByTestId('close-button');
        fireEvent.click(closeButton);

        expect(mockClose).toHaveBeenCalledTimes(1);
    });
});
