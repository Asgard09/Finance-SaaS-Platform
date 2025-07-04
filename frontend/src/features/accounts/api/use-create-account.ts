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
  const response = await fetch("http://localhost:8080/api/account/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include", // This is important for OAuth2 authentication
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    if (response.status === 401) {
      throw new Error("Authentication required. Please log in.");
    }
    throw new Error("Failed to create account");
  }

  return response.json();
};

export const useCreateAccount = () => {
  const queryClient = useQueryClient();

  return useMutation<CreateAccountResponse, Error, CreateAccountRequest>({
    mutationFn: createAccount,
    onSuccess: () => {
      console.log("Account created successfully");
      queryClient.invalidateQueries({ queryKey: ["accounts"] });
    },
    onError: (error) => {
      console.error(`Failed to create account: ${error.message}`);
    },
  });
};
