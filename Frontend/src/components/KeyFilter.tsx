"use client";

import React, { useEffect, useState } from "react";
import { doc, setDoc, getDoc } from "firebase/firestore";
import { db } from "@/config/firebase";
import { getAuth } from "firebase/auth";

export default function KeyFilter() {

  const [formData, setFormData] = useState({
    peso: "",
    altura: "",
    tipoDiabetes: "",
    tipoInsulina: [] as string[],
    comorbidades: [] as string[],
    horarios: [] as string[]
  });

  const [novoHorario, setNovoHorario] = useState("");
  const [perfilSalvo, setPerfilSalvo] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    carregarDadosPerfil();
  }, []);

  const carregarDadosPerfil = async () => {
    try {
      const auth = getAuth();
      const user = auth.currentUser;
      
      if (!user) {
        alert("Usuário não encontrado");
        return;
      }

      const userDoc = await getDoc(doc(db, "usuarios", user.uid));
      
      if (userDoc.exists()) {
        const userData = userDoc.data();
        setFormData({
          peso: userData.peso || "",
          altura: userData.altura || "",
          tipoDiabetes: userData.tipoDiabetes || "",
          tipoInsulina: Array.isArray(userData.tipoInsulina) ? userData.tipoInsulina : [],
          comorbidades: Array.isArray(userData.comorbidades) ? userData.comorbidades : [],
          horarios: Array.isArray(userData.horarios_afericao) ? userData.horarios_afericao : []
        });
        setPerfilSalvo(true);
      }
      setLoading(false);
    } catch (error) {
      console.error("Erro ao carregar dados:", error);
      alert("Erro ao carregar dados do perfil");
      setLoading(false);
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleAddHorario = () => {
    if (!novoHorario) {
      alert("Por favor, insira um horário");
      return;
    }

    if (formData.horarios.includes(novoHorario)) {
      alert("Este horário já foi adicionado");
      return;
    }

    setFormData(prev => ({
      ...prev,
      horarios: [...prev.horarios, novoHorario].sort()
    }));
    setNovoHorario("");
  };

  const handleRemoveHorario = (horario: string) => {
    setFormData(prev => ({
      ...prev,
      horarios: prev.horarios.filter(h => h !== horario)
    }));
  };

  const handleInsulinaChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      tipoInsulina: checked 
        ? [...prev.tipoInsulina, value]
        : prev.tipoInsulina.filter(i => i !== value)
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    try {
      const auth = getAuth();
      const user = auth.currentUser;
      
      if (!user) {
        alert("Usuário não encontrado");
        return;
      }
      
      await setDoc(doc(db, "usuarios", user.uid), {
        peso: formData.peso,
        altura: formData.altura,
        tipoDiabetes: formData.tipoDiabetes,
        tipoInsulina: formData.tipoInsulina,
        comorbidades: formData.comorbidades,
        horarios_afericao: formData.horarios,
        cadastroCompleto: true
      }, { merge: true });

      alert("Perfil salvo com sucesso!");
      setPerfilSalvo(true);
      setEditMode(false);
    } catch (error) {
      console.error("Erro ao salvar perfil:", error);
      alert("Erro ao salvar o perfil");
    }
  };

  if (loading) {
    return <div className="flex justify-center items-center h-full">Carregando...</div>;
  }

  if (perfilSalvo && !editMode) {
    return (
      <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-4xl mx-auto my-4">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-bold text-gray-800">Seu Perfil</h2>
          <button
            onClick={() => setEditMode(true)}
            className="text-[#38B2AC] hover:text-[#2C9A94] font-medium"
          >
            Editar Perfil
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <p className="text-sm font-medium text-gray-600 mb-1">Peso</p>
            <p className="text-lg text-gray-800">{formData.peso} kg</p>
          </div>
          <div>
            <p className="text-sm font-medium text-gray-600 mb-1">Altura</p>
            <p className="text-lg text-gray-800">{formData.altura} cm</p>
          </div>
          <div>
            <p className="text-sm font-medium text-gray-600 mb-1">Tipo de Diabetes</p>
            <p className="text-lg text-gray-800">{formData.tipoDiabetes || "-"}</p>
          </div>
          <div>
            <p className="text-sm font-medium text-gray-600 mb-1">Tipo de Insulina</p>
            <div className="flex flex-wrap gap-2">
              {formData.tipoInsulina.map((insulina) => (
                <span
                  key={insulina}
                  className="bg-[#38B2AC] text-white px-3 py-1 rounded-full text-sm"
                >
                  {insulina}
                </span>
              ))}
            </div>
          </div>
        </div>

        <div className="mt-6">
          <p className="text-sm font-medium text-gray-600 mb-2">Horários de Medição</p>
          <div className="flex flex-wrap gap-2">
            {formData.horarios.map((horario) => (
              <span
                key={horario}
                className="bg-[#38B2AC] text-white px-3 py-1 rounded-full text-sm"
              >
                {horario}
              </span>
            ))}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-4xl mx-auto my-4">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">
        {editMode ? "Editar Perfil" : "Complete seu Perfil"}
      </h2>

      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Peso (kg)
            </label>
            <input
              type="number"
              name="peso"
              value={formData.peso}
              onChange={handleInputChange}
              placeholder="Digite seu peso"
              className="w-full p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC]"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Altura (cm)
            </label>
            <input
              type="number"
              name="altura"
              value={formData.altura}
              onChange={handleInputChange}
              placeholder="Digite sua altura"
              className="w-full p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC]"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Tipo de Diabetes
            </label>
            <select
              name="tipoDiabetes"
              value={formData.tipoDiabetes}
              onChange={handleInputChange}
              className="w-full p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC]"
              required
            >
              <option value="">Selecione o tipo</option>
              <option value="Tipo 1">Tipo 1</option>
              <option value="Tipo 2">Tipo 2</option>
              <option value="Gestacional">Gestacional</option>
              <option value="LADA">LADA</option>
              <option value="MODY">MODY</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Tipo de Insulina
            </label>
            <div className="grid grid-cols-2 gap-2">
              <label className="flex items-center space-x-2 p-2 bg-[#E5E5E5] rounded-md">
                <input
                  type="checkbox"
                  value="Ultrarrápida"
                  checked={formData.tipoInsulina.includes("Ultrarrápida")}
                  onChange={handleInsulinaChange}
                  className="h-4 w-4 text-[#38B2AC] focus:ring-[#38B2AC]"
                />
                <span>Ultrarrápida</span>
              </label>
              <label className="flex items-center space-x-2 p-2 bg-[#E5E5E5] rounded-md">
                <input
                  type="checkbox"
                  value="Rápida"
                  checked={formData.tipoInsulina.includes("Rápida")}
                  onChange={handleInsulinaChange}
                  className="h-4 w-4 text-[#38B2AC] focus:ring-[#38B2AC]"
                />
                <span>Rápida</span>
              </label>
              <label className="flex items-center space-x-2 p-2 bg-[#E5E5E5] rounded-md">
                <input
                  type="checkbox"
                  value="Intermediária"
                  checked={formData.tipoInsulina.includes("Intermediária")}
                  onChange={handleInsulinaChange}
                  className="h-4 w-4 text-[#38B2AC] focus:ring-[#38B2AC]"
                />
                <span>Intermediária</span>
              </label>
              <label className="flex items-center space-x-2 p-2 bg-[#E5E5E5] rounded-md">
                <input
                  type="checkbox"
                  value="Lenta"
                  checked={formData.tipoInsulina.includes("Lenta")}
                  onChange={handleInsulinaChange}
                  className="h-4 w-4 text-[#38B2AC] focus:ring-[#38B2AC]"
                />
                <span>Lenta</span>
              </label>
              <label className="flex items-center space-x-2 p-2 bg-[#E5E5E5] rounded-md">
                <input
                  type="checkbox"
                  value="Basal"
                  checked={formData.tipoInsulina.includes("Basal")}
                  onChange={handleInsulinaChange}
                  className="h-4 w-4 text-[#38B2AC] focus:ring-[#38B2AC]"
                />
                <span>Basal</span>
              </label>
              <label className="flex items-center space-x-2 p-2 bg-[#E5E5E5] rounded-md">
                <input
                  type="checkbox"
                  value="Mista"
                  checked={formData.tipoInsulina.includes("Mista")}
                  onChange={handleInsulinaChange}
                  className="h-4 w-4 text-[#38B2AC] focus:ring-[#38B2AC]"
                />
                <span>Mista</span>
              </label>
            </div>
          </div>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Horários de Medição
          </label>
          <div className="flex gap-2 mb-2">
            <input
              type="time"
              value={novoHorario}
              onChange={(e) => setNovoHorario(e.target.value)}
              className="flex-1 p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC]"
            />
            <button
              type="button"
              onClick={handleAddHorario}
              className="px-4 py-2 bg-[#38B2AC] text-white rounded-md hover:bg-[#2C9A94]"
            >
              Adicionar
            </button>
          </div>
          
          <div className="flex flex-wrap gap-2">
            {formData.horarios.map((horario) => (
              <div
                key={horario}
                className="bg-[#E5E5E5] text-gray-800 px-3 py-1 rounded-full flex items-center gap-2"
              >
                <span>{horario}</span>
                <button
                  type="button"
                  onClick={() => handleRemoveHorario(horario)}
                  className="text-red-500 hover:text-red-700"
                >
                  ×
                </button>
              </div>
            ))}
          </div>
        </div>

        <div className="flex justify-end gap-4">
          {editMode && (
            <button
              type="button"
              onClick={() => {
                setEditMode(false);
                carregarDadosPerfil();
              }}
              className="px-6 py-2 text-gray-600 hover:text-gray-800"
            >
              Cancelar
            </button>
          )}
          <button
            type="submit"
            className="px-6 py-2 bg-[#38B2AC] text-white rounded-md hover:bg-[#2C9A94]"
          >
            {editMode ? "Salvar Alterações" : "Salvar Perfil"}
          </button>
        </div>
      </form>
    </div>
  );
}