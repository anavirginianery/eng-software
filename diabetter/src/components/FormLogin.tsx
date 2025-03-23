"use client";
import React, { useState } from "react";
import { InputText } from "primereact/inputtext";
import { FloatLabel } from "primereact/floatlabel";
import { Button } from "primereact/button";
import Image from "next/image";
import Link from "next/link";

export default function BasicDemo() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  return (
    <div className="flex justify-center items-center min-h-screen bg-gradient-to-b from-teal-200 to-white">
      <div className="bg-white p-8 rounded-lg shadow-lg w-96 border border-teal-500">
        <div className="flex justify-center mb-6">
          <Image
            src={"/img/logo.png"}
            alt="Diabetter Logo"
            width={150}
            height={50}
          />
        </div>
        <h2 className="text-center text-xl font-bold mb-6">Cadastre-se</h2>

        <div className="mb-6">
          <FloatLabel>
            <InputText
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="w-full p-3"
            />
            <label htmlFor="username">Nome Completo</label>
          </FloatLabel>
        </div>

        <div className="mb-6">
          <FloatLabel>
            <InputText
              id="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full p-3"
            />
            <label htmlFor="email">Email</label>
          </FloatLabel>
        </div>

        <div className="mb-6">
          <FloatLabel>
            <InputText
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-3"
            />
            <label htmlFor="password">Senha</label>
          </FloatLabel>
        </div>

        <div className="mb-6">
          <FloatLabel>
            <InputText
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="w-full p-3"
            />
            <label htmlFor="confirmPassword">Confirme sua senha</label>
          </FloatLabel>
        </div>
        <Button
          label="Entrar com a conta Google"
          icon="pi pi-google"
          className="w-full mb-4 p-button-outlined p-button-secondary p-3"
        />
        <Link href="/infocadastro">
          <Button label="Salvar" className="w-full bg-teal-600 text-white p-3" />
        </Link>

        
      </div>
    </div>
  );
}
