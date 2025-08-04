import { Button } from "@nextui-org/react";
import { useAuth } from "../context/AuthContext";

export default function Header() {
    const { logout } = useAuth();
    return (
        // fixed ile sayfa kaydırılsa bile hep görünecek
        <header className="fixed top-4 left-4">
            <Button color="danger" variant="flat" onClick={logout}>
                Log Out
            </Button>
        </header>
    );
}