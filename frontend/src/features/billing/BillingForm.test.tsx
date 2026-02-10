import { act } from "react";
import { describe, it, expect, vi, beforeEach } from 'vitest';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import BillingForm from './BillingForm';
import { BILLING_LABELS, DOCTOR_LABELS, PATIENT_LABELS } from '../../constants';
import { patientAPI } from '../patient/patientApi';
import type { Doctor, Patient } from '../../shared/types';
import { doctorAPI } from "../doctor/doctorApi.ts";

vi.mock('../patient/patientApi');
vi.mock('../doctor/doctorApi');
vi.mock('../../hooks', () => ({
    useSnackbar: () => ({ showError: vi.fn() }),
    useApp: () => ({ refreshCounter: 0 })
}));

describe('BillingForm', () => {
    let mockOnOpenPatientModal: () => void;
    let mockOnOpenDoctorModal: () => void;
    const mockPatients: Patient[] = [
        {
            id: '1',
            firstName: 'John',
            lastName: 'Doe',
            dateOfBirth: '1990-01-01',
            insuranceBIN: 'BIN123',
            insurancePCN: 'PCN123',
            insuranceMemberID: 'MEM123'
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
        }
    ];

    beforeEach(() => {
        mockOnOpenPatientModal = vi.fn();
        mockOnOpenDoctorModal = vi.fn();
        vi.mocked(patientAPI.getAll).mockResolvedValue(mockPatients);
        vi.mocked(doctorAPI.getAll).mockResolvedValue(mockDoctors);
    });

    it('renders all UI elements correctly', async () => {
        render(
            <BillingForm
                onOpenPatientModal={mockOnOpenPatientModal}
                onOpenDoctorModal={mockOnOpenDoctorModal}
            />
        );

        expect(screen.getByText(BILLING_LABELS.GENERATE_BILL)).toBeInTheDocument();
        expect(screen.getByText(PATIENT_LABELS.NEW_PATIENT)).toBeInTheDocument();
        expect(screen.getByText(DOCTOR_LABELS.NEW_DOCTOR)).toBeInTheDocument();

        await waitFor(() => {
            expect(patientAPI.getAll).toHaveBeenCalled();
        });

        expect(screen.getByText(PATIENT_LABELS.SELECT_PATIENT)).toBeInTheDocument();
    });

    it('calls onOpenPatientModal when new patient button is clicked', async () => {
        render(
            <BillingForm
                onOpenPatientModal={mockOnOpenPatientModal}
                onOpenDoctorModal={mockOnOpenDoctorModal}
            />
        );

        const button = screen.getByRole('button', { name: /new patient/i });
        await act(async () => {
                fireEvent.click(button);
        })

        expect(mockOnOpenPatientModal).toHaveBeenCalledTimes(1);
    });

    it('loads and displays patients', async () => {
        render(
            <BillingForm
                onOpenPatientModal={mockOnOpenPatientModal}
                onOpenDoctorModal={mockOnOpenDoctorModal}
            />
        );

        await waitFor(() => {
            expect(patientAPI.getAll).toHaveBeenCalled();
        });

        const select = screen.getByRole('combobox', {
            name: /select patient/i
        });
        fireEvent.mouseDown(select);

        await waitFor(() => {
            expect(screen.getByText('John Doe')).toBeInTheDocument();
        });
    });

    it('calls onOpenDoctorModal when new doctor button is clicked', async () => {
        render(
            <BillingForm
                onOpenPatientModal={mockOnOpenPatientModal}
                onOpenDoctorModal={mockOnOpenDoctorModal}
            />
        );

        const button = screen.getByRole('button', { name: /new doctor/i });
        await act(async () => {
            fireEvent.click(button);
        })

        expect(mockOnOpenDoctorModal).toHaveBeenCalledTimes(1);
    });

    it('loads and displays doctors', async () => {
        render(
            <BillingForm
                onOpenPatientModal={mockOnOpenPatientModal}
                onOpenDoctorModal={mockOnOpenDoctorModal}
            />
        );

        await waitFor(() => {
            expect(doctorAPI.getAll).toHaveBeenCalled();
        });

        const select = screen.getByRole('combobox', {
            name: /select doctor/i
        });
        fireEvent.mouseDown(select);

        await waitFor(() => {
            expect(screen.getByText('Jolly Rancho - ORTHO')).toBeInTheDocument();
        });
    });
});




