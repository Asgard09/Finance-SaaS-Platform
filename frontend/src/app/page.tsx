"use client";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import Image from "next/image";
import Link from "next/link";
import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/lib/auth-context";
import {
  ArrowRight,
  BarChart3,
  DollarSign,
  PieChart,
  Shield,
  TrendingUp,
  Users,
  Loader2,
} from "lucide-react";

export default function LandingPage() {
  const { user, isLoaded } = useAuth();
  const router = useRouter();

  // Auto-redirect authenticated users to dashboard
  useEffect(() => {
    if (isLoaded && user) {
      router.push("/dashboard");
    }
  }, [isLoaded, user, router]);

  // Show loading while checking auth status
  if (!isLoaded) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Loader2 className="size-8 animate-spin text-blue-600" />
      </div>
    );
  }

  // Don't render landing page if user is authenticated (will redirect)
  if (user) {
    return null;
  }

  const features = [
    {
      icon: <DollarSign className="w-6 h-6" />,
      title: "Transaction Management",
      description:
        "Track all your income and expenses with detailed categorization and smart insights.",
    },
    {
      icon: <BarChart3 className="w-6 h-6" />,
      title: "Financial Analytics",
      description:
        "Visualize your spending patterns with interactive charts and comprehensive reports.",
    },
    {
      icon: <PieChart className="w-6 h-6" />,
      title: "Budget Planning",
      description:
        "Set budgets, track progress, and receive alerts to stay on top of your finances.",
    },
    {
      icon: <TrendingUp className="w-6 h-6" />,
      title: "Investment Tracking",
      description:
        "Monitor your investments and track portfolio performance in real-time.",
    },
    {
      icon: <Shield className="w-6 h-6" />,
      title: "Secure & Private",
      description:
        "Bank-level security with OAuth2 authentication and encrypted data storage.",
    },
    {
      icon: <Users className="w-6 h-6" />,
      title: "Multi-Account Support",
      description:
        "Manage multiple accounts and categories for complete financial oversight.",
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-indigo-50">
      {/* Header */}
      <header className="container mx-auto px-4 py-6">
        <nav className="flex items-center justify-between">
          <div className="flex items-center space-x-2">
            <Image
              src="/logo.svg"
              alt="Finance Platform"
              width={40}
              height={40}
            />
            <span className="text-2xl font-bold text-gray-900">Finance</span>
          </div>
          <div className="flex items-center space-x-4">
            <Link href="/login">
              <Button variant="outline" size="sm">
                Sign In
              </Button>
            </Link>
            <Link href="/login">
              <Button size="sm">
                Get Started
                <ArrowRight className="w-4 h-4 ml-2" />
              </Button>
            </Link>
          </div>
        </nav>
      </header>

      {/* Hero Section */}
      <section className="container mx-auto px-4 py-20">
        <div className="text-center max-w-4xl mx-auto">
          <h1 className="text-5xl md:text-6xl font-bold text-gray-900 mb-6 leading-tight">
            Take Control of Your
            <span className="text-blue-600 block">Financial Future</span>
          </h1>
          <p className="text-xl text-gray-600 mb-8 max-w-2xl mx-auto leading-relaxed">
            Comprehensive financial management platform that helps you track
            expenses, analyze spending patterns, and make informed financial
            decisions434.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center items-center">
            <Link href="/login">
              <Button
                size="lg"
                className="bg-blue-600 hover:bg-blue-700 text-white px-8 py-4 text-lg"
              >
                Start Free Trial
                <ArrowRight className="w-5 h-5 ml-2" />
              </Button>
            </Link>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="container mx-auto px-4 py-20">
        <div className="text-center mb-16">
          <h2 className="text-4xl font-bold text-gray-900 mb-4">
            Everything You Need to Manage Your Finances
          </h2>
          <p className="text-xl text-gray-600 max-w-2xl mx-auto">
            Powerful tools and insights to help you make better financial
            decisions
          </p>
        </div>

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
          {features.map((feature, index) => (
            <Card
              key={index}
              className="border-0 shadow-lg hover:shadow-xl transition-shadow duration-300"
            >
              <CardContent className="p-8">
                <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center text-blue-600 mb-4">
                  {feature.icon}
                </div>
                <h3 className="text-xl font-semibold text-gray-900 mb-3">
                  {feature.title}
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  {feature.description}
                </p>
              </CardContent>
            </Card>
          ))}
        </div>
      </section>


      {/* Footer */}
      <footer className="bg-gray-900 text-white py-12">
        <div className="container mx-auto px-4">
          <div className="flex flex-col md:flex-row justify-between items-center">
            <div className="flex items-center space-x-2 mb-4 md:mb-0">
              <Image
                src="/logo.svg"
                alt="Finance Platform"
                width={32}
                height={32}
              />
              <span className="text-xl font-bold">Finance</span>
            </div>
            <div className="text-gray-400">
              © 2024 Finance. All rights reserved.
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}
