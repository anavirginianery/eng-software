"use client";

import FormCadastro from "@/components/FormCadastro";
import Header from "@/components/Header";
import { Footer } from "@/components/Footer";
import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { getAuth, onAuthStateChanged } from "firebase/auth";

export default function Cadastro() {
  const router = useRouter();

  useEffect(() => {
    const auth = getAuth();
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      if (user) {
        router.push("/home");
      }
    });

    return () => unsubscribe();
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
