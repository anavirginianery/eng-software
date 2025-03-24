import Link from "next/link";
import Image from "next/image";

export default function InserirDados() {
    return (
        <div className="min-h-screen bg-white relative overflow-hidden">
            {/* Botão Sair - apenas desktop */}
            <div className="hidden md:block absolute top-4 right-4 z-10">
                <Link 
                    href="/login" 
                    className="text-[#38B2AC] text-sm hover:underline"
                >
                    Sair
                </Link>
            </div>

            {/* Container principal */}
            <div className="flex min-h-screen flex-col md:flex-row">
                {/* Seção esquerda */}
                <div className="w-full md:w-[40%] flex flex-col items-center md:items-start justify-center px-4 md:pl-8 py-8 z-10 relative">
                    <h1 className="text-4xl md:text-5xl mb-6 md:mb-8 text-center md:text-left">
                        Insira seus dados de hoje!
                    </h1>
                    <div className="w-full max-w-[200px] md:max-w-none">
                        <Image 
                            src="/img/clock.png"
                            alt="Relógio" 
                            width={300}
                            height={200}
                            className="w-full h-auto md:w-auto"
                        />
                    </div>
                </div>

                {/* Forma circular verde */}
                <div 
                    className="absolute right-[-80%] top-[30%] md:right-[-55%] md:top-0.5 w-[160%] h-[160%] md:w-[120%] md:h-[140%]"
                    style={{
                        backgroundColor: '#38B2AC',
                        borderRadius: '100%',
                    }}
                />

                {/* Seção direita - Formulário */}
                <div className="w-full md:w-[65%] relative flex items-center justify-center py-8 md:py-0">
                    <div className="bg-white p-6 md:p-8 rounded-2xl shadow-lg w-[85%] max-w-[400px] z-20">
                        <div className="flex justify-center w-full mb-8">
                            <Image
                                src="/img/logo.png"
                                alt="Logo"
                                width={350}  
                                height={290}
                                className="w-auto h-auto"
                                priority
                            />
                        </div>
                        
                        <form className="px-4"> 
                            <div className="mb-8">
                                <label className="block text-sm font-medium text-gray-700 mb-2">Insulina</label>
                                <input
                                    type="text"
                                    placeholder="Digite a quantidade de insulina"
                                    className="w-full p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC] transition-all"
                                />
                            </div>
                            
                            <div className="mb-8">
                                <label className="block text-sm font-medium text-gray-700 mb-2">Horário</label>
                                <input
                                    type="time"
                                    className="w-full p-3 bg-[#E5E5E5] rounded-md border-none focus:ring-2 focus:ring-[#38B2AC] transition-all"
                                />
                            </div>
                            
                            <div className="mb-10">
                                <button
                                    type="submit"
                                    className="w-full bg-[#38B2AC] text-white py-3 rounded-md hover:bg-[#2C9A94] transition-colors font-medium text-lg"
                                >
                                    Inserir
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}