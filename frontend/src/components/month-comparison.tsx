"use client";

import { useState } from "react";
import { Loader2 } from "lucide-react";
import { MonthSelector } from "@/components/month-selector";
import { ComparisonSummary } from "@/components/comparison-summary";
import { CategoryComparison } from "@/components/category-comparison";
import { useGetComparison } from "@/features/summary/api/use-get-comparison";

export const MonthComparison = () => {
  const [currentSelection, setCurrentSelection] = useState<{ year: number; month: number }>({
    year: new Date().getFullYear(),
    month: new Date().getMonth() + 1,
  });
  
  const [previousSelection, setPreviousSelection] = useState<{ year: number; month: number }>({
    year: new Date().getFullYear(),
    month: new Date().getMonth(),
  });

  const { data, isLoading, error } = useGetComparison(
    currentSelection.year,
    currentSelection.month,
    previousSelection.year,
    previousSelection.month
  );

  const handleSelectionChange = (
    current: { year: number; month: number },
    previous: { year: number; month: number }
  ) => {
    setCurrentSelection(current);
    setPreviousSelection(previous);
  };

  if (error) {
    return (
      <div className="text-center py-8">
        <p className="text-red-500">Error loading comparison data</p>
      </div>
    );
  }

  return (
    <div className="space-y-6 mt-8">
      <MonthSelector onSelectionChange={handleSelectionChange} />
      
      {isLoading ? (
        <div className="flex justify-center py-8">
          <Loader2 className="h-6 w-6 animate-spin" />
        </div>
      ) : data ? (
        <>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <ComparisonSummary
              title="Remaining"
              currentValue={data.current.remainingAmount}
              previousValue={data.previous.remainingAmount}
              change={data.remainingChange}
              changePercentage={data.remainingChangePercentage}
            />
            <ComparisonSummary
              title="Income"
              currentValue={data.current.incomeAmount}
              previousValue={data.previous.incomeAmount}
              change={data.incomeChange}
              changePercentage={data.incomeChangePercentage}
            />
            <ComparisonSummary
              title="Expenses"
              currentValue={data.current.expenseAmount}
              previousValue={data.previous.expenseAmount}
              change={data.expenseChange}
              changePercentage={data.expenseChangePercentage}
            />
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <CategoryComparison
              title="Income Category Changes"
              comparisons={data.categoryComparisons.income}
            />
            <CategoryComparison
              title="Expense Category Changes"
              comparisons={data.categoryComparisons.expense}
            />
          </div>
        </>
      ) : null}
    </div>
  );
};