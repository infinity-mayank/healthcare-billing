import { render, screen, fireEvent } from '@testing-library/react';
import {describe, it, expect, vi} from 'vitest';
import type {Mock} from 'vitest';
import PatientManagement from './PatientManagement';
import { useApp } from '../../hooks';

vi.mock('../../hooks/useApp');
vi.mock('./PatientModal', () => ({
    default: ({ isOpen, onClose, onSuccess }: { isOpen: boolean; onClose: () => void; onSuccess: () => void }) => (
        isOpen ? (
            <div data-testid="patient-modal">
                <button onClick={onClose} data-testid="close-button">Close</button>
                <button onClick={onSuccess} data-testid="success-button">Success</button>
            </div>
        ) : null
    ),
}));

const mockUseApp = useApp as Mock;

describe('PatientManagement', () => {
    it('render PatientModal with closed state', () => {
        const mockClose = vi.fn();
        const mockRefreshData = vi.fn();
        mockUseApp.mockReturnValue({
            patientModal: {
                isOpen: false,
                close: mockClose,
            },
            refreshData: mockRefreshData,
        });

        render(<PatientManagement />);

        expect(screen.queryByTestId('patient-modal')).not.toBeInTheDocument();
    });

    it('render PatientModal with open state', () => {
        const mockClose = vi.fn();
        const mockRefreshData = vi.fn();
        mockUseApp.mockReturnValue({
            patientModal: {
                isOpen: true,
                close: mockClose,
            },
            refreshData: mockRefreshData,
        });

        render(<PatientManagement />);

        expect(screen.getByTestId('patient-modal')).toBeInTheDocument();
    });

    it('pass close function to PatientModal and close method clicked', () => {
        const mockClose = vi.fn();
        const mockRefreshData = vi.fn();
        mockUseApp.mockReturnValue({
            patientModal: {
                isOpen: true,
                close: mockClose,
            },
            refreshData: mockRefreshData,
        });

        render(<PatientManagement />);

        const closeButton = screen.getByTestId('close-button');
        fireEvent.click(closeButton);

        expect(mockClose).toHaveBeenCalledTimes(1);
    });

    it('pass success function to PatientModal and success method clicked', () => {
        const mockClose = vi.fn();
        const mockRefreshData = vi.fn();
        mockUseApp.mockReturnValue({
            patientModal: {
                isOpen: true,
                close: mockClose,
            },
            refreshData: mockRefreshData,
        });

        render(<PatientManagement />);

        const successButton = screen.getByTestId('success-button');
        fireEvent.click(successButton);

        expect(mockRefreshData).toHaveBeenCalledTimes(1);
    });
});
