import { useQuery } from "@tanstack/react-query";


// export const useGetAccount = (id?: string) => {
//   const query = useQuery({
//     enabled: !!id,
//     queryKey: ["account", { id }],
//     queryFn: async () => {
//       const response = await client.api.accounts[":id"].$get({ param: { id } });

//       if (!response.ok) {
//         throw new Error("Failed to fetch account");
//       }

//       const { data } = await response.json();
//       return data;
//     },
//   });
//   return query;
// };

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
