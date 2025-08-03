/* eslint-disable @typescript-eslint/no-explicit-any */
import { useQuery } from "@tanstack/react-query";
import { useSearchParams } from "next/navigation";

import { convertAmountFromMiliunits } from "@/lib/utils";

type SummaryResponse = {
  remainingAmount: number;
  remainingChange: number;
  incomeAmount: number;
  incomeChange: number;
  expenseAmount: number;
  expenseChange: number;
  expenseCategories: Array<{
    name: string;
    value: number;
  }>;
  incomeCategories: Array<{
    name: string;
    value: number;
  }>;
  days: Array<{
    date: string;
    income: number;
    expense: number;
  }>;
};

const fetchSummary = async (
  from?: string,
  to?: string,
  accountId?: string
): Promise<SummaryResponse> => {
  const params = new URLSearchParams();
  if (from) params.append("from", from);
  if (to) params.append("to", to);
  if (accountId) params.append("accountId", accountId);

  const queryString = params.toString();
  const url = `${process.env.NEXT_PUBLIC_API_URL}/api/summary${
    queryString ? `?${queryString}` : ""
  }`;

  const response = await fetch(url, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
  });

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error("Authentication required. Please log in.");
    }
    throw new Error("Failed to fetch summary");
  }

  const data: SummaryResponse = await response.json();
  return {
    ...data,
    incomeAmount: convertAmountFromMiliunits(data.incomeAmount || 0),
    expenseAmount: convertAmountFromMiliunits(
      Math.abs(data.expenseAmount || 0)
    ),
    remainingAmount: convertAmountFromMiliunits(data.remainingAmount || 0),
    expenseCategories: (data.expenseCategories || []).map((category: any) => ({
      ...category,
      value: convertAmountFromMiliunits(category.value || 0),
    })),
    incomeCategories: (data.incomeCategories || []).map((category: any) => ({
      ...category,
      value: convertAmountFromMiliunits(category.value || 0),
    })),
    days: (data.days || []).map((day: any) => ({
      ...day,
      income: convertAmountFromMiliunits(day.income || 0),
      expense: convertAmountFromMiliunits(day.expense || 0),
    })),
  };
};

export const useGetSummary = () => {
  const params = useSearchParams();
  const from = params.get("from") || "";
  const to = params.get("to") || "";
  const accountId = params.get("accountId") || "";

  const query = useQuery({
    queryKey: ["summary", { from, to, accountId }],
    queryFn: () => fetchSummary(from, to, accountId),
  });
  return query;
};
