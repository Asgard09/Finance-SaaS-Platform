"use client";

import { usePathname } from "next/navigation";
import { NavButton } from "./nav-button";

const routers = [
    {
        href: "/",
        label: "Overview",
    },
    {
        href: "/transactions",
        label: "Transactions",
    },
    {
        href: "/accounts",
        label: "Accounts",
    },
    {
        href: "/categories",
        label: "Categories",
    },
    {
        href: "/settings",
        label: "Settings",
    },
];

export const Navigation = () =>{
    const pathname = usePathname();
    return(
        <nav className="hidden lg:flex items-center gap-x-2
        overflow-x-auto">
            {routers.map((router) =>(
                <NavButton
                    key= {router.href}
                    href={router.href}
                    label={router.label}
                    isActive={pathname === router.href}
                />
            ))}
        </nav>
    )
}