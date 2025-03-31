"use client";

import React from "react";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";

const data = [
  { data: "Jan", glicemia: 120, insulina: 15 },
  { data: "Fev", glicemia: 125, insulina: 18 },
  { data: "Mar", glicemia: 118, insulina: 14 },
  { data: "Abr", glicemia: 130, insulina: 20 },
  { data: "Mai", glicemia: 128, insulina: 16 },
  { data: "Jun", glicemia: 135, insulina: 22 },
];

export default function DashboardPage() {
  return (
    <div className="p-6">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Gráfico de Média de Glicemia */}
        <Card>
          <CardHeader>
            <CardTitle>Média de Glicemia</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="h-[300px]">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={data}>
                  <XAxis dataKey="data" />
                  <YAxis />
                  <Tooltip />
                  <Line type="monotone" dataKey="glicemia" stroke="#8884d8" />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </CardContent>
        </Card>

        {/* Gráfico de Média de Insulina */}
        <Card>
          <CardHeader>
            <CardTitle>Média de Insulina</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="h-[300px]">
              <ResponsiveContainer width="100%" height="100%">
                <LineChart data={data}>
                  <XAxis dataKey="data" />
                  <YAxis />
                  <Tooltip />
                  <Line type="monotone" dataKey="insulina" stroke="#82ca9d" />
                </LineChart>
              </ResponsiveContainer>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
} 