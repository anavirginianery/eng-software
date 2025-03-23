"use client";
import FormInfo from "@/components/FormInfoCad";
import { useRouter } from "next/navigation";
export default function Header() {
  const router = useRouter();

  return (
    
    <main>
      <header className="flex justify-between items-center p-4 bg-gradient-to-b from-teal-200 to-white shadow-md">
      
      <button
        onClick={() => router.push("/")}
        className="text-black text-2xl font-bold hover:underline ml-auto pr-4"
      >
        Sair
      </button>
    </header>
    <div><FormInfo /></div>
    
    </main>
  );
}
