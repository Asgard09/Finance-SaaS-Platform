import { useQuery } from "@tanstack/react-query";

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

const fetchTransaction = async (id: string): Promise<Transaction> => {
  const response = await fetch(`http://localhost:8080/api/transaction/${id}`, {
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
    if (response.status === 404) {
      throw new Error("Transaction not found");
    }
    throw new Error("Failed to fetch transaction");
  }

  return response.json();
};

export const useGetTransaction = (id?: string) => {
  const query = useQuery({
    enabled: !!id,
    queryKey: ["transaction", { id }],
    queryFn: () => {
      if (!id) throw new Error("Transaction ID is required");
      return fetchTransaction(id);
    },
  });
  return query;
};
