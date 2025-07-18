import { useQuery } from "@tanstack/react-query";

type Category = {
  id: number;
  name: string;
  plaiId?: number;
  userId: number;
};

const fetchCategories = async (): Promise<Category[]> => {
  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_URL}/api/category/getAll`,
    {
      method: "GET",
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
    throw new Error("Failed to fetch categories");
  }

  return response.json();
};

export const useGetCategories = () => {
  const query = useQuery({
    queryKey: ["categories"],
    queryFn: fetchCategories,
  });
  return query;
};
