"use client";

import { useAuth } from "@/lib/auth-context";

export const WelcomeMsg = () => {
  const { user, isLoaded } = useAuth();

  return (
    <div className="space-y-2 mb-4">
      <h2 className="text-2xl lg:text-4xl text-white font-medium">
        Welcome Back{isLoaded && user ? ", " : " "}
        {user?.firstName}🖐
      </h2>
      <p className="text-sm lg:text-base text-[#89b6fd]">
        This is your Financial Overview Report
      </p>
    </div>
  );
};
