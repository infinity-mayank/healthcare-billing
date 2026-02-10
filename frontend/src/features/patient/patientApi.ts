import type { Patient, PatientFormData } from "../../shared/types.ts";
import { apiClient } from "../../services/apiClient.ts";


export const patientAPI = {
    register: (data: PatientFormData): Promise<void> => {
        return apiClient.post<void>('/api/patients', data);
    },
    getAll: (): Promise<Patient[]> => {
        return apiClient.get<Patient[]>('/api/patients');
    }
};
