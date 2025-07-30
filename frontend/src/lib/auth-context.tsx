"use client";

import {
  createContext,
  useContext,
  useEffect,
  useState,
  ReactNode,
} from "react";

interface User {
  id: string;
  email: string;
  name: string;
  firstName: string;
  lastName: string;
  picture: string;
  authenticated: boolean;
}

interface AuthContextType {
  user: User | null;
  isLoaded: boolean;
  logout: () => Promise<void>;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoaded, setIsLoaded] = useState(false);

  useEffect(() => {
    // Check if we're coming back from OAuth
    // const urlParams = new URLSearchParams(window.location.search);
    const fromOAuth =
      window.location.pathname === "/" &&
      document.referrer.includes("accounts.google.com");

    if (fromOAuth) {
      // Small delay to let session establish
      setTimeout(() => checkAuthStatus(), 500);
    } else {
      checkAuthStatus();
    }
  }, []);

  const checkAuthStatus = async () => {
    try {
      console.log(
        "Checking auth with URL:",
        `${process.env.NEXT_PUBLIC_API_URL}/user/profile`
      );

      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_URL}/user/profile`,
        {
          credentials: "include",
          mode: "cors", // Explicitly set CORS mode
        }
      );

      console.log("Auth response status:", response.status);
      console.log("Auth response headers:", response.headers);

      if (response.ok) {
        const userData = await response.json();
        console.log("User data received:", userData);
        setUser(userData);
      } else {
        console.log("Auth failed with status:", response.status);
        setUser(null);
      }
    } catch (error) {
      console.error("Auth check failed:", error);
      setUser(null);
    } finally {
      setIsLoaded(true);
    }
  };

  const logout = async () => {
    try {
      await fetch(`${process.env.NEXT_PUBLIC_API_URL}/logout`, {
        method: "POST",
        credentials: "include",
      });
    } catch (error) {
      console.error("Logout failed:", error);
    } finally {
      setUser(null);
    }
  };

  return (
    <AuthContext.Provider value={{ user, isLoaded, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
