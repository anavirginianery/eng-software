import Form from "@/app/components/inserirDados/Form";
import Image from "next/image";
import clockImage from "@/app/img/clock.png";

export default function InserirDados() {
    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100 relative">
            <div className="flex-1 flex flex-col items-center justify-center text-left pl-16">
                <h1 className="text-3xl font-bold mb-4">Insira seus dados de hoje!</h1>
                <Image src={clockImage} alt="Relógio" width={500} />
            </div>
            <div className="relative w-1/2 flex justify-center items-center">
                <div className="absolute right-0 w-full h-full bg-teal-500 rounded-l-full"></div>
                <div className="relative z-10">
                    <Form />
                </div>
            </div>
        </div>
    );
}