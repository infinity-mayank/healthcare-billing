import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent } from '@testing-library/react';
import BillingForm from './BillingForm';
import { BILLING_LABELS, PATIENT_LABELS } from '../../constants';

describe('BillingForm', () => {
    let mockOnOpenPatientModal: () => void;

    beforeEach(() => {
        mockOnOpenPatientModal = vi.fn();
        render(<BillingForm onOpenPatientModal={mockOnOpenPatientModal} />);
    });

    it('renders all UI elements correctly', () => {
        expect(screen.getByText(BILLING_LABELS.GENERATE_BILL)).toBeInTheDocument();
        expect(screen.getByText(PATIENT_LABELS.NEW_PATIENT)).toBeInTheDocument();

        const button = screen.getByRole('button', { name: /new patient/i });
        expect(button).toBeInTheDocument();;
    });

    it('calls onOpenPatientModal when new patient button is clicked', () => {
        const button = screen.getByRole('button', { name: /new patient/i });
        fireEvent.click(button);

        expect(mockOnOpenPatientModal).toHaveBeenCalledTimes(1);
    });
});




