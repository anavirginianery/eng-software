'use client';

import { useState } from "react";

interface FormData {
  nomeCompleto: string;
  dataNascimento: string;
  sexo: string;
  peso: string;
  altura: string;
  tipoDiabetes: string;
  tipoInsulina: string;
  quantidadeInsulina: string;
  glicemia: string;
  comorbidades: string;
  horarios: string[];
  novoHorario: string;
}

export default function Perfil() {
  const [formData, setFormData] = useState<FormData>({
    nomeCompleto: "",
    dataNascimento: "",
    sexo: "",
    peso: "",
    altura: "",
    tipoDiabetes: "",
    tipoInsulina: "",
    quantidadeInsulina: "",
    glicemia: "",
    comorbidades: "",
    horarios: [],
    novoHorario: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const adicionarHorario = () => {
    if (formData.novoHorario) {
      setFormData({
        ...formData,
        horarios: [...formData.horarios, formData.novoHorario],
        novoHorario: "",
      });
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Dados editados:", formData);
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100 p-4">
      <div className="bg-[#f9f9f9] shadow-[0px_10px_20px_rgba(0,0,0,0.2)] rounded-xl p-8 w-full max-w-3xl">
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-semibold">Nome Completo</label>
            <input
              type="text"
              name="nomeCompleto"
              value={formData.nomeCompleto}
              onChange={handleChange}
              className="border p-2 rounded w-full bg-gray-200"
            />
          </div>

          <div className="grid grid-cols-3 gap-4">
            <div>
              <label className="block text-sm font-semibold">Data de Nascimento</label>
              <input type="date" name="dataNascimento" value={formData.dataNascimento} onChange={handleChange} className="border p-2 rounded w-full bg-gray-200" />
            </div>
            <div>
              <label className="block text-sm font-semibold">Sexo</label>
              <input type="text" name="sexo" value={formData.sexo} onChange={handleChange} className="border p-2 rounded w-full bg-gray-200" />
            </div>
            <div>
              <label className="block text-sm font-semibold">Peso</label>
              <input type="text" name="peso" value={formData.peso} onChange={handleChange} className="border p-2 rounded w-full bg-gray-200" />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-semibold">Altura (cm)</label>
              <input type="text" name="altura" value={formData.altura} onChange={handleChange} className="border p-2 rounded w-full bg-gray-200" />
            </div>
            <div>
              <label className="block text-sm font-semibold">Tipo de diabetes</label>
              <input type="text" name="tipoDiabetes" value={formData.tipoDiabetes} onChange={handleChange} className="border p-2 rounded w-full bg-gray-200" />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-semibold">Tipo de insulina</label>
              <input type="text" name="tipoInsulina" value={formData.tipoInsulina} onChange={handleChange} className="border p-2 rounded w-full bg-gray-200" />
            </div>
            <div>
              <label className="block text-sm font-semibold">Quantidade</label>
              <input type="text" name="quantidadeInsulina" value={formData.quantidadeInsulina} onChange={handleChange} className="border p-2 rounded w-full bg-gray-200" />
            </div>
          </div>

          <div>
            <label className="block text-sm font-semibold">Comorbidades</label>
            <input type="text" name="comorbidades" value={formData.comorbidades} onChange={handleChange} className="border p-2 rounded w-full bg-gray-200" />
          </div>

          <div className="flex justify-between mt-2">
  <div className="flex items-center gap-2 w-1/2">
    <label className="text-sm font-semibold">Horários de medição diária</label>
  </div>
  <div className="flex items-center gap-2 w-1/2 justify-start">
    <button
      type="button"
      onClick={adicionarHorario}
      className="bg-gray-300 px-4 py-2 rounded font-semibold w-full"
    >
      <input
        type="time"
        name="novoHorario"
        value={formData.novoHorario}
        onChange={handleChange}
        className="p-2 rounded text-center bg-gray-300"
      />
      <span className="ml-4">+</span>
    </button>
  </div>
</div>

<div className="mt-2">
  <div className="grid grid-cols-4 gap-2">
    {formData.horarios.map((horario, index) => (
      <div
        key={index}
        className="bg-gray-300 p-2 rounded text-center font-semibold"
      >
        {horario}
      </div>
    ))}
  </div>
</div>


          <div className="flex justify-end mt-4">
            <button type="submit" className="bg-green-500 text-white px-6 py-2 rounded-lg font-semibold">Editar</button>
          </div>
        </form>
      </div>
    </div>
  );
}
