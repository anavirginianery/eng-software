"use client";

import React, { useState } from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import { Eye } from "lucide-react";

type TimeFrame = "dia" | "semana" | "mês" | "ano" | "geral";

const data = [
  { name: "Segunda", glicemia: 150 },
  { name: "Terça", glicemia: 350 },
  { name: "Quarta", glicemia: 190 },
  { name: "Quinta", glicemia: 150 },
  { name: "Sexta", glicemia: 300 },
  { name: "Sábado", glicemia: 280 },
  { name: "Domingo", glicemia: 220 },
];

const timeButtons = [
  { label: "Todos", value: "" },
  { label: "08:00", value: "08:00" },
  { label: "11:00", value: "11:00" },
  { label: "14:00", value: "14:00" },
  { label: "16:00", value: "16:00" },
  { label: "19:00", value: "19:00" },
  { label: "22:00", value: "22:00" },
];

const timeFrames: TimeFrame[] = ["dia", "semana", "mês", "ano", "geral"];

export default function Dashboard() {
  const [selectedTime, setSelectedTime] = useState<TimeFrame>("semana");
  const [selectedHour, setSelectedHour] = useState<string>("");

  return (
    <main className="px-4 sm:px-6 lg:px-8 py-6 bg-gradient-to-t from-[#B4E4E2] to-[#E7F5F4] min-h-screen">
      <div className="h-full">
        <div className="bg-white rounded-2xl shadow flex flex-col lg:flex-row h-[calc(100vh-2rem)]">
          {/* TimeFilter Section */}
          <div className="flex flex-col gap-2 p-3 border-b lg:border-b-0 lg:border-r border-gray-200 w-full lg:w-48">
            <div className="flex items-center gap-2">
              <h3 className="text-sm font-semibold text-gray-600 inline-flex items-center">
                <Eye className="w-4 h-4 mr-2" />
                Visualização
              </h3>
            </div>
            <div className="flex flex-row lg:flex-col gap-2 overflow-x-auto">
              {timeFrames.map((timeFrame) => (
                <button
                  key={timeFrame}
                  onClick={() => setSelectedTime(timeFrame)}
                  className={`text-left px-3 py-1.5 rounded-md text-sm transition-colors whitespace-nowrap
                    ${
                      selectedTime === timeFrame
                        ? "bg-[#337F7B] text-white"
                        : "bg-gray-100 hover:bg-gray-200 text-gray-700"
                    }`}
                >
                  {timeFrame.charAt(0).toUpperCase() + timeFrame.slice(1)}
                </button>
              ))}
            </div>
          </div>

          {/* Chart Section */}
          <div className="flex-1 flex flex-col p-3">
            <div className="flex flex-col flex-1">
              {/* Controls Section */}
              <div className="flex items-center mb-2">
                <div className="w-full overflow-x-auto">
                  <div className="flex gap-2 min-w-max">
                    {timeButtons.map((button) => (
                      <button
                        key={button.value}
                        onClick={() => setSelectedHour(button.value)}
                        className={`px-3 py-1.5 text-sm font-medium transition-colors rounded-md min-w-[70px]
                          ${
                            selectedHour === button.value
                              ? "bg-[#337F7B] text-white"
                              : "bg-gray-100 hover:bg-gray-200 text-gray-700"
                          }`}
                      >
                        {button.label}
                      </button>
                    ))}
                  </div>
                </div>
              </div>

              {/* Chart Section */}
              <div className="flex-1 min-h-0">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart
                    data={data}
                    margin={{ top: 10, right: 10, left: 0, bottom: 0 }}
                  >
                    <CartesianGrid strokeDasharray="3 3" stroke="#E5E7EB" />
                    <XAxis
                      dataKey="name"
                      stroke="#4B5563"
                      tick={{ fill: "#4B5563" }}
                      fontSize={12}
                    />
                    <YAxis
                      stroke="#4B5563"
                      tick={{ fill: "#4B5563" }}
                      domain={[0, 400]}
                      ticks={[0, 100, 200, 300, 400]}
                      fontSize={12}
                    />
                    <Tooltip
                      contentStyle={{
                        backgroundColor: "white",
                        border: "none",
                        borderRadius: "6px",
                        boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
                        fontSize: "12px",
                        padding: "4px 8px",
                      }}
                    />
                    <Line
                      type="monotone"
                      dataKey="glicemia"
                      stroke="#38B2AC"
                      strokeWidth={2}
                      dot={{ fill: "#38B2AC", strokeWidth: 2, r: 3 }}
                      activeDot={{ r: 5 }}
                    />
                  </LineChart>
                </ResponsiveContainer>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
