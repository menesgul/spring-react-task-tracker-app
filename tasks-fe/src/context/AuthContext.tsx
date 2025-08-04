import React, { createContext, useContext, useState, useEffect, ReactNode } from "react";

interface AuthCtx {
    token: string | null;
    login: (t: string) => void;
    logout: () => void;
}
const AuthContext = createContext<AuthCtx>({
    token: null,
    login: () => {},
    logout: () => {},
});

export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
    const [token, setToken] = useState<string | null>(null);

    // İlk mount'ta localStorage'dan oku
    useEffect(() => {
        setToken(localStorage.getItem("token"));
    }, []);

    // login: hem state'i hem de storage'ı günceller
    const login = (t: string) => {
        localStorage.setItem("token", t);
        setToken(t);
    };

    const logout = () => {
        localStorage.removeItem("token");
        setToken(null);
        window.location.href = "/login";
    };

    return (
        <AuthContext.Provider value={{ token, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
