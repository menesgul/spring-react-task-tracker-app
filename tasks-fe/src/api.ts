// src/api.ts
import axios from "axios";

const apiClient = axios.create({
    // Eğer .env’de VITE_API_URL tanımlıysa onu kullan, yoksa fallback olarak backend’in URL’ini yaz
    baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080/api",
});

// Her isteğe JWT header’ını ekle
apiClient.interceptors.request.use((config: any) => {
    const token = localStorage.getItem("token");
    if (token && config.headers) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// 401/403 dönünce otomatik logout + /login yönlendirmesi
apiClient.interceptors.response.use(
    (response: any) => response,
    (error: any) => {
        if (error.response?.status === 401 || error.response?.status === 403) {
            localStorage.removeItem("token");
            window.location.href = "/login";
        }
        return Promise.reject(error);
    }
);

export default apiClient;
