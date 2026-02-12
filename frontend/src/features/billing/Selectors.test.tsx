import { describe, it, expect, vi } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import Selectors from './Selectors';
import { DOCTOR_LABELS, PATIENT_LABELS } from '../../constants';
import type { Doctor, Patient } from '../../shared/types';

describe('Selectors', () => {
    const mockPatients: Patient[] = [
        {
            id: '1',
            firstName: 'John',
            lastName: 'Doe',
            dateOfBirth: '1990-01-01',
            insuranceBIN: 'BIN123',
            insurancePCN: 'PCN123',
            insuranceMemberID: 'MEM123'
        },
        {
            id: '2',
            firstName: 'Jane',
            lastName: 'Smith',
            dateOfBirth: '1985-05-15',
            insuranceBIN: 'BIN456',
            insurancePCN: 'PCN456',
            insuranceMemberID: 'MEM456'
        }
    ];

    const mockDoctors: Doctor[] = [
        {
            npiNumber: '1234567890',
            firstName: 'Jolly',
            lastName: 'Rancho',
            specialty: 'ORTHO',
            practiceStartDate: '2010-01-01',
            yearsOfExperience: 14
        },
        {
            npiNumber: '0987654321',
            firstName: 'Sarah',
            lastName: 'Connor',
            specialty: 'CARDIO',
            practiceStartDate: '2015-06-01',
            yearsOfExperience: 9
        }
    ];

    const mockOnPatientChange = vi.fn();
    const mockOnDoctorChange = vi.fn();

    it('renders patient and doctor selectors', () => {
        render(
            <Selectors
                patients={mockPatients}
                doctors={mockDoctors}
                selectedPatientId=""
                selectedDoctorId=""
                onPatientChange={mockOnPatientChange}
                onDoctorChange={mockOnDoctorChange}
            />
        );

        expect(screen.getByText(PATIENT_LABELS.SELECT_PATIENT)).toBeInTheDocument();
        expect(screen.getByText(DOCTOR_LABELS.SELECT_DOCTOR)).toBeInTheDocument();
    });

    it('displays patients when dropdown is opened', async () => {
        render(
            <Selectors
                patients={mockPatients}
                doctors={mockDoctors}
                selectedPatientId=""
                selectedDoctorId=""
                onPatientChange={mockOnPatientChange}
                onDoctorChange={mockOnDoctorChange}
            />
        );

        const patientSelect = screen.getByRole('combobox', { name: /select patient/i });
        fireEvent.mouseDown(patientSelect);

        await waitFor(() => {
            expect(screen.getByText('John Doe')).toBeInTheDocument();
            expect(screen.getByText('Jane Smith')).toBeInTheDocument();
        });
    });

    it('displays doctors when dropdown is opened', async () => {
        render(
            <Selectors
                patients={mockPatients}
                doctors={mockDoctors}
                selectedPatientId=""
                selectedDoctorId=""
                onPatientChange={mockOnPatientChange}
                onDoctorChange={mockOnDoctorChange}
            />
        );

        const doctorSelect = screen.getByRole('combobox', { name: /select doctor/i });
        fireEvent.mouseDown(doctorSelect);

        await waitFor(() => {
            expect(screen.getByText('Jolly Rancho - ORTHO')).toBeInTheDocument();
            expect(screen.getByText('Sarah Connor - CARDIO')).toBeInTheDocument();
        });
    });

    it('calls onPatientChange when patient is selected', async () => {
        render(
            <Selectors
                patients={mockPatients}
                doctors={mockDoctors}
                selectedPatientId=""
                selectedDoctorId=""
                onPatientChange={mockOnPatientChange}
                onDoctorChange={mockOnDoctorChange}
            />
        );

        const patientSelect = screen.getByRole('combobox', { name: /select patient/i });
        fireEvent.mouseDown(patientSelect);

        await waitFor(() => {
            expect(screen.getByText('John Doe')).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText('John Doe'));

        expect(mockOnPatientChange).toHaveBeenCalledTimes(1);
        expect(mockOnPatientChange).toHaveBeenCalledWith('1');
    });

    it('calls onDoctorChange when doctor is selected', async () => {
        render(
            <Selectors
                patients={mockPatients}
                doctors={mockDoctors}
                selectedPatientId=""
                selectedDoctorId=""
                onPatientChange={mockOnPatientChange}
                onDoctorChange={mockOnDoctorChange}
            />
        );

        const doctorSelect = screen.getByRole('combobox', { name: /select doctor/i });
        fireEvent.mouseDown(doctorSelect);

        await waitFor(() => {
            expect(screen.getByText('Jolly Rancho - ORTHO')).toBeInTheDocument();
        });

        fireEvent.click(screen.getByText('Jolly Rancho - ORTHO'));

        expect(mockOnDoctorChange).toHaveBeenCalledTimes(1);
        expect(mockOnDoctorChange).toHaveBeenCalledWith('1234567890');
    });
});

