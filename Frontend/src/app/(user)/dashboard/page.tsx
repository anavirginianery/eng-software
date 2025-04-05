"use client";

import logo from "../../../../public/img/logo.png";
import html2canvas from "html2canvas";
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
import { getAuth, onAuthStateChanged } from "firebase/auth";

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
  const [dadosPaciente, setDadosPaciente] = useState({
    nome: "",
    idade: "",
    sexo: "",
    peso: "",
    altura: "",
    tipo_diabetes: "",
    tipo_insulina: ""
  });

  useEffect(() => {
    const buscarDados = async () => {
      try {
        const auth = getAuth();
        
        // Wait for auth state to be initialized
        const unsubscribe = onAuthStateChanged(auth, async (user) => {
          if (!user) {
            console.error("Usuário não autenticado");
            return;
          }

          console.log("Dados do usuário:", user);
          
          // Buscar horários cadastrados do usuário
          try {
            const userDoc = await getDoc(doc(db, "usuarios", user.uid));
            console.log("Documento do usuário:", userDoc.exists() ? userDoc.data() : "Não encontrado");
            
            if (userDoc.exists()) {
              const userData = userDoc.data();
              const horarios = userData.horarios_afericao || [];
              setDadosPaciente({
                nome: userData.nome || "",
                idade: userData.dataNasc || "",
                sexo: userData.genero || "",
                peso: userData.peso || "",
                altura: userData.altura || "",
                tipo_diabetes: userData.tipoDiabetes || "",
                tipo_insulina: userData.tipoInsulina || ""
              });
              
              console.log("Horários encontrados:", horarios);
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

          console.log("Data inicial para busca:", dataInicial);

          // Buscar registros de medição
          const medicoesRef = collection(db, "medicoes");
          const q = query(
            medicoesRef,
            where("userId", "==", user.uid),
            where("timestamp", ">=", dataInicial.getTime())
          );

          console.log("Query de medições:", q);

          const querySnapshot = await getDocs(q);
          console.log("Número de documentos encontrados:", querySnapshot.size);
          
          const medicoes: RegistroMedicao[] = [];
          
          querySnapshot.forEach((doc) => {
            const data = doc.data();
            console.log("Dados do documento:", data);
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

          console.log("Medições processadas:", medicoes);
          setRegistros(medicoes);
          setLoading(false);
        });

        // Cleanup subscription
        return () => unsubscribe();
      } catch (error) {
        console.error("Erro detalhado ao buscar dados:", error);
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

  const exportarParaPDF = async () => {
    const doc = new jsPDF();
    const pageWidth = doc.internal.pageSize.getWidth();
    const pageHeight = doc.internal.pageSize.getHeight();
    const dataAtual = new Date().toLocaleDateString('pt-BR');
  
    doc.setFillColor(255, 255, 255);
    doc.rect(0, 0, pageWidth, pageHeight, 'F');

    const img = new Image();
    img.src = logo.src;
  
    await new Promise((resolve) => {
      img.onload = () => {
        const logoWidth = 50;
        const logoHeight = logoWidth * (img.height / img.width);
        const xLogo = (pageWidth - logoWidth) / 2;
        doc.addImage(img, 'PNG', xLogo, 10, logoWidth, logoHeight);
        resolve(null);
      };
    });
  
    let yOffset = 10 + 30;
  
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(18);
    doc.setTextColor(40, 40, 40);
    doc.text("Relatório de Medições", pageWidth / 2, yOffset, { align: "center" });
  
    yOffset += 10;
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(13);
    doc.text("Dados do Paciente", 20, yOffset);
  
    const boxStartY = yOffset + 4;
    const boxPadding = 4;
    const boxHeight = 60;
    doc.setDrawColor(180);
    doc.setLineWidth(0.2);
    doc.rect(18, boxStartY, pageWidth - 36, boxHeight);
  
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(11);
    const dados = dadosPaciente;
    const dadosPacienteFormatado = [
      `Nome: ${dados.nome}`,
      `Idade: ${dados.idade}`,
      `Sexo: ${dados.sexo}`,
      `Peso: ${dados.peso} kg`,
      `Altura: ${dados.altura} cm`,
      `Tipo de Diabetes: ${dados.tipo_diabetes}`,
      `Tipo de Insulina: ${dados.tipo_insulina}`,
      `Data de Geração: ${dataAtual}`,
      `Período: ${selectedTime.charAt(0).toUpperCase() + selectedTime.slice(1)}`,
      selectedHour ? `Horário: ${selectedHour}` : null
    ].filter(Boolean);
  
    let currentY = boxStartY + boxPadding;
    dadosPacienteFormatado.forEach((linha) => {
      doc.text(linha!, 22, currentY);
      currentY += 6;
    });
  
    yOffset = boxStartY + boxHeight + 10;
  
    doc.setDrawColor(100);
    doc.line(18, yOffset, pageWidth - 18, yOffset);
    yOffset += 8;
  
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(13);
    doc.text("Gráfico de Medições", 20, yOffset);
  
    yOffset += 6;
    const graficoElement = document.getElementById("graficoContainer");
  
    if (graficoElement) {
      const canvas = await html2canvas(graficoElement, {
        scale: 3,
        useCORS: true
      });
  
      const imgData = canvas.toDataURL("image/png");
      const imgWidth = pageWidth - 30;
      const aspectRatio = canvas.width / canvas.height;
      const maxHeight = pageHeight - yOffset - 30;
      const imgHeight = Math.min(imgWidth / aspectRatio, maxHeight);
      const xOffset = (pageWidth - imgWidth) / 2;
  
      doc.addImage(imgData, "PNG", xOffset, yOffset, imgWidth, imgHeight);
      yOffset += imgHeight + 20;
    }
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(13);
    doc.text("Histórico de Registros de Medições", 20, yOffset);
    yOffset += 8;

    doc.setFont('helvetica', 'normal');
    doc.setFontSize(11);

    dadosGrafico.forEach((dado) => {
      if (yOffset > pageHeight - 40) {
        doc.addPage();
        yOffset = 20;
      }

      const boxHeight = 18;
      doc.setFillColor(245, 245, 245);
      doc.roundedRect(18, yOffset, pageWidth - 36, boxHeight, 2, 2, 'F');

      doc.setTextColor(30, 30, 30);
      doc.text(`Data: ${dado.name}`, 22, yOffset + 6);
      doc.text(`Glicemia: ${dado.glicemia} mg/dL`, 22, yOffset + 12);
      doc.text(`Insulina: ${dado.insulina} UI`, 90, yOffset + 12);

      yOffset += boxHeight + 6;
    });
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
              <div id="graficoContainer" className="flex-1 min-h-0">
                <ResponsiveContainer width="100%" height="100%">
                  <LineChart
                    data={dadosGrafico}
                    margin={{ top: 10, right: 50, left: 0, bottom: 0 }}
                  >
                    <CartesianGrid strokeDasharray="3 3" stroke="#E5E7EB" />
                    <XAxis
                      dataKey="name"
                      stroke="#4B5563"
                      tick={{ fill: "#4B5563" }}
                      fontSize={12}
                    />
                    <YAxis
                      yAxisId="glicemia"
                      stroke="#38B2AC"
                      tick={{ fill: "#4B5563" }}
                      domain={[0, 400]}
                      ticks={[0, 100, 200, 300, 400]}
                      fontSize={12}
                      label={{ value: 'Glicemia (mg/dL)', angle: -90, position: 'insideLeft' }}
                    />
                    <YAxis
                      yAxisId="insulina"
                      orientation="right"
                      stroke="#F59E0B"
                      tick={{ fill: "#4B5563" }}
                      domain={[0, 20]}
                      ticks={[0, 5, 10, 15, 20]}
                      fontSize={12}
                      label={{ value: 'Insulina (UI)', angle: 90, position: 'insideRight' }}
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
                      yAxisId="glicemia"
                      connectNulls={true}
                    />
                    <Line
                      type="monotone"
                      dataKey="insulina"
                      stroke="#F59E0B"
                      strokeWidth={2}
                      dot={{ fill: "#F59E0B", strokeWidth: 2, r: 3 }}
                      activeDot={{ r: 5 }}
                      yAxisId="insulina"
                      connectNulls={true}
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
