"use client";

import React, { useState, useEffect } from "react";
import { Eye } from "lucide-react";
import { LineChart, Line, ResponsiveContainer } from "recharts";
import { doc, getDoc } from "firebase/firestore";
import { db } from "@/config/firebase";
import { collection, query, where, getDocs } from "firebase/firestore";
import { getAuth } from "firebase/auth";
import { onAuthStateChanged } from "firebase/auth";

interface Registro {
  id: string;
  glicemia: number;
  insulina?: number;
  horario: string;
  timestamp: number;
}

interface ChartData {
  name: string;
  value: number;
}

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
  acValue,
  circleValue,
  bgColor,
  lineColor,
  progressColor,
  data,
}: {
  title: string;
  acValue: number;
  circleValue: number;
  bgColor: string;
  lineColor: string;
  progressColor: string;
  data: ChartData[];
}) => {
  return (
    <div
      className={`px-10 sm:px-6 lg:px-8 py-6 ${bgColor} rounded-xl shadow-sm relative`}
    >
      <div className="flex items-center justify-between mb-6">
        <div className="flex-1">
          <h3 className="text-gray-700 font-medium mb-4">{title}</h3>
          <div>
            <span className="text-sm text-gray-600">AC</span>
            <div className="text-2xl font-bold text-gray-900">{acValue}</div>
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
  const [registros, setRegistros] = useState<Registro[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const carregarDados = async () => {
      try {
        const auth = getAuth();
        
        // Wait for auth state to be initialized
        const unsubscribe = onAuthStateChanged(auth, async (user) => {
          if (!user) {
            console.error("Usuário não autenticado");
            return;
          }

          try {
            const userDoc = await getDoc(doc(db, "usuarios", user.uid));
            
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

              // Buscar registros de medição
              const medicoesRef = collection(db, "medicoes");
              const q = query(
                medicoesRef,
                where("userId", "==", user.uid)
              );

              const querySnapshot = await getDocs(q);
              const medicoes = querySnapshot.docs.map(doc => ({
                id: doc.id,
                glicemia: doc.data().glicemia,
                insulina: doc.data().insulina,
                horario: doc.data().horario,
                timestamp: doc.data().timestamp
              }));

              setRegistros(medicoes);
            }
          } catch (error) {
            console.error("Erro ao carregar dados:", error);
          } finally {
            setLoading(false);
          }
        });

        // Cleanup subscription
        return () => unsubscribe();
      } catch (error) {
        console.error("Erro ao carregar dados:", error);
        setLoading(false);
      }
    };

    carregarDados();
  }, []);

  const filtrarDados = () => {
    let dadosFiltrados = [...registros];
    
    if (selectedHour) {
      dadosFiltrados = dadosFiltrados.filter(registro => registro.horario === selectedHour);
    }

    return dadosFiltrados;
  };

  const calcularMediaGlicemia = () => {
    const dados = filtrarDados();
    if (dados.length === 0) return 0;
    const soma = dados.reduce((acc, curr) => acc + curr.glicemia, 0);
    return Math.round(soma / dados.length);
  };

  const calcularMediaInsulina = () => {
    const dados = filtrarDados();
    const dadosComInsulina = dados.filter((registro): registro is Registro & { insulina: number } => 
      registro.insulina !== undefined && registro.insulina !== null
    );
    if (dadosComInsulina.length === 0) return 0;
    const soma = dadosComInsulina.reduce((acc, curr) => acc + curr.insulina, 0);
    return Math.round(soma / dadosComInsulina.length);
  };

  const dadosGlicemia = filtrarDados().map(registro => ({
    name: new Date(registro.timestamp).toLocaleDateString('pt-BR'),
    value: registro.glicemia
  }));

  const dadosInsulina = filtrarDados()
    .filter((registro): registro is Registro & { insulina: number } => 
      registro.insulina !== undefined && registro.insulina !== null
    )
    .map(registro => ({
      name: new Date(registro.timestamp).toLocaleDateString('pt-BR'),
      value: registro.insulina
    }));

  if (loading) {
    return (
      <main className="p-4 sm:p-8 bg-gradient-to-t from-[#B4E4E2] to-[#E7F5F4] min-h-screen">
        <div className="flex items-center justify-center h-full">
          <div className="text-gray-600">Carregando dados...</div>
        </div>
      </main>
    );
  }

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
            acValue={calcularMediaGlicemia()}
            circleValue={calcularMediaGlicemia()}
            bgColor="bg-gradient-to-br from-green-50 to-white"
            lineColor="#10B981"
            progressColor="text-emerald-500"
            data={dadosGlicemia}
          />
          <MetricCard
            title="Média Insulina"
            acValue={calcularMediaInsulina()}
            circleValue={calcularMediaInsulina()}
            bgColor="bg-gradient-to-br from-blue-50 to-white"
            lineColor="#3B82F6"
            progressColor="text-blue-500"
            data={dadosInsulina}
          />
        </div>
      </div>
    </main>
  );
}
