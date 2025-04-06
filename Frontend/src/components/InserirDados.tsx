"use client";

import { useState } from "react";
import { db } from "@/config/firebase";
import { collection, addDoc, getDocs, query, where } from "firebase/firestore";
import { getAuth } from "firebase/auth";

export default function Form() {
  const [insulina, setInsulina] = useState("");
  const [glicemia, setGlicemia] = useState("");
  const [horario, setHorario] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      // Verifica se tem usuário logado
      const auth = getAuth();
      const user = auth.currentUser;

      if (!user) {
        alert("Usuário não encontrado. Por favor, faça login novamente.");
        return;
      }

      const usuarioData = JSON.parse(usuarioLocal);
      if (!usuarioData.uid) {
        alert("ID do usuário não encontrado. Por favor, faça login novamente.");
        return;
      }

      const hoje = new Date();
      hoje.setHours(0, 0, 0, 0);
      
      const medicoesRef = collection(db, "medicoes");
      const q = query(
        medicoesRef,
        where("userId", "==", user.uid),
        where("horario", "==", horario),
        where("timestamp", ">=", hoje.getTime())
      );
      
      const querySnapshot = await getDocs(q);
      if (!querySnapshot.empty) {
        alert("Já existe uma medição registrada para este horário hoje.");
        return;
      }

      const dadosMedicao = {
        userId: user.uid,
        insulina: Number(insulina),
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
        nomeUsuario: user.displayName || "Usuário"
      };

      console.log("Tentando salvar dados:", dadosMedicao);

      // Salva os dados
      const docRef = await addDoc(collection(db, "medicoes"), dadosMedicao);
      console.log("Documento salvo com ID:", docRef.id);

      alert("Dados salvos com sucesso!");
      
      // Limpa o formulário
      setInsulina("");
      setGlicemia("");
      setHorario("");
      
    } catch (error: Error | unknown) {
      console.error("Erro detalhado ao salvar dados:", error);
      const errorMessage = error instanceof Error ? error.message : "Tente novamente.";
      alert(`Erro ao salvar os dados: ${errorMessage}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <div className="mb-8 mt-8">
        <label className="block text-sm font-normal text-gray-700">Glicemia (mg/dL)</label>
        <input 
          type="number"
          value={glicemia}
          onChange={(e) => setGlicemia(e.target.value)}
          className="w-full p-2 bg-gray-200 rounded mt-1"
          required
          min="0"
          disabled={loading}
        />
      </div>

      <div className="mb-8">
        <label className="block text-sm font-normal text-gray-700">Insulina (UI)</label>
        <input 
          type="number"
          value={insulina}
          onChange={(e) => setInsulina(e.target.value)}
          className="w-full p-2 bg-gray-200 rounded mt-1"
          required
          min="0"
          disabled={loading}
        />
      </div>
      
      <div className="mb-6">
        <label className="block text-sm font-normal text-gray-700">Horário</label>
        <input 
          type="time"
          value={horario}
          onChange={(e) => setHorario(e.target.value)}
          className="w-full p-2 bg-gray-200 rounded mt-1"
          required
          disabled={loading}
        />
      </div>
      
      <button 
        type="submit" 
        className={`w-full bg-[#38B2AC] text-white py-2 rounded hover:bg-[#2C9A94] transition-colors ${loading ? 'opacity-50 cursor-not-allowed' : ''}`}
        disabled={loading}
      >
        {loading ? "Salvando..." : "Inserir"}
      </button>
    </form>
  );
}