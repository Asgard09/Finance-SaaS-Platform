"use client";

import { useAuth } from "@/lib/auth-context";
import { useRouter } from "next/navigation";
import { useEffect } from "react";
import { Loader2 } from "lucide-react";

export const AuthGuard = ({ children }: { children: React.ReactNode }) => {
  const { user, isLoaded } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (isLoaded && !user) {
      router.push("/login");
    }
  }, [isLoaded, user, router]);

  if (!isLoaded) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Loader2 className="size-8 animate-spin text-blue-600" />
      </div>
    );
  }

  if (!user) {
    return null;
  }

  return <>{children}</>;
};
