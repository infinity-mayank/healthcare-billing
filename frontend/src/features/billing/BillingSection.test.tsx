import { describe, it, expect, vi, type Mock } from 'vitest';
import { render } from '@testing-library/react';
import BillingSection from './BillingSection';
import { useApp } from '../../hooks';

vi.mock('../../hooks/useApp');
vi.mock('./GenerateBill', () => ({
    default: ({ onOpenPatientModal, onOpenDoctorModal }: { onOpenPatientModal: () => void, onOpenDoctorModal: () => void }) => (
        <div data-testid="billing-form">
            <button onClick={onOpenPatientModal}>Mock Open Patient Modal</button>
            <button onClick={onOpenDoctorModal}>Mock Open Doctor Modal</button>
        </div>
    ),
}));

const mockUseApp = useApp as Mock;

describe('BillingSection', () => {
    it('render GenerateBill', () => {
        const mockOpenPatient = vi.fn();
        const mockOpenDoctor = vi.fn();
        mockUseApp.mockReturnValue({
            patientModal: {
                isOpen: false,
                open: mockOpenPatient,
                close: vi.fn(),
            },
            doctorModal: {
                isOpen: false,
                open: mockOpenDoctor,
                close: vi.fn(),
            }
        });

        const { getByTestId, getByText } = render(<BillingSection />);

        expect(getByTestId('billing-form')).toBeInTheDocument();

        const patientButton = getByText('Mock Open Patient Modal');
        patientButton.click();
        expect(mockOpenPatient).toHaveBeenCalledTimes(1);

        const doctorButton = getByText('Mock Open Doctor Modal');
        doctorButton.click();
        expect(mockOpenDoctor).toHaveBeenCalledTimes(1);
    });
});

