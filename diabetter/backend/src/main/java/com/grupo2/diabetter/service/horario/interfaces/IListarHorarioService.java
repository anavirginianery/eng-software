package com.grupo2.diabetter.service.horario.interfaces;

import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IListarHorarioService {
    List<HorarioResponseDTO> listarHorario(UUID userId);
}
