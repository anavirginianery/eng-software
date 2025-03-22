  'use client';

  import React, { useState } from 'react';
  import { Button } from 'primereact/button';
  import { TreeSelect } from 'primereact/treeselect';
  import Form from '@/app/pages/form/page';
  import '@/app/lib/chart';
  import { Line } from 'react-chartjs-2'
  import dynamic from 'next/dynamic';
  import { ChartData } from 'chart.js';
  import { CiFilter } from "react-icons/ci";


  const LineChart = dynamic(() => import('@/app/components/charts/LineChart'), { ssr: false });
  const TimeFilter = dynamic(() => import('@/app/components/dashboard/TimeFilter'), {
    ssr: false,
  });


  export default function DashBoard() {
    
    const [selectedTime, setSelectedTime] = useState<string>('week');

    const data: ChartData<'line'> = {
      labels: ['Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado', 'Domingo'],
      datasets: [{
        label: 'Glicemia',
        data: [150, 350, 190, 150, 300],
        borderColor: 'rgb(160, 32, 240)',
        backgroundColor: 'rgba(160, 32, 240, 0.2)',
        tension: 0,
      }],
    };

    const options = {
      responsive: true,
      plugins: {
        legend: {
          position: 'top' as const,
        },
        title: {
          display: true,
          text: 'Glicemia',
        },
      },
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            stepSize: 100,
            max: 400,
          },
        },
      },
    };

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
        <main className="flex-col p-6 bg-gray-100 flex justify-center items-center w-full min-h-screen max-h-full">
        <div className='flex items-center justify-around w-full p-2'>
            <div className='flex flex-col'>
                <h2>Selecione um horário para visualizar suas métricas:</h2>
                <div className='flex p-2 items-center'>
                    <CiFilter />
                    <Button label='08:00' severity='secondary' text raised />
                    <Button label='11:00' severity='secondary' text raised />
                    <Button label='14:00' severity='secondary' text raised />
                </div>
                <div className='flex p-2 items-center'>
                    <CiFilter />
                    <Button label='16:00' severity='secondary' text raised />
                    <Button label='19:00' severity='secondary' text raised />
                    <Button label='22:00' severity='secondary' text raised />
                </div>
            </div>
            <div className="flex flex-col items-start">
      <div className="flex flex-col">
      <TimeFilter 
        onChange={(time) => setSelectedTime(time)}
      />
      </div>
    </div> 
        </div>
        <div style={{width: '80%', margin: '0 auto'}}>
          <LineChart data={data} options={options} />
        </div>
        </main>
      </div>
    );
  }
