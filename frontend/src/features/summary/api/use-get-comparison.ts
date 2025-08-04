import { useQuery } from "@tanstack/react-query";
import { convertAmountFromMiliunits } from "@/lib/utils";

type ComparisonData = {
  month: string;
  remainingAmount: number;
  incomeAmount: number;
  expenseAmount: number;
  expenseCategories: Array<{
    name: string;
    value: number;
  }>;
  incomeCategories: Array<{
    name: string;
    value: number;
  }>;
};

type CategoryComparison = {
  name: string;
  currentValue: number;
  previousValue: number;
  change: number;
  changePercentage: number;
};

type ComparisonResult = {
  current: ComparisonData;
  previous: ComparisonData;
  incomeChange: number;
  incomeChangePercentage: number;
  expenseChange: number;
  expenseChangePercentage: number;
  remainingChange: number;
  remainingChangePercentage: number;
  categoryComparisons: {
    income: CategoryComparison[];
    expense: CategoryComparison[];
  };
};

const fetchMonthSummary = async (year: number, month: number): Promise<ComparisonData> => {
  const startDate = new Date(year, month - 1, 1);
  const endDate = new Date(year, month, 0);
  
  const from = startDate.toISOString().split('T')[0];
  const to = endDate.toISOString().split('T')[0];

  const url = `${process.env.NEXT_PUBLIC_API_URL}/api/summary?from=${from}&to=${to}`;

  const response = await fetch(url, {
    method: "GET",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
  });

  if (!response.ok) {
    throw new Error("Failed to fetch summary");
  }

  const data = await response.json();
  
  return {
    month: `${year}-${month.toString().padStart(2, '0')}`,
    remainingAmount: convertAmountFromMiliunits(data.remainingAmount || 0),
    incomeAmount: convertAmountFromMiliunits(data.incomeAmount || 0),
    expenseAmount: convertAmountFromMiliunits(Math.abs(data.expenseAmount || 0)),
    expenseCategories: (data.expenseCategories || []).map((category: any) => ({
      ...category,
      value: convertAmountFromMiliunits(category.value || 0),
    })),
    incomeCategories: (data.incomeCategories || []).map((category: any) => ({
      ...category,
      value: convertAmountFromMiliunits(category.value || 0),
    })),
  };
};

const calculateCategoryComparisons = (
  currentCategories: Array<{ name: string; value: number }>,
  previousCategories: Array<{ name: string; value: number }>
): CategoryComparison[] => {
  const allCategoryNames = new Set([
    ...currentCategories.map(c => c.name),
    ...previousCategories.map(c => c.name)
  ]);

  return Array.from(allCategoryNames).map(name => {
    const current = currentCategories.find(c => c.name === name)?.value || 0;
    const previous = previousCategories.find(c => c.name === name)?.value || 0;
    const change = current - previous;
    const changePercentage = previous === 0 
      ? (current === 0 ? 0 : 100) 
      : (change / previous) * 100;

    return {
      name,
      currentValue: current,
      previousValue: previous,
      change,
      changePercentage,
    };
  }).sort((a, b) => Math.abs(b.changePercentage) - Math.abs(a.changePercentage));
};

const calculatePercentageChange = (current: number, previous: number): number => {
  if (previous === 0) return current === 0 ? 0 : 100;
  return ((current - previous) / previous) * 100;
};

export const useGetComparison = (
  currentYear: number,
  currentMonth: number,
  previousYear: number,
  previousMonth: number
) => {
  return useQuery({
    queryKey: ["comparison", { currentYear, currentMonth, previousYear, previousMonth }],
    queryFn: async (): Promise<ComparisonResult> => {
      const [current, previous] = await Promise.all([
        fetchMonthSummary(currentYear, currentMonth),
        fetchMonthSummary(previousYear, previousMonth),
      ]);

      const incomeChange = current.incomeAmount - previous.incomeAmount;
      const expenseChange = current.expenseAmount - previous.expenseAmount;
      const remainingChange = current.remainingAmount - previous.remainingAmount;

      return {
        current,
        previous,
        incomeChange,
        incomeChangePercentage: calculatePercentageChange(current.incomeAmount, previous.incomeAmount),
        expenseChange,
        expenseChangePercentage: calculatePercentageChange(current.expenseAmount, previous.expenseAmount),
        remainingChange,
        remainingChangePercentage: calculatePercentageChange(current.remainingAmount, previous.remainingAmount),
        categoryComparisons: {
          income: calculateCategoryComparisons(current.incomeCategories, previous.incomeCategories),
          expense: calculateCategoryComparisons(current.expenseCategories, previous.expenseCategories),
        },
      };
    },
    enabled: !!(currentYear && currentMonth && previousYear && previousMonth),
  });
};