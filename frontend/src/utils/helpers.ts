export const validateRequired = (value: string, fieldName: string): string | null => {
    return !value.trim() ? `${fieldName} is required` : null;
};

export const formatCurrency = (amount: number): string => {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
    }).format(amount);
};