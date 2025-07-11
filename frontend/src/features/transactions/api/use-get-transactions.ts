import { useQuery } from "@tanstack/react-query";
import { useSearchParams } from "next/navigation";

type Transaction = {
  id: number;
  amount: number;
  payee: string;
  notes?: string;
  date: string;
  accountId: number;
  categoryId?: number;
  account: string; // Add account name
  category?: string; // Add category name (optional since it can be null)
};

const fetchTransactions = async (
  from?: string,
  to?: string,
  accountId?: string
): Promise<Transaction[]> => {
  const params = new URLSearchParams();
  if (from) params.append("from", from);
  if (to) params.append("to", to);
  if (accountId) params.append("accountId", accountId);

  const queryString = params.toString();
  const url = `http://localhost:8080/api/transaction/getAll${
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
    throw new Error("Failed to fetch transactions");
  }

  return response.json();
};

export const useGetTransactions = () => {
  const params = useSearchParams();
  const from = params.get("from") || "";
  const to = params.get("to") || "";
  const accountId = params.get("accountId") || "";

  const query = useQuery({
    queryKey: ["transactions", { from, to, accountId }],
    queryFn: () => fetchTransactions(from, to, accountId),
  });
  return query;
};
