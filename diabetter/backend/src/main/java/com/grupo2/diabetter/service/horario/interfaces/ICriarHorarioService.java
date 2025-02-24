package com.grupo2.diabetter.service.horario.interfaces;

import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;
import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;

public interface ICriarHorarioService {
    HorarioResponseDTO createHorario(HorarioPostPutRequestDTO dto);
}
