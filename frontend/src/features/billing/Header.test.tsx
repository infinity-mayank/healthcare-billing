import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import Header from './Header';
import { BILLING_LABELS, DOCTOR_LABELS, PATIENT_LABELS } from '../../constants';

describe('Header', () => {
    const mockOnOpenPatientModal = vi.fn();
    const mockOnOpenDoctorModal = vi.fn();

    it('renders title and buttons', () => {
        render(
            <Header
                onOpenPatientModal={mockOnOpenPatientModal}
                onOpenDoctorModal={mockOnOpenDoctorModal}
            />
        );

        expect(screen.getByText(BILLING_LABELS.GENERATE_BILL)).toBeInTheDocument();
        expect(screen.getByText(PATIENT_LABELS.NEW_PATIENT)).toBeInTheDocument();
        expect(screen.getByText(DOCTOR_LABELS.NEW_DOCTOR)).toBeInTheDocument();
    });

    it('calls onOpenPatientModal when new patient button is clicked', () => {
        render(
            <Header
                onOpenPatientModal={mockOnOpenPatientModal}
                onOpenDoctorModal={mockOnOpenDoctorModal}
            />
        );

        const button = screen.getByRole('button', { name: /new patient/i });
        fireEvent.click(button);

        expect(mockOnOpenPatientModal).toHaveBeenCalledTimes(1);
    });

    it('calls onOpenDoctorModal when new doctor button is clicked', () => {
        render(
            <Header
                onOpenPatientModal={mockOnOpenPatientModal}
                onOpenDoctorModal={mockOnOpenDoctorModal}
            />
        );

        const button = screen.getByRole('button', { name: /new doctor/i });
        fireEvent.click(button);

        expect(mockOnOpenDoctorModal).toHaveBeenCalledTimes(1);
    });
});

