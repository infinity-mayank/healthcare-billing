import { describe, it, expect, vi, type Mock } from 'vitest';
import { render } from '@testing-library/react';
import BillingSection from './BillingSection';
import { useApp } from '../../hooks/useApp';

vi.mock('../../hooks/useApp');
vi.mock('./BillingForm', () => ({
    default: ({ onOpenPatientModal }: { onOpenPatientModal: () => void }) => (
        <div data-testid="billing-form">
            <button onClick={onOpenPatientModal}>Mock Open Modal</button>
        </div>
    ),
}));

const mockUseApp = useApp as Mock;

describe('BillingSection', () => {
    it('render BillingForm', () => {
        const mockOpen = vi.fn();
        mockUseApp.mockReturnValue({
            patientModal: {
                isOpen: false,
                open: mockOpen,
                close: vi.fn(),
            },
        });

        const { getByTestId, getByText } = render(<BillingSection />);

        expect(getByTestId('billing-form')).toBeInTheDocument();

        const button = getByText('Mock Open Modal');
        button.click();
        expect(mockOpen).toHaveBeenCalledTimes(1);
    });
});

