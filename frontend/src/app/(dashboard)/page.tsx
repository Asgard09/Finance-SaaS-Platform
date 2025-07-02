"use client";

const DashboardPage = () => {
  return (
    <div className="max-w-screen-2xl mx-auto w-full pb-10 -mt-24">
      <div className="bg-gradient-to-b from-blue-700 to-blue-500 px-6 py-16 lg:px-20 pb-36">
        <div className="text-white">
          <h1 className="text-4xl lg:text-6xl font-bold mb-4">
            Welcome to Finance SaaS Platform! 🎉
          </h1>
          <p className="text-blue-100 text-lg lg:text-xl mb-8">
            You have successfully logged in with Google OAuth2. This is your
            dashboard where you can manage your finances.
          </p>
          <div className="bg-white/10 rounded-lg p-6 backdrop-blur-sm">
            <h2 className="text-2xl font-semibold mb-4">Getting Started</h2>
            <ul className="text-blue-100 space-y-2">
              <li>• Manage your accounts and transactions</li>
              <li>• Track your expenses and income</li>
              <li>• View financial reports and analytics</li>
              <li>• Set up budget categories</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardPage;
