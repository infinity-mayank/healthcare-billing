export const APP_LABELS = {
    TITLE: 'Healthcare Billing System',
}

export const PATIENT_LABELS = {
    NEW_PATIENT: 'New Patient',
    REGISTER_PATIENT: 'Register Patient',
    REGISTERING_PATIENT: 'Registering Patient...',
    SELECT_PATIENT: 'Select Patient',
}

export const DOCTOR_LABELS = {
    NEW_DOCTOR: 'New Doctor',
    REGISTER_DOCTOR: 'Register Doctor',
    REGISTERING_DOCTOR: 'Registering Doctor...',
    SELECT_DOCTOR: 'Select Doctor',
}

export const HTTP_METHODS = {
    GET: 'GET',
    POST: 'POST',
    PUT: 'PUT',
    DELETE: 'DELETE'
}

export const BILLING_LABELS = {
    GENERATE_BILL: 'Generate Bill',
    BILL_BREAKDOWN: 'Bill Breakdown',
    CONSULTATION_FEE: 'Consultation Fee',
    GST: 'GST',
    SUBTOTAL: 'Subtotal',
    INSURANCE_COVERAGE: 'Insurance Coverage',
    CO_PAY: 'Co-Pay',
}

export type HttpMethod =
    (typeof HTTP_METHODS)[keyof typeof HTTP_METHODS];

export const EMPTY_STRING = '';