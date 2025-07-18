import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";

type UpdateTransactionRequest = {
  amount: number;
  payee: string;
  notes?: string;
  date: string;
  accountId: number;
  categoryId?: number;
};

type UpdateTransactionResponse = {
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

const updateTransaction = async (
  id: string,
  data: UpdateTransactionRequest
): Promise<UpdateTransactionResponse> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/transaction/${id}`,
    {
      method: "PUT",
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
    if (response.status === 404) {
      throw new Error(
        "Transaction not found or you don't have permission to update this transaction"
      );
    }
    throw new Error("Failed to update transaction");
  }

  return response.json();
};

export const useEditTransaction = (id?: string) => {
  const queryClient = useQueryClient();

  const mutation = useMutation<
    UpdateTransactionResponse,
    Error,
    UpdateTransactionRequest
  >({
    mutationFn: async (data) => {
      if (!id) throw new Error("Transaction ID is required");
      return updateTransaction(id, data);
    },
    onSuccess: () => {
      toast.success("Transaction updated");
      queryClient.invalidateQueries({ queryKey: ["transaction", { id }] });
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
      queryClient.invalidateQueries({ queryKey: ["summary"] });
    },
    onError: (error) => {
      toast.error(`Failed to edit transaction: ${error.message}`);
    },
  });

  return mutation;
};
