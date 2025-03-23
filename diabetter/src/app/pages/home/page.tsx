import { useState } from "react";
import Image from "next/image";
import phoneImage from "@/app/img/phone.png";
import tabela from "@/app/img/FAVCON tabela.png";
import fav01 from "@/app/img/favcon01.png";
import fav03 from "@/app/img/favcon03.png";

export default function Home() {
  const [showFeatures, setShowFeatures] = useState(false);

  return (
    <main className={`bg-gray-100 min-h-screen flex flex-col items-center ${!showFeatures ? "justify-center" : ""}`}>
      <section className="w-full max-w-3xl p-6 text-center flex flex-col md:flex-row items-center md:items-start">
        <div className="md:w-1/3 flex justify-center">
          <Image src={phoneImage} alt="Monitoramento de glicose" width={150} height={150} className="w-40" />
        </div>
        <div className="md:w-2/3 flex flex-col items-center md:items-start text-center md:text-left">
          <h1 className="text-3xl font-bold">
            Keep your glucose <span className="text-teal-600">under control</span>
          </h1>
          <p className="mt-4 text-gray-600">
            Plataforma intuitiva para pessoas com diabetes registrarem suas taxas de glicose e dados de saúde essenciais, simplificando o monitoramento diário.
          </p>
          <div className="mt-6 flex flex-col md:flex-row md:space-x-4 w-full justify-center md:justify-start">
            <button className="bg-teal-600 text-white px-6 py-2 rounded w-full md:w-auto">
              Começar agora
            </button>
            <button
              className="border border-teal-600 text-teal-600 px-6 py-2 rounded w-full md:w-auto mt-2 md:mt-0"
              onClick={() => setShowFeatures(true)}
            >
              Saiba Mais
            </button>
          </div>
        </div>
      </section>

      {showFeatures && (
        <section className="w-full max-w-3xl p-6">
          <h2 className="text-2xl font-bold text-center">Principais Funcionalidades</h2>
          <div className="mt-6 grid grid-cols-1 gap-4">
            {[ 
              { image: fav01, title: "Registros Detalhados", description: "Acompanhe suas taxas de glicose e insulina de forma organizada e intuitiva." },
              { image: tabela, title: "Relatórios Completos", description: "Visualize sua evolução através de gráficos e relatórios detalhados." },
              { image: fav03, title: "Monitoramento Inteligente", description: "Receba insights baseados nos seus registros para um melhor controle da glicose." }
            ].map((feature, index) => (
              <div key={index} className="bg-white shadow p-4 rounded flex flex-col items-start w-full">
                <div className="w-10 h-10 bg-teal-100 flex items-center justify-center rounded-full mb-4">
                  <Image src={feature.image} alt={feature.title} width={90} height={90} className="w-40" />
                </div>
                <div className="text-left">
                  <h3 className="font-bold">{feature.title}</h3>
                  <p className="text-gray-600">{feature.description}</p>
                </div>
              </div>
            ))}
          </div>
        </section>
      )}
    </main>
  );
}