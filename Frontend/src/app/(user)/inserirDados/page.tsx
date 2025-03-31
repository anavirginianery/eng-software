"use client";

import Image from "next/image";
import { useState } from "react";

export default function InserirDados() {
  const [quantidade, setQuantidade] = useState("");
  const [horario, setHorario] = useState("");
  const [glicemia, setGlicemia] = useState("");


  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const usuario = JSON.parse(localStorage.getItem("usuario") || "{}");

    if (!usuario.id) {
      alert("Usuário não encontrado. Faça login novamente.");
      return;
    }

    try {
      const response = await fetch("/api/api/insulin", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          quantidade: parseFloat(quantidade),
          horario,
          usuarioId: usuario.id,
        }),
      });

      if (!response.ok) {
        throw new Error("Erro ao inserir insulina");
      }

      alert("Dados inseridos com sucesso!");
      setQuantidade("");
      setHorario("");
    } catch (error) {
      console.error(error);
      alert("Erro ao enviar dados. Tente novamente.");
    }
  };

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
                  Insulina
                </label>
                <input
                  type="number"
                  value={quantidade}
                  onChange={(e) => setQuantidade(e.target.value)}
                  placeholder="Digite a quantidade de insulina"
                  className="w-full p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC] transition-all"
                  required
                />
              </div>

              <div className="mb-8">
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Horário
                </label>
                <input
                  type="time"
                  value={horario}
                  onChange={(e) => setHorario(e.target.value)}
                  className="w-full p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC] transition-all"
                  required
                />
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
