"use client";

import React, { useRef, useState } from "react";
import { ConfirmDialog, confirmDialog } from "primereact/confirmdialog";
import { Toast } from "primereact/toast";
import { InputText } from "primereact/inputtext";
import { Button } from "primereact/button";
import { useRouter } from "next/navigation";

export default function ProfileForm() {
  const toast = useRef<Toast | null>(null);
  const router = useRouter();

  const [formData, setFormData] = useState({
    dob: "",
    sex: "",
    weight: "",
    height: "",
    diabetesType: "",
    insulinType: "",
    quantity: "",
    glucose: "",
    comorbidities: "",
    schedule: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.id]: e.target.value });
  };

  const showToast = (
    severity: "info" | "warn" | "success" | "error",
    summary: string,
    detail: string
  ) => {
    if (toast.current) {
      toast.current.show({
        severity,
        summary,
        detail,
        life: 3000,
      });
    }
  };

  const handleSubmit = async () => {
    try {
      const response = await fetch("/api/usuarios", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) throw new Error("Erro ao enviar dados");

      showToast("success", "Sucesso", "Perfil cadastrado com sucesso!");
      router.push("/"); // redireciona após envio
    } catch (error) {
      console.error("Erro ao cadastrar:", error);
      showToast("error", "Erro", "Falha ao cadastrar. Tente novamente.");
    }
  };

  const accept = () => {
    handleSubmit();
  };

  const reject = () => {
    showToast("warn", "Cancelado", "Envio cancelado.");
  };

  const confirm1 = () => {
    confirmDialog({
      message: "Are you sure you want to proceed?",
      header: "Confirmation",
      icon: "pi pi-exclamation-triangle",
      defaultFocus: "accept",
      accept,
      reject,
    });
  };

  const confirm2 = () => {
    confirmDialog({
      message: "Do you want to delete this record?",
      header: "Delete Confirmation",
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
          <InputText
            id="dob"
            value={formData.dob}
            onChange={handleChange}
            placeholder="dd/mm/yyyy"
            className="w-full"
          />
        </div>
        <div>
          <label htmlFor="sex" className="font-bold block mb-1">
            Sexo
          </label>
          <InputText
            id="sex"
            value={formData.sex}
            onChange={handleChange}
            className="w-full"
          />
        </div>
        <div>
          <label htmlFor="weight" className="font-bold block mb-1">
            Peso
          </label>
          <InputText
            id="weight"
            value={formData.weight}
            onChange={handleChange}
            placeholder="kg"
            className="w-full"
          />
        </div>
        <div>
          <label htmlFor="height" className="font-bold block mb-1">
            Altura (cm)
          </label>
          <InputText
            id="height"
            value={formData.height}
            onChange={handleChange}
            placeholder="cm"
            className="w-full"
          />
        </div>
        <div>
          <label htmlFor="diabetesType" className="font-bold block mb-1">
            Tipo de diabetes
          </label>
          <InputText
            id="diabetesType"
            value={formData.diabetesType}
            onChange={handleChange}
            className="w-full"
          />
        </div>
        <div>
          <label htmlFor="insulinType" className="font-bold block mb-1">
            Tipo de insulina
          </label>
          <InputText
            id="insulinType"
            value={formData.insulinType}
            onChange={handleChange}
            className="w-full"
          />
        </div>
        <div>
          <label htmlFor="quantity" className="font-bold block mb-1">
            Quantidade
          </label>
          <InputText
            id="quantity"
            value={formData.quantity}
            onChange={handleChange}
            placeholder="UI"
            className="w-full"
          />
        </div>
        <div>
          <label htmlFor="glucose" className="font-bold block mb-1">
            Quanto está sua glicemia nesse momento?
          </label>
          <InputText
            id="glucose"
            value={formData.glucose}
            onChange={handleChange}
            placeholder="mg/dL"
            className="w-full"
          />
        </div>
        <div className="col-span-2">
          <label htmlFor="comorbidities" className="font-bold block mb-1">
            Possui outras comorbidades? (Caso positivo, indique quais)
          </label>
          <InputText
            id="comorbidities"
            value={formData.comorbidities}
            onChange={handleChange}
            placeholder="Ex: Hipertensão, Colesterol elevado..."
            className="w-full"
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
              className="w-1/4"
            />
            <Button
              icon="pi pi-plus"
              className="p-button-rounded p-button-outlined"
            />
          </div>
        </div>
      </div>

      <div className="flex justify-between mt-6">
        <Button
          onClick={confirm1}
          icon="pi pi-check"
          label="Confirmar"
          className="mr-2"
        />
        <Button
          onClick={confirm2}
          icon="pi pi-times"
          label="Deletar"
          className="p-button-danger"
        />
      </div>
    </div>
  );
}
