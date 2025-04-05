"use client";

import Image from "next/image";
import { useState, useEffect } from "react";
import { db } from "@/config/firebase";
import { collection, addDoc, doc, getDoc, query, where, getDocs } from "firebase/firestore";
import { useRouter } from "next/navigation";
import { getAuth, onAuthStateChanged } from "firebase/auth";

interface MedicaoData {
  userId: string;
  glicemia: number;
  horario: string;
  data: Date;
  timestamp: number;
  nomeUsuario: string;
  insulina?: number;
  tipoInsulina?: string;
}

export default function InserirDados() {
  const router = useRouter();
  const [insulina, setInsulina] = useState("");
  const [tipoInsulina, setTipoInsulina] = useState("");
  const [glicemia, setGlicemia] = useState("");
  const [horario, setHorario] = useState("");
  const [horariosDisponiveis, setHorariosDisponiveis] = useState<string[]>([]);
  const [tiposInsulinaDisponiveis, setTiposInsulinaDisponiveis] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);
  const [userId, setUserId] = useState<string | null>(null);

  useEffect(() => {
    const auth = getAuth();
    
    const unsubscribe = onAuthStateChanged(auth, async (user) => {
      if (!user) {
        router.push("/login");
        return;
      }

      try {
        setUserId(user.uid);
        const userDoc = await getDoc(doc(db, "usuarios", user.uid));
        
        if (userDoc.exists()) {
          const userData = userDoc.data();
          setHorariosDisponiveis(userData.horarios_afericao || []);
          setTiposInsulinaDisponiveis(userData.tipoInsulina || []);
        } else {
          router.push("/login");
          return;
        }
      } catch (error) {
        console.error("Erro ao carregar dados:", error);
        alert("Erro ao carregar dados do usuário. Tente novamente.");
      } finally {
        setLoading(false);
      }
    });

    return () => unsubscribe();
  }, [router]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!userId) {
      alert("Usuário não encontrado. Por favor, faça login novamente.");
      router.push("/login");
      return;
    }

    if (!horario) {
      alert("Por favor, selecione um horário");
      return;
    }

    if (!glicemia) {
      alert("Por favor, preencha o campo de glicemia");
      return;
    }

    if (insulina && !tipoInsulina) {
      alert("Por favor, selecione o tipo de insulina");
      return;
    }

    try {
      // Verificar se já existe uma medição para este horário hoje
      const hoje = new Date();
      hoje.setHours(0, 0, 0, 0);
      
      const medicoesRef = collection(db, "medicoes");
      const q = query(
        medicoesRef,
        where("userId", "==", userId),
        where("horario", "==", horario)
      );
      
      const querySnapshot = await getDocs(q);
      const medicoesHoje = querySnapshot.docs.filter(doc => {
        const data = doc.data().data.toDate();
        return data >= hoje;
      });

      if (medicoesHoje.length > 0) {
        alert("Já existe uma medição registrada para este horário hoje");
        return;
      }

      // Salvar nova medição
      const auth = getAuth();
      const medicaoData: MedicaoData = {
        userId: userId,
        glicemia: Number(glicemia),
        horario,
        data: (() => {
          const hoje = new Date();
          const [hours, minutes] = horario.split(':');
          hoje.setHours(Number(hours), Number(minutes), 0, 0);
          return hoje;
        })(),
        timestamp: (() => {
          const hoje = new Date();
          const [hours, minutes] = horario.split(':');
          hoje.setHours(Number(hours), Number(minutes), 0, 0);
          return hoje.getTime();
        })(),
        nomeUsuario: auth.currentUser?.displayName || "Usuário"
      };

      // Adicionar insulina apenas se foi preenchida
      if (insulina) {
        medicaoData.insulina = Number(insulina);
        medicaoData.tipoInsulina = tipoInsulina;
      }

      await addDoc(collection(db, "medicoes"), medicaoData);

      alert("Dados inseridos com sucesso!");
      setInsulina("");
      setTipoInsulina("");
      setGlicemia("");
      setHorario("");
      router.push("/dashboard");
    } catch (error) {
      console.error("Erro ao salvar dados:", error);
      alert("Erro ao salvar os dados. Por favor, tente novamente.");
    }
  };

  if (loading) {
    return <div className="flex justify-center items-center h-screen">Carregando...</div>;
  }

  return (
    <div className="h-full px-4 sm:px-6 lg:px-8 py-6 bg-white relative overflow-hidden">
      <div className="flex h-full flex-col md:flex-row">
        <div className="w-full md:w-[40%] flex flex-col items-center md:items-start justify-center px-4 md:pl-8 py-8 z-10 relative">
          <h1 className="text-4xl md:text-5xl mb-6 md:mb-8 text-center md:text-left">
            Insira seus dados de hoje!
          </h1>
          <div className="w-full max-w-[200px] md:max-w-none">
            <Image
              src="/img/clock.png"
              alt="Relógio"
              width={300}
              height={200}
              className="w-full h-auto md:w-auto"
            />
          </div>
        </div>

        <div
          className="absolute right-[-80%] top-[30%] md:right-[-55%] md:top-0.5 w-[160%] h-[160%] md:w-[120%] md:h-[140%]"
          style={{
            backgroundColor: "#38B2AC",
            borderRadius: "100%",
          }}
        />

        <div className="w-full md:w-[65%] relative flex items-center justify-center py-8 md:py-0">
          <div className="bg-white p-6 md:p-8 rounded-2xl shadow-lg w-[85%] max-w-[400px] z-20">
            <div className="flex justify-center w-full mb-8">
              <Image
                src="/img/logo.png"
                alt="Logo"
                width={350}
                height={290}
                className="w-auto h-auto"
                priority
              />
            </div>

            <form className="px-4" onSubmit={handleSubmit}>
              <div className="mb-8">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Glicemia
                </label>
                <input
                  type="number"
                  value={glicemia}
                  onChange={(e) => setGlicemia(e.target.value)}
                  placeholder="Digite sua glicemia (mg/dL)"
                  className="w-full p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC] transition-all"
                  required
                />
              </div>

              <div className="mb-8">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Insulina (opcional)
                </label>
                <input
                  type="number"
                  value={insulina}
                  onChange={(e) => setInsulina(e.target.value)}
                  placeholder="Digite a quantidade de insulina"
                  className="w-full p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC] transition-all"
                />
              </div>

              {insulina && (
                <div className="mb-8">
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Tipo de Insulina
                  </label>
                  <select
                    value={tipoInsulina}
                    onChange={(e) => setTipoInsulina(e.target.value)}
                    className="w-full p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC] transition-all"
                    required={!!insulina}
                  >
                    <option value="">Selecione o tipo de insulina</option>
                    {tiposInsulinaDisponiveis.map((tipo) => (
                      <option key={tipo} value={tipo}>
                        {tipo}
                      </option>
                    ))}
                  </select>
                </div>
              )}

              <div className="mb-8">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Horário
                </label>
                <select
                  value={horario}
                  onChange={(e) => setHorario(e.target.value)}
                  className="w-full p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC] transition-all"
                  required
                >
                  <option value="">Selecione o horário</option>
                  {horariosDisponiveis.sort().map((h) => (
                    <option key={h} value={h}>
                      {h}
                    </option>
                  ))}
                </select>
              </div>

              <div className="mb-10">
                <button
                  type="submit"
                  className="w-full bg-[#38B2AC] text-white py-3 rounded-md hover:bg-[#2C9A94] transition-colors font-medium text-lg"
                >
                  Inserir
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}
