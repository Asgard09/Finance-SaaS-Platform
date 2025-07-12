"use client";

import { NewAccountSheet } from "@/features/accounts/components/new-account-sheet";
import { NewCategorySheet } from "@/features/categories/components/new-category-sheet";
import { NewTransactionSheet } from "@/features/transactions/components/new-transaction-sheet";
import { useMountedState } from "react-use";

export const SheetProvider = () => {
  const isMounted = useMountedState();

  if (!isMounted) return null;

  return (
    <>
      <NewAccountSheet />
      <NewCategorySheet />
      <NewTransactionSheet />
    </>
  );
};
