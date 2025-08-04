// src/pages/RegisterPage.tsx
import React, { useState } from "react";
import { Input, Button, Card, Spacer } from "@nextui-org/react";
import { useNavigate } from "react-router-dom";
import apiClient from "../api";

export default function RegisterPage() {
    const [username, setUsername] = useState("");
    const [email, setEmail]       = useState("");
    const [password, setPassword] = useState("");
    const [error, setError]       = useState<string>();
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await apiClient.post<{ token: string }>(
                "/auth/register",
                { username, email, password }
            );
            localStorage.setItem("token", res.data.token);
            navigate("/");
        } catch (err: any) {
            setError(err.response?.data?.message || "Registration failed.");
        }
    };

    return (
        <div className="p-4 max-w-md mx-auto mt-20">
            <Card className="p-4">
                <h3 className="text-2xl mb-4">Sign Up</h3>
                {error && <p className="text-red-600 mb-2">{error}</p>}

                <form onSubmit={handleSubmit} className="space-y-4">
                    <Input
                        label="User Name"
                        fullWidth
                        required
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <Input
                        label="Email"
                        type="email"
                        fullWidth
                        required
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                    <Input
                        label="Password"
                        type="password"
                        fullWidth
                        required
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <Spacer y={1} />
                    <Button type="submit" className="w-full">
                        Register
                    </Button>
                </form>

                <Spacer y={1} />
                <p className="text-sm">
                    Do you have an account?{" "}
                    <span
                        className="text-blue-600 cursor-pointer"
                        onClick={() => navigate("/login")}
                    >
            Login
          </span>
                </p>
            </Card>
        </div>
    );
}
