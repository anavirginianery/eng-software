package com.grupo2.diabetter.service.horario.interfaces;

import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;

import java.util.UUID;

public interface IRecuperarHorarioService {
    HorarioResponseDTO recuperarHorario(UUID uuid);
}
