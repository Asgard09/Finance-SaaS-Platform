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

const createTransaction = async (
  data: CreateTransactionRequest
): Promise<CreateTransactionResponse> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/transaction/create`,
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
    throw new Error("Failed to create transaction");
  }

  return response.json();
};

export const useCreateTransaction = () => {
  const queryClient = useQueryClient();

  const mutation = useMutation<
    CreateTransactionResponse,
    Error,
    CreateTransactionRequest
  >({
    mutationFn: createTransaction,
    onSuccess: () => {
      toast.success("Transaction created");
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
      queryClient.invalidateQueries({ queryKey: ["summary"] });
    },
    onError: (error) => {
      toast.error(`Failed to create transaction: ${error.message}`);
    },
  });

  return mutation;
};
