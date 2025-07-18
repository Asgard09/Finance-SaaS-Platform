import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";

const deleteAllTransactions = async (): Promise<string> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/transaction/deleteAll`,
    {
      method: "DELETE",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
    }
  );

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error("Authentication required. Please log in.");
    }
    throw new Error("Failed to delete transactions");
  }

  return response.text();
};

export const useBulkDeleteTransactions = () => {
  const queryClient = useQueryClient();

  const mutation = useMutation<string, Error>({
    mutationFn: deleteAllTransactions,
    onSuccess: () => {
      toast.success("All transactions deleted");
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
      queryClient.invalidateQueries({ queryKey: ["summary"] });
    },
    onError: (error) => {
      toast.error(`Failed to delete transactions: ${error.message}`);
    },
  });

  return mutation;
};
