"use client";

import React, { useEffect, useRef, useState } from "react";
import { ConfirmDialog, confirmDialog } from "primereact/confirmdialog";
import { Toast } from "primereact/toast";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { useRouter } from "next/navigation";
import { TipoDiabetes, TipoInsulina } from "./types";

export default function ProfileForm() {
  const toast = useRef<Toast | null>(null);
  const router = useRouter();

  const [formData, setFormData] = useState({
    dob: "",
    sex: "",
    weight: "",
    height: "",
    diabetesType: "" as TipoDiabetes,
    insulinType: "" as TipoInsulina,
    comorbidities: [] as string[],
    schedule: "",
    email: "",
    password: "",
  });

  const [prefilledFields, setPrefilledFields] = useState<Set<string>>(new Set());

  const usuarioLocal = typeof window !== "undefined" ? localStorage.getItem("usuario") : null;
  const usuario = usuarioLocal ? JSON.parse(usuarioLocal) : null;

  useEffect(() => {
    if (usuario) {
      const newFormData = {
        ...formData,
        dob: usuario.dataNasc || "",
        sex: usuario.genero || "",
        email: usuario.email || "",
        password: usuario.password || "",
        weight: usuario.peso?.toString() || "",
        height: usuario.altura?.toString() || "",
        diabetesType: usuario.tipoDiabetes || "",
        insulinType: usuario.tipoInsulina || "",
        comorbidities: usuario.comorbidades || [],
        schedule: usuario.horarios_afericao?.map((h: { horario: string }) => h.horario).join(", ") || "",
      };

      const newPrefilledFields = new Set<string>();
      Object.entries(newFormData).forEach(([key, value]) => {
        if (value) newPrefilledFields.add(key);
      });

      setFormData(newFormData);
      setPrefilledFields(newPrefilledFields);
    }
  }, []);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const field = e.target.id;
    setFormData({ ...formData, [field]: e.target.value });
    setPrefilledFields(prev => {
      const newSet = new Set(prev);
      newSet.delete(field);
      return newSet;
    });
  };

  const handleComorbidityChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    const comorbidities = value.split(",").map(c => c.trim()).filter(c => c);
    setFormData({ ...formData, comorbidities });
    // Remove from prefilled fields when user starts editing
    setPrefilledFields(prev => {
      const newSet = new Set(prev);
      newSet.delete("comorbidities");
      return newSet;
    });
  };

  const showToast = (
    severity: "info" | "warn" | "success" | "error",
    summary: string,
    detail: string
  ) => {
    if (toast.current) {
      toast.current.show({ severity, summary, detail, life: 3000 });
    }
  };

  const handleSubmit = async () => {
    try {
      if (!usuario?.id) {
        showToast("error", "Erro", "Usuário não encontrado");
        return;
      }
  
      const response = await fetch(`/api/usuarios/${usuario.id}/completar-perfil`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          nome: usuario.nome,
          email: usuario.email,
          password: usuario.password,
          dataNasc: formData.dob,
          genero: formData.sex,
          peso: parseFloat(formData.weight),
          altura: parseFloat(formData.height),
          tipoDiabetes: formData.diabetesType,
          tipoInsulina: formData.insulinType,
          comorbidades: formData.comorbidities,
          horarios_afericao: formData.schedule
            ? formData.schedule.split(",").map((h) => ({ horario: h.trim() }))
            : [],
        }),
      });
  
      if (!response.ok) throw new Error("Erro ao enviar dados");
  
      showToast("success", "Sucesso", "Perfil cadastrado com sucesso!");
      router.push("/inserirDados");
    } catch (error) {
      console.error("Erro ao cadastrar:", error);
      showToast("error", "Erro", "Falha ao cadastrar. Tente novamente.");
    }
  };
  

  const accept = () => handleSubmit();
  const reject = () => showToast("warn", "Cancelado", "Envio cancelado.");

  const confirm1 = () => {
    confirmDialog({
      message: "Tem certeza que deseja enviar os dados?",
      header: "Confirmação",
      icon: "pi pi-exclamation-triangle",
      defaultFocus: "accept",
      accept,
      reject,
    });
  };

  const confirm2 = () => {
    confirmDialog({
      message: "Tem certeza que deseja deletar os dados?",
      header: "Confirmação",
      icon: "pi pi-info-circle",
      defaultFocus: "reject",
      acceptClassName: "p-button-danger",
      accept,
      reject,
    });
  };

  return (
    <div className="card p-6 max-w-2xl mx-auto bg-white rounded-lg shadow-lg">
      <Toast ref={toast} />
      <ConfirmDialog />
      <h2 className="text-2xl font-bold text-center mb-6">Complete seu perfil:</h2>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label htmlFor="dob" className="font-bold block mb-1">
            Data de Nascimento
          </label>
          <input
            type="date"
            id="dob"
            value={formData.dob}
            onChange={handleChange}
            className={`w-full p-2.5 bg-gray-100 rounded-md border-none ${prefilledFields.has('dob') ? 'bg-gray-100' : ''}`}
          />
        </div>
        <div>
          <label htmlFor="sex" className="font-bold block mb-1">
            Gênero
          </label>
          <select
            id="sex"
            value={formData.sex}
            onChange={handleChange}
            className={`w-full p-2.5 bg-gray-100 rounded-md border-none ${prefilledFields.has('sex') ? 'bg-gray-100' : ''}`}
          >
            <option value="">Selecione</option>
            <option value="MASCULINO">Masculino</option>
            <option value="FEMININO">Feminino</option>
            <option value="OUTRO">Outro</option>
            <option value="NAO_INFORMADO">Prefiro não dizer</option>
          </select>
        </div>
        <div>
          <label htmlFor="weight" className="font-bold block mb-1">
            Peso
          </label>
          <input
            type="number"
            step="0.1"
            id="weight"
            value={formData.weight}
            onChange={handleChange}
            placeholder="kg"
            className={`w-full p-2.5 bg-gray-100 rounded-md border-none ${prefilledFields.has('weight') ? 'bg-gray-100' : ''}`}
          />
        </div>
        <div>
          <label htmlFor="height" className="font-bold block mb-1">
            Altura (cm)
          </label>
          <input
            type="number"
            step="0.1"
            id="height"
            value={formData.height}
            onChange={handleChange}
            placeholder="cm"
            className={`w-full p-2.5 bg-gray-100 rounded-md border-none ${prefilledFields.has('height') ? 'bg-gray-100' : ''}`}
          />
        </div>
        <div>
          <label htmlFor="diabetesType" className="font-bold block mb-1">
            Tipo de diabetes
          </label>
          <select
            id="diabetesType"
            value={formData.diabetesType}
            onChange={handleChange}
            className={`w-full p-2.5 bg-gray-100 rounded-md border-none ${prefilledFields.has('diabetesType') ? 'bg-gray-100' : ''}`}
          >
            <option value="">Selecione</option>
            <option value="TIPO_1">Tipo 1</option>
            <option value="TIPO_2">Tipo 2</option>
            <option value="GESTACIONAL">Gestacional</option>
            <option value="LADA">LADA</option>
            <option value="MODY">MODY</option>
            <option value="SECUNDARIO">Secundário</option>
            <option value="INDETERMINADO">Indeterminado</option>
          </select>
        </div>
        <div>
          <label htmlFor="insulinType" className="font-bold block mb-1">
            Tipo de insulina
          </label>
          <select
            id="insulinType"
            value={formData.insulinType}
            onChange={handleChange}
            className={`w-full p-2.5 bg-gray-100 rounded-md border-none ${prefilledFields.has('insulinType') ? 'bg-gray-100' : ''}`}
          >
            <option value="">Selecione</option>
            <option value="ULTRARRAPIDA">Ultrarrápida</option>
            <option value="RAPIDA">Rápida</option>
            <option value="INTERMEDIARIA">Intermediária</option>
            <option value="LENTA">Lenta</option>
            <option value="BASAL">Basal</option>
            <option value="MISTA">Mista</option>
            <option value="INDETERMINADA">Indeterminada</option>
          </select>
        </div>
       
        <div className="col-span-2">
          <label htmlFor="comorbidities" className="font-bold block mb-1">
            Possui outras comorbidades? (Caso positivo, indique quais)
          </label>
          <input
            type="text"
            id="comorbidities"
            value={formData.comorbidities.join(", ")}
            onChange={handleComorbidityChange}
            placeholder="Ex: Hipertensão, Colesterol elevado..."
            className={`w-full p-2.5 bg-gray-100 rounded-md border-none ${prefilledFields.has('comorbidities') ? 'bg-gray-100' : ''}`}
          />
        </div>
        <div className="col-span-2">
          <label htmlFor="schedule" className="font-bold block mb-1">
            Em quais horários você realiza seu controle diário? (Medição de glicemia e aplicação de insulina)
          </label>
          <div className="flex gap-2">
            <InputText
              id="schedule"
              value={formData.schedule}
              onChange={handleChange}
              placeholder="--:--"
              className={`w-1/4 ${prefilledFields.has('schedule') ? 'bg-gray-100' : ''}`}
            />
            <Button icon="pi pi-plus" className="p-button-rounded p-button-outlined" />
          </div>
        </div>
      </div>

      <div className="flex justify-between mt-6">
        <Button onClick={confirm1} icon="pi pi-check" label="Confirmar" className="mr-2" />
        <Button onClick={confirm2} icon="pi pi-times" label="Deletar" className="p-button-danger" />
      </div>
    </div>
  );
}