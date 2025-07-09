import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";

type ResponseType = {
  message?: string;
};

const deleteAccount = async (id: string): Promise<ResponseType> => {
  const response = await fetch(`http://localhost:8080/api/account/${id}`, {
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
      throw new Error("Account not found or you don't have permission to delete this account");
    }
    throw new Error("Failed to delete account");
  }

  const textResponse = await response.text();
  return { message: textResponse };
};

export const useDeleteAccount = (id?: string) => {
  const queryClient = useQueryClient();

  const mutation = useMutation<ResponseType, Error>({
    mutationFn: async () => {
      if (!id) {
        throw new Error("Account ID is required");
      }
      return await deleteAccount(id);
    },
    onSuccess: () => {
      toast.success("Account deleted");
      queryClient.invalidateQueries({ queryKey: ["account", { id }] });
      queryClient.invalidateQueries({ queryKey: ["accounts"] });
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
      queryClient.invalidateQueries({ queryKey: ["summary"] });
    },
    onError: (error) => {
      toast.error(error.message || "Failed to delete account");
    },
  });

  return mutation;
};