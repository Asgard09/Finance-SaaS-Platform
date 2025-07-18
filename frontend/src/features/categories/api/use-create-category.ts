import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";

type CreateCategoryRequest = {
  name: string;
};

type CreateCategoryResponse = {
  name: string;
  // Add other fields that your CategoryDTO might have
};

const createCategory = async (
  data: CreateCategoryRequest
): Promise<CreateCategoryResponse> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/category/create`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include", // This is important for OAuth2 authentication
      body: JSON.stringify(data),
    }
  );

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error("Authentication required. Please log in.");
    }
    throw new Error("Failed to create category");
  }

  return response.json();
};

export const useCreateCategory = () => {
  /* Note: use cache data query without fetching from the server again*/
  const queryClient = useQueryClient();

  /* Note: useMutation --> change data (POST/PUT/DELETE)*/
  return useMutation<CreateCategoryResponse, Error, CreateCategoryRequest>({
    mutationFn: createCategory,
    onSuccess: () => {
      toast.success("Category created");
      queryClient.invalidateQueries({ queryKey: ["categories"] });
    },
    onError: (error) => {
      toast.error(`Failed to create category: ${error.message}`);
    },
  });
};
