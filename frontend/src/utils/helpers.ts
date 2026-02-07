export const validateRequired = (value: string, fieldName: string): string | null => {
    return !value.trim() ? `${fieldName} is required` : null;
};
