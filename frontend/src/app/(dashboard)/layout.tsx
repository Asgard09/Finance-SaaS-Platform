import { Header } from "@/components/header";
import { AuthGuard } from "@/components/auth-guard";

type Props = {
  children: React.ReactNode;
};

const DashboardLayout = ({ children }: Props) => {
  return (
    <AuthGuard>
      <Header />
      <main className="px-3 lg:px-14">{children}</main>
    </AuthGuard>
  );
};

export default DashboardLayout;
