"use client";
import React, { useState } from "react";
import { InputText } from "primereact/inputtext";
import { FloatLabel } from "primereact/floatlabel";
import { Button } from "primereact/button";
import Image from "next/image";
import logo from "@/app/img/logo.png";

export default function InsertDataForm() {
    const [insulin, setInsulin] = useState("");
    const [time, setTime] = useState("");

    return (
        <div className="bg-white p-8 rounded-lg shadow-lg w-96 border border-teal-500">
            <div className="flex justify-center mb-6">
                <Image src={logo} alt="Diabetter Logo" width={150} height={50} />
            </div>
            <h2 className="text-center text-xl font-bold mb-6">Insira seus dados</h2>
            
            <div className="mb-6">
                <FloatLabel>
                    <InputText id="insulin" value={insulin} onChange={(e) => setInsulin(e.target.value)} className="w-full p-3" />
                    <label htmlFor="insulin">Insulina</label>
                </FloatLabel>
            </div>

            <div className="mb-6">
                <FloatLabel>
                    <InputText id="time" value={time} onChange={(e) => setTime(e.target.value)} className="w-full p-3" />
                    <label htmlFor="time">Horário</label>
                </FloatLabel>
            </div>

            <Button label="Inserir" className="w-full bg-teal-600 text-white p-3" />
        </div>
    );
}