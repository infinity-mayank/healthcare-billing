export interface PatientFormData {
    firstName: string;
    lastName: string;
    dateOfBirth: string;
    insuranceBIN: string;
    insurancePCN: string;
    insuranceMemberID: string;
}

export interface FormErrors {
    [key: string]: string | null | undefined;
}
