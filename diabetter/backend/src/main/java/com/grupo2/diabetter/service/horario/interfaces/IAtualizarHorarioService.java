package com.grupo2.diabetter.service.horario.interfaces;

import com.grupo2.diabetter.dto.horario.HorarioObject;
import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IAtualizarHorarioService {
    ResponseEntity<?> updateHorario(UUID uuid, HorarioPostPutRequestDTO dto);
}
