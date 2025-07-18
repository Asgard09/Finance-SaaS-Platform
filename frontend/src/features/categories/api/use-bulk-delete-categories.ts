import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";

const deleteAllCategories = async (): Promise<string> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/category/deleteAll`,
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
    throw new Error("Failed to delete categories");
  }

  return response.text();
};

export const useBulkDeleteCategories = () => {
  const queryClient = useQueryClient();

  const mutation = useMutation<string, Error>({
    mutationFn: deleteAllCategories,
    onSuccess: () => {
      toast.success("All categories deleted");
      queryClient.invalidateQueries({ queryKey: ["categories"] });
      queryClient.invalidateQueries({ queryKey: ["transactions"] });
    },
    onError: (error) => {
      toast.error(`Failed to delete categories: ${error.message}`);
    },
  });

  return mutation;
};
