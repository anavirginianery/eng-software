package com.grupo2.diabetter.service.glicemia.interfaces;

import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;
import com.grupo2.diabetter.dto.glicemia.IntervaloDataDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IListarGlicemiaPorIntervalo {
    public Map<LocalDate, Double> listarGlicemiasPorIntervalo(IntervaloDataDTO dto);
}
