import { toast } from "sonner";
import { useMutation, useQueryClient } from "@tanstack/react-query";

type CreateAccountRequest = {
  name: string;
};

type CreateAccountResponse = {
  name: string;
  // Add other fields that your AccountDTO might have
};

const createAccount = async (
  data: CreateAccountRequest
): Promise<CreateAccountResponse> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/account/create`,
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
    throw new Error("Failed to create account");
  }

  return response.json();
};

export const useCreateAccount = () => {
  /* Note: use cache data query without fetching from the server again*/
  const queryClient = useQueryClient();

  /* Note: useMutation --> change data (POST/PUT/DELETE)*/
  return useMutation<CreateAccountResponse, Error, CreateAccountRequest>({
    mutationFn: createAccount,
    onSuccess: () => {
      toast.success("Account created");
      queryClient.invalidateQueries({ queryKey: ["accounts"] });
    },
    onError: (error) => {
      toast.error(`Failed to create account: ${error.message}`);
    },
  });
};
