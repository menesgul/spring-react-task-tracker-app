import React, { useState } from "react";
import { Input, Button, Card, Spacer } from "@nextui-org/react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import { useAuth } from "../context/AuthContext";

export default function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [err, setErr] = useState("");
    const navigate = useNavigate();
    const { login } = useAuth();   // ← yeni

    const submit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await api.post("/auth/authenticate", { email, password });
            // artık hem state hem storage güncelleniyor:
            login(res.data.token);
            navigate("/");
        } catch (e: any) {
            setErr(e.response?.data?.message || "Login failed");
        }
    };

    return (
        <div className="p-4 max-w-md mx-auto mt-20">
            <Card className="p-4">
                <h1 className="text-2xl mb-4">Log In</h1>
                {err && <div className="text-red-600">{err}</div>}
                <form onSubmit={submit} className="space-y-4">
                    <Input
                        label="Email"
                        type="email"
                        required
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        fullWidth
                    />
                    <Input
                        label="Password"
                        type="password"
                        required
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        fullWidth
                    />
                    <Spacer y={1} />
                    <Button type="submit" className="w-full">
                        Login
                    </Button>
                </form>

                <Spacer y={1} />
                <p className="text-sm">
                    Don't you have an account ?{" "}
                    <span className="text-blue-600 cursor-pointer" onClick={() => navigate("/register")}>
            Sign In
          </span>
                </p>
            </Card>
        </div>
    );
}
