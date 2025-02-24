package com.grupo2.diabetter.service.horario.interfaces;

import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;

import java.util.List;

public interface IListarHorarioService {
    List<HorarioResponseDTO> listarHorario(Long userId);
}
