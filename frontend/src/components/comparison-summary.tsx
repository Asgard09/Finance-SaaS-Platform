"use client";

import { TrendingUp, TrendingDown, Minus } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { formatCurrency } from "@/lib/utils";

type ComparisonSummaryProps = {
  title: string;
  currentValue: number;
  previousValue: number;
  change: number;
  changePercentage: number;
};

export const ComparisonSummary = ({
  title,
  currentValue,
  previousValue,
  change,
  changePercentage,
}: ComparisonSummaryProps) => {
  const isPositive = change > 0;
  const isNeutral = change === 0;

  const getIcon = () => {
    if (isNeutral) return <Minus className="h-4 w-4" />;
    return isPositive ? <TrendingUp className="h-4 w-4" /> : <TrendingDown className="h-4 w-4" />;
  };

  const getColorClass = () => {
    if (isNeutral) return "text-gray-500";
    return isPositive ? "text-green-600" : "text-red-600";
  };

  return (
    <Card className="border-none drop-shadow-sm">
      <CardHeader className="pb-3">
        <CardTitle className="text-sm font-medium">{title}</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-2">
          <div className="text-2xl font-bold">
            {formatCurrency(currentValue)}
          </div>
          <div className="text-xs text-muted-foreground">
            Previous: {formatCurrency(previousValue)}
          </div>
          <div className={`flex items-center text-sm ${getColorClass()}`}>
            {getIcon()}
            <span className="ml-1">
              {Math.abs(changePercentage).toFixed(1)}%
            </span>
            <span className="ml-1">
              ({isPositive ? '+' : ''}{formatCurrency(change)})
            </span>
          </div>
        </div>
      </CardContent>
    </Card>
  );
};