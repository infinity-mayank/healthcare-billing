import type { Doctor, DoctorFormData } from "../../shared/types.ts";
import { apiClient } from "../../services/apiClient.ts";

export const doctorAPI = {
    register: (data: DoctorFormData): Promise<void> => {
        return apiClient.post<void>('/api/doctors', data);
    },
    getAll: (): Promise<Doctor[]> => {
        return apiClient.get<Doctor[]>('/api/doctors');
    }
};
