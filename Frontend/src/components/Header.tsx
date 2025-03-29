"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import Image from "next/image";
import { useRouter } from "next/navigation";

export default function Header() {
  const [usuario, setUsuario] = useState<string | null>(null);
  const router = useRouter();

  useEffect(() => {
    const dados = localStorage.getItem("usuario");
    if (dados) {
      const { nome } = JSON.parse(dados);
      const primeiroNome = nome.split(" ")[0];
      setUsuario(primeiroNome);
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("usuario");
    router.push("/login");
  };

  return (
    <header className="w-full bg-white shadow p-4">
      <div className="max-w-7xl mx-auto flex justify-between items-center">
        <Link href="/">
          <Image
            src="/img/logo.png"
            alt="Logo"
            width={96}
            height={96}
            unoptimized
            className="h-8 w-auto"
          />
        </Link>
        <div>
          {usuario ? (
            <div className="flex items-center gap-4">
              <span className="text-teal-600 font-medium">Olá, {usuario}!</span>
              <button
                onClick={handleLogout}
                className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600 transition"
              >
                Sair
              </button>
            </div>
          ) : (
            <>
              <Link href="/login" className="text-teal-600 mx-2 mr-4">
                Login
              </Link>
              <Link
                href="/cadastro"
                className="bg-teal-600 text-white px-4 py-2 rounded"
              >
                Cadastrar
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
