"use client";

import { useAuth } from "@/lib/auth-context";
import { HeaderLogo } from "./header-logo";
import { Navigation } from "./navigation";
import { Button } from "@/components/ui/button";
import { Loader2, LogOut, User } from "lucide-react";
import { WelcomeMsg } from "./welcome-msg";
import Image from "next/image";

export const Header = () => {
  const { user, isLoaded, logout } = useAuth();

  const handleLogout = () => {
    logout();
  };

  return (
    <header className="bg-gradient-to-b from-blue-700 to-blue-500 px-4 py-8 lg:px-14 pb-36">
      <div className="max-w-screen-2xl mx-auto">
        <div className="w-full flex items-center justify-between mb-14">
          <div className="flex items-center lg:gap-x-16">
            <HeaderLogo />
            <Navigation />
          </div>
          {isLoaded ? (
            user ? (
              <div className="flex items-center gap-x-2">
                <div className="flex items-center gap-x-2 bg-white/10 rounded-lg px-3 py-2">
                  {user.picture ? (
                    <Image
                      src={user.picture}
                      alt={user.name}
                      width={32}
                      height={32}
                      className="w-8 h-8 rounded-full"
                    />
                  ) : (
                    <User className="w-8 h-8 text-white" />
                  )}
                  <span className="text-white text-sm font-medium">
                    {user.firstName}
                  </span>
                </div>
                <Button
                  onClick={handleLogout}
                  variant="ghost"
                  size="sm"
                  className="text-white hover:bg-white/10"
                >
                  <LogOut className="w-4 h-4" />
                </Button>
              </div>
            ) : (
              <Button
                onClick={() => (window.location.href = "/login")}
                className="bg-white text-blue-600 hover:bg-white/90"
              >
                Sign In
              </Button>
            )
          ) : (
            <Loader2 className="size-8 animate-spin text-slate-400" />
          )}
        </div>
        <WelcomeMsg />
      </div>
    </header>
  );
};
