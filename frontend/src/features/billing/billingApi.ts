import { apiClient } from "../../services/apiClient.ts";
import type {Bill, BillSaveRequest} from "../../shared/types.ts";

export const billingAPI = {
    generateBill: (doctorNpiNumber: string, patientId: string): Promise<Bill> => {
        const query = new URLSearchParams({
            doctorNpiNumber,
            patientId
        }).toString();

        return apiClient.get<Bill>(`/api/bill/generate?${query}`);
    },
    saveBill: (data: BillSaveRequest): Promise<void> => {
        return apiClient.post('/api/bill/save', data);
    }
};
