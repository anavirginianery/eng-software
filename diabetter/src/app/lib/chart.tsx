// lib/chart.ts
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    LineController,
    LineElement,
    PointElement,
    Title,
    Tooltip,
    Legend
  } from 'chart.js';
  
  ChartJS.register(
    CategoryScale,
    LinearScale,
    LineController,
    LineElement,
    PointElement,
    Title,
    Tooltip,
    Legend
  );
// Lib pediu para usar esse modelo para prevenir erros (tree-shaking issues).