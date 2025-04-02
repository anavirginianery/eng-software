"use client";

import React, { useState } from "react";
import Image from "next/image";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { login } from "../services/authService";

export default function FormLogin() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const router = useRouter();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!email.includes("@")) {
      return alert("Por favor, insira um email válido");
    }

    try {
      const user = await login(email, password);
      if (user) {
        localStorage.setItem("usuario", JSON.stringify({
          uid: user.uid,
          email: user.email,
          displayName: user.displayName
        }));
        router.push("/home");
      }
    } catch (error: any) {
      console.error("Erro ao fazer login:", error);
      alert("Email ou senha inválidos. Tente novamente.");
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
          />
        </div>

        <h2 className="mt-4 text-xl font-medium mb-4 text-center">Login</h2>

        <form className="space-y-4" onSubmit={handleLogin}>
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

          <div className="mb-2">
            <label className="block text-sm text-gray-800 mb-1">Senha</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-2.5 bg-gray-100 rounded-md border-none"
              required
            />
          </div>

          <div className="text-center mb-8">
            <Link
              href="/esqueci-senha"
              className="text-sm text-gray-600 hover:text-gray-800"
            >
              Esqueci minha senha
            </Link>
          </div>

          <button
            type="submit"
            className="w-full p-2.5 bg-[#38B2AC] text-white rounded-md hover:bg-[#2C9A94] transition-colors cursor-pointer"
          >
            Entrar
          </button>
        </form>
      </div>
    </div>
  );
}
