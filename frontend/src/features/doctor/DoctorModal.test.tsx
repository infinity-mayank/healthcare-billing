import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { describe, it, expect, vi, beforeEach } from 'vitest';
import type { Mock } from 'vitest';
import DoctorModal from './DoctorModal';
import { doctorAPI } from './doctorApi';
import { useSnackbar } from '../../hooks';

vi.mock('./doctorApi', () => ({
    doctorAPI: {
        register: vi.fn(),
    },
}));

vi.mock('../../hooks', () => ({
    useSnackbar: vi.fn(),
}));

const mockRegister = doctorAPI.register as Mock;
const mockUseSnackbar = useSnackbar as Mock;

describe('DoctorModal', () => {
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
        render(<DoctorModal isOpen={false} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        expect(screen.queryByText('Register Doctor')).not.toBeInTheDocument();
    });

    it('renders modal when open', () => {
        render(<DoctorModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        expect(screen.getAllByText(/Register Doctor/i)[0]).toBeInTheDocument();
        expect(screen.getByLabelText(/First Name/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Last Name/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/practice start date/i, {
            selector: 'input',
        })).toBeInTheDocument();
        expect(screen.getByLabelText(/NPI Number/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/Specialty/i)).toBeInTheDocument();
    });

    it('call onClose when cancel button is clicked', () => {
        render(<DoctorModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess}/>);

        const cancelButton = screen.getByRole('button', { name: /Cancel/i });
        fireEvent.click(cancelButton);

        expect(mockOnClose).toHaveBeenCalled();
        expect(mockOnClose).toHaveBeenCalledTimes(1);
    });

    it('call onClose when close icon is clicked', () => {
        render(<DoctorModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        const closeButton = screen.getByRole('button', { name: '' });
        fireEvent.click(closeButton);

        expect(mockOnClose).toHaveBeenCalled();
        expect(mockOnClose).toHaveBeenCalledTimes(1);
    });

    it('update form fields on user input', () => {
        render(<DoctorModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        const firstNameInput = screen.getByLabelText(/First Name/i) as HTMLInputElement;
        const lastNameInput = screen.getByLabelText(/Last Name/i) as HTMLInputElement;

        fireEvent.change(firstNameInput, { target: { value: 'John' } });
        fireEvent.change(lastNameInput, { target: { value: 'Doe' } });

        expect(firstNameInput.value).toBe('John');
        expect(lastNameInput.value).toBe('Doe');
    });

    it('show validation errors when submitting empty form', async () => {
        render(<DoctorModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        const submitButton = screen.getByRole('button', { name: /Register Doctor/i });
        fireEvent.click(submitButton);

        await waitFor(() => {
            expect(screen.getByText(/First name is required/i)).toBeInTheDocument();
            expect(screen.getByText(/Last name is required/i)).toBeInTheDocument();
            expect(screen.getByText(/Practice start date is required/i)).toBeInTheDocument();
            expect(screen.getByText(/NPI Number is required/i)).toBeInTheDocument();
            expect(screen.getByText(/Specialty is required/i)).toBeInTheDocument();
        });

        expect(mockRegister).not.toHaveBeenCalled();
    });

    it('submits form with valid data', async () => {
        mockRegister.mockResolvedValue({});

        render(<DoctorModal isOpen={true} onClose={mockOnClose} onSuccess={mockOnSuccess} />);

        fireEvent.change(screen.getByLabelText(/First Name/i), { target: { value: 'John' } });
        fireEvent.change(screen.getByLabelText(/Last Name/i), { target: { value: 'Doe' } });
        fireEvent.change(screen.getByLabelText(/NPI Number/i), { target: { value: '1234567890' } });

        const specialtySelect = screen.getByLabelText(/Specialty/i);
        fireEvent.mouseDown(specialtySelect);
        const orthoOption = await screen.findByText('Orthopedics');
        fireEvent.click(orthoOption);

        const practiceStartDateInput = screen.getByLabelText(/practice start date/i, {
            selector: 'input',
        });
        fireEvent.change(practiceStartDateInput, { target: { value: '01/01/2020' } });

        const submitButton = screen.getByRole('button', { name: /Register Doctor/i });
        fireEvent.click(submitButton);

        await waitFor(() => {
            expect(mockRegister).toHaveBeenCalledWith({
                firstName: 'John',
                lastName: 'Doe',
                npiNumber: '1234567890',
                specialty: 'ORTHO',
                practiceStartDate: expect.any(String),
            });
            expect(mockOnSuccess).toHaveBeenCalled();
            expect(mockOnClose).toHaveBeenCalled();
        });
    });
});

