package com.grupo2.diabetter.service.insulin.interfaces;

import com.grupo2.diabetter.dto.insulin.InsulinResponseDTO;
import java.util.List;
import java.util.UUID;

public interface IListarInsulinService {
    List<InsulinResponseDTO> listarTodasInsulinas();

    List<InsulinResponseDTO> listarInsulin(UUID horarioId);
}