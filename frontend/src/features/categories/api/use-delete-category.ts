import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";

const deleteCategory = async (id: string): Promise<string> => {
  const response = await fetch(`http://localhost:8080/api/category/${id}`, {
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
        "Category not found or you don't have permission to delete this category"
      );
    }
    throw new Error("Failed to delete category");
  }

  return response.text();
};

export const useDeleteCategory = (id?: string) => {
  const queryClient = useQueryClient();

  const mutation = useMutation<string, Error>({
    mutationFn: async () => {
      if (!id) throw new Error("Category ID is required");
      return deleteCategory(id);
    },
    onSuccess: () => {
      toast.success("Category deleted");
      queryClient.invalidateQueries({ queryKey: ["category", { id }] });
      queryClient.invalidateQueries({ queryKey: ["categories"] });
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
    },
    onError: (error) => {
      toast.error(`Failed to delete category: ${error.message}`);
    },
  });

  return mutation;
};
