import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";

type CreateTransactionRequest = {
  amount: number;
  payee: string;
  notes?: string;
  date: string;
  accountId: number;
  categoryId?: number;
};

type CreateTransactionResponse = {
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

const createTransactionsBulk = async (
  data: CreateTransactionRequest[]
): Promise<CreateTransactionResponse[]> => {
  const response = await fetch(
    "http://localhost:8080/api/transaction/bulk-create",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(data),
    }
  );

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error("Authentication required. Please log in.");
    }
    throw new Error("Failed to create transactions");
  }

  return response.json();
};

export const useBulkCreateTransactions = () => {
  const queryClient = useQueryClient();

  const mutation = useMutation<
    CreateTransactionResponse[],
    Error,
    CreateTransactionRequest[]
  >({
    mutationFn: createTransactionsBulk,
    onSuccess: () => {
      toast.success("Transactions created");
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
      queryClient.invalidateQueries({ queryKey: ["summary"] });
    },
    onError: (error) => {
      toast.error(`Failed to create transactions: ${error.message}`);
    },
  });

  return mutation;
};
