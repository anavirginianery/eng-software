"use client";

import React, { useState, useEffect } from "react";
import { Eye } from "lucide-react";
import { LineChart, Line, ResponsiveContainer } from "recharts";
import { doc, getDoc } from "firebase/firestore";
import { db } from "@/config/firebase";

const generateMockData = () => {
  return Array.from({ length: 7 }, (_, i) => ({
    name: i,
    value: Math.floor(Math.random() * 300) + 100,
  }));
};

const CircularProgress = ({
  value,
  color,
}: {
  value: number;
  color: string;
}) => {
  const radius = 30;
  const circumference = 2 * Math.PI * radius;
  const progress = (value / 400) * circumference;

  return (
    <div className="relative w-16 h-16">
      <svg className="w-full h-full transform -rotate-90">
        <circle
          className="text-gray-200"
          strokeWidth="4"
          stroke="currentColor"
          fill="transparent"
          r={radius}
          cx="32"
          cy="32"
        />
        <circle
          className={color}
          strokeWidth="4"
          strokeDasharray={circumference}
          strokeDashoffset={circumference - progress}
          strokeLinecap="round"
          stroke="currentColor"
          fill="transparent"
          r={radius}
          cx="32"
          cy="32"
        />
      </svg>
      <span className="absolute inset-0 flex items-center justify-center text-sm font-medium">
        {value}
      </span>
    </div>
  );
};

const MetricCard = ({
  title,
  diaValue,
  acValue,
  circleValue,
  bgColor,
  lineColor,
  progressColor,
}: {
  title: string;
  diaValue: number;
  acValue: number;
  circleValue: number;
  bgColor: string;
  lineColor: string;
  progressColor: string;
}) => {
  const data = generateMockData();

  return (
    <div
      className={` px-10 sm:px-6 lg:px-8 py-6 ${bgColor} rounded-xl shadow-sm relative`}
    >
      <div className="flex items-center justify-between mb-6">
        <div className="flex-1">
          <h3 className="text-gray-700 font-medium mb-4">{title}</h3>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <span className="text-sm text-gray-600">Dia</span>
              <div className="text-2xl font-bold text-gray-900">{diaValue}</div>
            </div>
            <div>
              <span className="text-sm text-gray-600">AC</span>
              <div className="text-2xl font-bold text-gray-900">{acValue}</div>
            </div>
          </div>
        </div>
        <div className="ml-4">
          <CircularProgress value={circleValue} color={progressColor} />
        </div>
      </div>
      <div className="h-16">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={data}>
            <Line
              type="monotone"
              dataKey="value"
              stroke={lineColor}
              strokeWidth={2}
              dot={false}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default function Home() {
  const [selectedHour, setSelectedHour] = useState<string>("");
  const [timeButtons, setTimeButtons] = useState<{ label: string; value: string }[]>([
    { label: "Todos", value: "" }
  ]);

  useEffect(() => {
    const carregarHorarios = async () => {
      try {
        const usuarioLocal = localStorage.getItem("usuario");
        if (!usuarioLocal) return;

        const usuarioData = JSON.parse(usuarioLocal);
        const userDoc = await getDoc(doc(db, "usuarios", usuarioData.uid));
        
        if (userDoc.exists()) {
          const userData = userDoc.data();
          const horarios = userData.horarios_afericao || [];
          
          const buttons = [
            { label: "Todos", value: "" },
            ...horarios.map((horario: string) => ({
              label: horario,
              value: horario
            }))
          ];
          
          setTimeButtons(buttons);
        }
      } catch (error) {
        console.error("Erro ao carregar horários:", error);
      }
    };

    carregarHorarios();
  }, []);

  return (
    <main className="p-4 sm:p-8 bg-gradient-to-t from-[#B4E4E2] to-[#E7F5F4] min-h-screen">
      <div className="max-w-7xl mx-auto">
        <div className="bg-white rounded-xl shadow-sm p-4 mb-4">
          <div className="flex items-center gap-2 mb-3">
            <h3 className="text-sm font-semibold text-gray-600 inline-flex items-center">
              <Eye className="w-4 h-4 mr-2" />
              Horário
            </h3>
          </div>
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

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <MetricCard
            title="Média Glicemia"
            diaValue={1000}
            acValue={500}
            circleValue={200}
            bgColor="bg-gradient-to-br from-green-50 to-white"
            lineColor="#10B981"
            progressColor="text-emerald-500"
          />
          <MetricCard
            title="Desvio Padrão"
            diaValue={1000}
            acValue={500}
            circleValue={59}
            bgColor="bg-gradient-to-br from-pink-50 to-white"
            lineColor="#EC4899"
            progressColor="text-pink-500"
          />
          <MetricCard
            title="CV Geral"
            diaValue={1000}
            acValue={500}
            circleValue={200}
            bgColor="bg-gradient-to-br from-blue-50 to-white"
            lineColor="#3B82F6"
            progressColor="text-blue-500"
          />
          <MetricCard
            title="HbA1c"
            diaValue={1000}
            acValue={500}
            circleValue={200}
            bgColor="bg-gradient-to-br from-yellow-50 to-white"
            lineColor="#F59E0B"
            progressColor="text-yellow-500"
          />
        </div>
      </div>
    </main>
  );
}
