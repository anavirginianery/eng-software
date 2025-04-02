"use client";

import FormCadastro from "@/components/FormCadastro";
import Header from "@/components/Header";
import { Footer } from "@/components/Footer";
import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function Cadastro() {
  const router = useRouter();

  useEffect(() => {
    const usuario = localStorage.getItem("usuario");
    if (usuario) {
      router.push("/home");
    }
  }, [router]);
  
  return (
    <main>
      <Header />
      <div className="h-full">
        <FormCadastro />
      </div>
      <Footer />
    </main>
  );
}
