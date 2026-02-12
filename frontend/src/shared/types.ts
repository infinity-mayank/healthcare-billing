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

export interface Patient {
    id: string;
    firstName: string;
    lastName: string;
    dateOfBirth: string;
    insuranceBIN: string;
    insurancePCN: string;
    insuranceMemberID: string;
}

export type Specialty = 'ORTHO' | 'CARDIO';

export interface DoctorFormData {
    firstName: string;
    lastName: string;
    npiNumber: string;
    specialty: Specialty | '';
    practiceStartDate: string;
}

export interface Doctor {
    npiNumber: string;
    firstName: string;
    lastName: string;
    specialty: Specialty;
    practiceStartDate: string;
    yearsOfExperience: number;
}

export interface Bill {
    patientId: string;
    doctorNpiNumber: string;
    consultationFee: number;
    taxAmount: number;
    totalAmount: number;
    coPayAmount: number;
    insurancePayableAmount: number;
    taxRatePercentage: number;
    coPayRatePercentage: number;
    discountAmount: number;
    discountPercentage: number;
}
