import { render, screen, fireEvent } from '@testing-library/react';
import {describe, it, expect, vi} from 'vitest';
import type {Mock} from 'vitest';
import DoctorManagement from './DoctorManagement';
import { useApp } from '../../hooks';

vi.mock('../../hooks/useApp');
vi.mock('./DoctorModal', () => ({
    default: ({ isOpen, onClose, onSuccess }: { isOpen: boolean; onClose: () => void; onSuccess: () => void }) => (
        isOpen ? (
            <div data-testid="doctor-modal">
                <button onClick={onClose} data-testid="close-button">Close</button>
                <button onClick={onSuccess} data-testid="success-button">Success</button>
            </div>
        ) : null
    ),
}));

const mockUseApp = useApp as Mock;

describe('DoctorManagement', () => {
    it('render DoctorModal with closed state', () => {
        const mockClose = vi.fn();
        const mockRefreshData = vi.fn();
        mockUseApp.mockReturnValue({
            doctorModal: {
                isOpen: false,
                close: mockClose,
            },
            refreshData: mockRefreshData,
        });

        render(<DoctorManagement />);

        expect(screen.queryByTestId('doctor-modal')).not.toBeInTheDocument();
    });

    it('render DoctorModal with open state', () => {
        const mockClose = vi.fn();
        const mockRefreshData = vi.fn();
        mockUseApp.mockReturnValue({
            doctorModal: {
                isOpen: true,
                close: mockClose,
            },
            refreshData: mockRefreshData,
        });

        render(<DoctorManagement />);

        expect(screen.getByTestId('doctor-modal')).toBeInTheDocument();
    });

    it('pass close function to DoctorModal and close method clicked', () => {
        const mockClose = vi.fn();
        const mockRefreshData = vi.fn();
        mockUseApp.mockReturnValue({
            doctorModal: {
                isOpen: true,
                close: mockClose,
            },
            refreshData: mockRefreshData,
        });

        render(<DoctorManagement />);

        const closeButton = screen.getByTestId('close-button');
        fireEvent.click(closeButton);

        expect(mockClose).toHaveBeenCalledTimes(1);
    });

    it('pass success function to DoctorModal and success method clicked', () => {
        const mockClose = vi.fn();
        const mockRefreshData = vi.fn();
        mockUseApp.mockReturnValue({
            doctorModal: {
                isOpen: true,
                close: mockClose,
            },
            refreshData: mockRefreshData,
        });

        render(<DoctorManagement />);

        const successButton = screen.getByTestId('success-button');
        fireEvent.click(successButton);

        expect(mockRefreshData).toHaveBeenCalledTimes(1);
    });
});

