// components/TimeFilter.tsx
'use client';

import { useState } from 'react';
import { CiFilter } from 'react-icons/ci';

type TimeFrame = 'dia' | 'semana' | 'mês' | '3 meses' | 'ano' | 'geral';

interface TimeFilterProps {
  timeFrames?: TimeFrame[];
  defaultValue?: TimeFrame;
  onChange?: (selected: TimeFrame) => void;
}

export default function TimeFilter({
    timeFrames = ['dia', 'semana', 'mês', '3 meses', 'ano', 'geral'],
    defaultValue = 'dia',
    onChange
}: TimeFilterProps) {
    const [selectedTimeFrame, setSelectedTimeFrame] = useState<TimeFrame>(defaultValue);

    const handleSelect = (timeFrame: TimeFrame) => {
        setSelectedTimeFrame(timeFrame);
        onChange?.(timeFrame);
    };

    return (
        <div className="flex flex-col gap-2 p-4 border-r border-gray-200 w-48">
            <div className='flex p-2 items-center'>
                <CiFilter />
            <h3 className="text-sm font-semibold text-gray-600 mb-2">Filtrar por:</h3>
            </div>
            {timeFrames.map((timeFrame) => (
                <button
                    key={timeFrame}
                    onClick={() => handleSelect(timeFrame)}
                    className={`text-left px-4 py-2 rounded-md text-sm transition-colors
                        ${
                            selectedTimeFrame === timeFrame
                                ? 'bg-blue-500 text-white'
                                : 'bg-gray-100 hover:bg-gray-200 text-gray-700'
                        }`}
                >
                    {timeFrame.charAt(0).toUpperCase() + timeFrame.slice(1)}
                </button>
            ))}
        </div>
    );
}