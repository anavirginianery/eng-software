"use client";

import { Footer } from "@/components/Footer";
import Header from "@/components/Header";
import Image from "next/image";
import { useRouter } from "next/navigation";


const phoneImage = "/img/phone.png";
const fav01 = "/img/favcon01.png";
const tabela = "/img/FAVCON.png";
const fav03 = "/img/favcon03.png";

export default function Home() {
  const router = useRouter();
  const [showFeatures, setShowFeatures] = useState(false);


  const handleComecarAgora = () => {
    router.push("/cadastro");
  };

  return (
    <>
      <Header />
      <main className="bg-gray-100 min-h-screen flex flex-col items-center justify-center transition-all duration-500">
        <h1
          className={`font-bold text-center transition-all duration-700 ease-in-out ${
            showFeatures ? "text-3xl mt-6" : "text-4xl mt-12"
          }`}
        >
          Keep your glucose <span className="text-teal-600">under control</span>
        </h1>

        <section
          className={`w-full max-w-3xl p-6 text-center flex flex-col md:flex-row items-center justify-center transition-[margin,height] duration-700 ease-in-out ${
            showFeatures ? "mt-4 h-auto" : "mt-10 min-h-[90vh]"
          }`}
        >
          <div className="md:w-1/3 flex justify-center">
            <Image
              src={phoneImage}
              alt="Monitoramento de glicose"
              width={200}
              height={200}
              className="w-48 transition-transform duration-500"
            />
          </div>
          <div className="md:w-2/3 flex flex-col items-center text-center transition-all duration-700 ease-in-out">
            <p className="mt-8 text-black text-[1.1rem] font-bold text-left">
              Plataforma intuitiva para pessoas com diabetes registrarem suas taxas de glicose e
              dados de saúde essenciais, simplificando o monitoramento diário.
            </p>

            <div className="mt-6 flex flex-col md:flex-row md:space-x-4 w-full justify-start">
              <button className="bg-teal-600 text-white px-6 py-2 rounded w-full md:w-auto transition-all duration-300 hover:bg-white hover:text-teal-600 hover:border hover:border-teal-600">
                Começar agora
              </button>
              <button
                className="border border-teal-600 text-teal-600 px-6 py-2 rounded w-full md:w-auto transition-all duration-300 hover:bg-teal-600 hover:text-white"
                onClick={() => setShowFeatures(!showFeatures)}
              >
                {showFeatures ? "Ocultar" : "Saiba Mais"}
              </button>
            </div>
          </div>
        </section>

        <section
          className={`w-full max-w-3xl p-8 transition-all duration-700 overflow-hidden ${
            showFeatures ? "opacity-100 h-auto" : "opacity-0 h-0"
          }`}
        >
          <h2 className="text-[1.65rem] font-bold text-center">Principais Funcionalidades</h2>
          <div className="mt-8 grid grid-cols-1 gap-9">
            {[
              {
                image: fav01,
                title: "Registros Detalhados",
                description: "Monitore glicose e insulina com facilidade e precisão.",
              },
              {
                image: tabela,
                title: "Relatórios Completos",
                description: "Visualize gráficos para acompanhar sua evolução.",
              },
              {
                image: fav03,
                title: "Compartilhamento Médico",
                description: "Envie seus dados para sua equipe médica com praticidade.",
              },
            ].map((feature, index) => (
              <div
                key={index}
                className="bg-white shadow p-6 rounded flex flex-col items-start w-full transition-all duration-300 hover:bg-teal-600 hover:text-white"
              >
                <div className="w-10 h-10 bg-teal-100 flex items-center justify-center rounded-full mb-6 transition-all duration-300 hover:bg-white">
                  <Image src={feature.image} alt={feature.title} width={60} height={60} className="w-40" />
                </div>
                <div className="text-left">
                  <h3 className="font-bold text-[1.4rem]">{feature.title}</h3>
                  <p className="text-gray-600 text-[1.2rem] font-medium">{feature.description}</p>
                </div>
              </div>
            ))}
          </div>
        </section>
      </main>
      <Footer />
    </>
  );
}
