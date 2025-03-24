"use client";
import React, { useState } from "react";
import Image from "next/image";
import Link from "next/link";

export default function FormLogin() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-t from-[#B4E4E2] to-[#E7F5F4]">
      <div className="bg-white rounded-3xl shadow-lg p-8 w-[400px]">
        <div className="flex flex-col items-center mb-8">
          <Image
            src="/img/logo.png"
            alt="Diabetter Logo"
            width={200}
            height={50}
            className="mb-2"
          />
          <span className="text-gray-500 text-sm">Keep your glucose under control</span>
        </div>

        <h2 className="text-2xl font-medium mb-6 text-center">Login</h2>

        <form className="space-y-4">
          <div>
            <label className="block text-sm text-gray-800 mb-1">Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full p-2.5 bg-gray-100 rounded-md border-none"
            />
          </div>

          <div className="mb-2">
            <label className="block text-sm text-gray-800 mb-1">Senha</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-2.5 bg-gray-100 rounded-md border-none"
            />
          </div>

          <div className="text-center mb-8">
            <Link href="/esqueci-senha" className="text-sm text-gray-600 hover:text-gray-800">
              Esqueci minha senha
            </Link>
          </div>

          <button
            type="submit"
            className="w-full p-2.5 bg-[#38B2AC] text-white rounded-md hover:bg-[#2C9A94] transition-colors"
          >
            Entrar
          </button>
        </form>
      </div>
    </div>
  );
}
