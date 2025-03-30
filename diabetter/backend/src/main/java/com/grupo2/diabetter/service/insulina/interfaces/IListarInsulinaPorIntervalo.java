package com.grupo2.diabetter.service.insulina.interfaces;

import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import com.grupo2.diabetter.dto.insulina.IntervaloDataDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IListarInsulinaPorIntervalo {
    public Map<LocalDate, Double> listarInsulinasPorIntervalo(IntervaloDataDTO dto);
}
