// components/LineChart.tsx
'use client'; // Required for Next.js 13+ App Router
import { Line } from 'react-chartjs-2';
import { Chart as ChartJS } from 'chart.js';

// Register Chart.js components if not already registered
ChartJS.register();
import { ChartData, ChartOptions } from 'chart.js';

interface LineChartProps {
  data: ChartData<'line'>;
  options?: ChartOptions<'line'>;
}

export default function LineChart({ data, options }: LineChartProps) {
  return <Line data={data} options={options} />;
}