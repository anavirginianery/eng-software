package com.grupo2.diabetter.service.horario.interfaces;

import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;
import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;

import java.util.UUID;

public interface IAtualizarHorarioService {
    HorarioResponseDTO updateHorario(UUID uuid, HorarioPostPutRequestDTO dto);
}
