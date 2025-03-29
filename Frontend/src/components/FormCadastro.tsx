"use client";

import React, { useState } from "react";
import Image from "next/image";
import { useRouter } from "next/navigation";

export default function FormCadastro() {
  const router = useRouter();
  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [confirmarSenha, setConfirmarSenha] = useState("");
  const [dataNascimento, setDataNascimento] = useState("");
  const [genero, setGenero] = useState("");

  const handleCadastro = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!email.includes("@")) {
      return alert("Por favor, insira um email válido.");
    }

    if (senha !== confirmarSenha) {
      return alert("As senhas não coincidem.");
    }

    try {
      const response = await fetch("/api/usuarios", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          nome,
          email,
          password: senha,
          dataNasc: dataNascimento,
          genero: genero.toUpperCase(),
        }),
      });

      if (!response.ok) {
        throw new Error("Erro na resposta da API");
      }

      const data = await response.json();
      localStorage.setItem("usuario", JSON.stringify(data));
      router.push("/dashboard");      
    } catch (error) {
      console.error("Erro ao cadastrar:", error);
      alert("Erro ao cadastrar. Verifique os dados ou tente novamente.");
    }
  };

  return (
    <div className="h-full px-4 sm:px-6 lg:px-8 py-6 flex items-center justify-center bg-gradient-to-t from-[#B4E4E2] to-[#E7F5F4]">
      <div className="bg-white rounded-3xl shadow-lg p-8 w-[400px]">
        <div className="flex flex-col items-center">
          <Image
            src="/img/logo.png"
            alt="Diabetter Logo"
            width={200}
            height={50}
            className="mb-2"
          />
        </div>

        <h2 className="mt-4 text-xl font-medium mb-4 text-center">
          Cadastre-se
        </h2>

        <form className="space-y-4" onSubmit={handleCadastro}>
          <div>
            <label className="block text-sm text-gray-800 mb-1">
              Nome Completo
            </label>
            <input
              type="text"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              className="w-full p-2.5 bg-gray-100 rounded-md border-none"
              required
            />
          </div>

          <div>
            <label className="block text-sm text-gray-800 mb-1">Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full p-2.5 bg-gray-100 rounded-md border-none"
              required
            />
          </div>

          <div>
            <label className="block text-sm text-gray-800 mb-1">
              Data de Nascimento
            </label>
            <input
              type="date"
              value={dataNascimento}
              onChange={(e) => setDataNascimento(e.target.value)}
              className="w-full p-2.5 bg-gray-100 rounded-md border-none"
              required
            />
          </div>

          <div>
            <label className="block text-sm text-gray-800 mb-1">Gênero</label>
            <select
              value={genero}
              onChange={(e) => setGenero(e.target.value)}
              className="w-full p-2.5 bg-gray-100 rounded-md border-none"
              required
            >
              <option value="">Selecione</option>
              <option value="MASCULINO">Masculino</option>
              <option value="FEMININO">Feminino</option>
              <option value="OUTRO">Outro</option>
              <option value="NAO_INFORMADO">Prefiro não dizer</option>
            </select>
          </div>

          <div>
            <label className="block text-sm text-gray-800 mb-1">Senha</label>
            <input
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
              className="w-full p-2.5 bg-gray-100 rounded-md border-none"
              required
            />
          </div>

          <div>
            <label className="block text-sm text-gray-800 mb-1">
              Confirme sua senha
            </label>
            <input
              type="password"
              value={confirmarSenha}
              onChange={(e) => setConfirmarSenha(e.target.value)}
              className="w-full p-2.5 bg-gray-100 rounded-md border-none"
              required
            />
          </div>

          <button
            type="submit"
            className="w-full p-2.5 bg-[#38B2AC] text-white rounded-md hover:bg-[#2C9A94] transition-colors cursor-pointer"
          >
            Cadastrar
          </button>
        </form>
      </div>
    </div>
  );
}
