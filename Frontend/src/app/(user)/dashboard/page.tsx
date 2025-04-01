"use client";

import React, { useState, useEffect } from "react";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import { Eye, Download } from "lucide-react";
import { db } from "@/config/firebase";
import { collection, query, where, getDocs, doc, getDoc } from "firebase/firestore";
import jsPDF from "jspdf";

type TimeFrame = "dia" | "semana" | "mês" | "ano" | "geral";

interface RegistroMedicao {
  id: string;
  userId: string;
  insulina: number;
  glicemia: number;
  horario: string;
  data: Date;
  timestamp: number;
  nomeUsuario: string;
}

interface HorarioCadastrado {
  horario: string;
}

const timeFrames: TimeFrame[] = ["dia", "semana", "mês", "ano", "geral"];

export default function Dashboard() {
  const [selectedTime, setSelectedTime] = useState<TimeFrame>("semana");
  const [selectedHour, setSelectedHour] = useState<string>("");
  const [registros, setRegistros] = useState<RegistroMedicao[]>([]);
  const [horariosCadastrados, setHorariosCadastrados] = useState<HorarioCadastrado[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const buscarDados = async () => {
      try {
        const usuarioLocal = localStorage.getItem("usuario");
        if (!usuarioLocal) {
          console.error("Usuário não encontrado no localStorage");
          alert("Usuário não encontrado. Por favor, faça login novamente.");
          return;
        }

        const usuarioData = JSON.parse(usuarioLocal);
        
        if (!usuarioData.uid) {
          console.error("UID do usuário não encontrado");
          alert("ID do usuário não encontrado. Por favor, faça login novamente.");
          return;
        }

        // Buscar horários cadastrados do usuário
        try {
          const userDoc = await getDoc(doc(db, "usuarios", usuarioData.uid));
          
          if (userDoc.exists()) {
            const userData = userDoc.data();
            const horarios = userData.horarios_afericao || [];
            setHorariosCadastrados(horarios.map((h: string) => ({ horario: h })));
          } else {
            console.warn("Documento do usuário não encontrado no Firestore");
            setHorariosCadastrados([]);
          }
        } catch (error) {
          console.error("Erro ao buscar dados do usuário:", error);
          setHorariosCadastrados([]);
        }

        // Calcular data inicial baseado no período selecionado
        const hoje = new Date();
        let dataInicial = new Date();
        
        switch (selectedTime) {
          case "dia":
            dataInicial.setHours(0, 0, 0, 0);
            break;
          case "semana":
            dataInicial.setDate(hoje.getDate() - 7);
            break;
          case "mês":
            dataInicial.setMonth(hoje.getMonth() - 1);
            break;
          case "ano":
            dataInicial.setFullYear(hoje.getFullYear() - 1);
            break;
          case "geral":
            dataInicial = new Date(0); // Data inicial do timestamp
            break;
        }


        // Buscar registros de medição
        const medicoesRef = collection(db, "medicoes");
        const q = query(
          medicoesRef,
          where("userId", "==", usuarioData.uid),
          where("timestamp", ">=", dataInicial.getTime())
        );


        const querySnapshot = await getDocs(q);
        
        const medicoes: RegistroMedicao[] = [];
        
        querySnapshot.forEach((doc) => {
          const data = doc.data();
          try {
            medicoes.push({
              id: doc.id,
              userId: data.userId,
              insulina: data.insulina,
              glicemia: data.glicemia,
              horario: data.horario,
              data: data.data.toDate(),
              timestamp: data.timestamp,
              nomeUsuario: data.nomeUsuario
            });
          } catch (error) {
            console.error("Erro ao processar documento:", doc.id, error);
          }
        });

        setRegistros(medicoes);
      } catch (error) {
        console.error("Erro detalhado ao buscar dados:", error);
        alert(`Erro ao carregar os dados do dashboard: ${error instanceof Error ? error.message : 'Erro desconhecido'}`);
      } finally {
        setLoading(false);
      }
    };

    buscarDados();
  }, [selectedTime]);

  const filtrarDados = () => {
    let dadosFiltrados = [...registros];
    
    if (selectedHour) {
      dadosFiltrados = dadosFiltrados.filter(registro => registro.horario === selectedHour);
    }

    // Ordenar por data
    dadosFiltrados.sort((a, b) => a.timestamp - b.timestamp);

    return dadosFiltrados;
  };

  const dadosGrafico = filtrarDados().map(registro => ({
    name: registro.data.toLocaleDateString('pt-BR', { 
      day: '2-digit',
      month: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    }),
    glicemia: registro.glicemia,
    insulina: registro.insulina
  }));

  const exportarParaPDF = () => {
    const doc = new jsPDF();
    const pageWidth = doc.internal.pageSize.getWidth();
    const pageHeight = doc.internal.pageSize.getHeight();
    
    // Título
    doc.setFontSize(20);
    doc.text("Relatório de Medições", pageWidth / 2, 20, { align: "center" });
    
    // Período selecionado
    doc.setFontSize(12);
    doc.text(`Período: ${selectedTime.charAt(0).toUpperCase() + selectedTime.slice(1)}`, 20, 30);
    
    // Horário selecionado
    if (selectedHour) {
      doc.text(`Horário: ${selectedHour}`, 20, 40);
    }
    
    // Data de geração
    const dataAtual = new Date().toLocaleDateString('pt-BR');
    doc.text(`Gerado em: ${dataAtual}`, 20, 50);
    
    // Dados
    doc.setFontSize(10);
    let yPos = 70;
    
    dadosGrafico.forEach((dado, index) => {
      if (yPos > pageHeight - 20) {
        doc.addPage();
        yPos = 20;
      }
      
      doc.text(`Data: ${dado.name}`, 20, yPos);
      doc.text(`Glicemia: ${dado.glicemia} mg/dL`, 20, yPos + 7);
      doc.text(`Insulina: ${dado.insulina} UI`, 20, yPos + 14);
      
      yPos += 30;
    });
    
    // Salvar o PDF
    doc.save(`relatorio-medicoes-${dataAtual}.pdf`);
  };

  if (loading) {
    return (
      <main className="px-4 sm:px-6 lg:px-8 py-6 bg-gradient-to-t from-[#B4E4E2] to-[#E7F5F4] min-h-screen">
        <div className="flex items-center justify-center h-full">
          <div className="text-gray-600">Carregando dados...</div>
        </div>
      </main>
    );
  }

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
              <div className="flex items-center justify-between mb-2">
                <div className="w-full overflow-x-auto">
                  <div className="flex gap-2 min-w-max">
                    <button
                      onClick={() => setSelectedHour("")}
                      className={`px-3 py-1.5 text-sm font-medium transition-colors rounded-md min-w-[70px]
                        ${
                          selectedHour === ""
                            ? "bg-[#337F7B] text-white"
                            : "bg-gray-100 hover:bg-gray-200 text-gray-700"
                        }`}
                    >
                      Todos
                    </button>
                    {horariosCadastrados.map((horario) => (
                      <button
                        key={horario.horario}
                        onClick={() => setSelectedHour(horario.horario)}
                        className={`px-3 py-1.5 text-sm font-medium transition-colors rounded-md min-w-[70px]
                          ${
                            selectedHour === horario.horario
                              ? "bg-[#337F7B] text-white"
                              : "bg-gray-100 hover:bg-gray-200 text-gray-700"
                          }`}
                      >
                        {horario.horario}
                      </button>
                    ))}
                  </div>
                </div>
                <button
                  onClick={exportarParaPDF}
                  className="ml-4 inline-flex items-center px-4 py-2 text-sm font-medium text-white bg-[#337F7B] rounded-md hover:bg-[#2A6A67] transition-colors"
                >
                  <Download className="w-4 h-4 mr-2" />
                  Exportar
                </button>
              </div>

              {/* Chart Section */}
              <div className="flex-1 min-h-0">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart
                    data={dadosGrafico}
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
                    <Line
                      type="monotone"
                      dataKey="insulina"
                      stroke="#F59E0B"
                      strokeWidth={2}
                      dot={{ fill: "#F59E0B", strokeWidth: 2, r: 3 }}
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
