import { describe, it, expect } from 'vitest';
import { render, screen } from '@testing-library/react';
import BillBreakdown from './BillBreakdown';
import { BILLING_LABELS } from '../../constants';
import type { Bill } from '../../shared/types';

describe('BillBreakdown', () => {
    const mockBill: Bill = {
        patientId: '1',
        doctorNpiNumber: '1234567890',
        consultationFee: 1000.0,
        taxAmount: 120.0,
        totalAmount: 1120.0,
        coPayAmount: 112.0,
        insurancePayableAmount: 1008.0,
        taxRatePercentage: 12.0,
        coPayRatePercentage: 10.0
    };

    it('renders bill breakdown with all fields', () => {
        render(<BillBreakdown bill={mockBill} />);

        expect(screen.getByText(BILLING_LABELS.BILL_BREAKDOWN)).toBeInTheDocument();
        expect(screen.getByText(BILLING_LABELS.CONSULTATION_FEE)).toBeInTheDocument();
        expect(screen.getByText(`${BILLING_LABELS.GST} (${mockBill.taxRatePercentage}%)`)).toBeInTheDocument();
        expect(screen.getByText(BILLING_LABELS.SUBTOTAL)).toBeInTheDocument();
        expect(screen.getByText(BILLING_LABELS.INSURANCE_COVERAGE)).toBeInTheDocument();
        expect(screen.getByText(`${BILLING_LABELS.CO_PAY} (${mockBill.coPayRatePercentage}%)`)).toBeInTheDocument();
        expect(screen.getByText('$1,000.00')).toBeInTheDocument();
        expect(screen.getByText('$120.00')).toBeInTheDocument();
        expect(screen.getByText('$1,120.00')).toBeInTheDocument();
        expect(screen.getByText('-$1,008.00')).toBeInTheDocument();
        expect(screen.getByText('$112.00')).toBeInTheDocument();
    });
});

