import React from 'react';
import {
    Paper,
    Typography,
    Stack,
    Divider
} from '@mui/material';
import { BILLING_LABELS } from '../../constants';
import type { Bill } from '../../shared/types';
import { formatCurrency } from '../../utils/helpers';

interface BillBreakdownProps {
    bill: Bill;
}

const BillBreakdown = ({ bill }: BillBreakdownProps): React.ReactNode => {
    return (
        <Paper
            sx={{
                p: 3,
                border: '1px solid',
                borderColor: 'teal.800',
                borderRadius: 3,
                mt: 4
            }}
        >
            <Typography fontWeight={700} fontSize={20} mb={2}>
                {BILLING_LABELS.BILL_BREAKDOWN}
            </Typography>

            <Stack spacing={1.5}>
                <Stack direction="row" justifyContent="space-between">
                    <Typography color="text.secondary">
                        {BILLING_LABELS.CONSULTATION_FEE}
                    </Typography>
                    <Typography fontWeight={600}>
                        {formatCurrency(bill.consultationFee)}
                    </Typography>
                </Stack>
                <Stack direction="row" justifyContent="space-between">
                    <Typography color="text.secondary">
                        {BILLING_LABELS.DISCOUNT} ({bill.discountPercentage}%)
                    </Typography>
                    <Typography fontWeight={600} color="success.main">
                        -{formatCurrency(bill.discountAmount)}
                    </Typography>
                </Stack>
                <Stack direction="row" justifyContent="space-between">
                    <Typography color="text.secondary">
                        {BILLING_LABELS.GST} ({bill.taxRatePercentage}%)
                    </Typography>
                    <Typography fontWeight={600}>
                        {formatCurrency(bill.taxAmount)}
                    </Typography>
                </Stack>

                <Divider />

                <Stack direction="row" justifyContent="space-between">
                    <Typography color="text.secondary">
                        {BILLING_LABELS.SUBTOTAL}
                    </Typography>
                    <Typography fontWeight={600}>
                        {formatCurrency(bill.totalAmount)}
                    </Typography>
                </Stack>

                <Stack direction="row" justifyContent="space-between">
                    <Typography color="text.secondary">
                        {BILLING_LABELS.INSURANCE_COVERAGE}
                    </Typography>
                    <Typography fontWeight={600} color="info.main">
                        -{formatCurrency(bill.insurancePayableAmount)}
                    </Typography>
                </Stack>

                <Stack direction="row" justifyContent="space-between">
                    <Typography color="text.secondary">
                        {BILLING_LABELS.CO_PAY} ({bill.coPayRatePercentage}%)
                    </Typography>
                    <Typography fontWeight={600}>
                        {formatCurrency(bill.coPayAmount)}
                    </Typography>
                </Stack>
            </Stack>
        </Paper>
    );
};

export default BillBreakdown;

