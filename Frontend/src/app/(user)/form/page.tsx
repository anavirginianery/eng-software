"use client";

import React, { useEffect, useState } from "react";
import KeyFilter from "@/components/KeyFilter";
import { getAuth, onAuthStateChanged } from "firebase/auth";
import { doc, getDoc } from "firebase/firestore";
import { db } from "@/config/firebase";

export default function Form() {
  const [showForm, setShowForm] = useState(false);
  const [cadastroCompleto, setCadastroCompleto] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const auth = getAuth();
    
    const unsubscribe = onAuthStateChanged(auth, async (user) => {
      if (user) {
        const userDoc = await getDoc(doc(db, "usuarios", user.uid));
        setCadastroCompleto(userDoc.data()?.cadastroCompleto || false);
      }
      setIsLoading(false);
    });

    return () => unsubscribe();
  }, []);

  if (isLoading) {
    return null;
  }

  if (showForm || cadastroCompleto) {
    return (
      <main>
        <div className="flex flex-col items-center justify-center min-h-screen px-4 sm:px-6 lg:px-8 py-6">
          <KeyFilter />
        </div>
      </main>
    );
  }

  return (
    <main>
      <div className="flex flex-col items-center justify-center min-h-screen px-4 sm:px-6 lg:px-8 py-6">
        <div className="bg-white rounded-lg shadow-lg p-8 w-full max-w-4xl mx-auto my-4">
          <h1 className="text-3xl font-bold text-gray-800 mb-6 text-center">
            Complete seu Perfil
          </h1>
          
          <div className="space-y-6 mb-8">
            <p className="text-lg text-gray-700">
              Para utilizar o sistema de forma eficiente, precisamos de algumas informações sobre você.
              Esses dados nos ajudarão a:
            </p>
            
            <ul className="list-disc pl-6 space-y-3 text-gray-700">
              <li>Personalizar recomendações específicas para o seu tipo de diabetes</li>
              <li>Estabelecer horários adequados para medição de glicemia</li>
              <li>Identificar possíveis comorbidades que podem afetar seu tratamento</li>
            </ul>
            
            <p className="text-lg text-gray-700">
              Todas as informações fornecidas são tratadas com confidencialidade e utilizadas
              exclusivamente para melhorar sua experiência com o sistema.
            </p>
          </div>
          
          <div className="flex justify-center">
            <button
              onClick={() => setShowForm(true)}
              className="px-6 py-3 bg-[#38B2AC] text-white rounded-md hover:bg-[#2C9A94] font-medium text-lg transition-colors"
            >
              Preencher Dados
            </button>
          </div>
        </div>
      </div>
    </main>
  );
}
