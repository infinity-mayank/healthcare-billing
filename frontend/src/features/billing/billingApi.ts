import { apiClient } from "../../services/apiClient.ts";
import type {Bill} from "../../shared/types.ts";

export const billingAPI = {
    generateBill: (doctorNpiNumber: string, patientId: string): Promise<Bill> => {
        const query = new URLSearchParams({
            doctorNpiNumber,
            patientId
        }).toString();

        return apiClient.get<Bill>(`/api/billing/generate?${query}`);
    }
};
