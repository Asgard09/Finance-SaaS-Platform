"use client";

import { TrendingUp, TrendingDown, Minus } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { formatCurrency } from "@/lib/utils";

type CategoryComparison = {
  name: string;
  currentValue: number;
  previousValue: number;
  change: number;
  changePercentage: number;
};

type CategoryComparisonProps = {
  title: string;
  comparisons: CategoryComparison[];
  maxItems?: number;
};

export const CategoryComparison = ({ title, comparisons, maxItems = 5 }: CategoryComparisonProps) => {
  const displayedComparisons = comparisons.slice(0, maxItems);

  const getIcon = (change: number) => {
    if (change === 0) return <Minus className="h-4 w-4 text-gray-500" />;
    return change > 0 
      ? <TrendingUp className="h-4 w-4 text-green-600" /> 
      : <TrendingDown className="h-4 w-4 text-red-600" />;
  };

  const getChangeColor = (change: number) => {
    if (change === 0) return "text-gray-500";
    return change > 0 ? "text-green-600" : "text-red-600";
  };

  return (
    <Card className="border-none drop-shadow-sm">
      <CardHeader>
        <CardTitle className="text-xl line-clamp-1">{title}</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="space-y-4">
          {displayedComparisons.length === 0 ? (
            <p className="text-muted-foreground text-sm">No data available</p>
          ) : (
            displayedComparisons.map((comparison) => (
              <div key={comparison.name} className="space-y-2">
                <div className="flex justify-between items-start">
                  <div className="flex-1">
                    <p className="font-medium text-sm">{comparison.name}</p>
                    <p className="text-xs text-muted-foreground">
                      {formatCurrency(comparison.previousValue)} → {formatCurrency(comparison.currentValue)}
                    </p>
                  </div>
                  <div className={`flex items-center ml-4 ${getChangeColor(comparison.change)}`}>
                    {getIcon(comparison.change)}
                    <span className="ml-1 text-sm font-medium">
                      {Math.abs(comparison.changePercentage).toFixed(1)}%
                    </span>
                  </div>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div
                    className={`h-2 rounded-full ${
                      comparison.change >= 0 ? 'bg-green-500' : 'bg-red-500'
                    }`}
                    style={{
                      width: `${Math.min(Math.abs(comparison.changePercentage), 100)}%`,
                    }}
                  />
                </div>
              </div>
            ))
          )}
        </div>
      </CardContent>
    </Card>
  );
};