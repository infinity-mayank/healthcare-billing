import React from 'react';
import { BiHealth } from "react-icons/bi";
import { APP_CONFIG } from "../../constants";

export const Header = (): React.ReactNode => {
    return (
        <header className="text-center mb-12">
            <div className="inline-flex items-center gap-3 mb-2">
                <BiHealth size={40} />
                <h1 className="text-3xl font-bold text-white">
                    {APP_CONFIG.APP_TITLE}
                </h1>
            </div>
        </header>
    );
};
