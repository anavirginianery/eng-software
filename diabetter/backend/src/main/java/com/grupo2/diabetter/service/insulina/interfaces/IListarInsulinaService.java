package com.grupo2.diabetter.service.insulina.interfaces;

import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import java.util.List;
import java.util.UUID;

public interface IListarInsulinaService {
    List<InsulinResponseDTO> listarTodasInsulinas();

    List<InsulinResponseDTO> listarInsulinaPorHorario(UUID horarioId);
}