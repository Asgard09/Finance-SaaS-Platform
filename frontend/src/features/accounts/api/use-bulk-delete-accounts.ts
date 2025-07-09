import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";

type ResponseType = {
  message?: string;
};

const deleteAllAccounts = async (): Promise<ResponseType> => {
  const response = await fetch("http://localhost:8080/api/account/deleteAll", {
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
    throw new Error("Failed to delete all accounts");
  }

  const textResponse = await response.text();
  return { message: textResponse };
};

export const useBulkDeleteAccounts = () => {
  const queryClient = useQueryClient();

  const mutation = useMutation<ResponseType, Error>({
    mutationFn: async () => {
      return await deleteAllAccounts();
    },
    onSuccess: () => {
      toast.success("All accounts deleted");
      queryClient.invalidateQueries({ queryKey: ["accounts"] });
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
      queryClient.invalidateQueries({ queryKey: ["summary"] });
    },
    onError: (error) => {
      toast.error(error.message || "Failed to delete accounts");
    },
  });

  return mutation;
};