import { type ReactNode } from 'react';
import { Header } from "../shared/components/Header.tsx";

export const MainLayout = (): ReactNode => {
    return (
        <div className="h-full w-full bg-teal-800 overflow-auto">
            <div className="min-h-full w-full p-4 md:p-8">
                <Header />
            </div>
        </div>
    );
};
