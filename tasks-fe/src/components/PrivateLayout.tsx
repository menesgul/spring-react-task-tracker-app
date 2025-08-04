// tasks-fe/src/components/PrivateLayout.tsx
import Header from "./Header";

export default function PrivateLayout({ children }: { children: React.ReactNode }) {
    return (
        <>
            <Header />
            <main>{children}</main>
        </>
    );
}