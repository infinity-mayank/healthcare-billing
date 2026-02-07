import { EMPTY_STRING, HTTP_METHODS, type HttpMethod } from "../constants";

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? EMPTY_STRING;

const request = async <T>(
    url: string,
    method: HttpMethod,
    body?: unknown
): Promise<T> => {
    const response = await fetch(`${BASE_URL}${url}`, {
        method,
        headers: {
            'Content-Type': 'application/json',
        },
        body: body ? JSON.stringify(body) : undefined,
    });

    if (!response.ok) {
        const message = await response.text();
        throw new Error(message || 'API request failed');
    }

    // Handle empty response (204 No Content)
    if (response.status === 204) {
        return undefined as T;
    }

    return response.json() as Promise<T>;
};

export const apiClient = {
    get: <T>(url: string) => request<T>(url, HTTP_METHODS.GET),
    post: <T>(url: string, body: unknown) => request<T>(url, HTTP_METHODS.POST, body),
    put: <T>(url: string, body: unknown) => request<T>(url, HTTP_METHODS.PUT, body),
    delete: <T>(url: string) => request<T>(url, HTTP_METHODS.DELETE),
};
