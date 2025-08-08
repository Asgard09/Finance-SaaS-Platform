import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";

type MonthSelectorProps = {
  onSelectionChange: (current: { year: number; month: number }, previous: { year: number; month: number }) => void;
};

export const MonthSelector = ({ onSelectionChange }: MonthSelectorProps) => {
  const [currentYear, setCurrentYear] = useState<number>(new Date().getFullYear());
  const [currentMonth, setCurrentMonth] = useState<number>(new Date().getMonth() + 1);
  const [previousYear, setPreviousYear] = useState<number>(new Date().getFullYear());
  const [previousMonth, setPreviousMonth] = useState<number>(new Date().getMonth());

  const currentDate = new Date();
  const years = Array.from({ length: 5 }, (_, i) => currentDate.getFullYear() - i);
  const months = [
    { value: 1, label: "January" },
    { value: 2, label: "February" },
    { value: 3, label: "March" },
    { value: 4, label: "April" },
    { value: 5, label: "May" },
    { value: 6, label: "June" },
    { value: 7, label: "July" },
    { value: 8, label: "August" },
    { value: 9, label: "September" },
    { value: 10, label: "October" },
    { value: 11, label: "November" },
    { value: 12, label: "December" },
  ];

  const handleSelectionChange = () => {
    onSelectionChange(
      { year: currentYear, month: currentMonth },
      { year: previousYear, month: previousMonth }
    );
  };

  const handleCurrentYearChange = (year: string) => {
    setCurrentYear(parseInt(year));
    setTimeout(handleSelectionChange, 0);
  };

  const handleCurrentMonthChange = (month: string) => {
    setCurrentMonth(parseInt(month));
    setTimeout(handleSelectionChange, 0);
  };

  const handlePreviousYearChange = (year: string) => {
    setPreviousYear(parseInt(year));
    setTimeout(handleSelectionChange, 0);
  };

  const handlePreviousMonthChange = (month: string) => {
    setPreviousMonth(parseInt(month));
    setTimeout(handleSelectionChange, 0);
  };

  return (
    <Card className="border-none drop-shadow-sm">
      <CardHeader>
        <CardTitle className="text-xl line-clamp-1">Compare Months</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <h3 className="text-sm font-medium mb-3">Current Month</h3>
            <div className="flex gap-2">
              <Select value={currentYear.toString()} onValueChange={handleCurrentYearChange}>
                <SelectTrigger className="flex-1">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {years.map(year => (
                    <SelectItem key={year} value={year.toString()}>
                      {year}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <Select value={currentMonth.toString()} onValueChange={handleCurrentMonthChange}>
                <SelectTrigger className="flex-1">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {months.map(month => (
                    <SelectItem key={month.value} value={month.value.toString()}>
                      {month.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>
          
          <div>
            <h3 className="text-sm font-medium mb-3">Compare Against</h3>
            <div className="flex gap-2">
              <Select value={previousYear.toString()} onValueChange={handlePreviousYearChange}>
                <SelectTrigger className="flex-1">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {years.map(year => (
                    <SelectItem key={year} value={year.toString()}>
                      {year}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <Select value={previousMonth.toString()} onValueChange={handlePreviousMonthChange}>
                <SelectTrigger className="flex-1">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {months.map(month => (
                    <SelectItem key={month.value} value={month.value.toString()}>
                      {month.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  );
};