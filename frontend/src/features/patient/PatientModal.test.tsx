import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import type { Mock } from 'vitest';
import PatientModal from './PatientModal';
import { patientAPI } from './patientApi';
import { useSnackbar } from '../../hooks';

vi.mock('./patientApi', () => ({
    patientAPI: {
        register: vi.fn(),
    },
}));

vi.mock('../../hooks', () => ({
    useSnackbar: vi.fn(),
}));

const mockRegister = patientAPI.register as Mock;
const mockUseSnackbar = useSnackbar as Mock;

describe('PatientModal', () => {
    const mockOnClose = vi.fn();
    const mockOnSuccess = vi.fn();
    const mockShowSuccess = vi.fn();
    const mockShowError = vi.fn();

    beforeEach(() => {
        vi.clearAllMocks();
        mockUseSnackbar.mockReturnValue({
            showSuccess: mockShowSuccess,
            showError: mockShowError,
        });
    });

    it('does not render modal when closed', () => {
        render(<PatientModal isOpen={false} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        expect(screen.queryByText('Register Patient')).not.toBeInTheDocument();
    });

    it('renders modal when open', () => {
        render(<PatientModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        expect(screen.getAllByText(/Register Patient/i)[0]).toBeInTheDocument();
        expect(screen.getByLabelText(/First Name/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Last Name/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/date of birth/i, {
            selector: 'input',
        })).toBeInTheDocument();
        expect(screen.getByLabelText(/BIN Number/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/PCN Number/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Member ID/i)).toBeInTheDocument();
    });

    it('call onClose when cancel button is clicked', () => {
        render(<PatientModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess}/>);

        const cancelButton = screen.getByRole('button', { name: /Cancel/i });
        fireEvent.click(cancelButton);

        expect(mockOnClose).toHaveBeenCalled();
        expect(mockOnClose).toHaveBeenCalledTimes(1);
    });

    it('call onClose when close icon is clicked', () => {
        render(<PatientModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        const closeButton = screen.getByRole('button', { name: '' });
        fireEvent.click(closeButton);

        expect(mockOnClose).toHaveBeenCalled();
        expect(mockOnClose).toHaveBeenCalledTimes(1);
    });

    it('update form fields on user input', () => {
        render(<PatientModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        const firstNameInput = screen.getByLabelText(/First Name/i) as HTMLInputElement;
        const lastNameInput = screen.getByLabelText(/Last Name/i) as HTMLInputElement;

        fireEvent.change(firstNameInput, { target: { value: 'John' } });
        fireEvent.change(lastNameInput, { target: { value: 'Doe' } });

        expect(firstNameInput.value).toBe('John');
        expect(lastNameInput.value).toBe('Doe');
    });

    it('show validation errors when submitting empty form', async () => {
        render(<PatientModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        const submitButton = screen.getByRole('button', { name: /Register Patient/i });
        fireEvent.click(submitButton);

        await waitFor(() => {
            expect(screen.getByText(/First name is required/i)).toBeInTheDocument();
            expect(screen.getByText(/Last name is required/i)).toBeInTheDocument();
            expect(screen.getByText(/Date of birth is required/i)).toBeInTheDocument();
            expect(screen.getByText(/Insurance BIN is required/i)).toBeInTheDocument();
            expect(screen.getByText(/Insurance PCN is required/i)).toBeInTheDocument();
            expect(screen.getByText(/Insurance Member ID is required/i)).toBeInTheDocument();
        });

        expect(mockRegister).not.toHaveBeenCalled();
    });

    it('submits form with valid data', async () => {
        mockRegister.mockResolvedValue({});

        render(<PatientModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        fireEvent.change(screen.getByLabelText(/First Name/i), { target: { value: 'John' } });
        fireEvent.change(screen.getByLabelText(/Last Name/i), { target: { value: 'Doe' } });
        fireEvent.change(screen.getByLabelText(/BIN Number/i), { target: { value: '123456' } });
        fireEvent.change(screen.getByLabelText(/PCN Number/i), { target: { value: '328743' } });
        fireEvent.change(screen.getByLabelText(/Member ID/i), { target: { value: '943732' } });

        const dobInput = screen.getByLabelText(/date of birth/i, {
            selector: 'input',
        });
        fireEvent.change(dobInput, { target: { value: '01/01/1990' } });

        const submitButton = screen.getByRole('button', { name: /Register Patient/i });
        fireEvent.click(submitButton);

        await waitFor(() => {
            expect(mockRegister).toHaveBeenCalledWith({
                firstName: 'John',
                lastName: 'Doe',
                dateOfBirth: expect.any(String),
                insuranceBIN: '123456',
                insurancePCN: '328743',
                insuranceMemberID: '943732',
            });
            expect(mockOnSuccess).toHaveBeenCalled();
            expect(mockOnClose).toHaveBeenCalled();
        });
    });
});
