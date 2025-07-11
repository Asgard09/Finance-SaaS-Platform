import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";

const deleteTransaction = async (id: string): Promise<string> => {
  const response = await fetch(`http://localhost:8080/api/transaction/${id}`, {
    method: "DELETE",
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
      throw new Error(
        "Transaction not found or you don't have permission to delete this transaction"
      );
    }
    throw new Error("Failed to delete transaction");
  }

  return response.text();
};

export const useDeleteTransaction = (id?: string) => {
  const queryClient = useQueryClient();

  const mutation = useMutation<string, Error>({
    mutationFn: async () => {
      if (!id) throw new Error("Transaction ID is required");
      return deleteTransaction(id);
    },
    onSuccess: () => {
      toast.success("Transaction deleted");
      queryClient.invalidateQueries({ queryKey: ["transaction", { id }] });
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
      queryClient.invalidateQueries({ queryKey: ["summary"] });
    },
    onError: (error) => {
      toast.error(`Failed to delete transaction: ${error.message}`);
    },
  });

  return mutation;
};
