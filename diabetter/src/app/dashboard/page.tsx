import React from "react";
import { Button } from "primereact/button";
import Form from "@/app/form/page";

export default function DashBoard() {
  return (
    <div className="flex h-screen">
      <aside className="w-64 bg-white shadow-md p-6 flex flex-col">
        <div className="flex flex-col items-center mb-6">
          <div className="w-20 h-20 bg-gray-300 rounded-full mb-2"></div>
          <span className="text-lg font-bold">João Gomes</span>
        </div>
        <nav className="flex flex-col space-y-4">
          <Button label="Home" className="p-button-text" />
          <Button label="Perfil" className="p-button-text p-button-primary" />
          <Button label="Medições" className="p-button-text" />
          <Button label="Dashboard" className="p-button-text" />
        </nav>
        <div className="mt-auto">
          <Button label="Sair" className="p-button-text text-right" />
        </div>
      </aside>
      <main className="flex-1 p-6 bg-gray-100 flex justify-center items-center">
        <Form />
      </main>
    </div>
  );
}
