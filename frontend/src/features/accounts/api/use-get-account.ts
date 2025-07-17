import { useQuery } from "@tanstack/react-query";


export const useGetAccount = (id?: string) => {
  const query = useQuery({
    enabled: !!id,
    queryKey: ["account", { id }],
    queryFn: async () => {
      // Simulate network delay
      await new Promise((r) => setTimeout(r, 500));

      // Return mock data
      return {
        id,
        name: "John Doe",
        email: "john@example.com",
        role: "Admin",
      };
    },
  });

  return query;
};
