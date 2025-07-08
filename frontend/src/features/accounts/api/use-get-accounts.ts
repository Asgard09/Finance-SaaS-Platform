import { useQuery } from "@tanstack/react-query";

type Account = {
  id: number;
  plaiId: number;
  name: string;
  userId: number;
};

const fetchAccounts = async (): Promise<Account[]> => {
  const response = await fetch("http://localhost:8080/api/account/getAll", {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
  });

  if(!response.ok){
    if(response.status === 401){
        throw new Error("Authentication required. Please log in.");
    }

    throw new Error("Failed to fetch accounts");
  }

  return response.json();
};

export const useGetAccounts = () => {
  const query = useQuery({
    queryKey: ["accounts"],
    queryFn: fetchAccounts,
  });
  return query;
};
