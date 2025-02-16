package com.grupo2.diabetter.service.horario.interfaces;

import com.grupo2.diabetter.dto.horario.HorarioObject;
import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import org.springframework.http.ResponseEntity;

public interface ICriarHorarioService {
    ResponseEntity<HorarioObject> createHorario(HorarioPostPutRequestDTO dto);
}
