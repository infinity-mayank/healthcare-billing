export const APP_LABELS = {
    TITLE: 'Healthcare Billing System',
}

export const PATIENT_LABELS = {
    NEW_PATIENT: 'New Patient',
    REGISTER_PATIENT: 'Register Patient',
    REGISTERING_PATIENT: 'Registering Patient...',
}

export const HTTP_METHODS = {
    GET: 'GET',
    POST: 'POST',
    PUT: 'PUT',
    DELETE: 'DELETE'
}

export type HttpMethod =
    (typeof HTTP_METHODS)[keyof typeof HTTP_METHODS];