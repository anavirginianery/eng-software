package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.dto.glicemia.HorarioObject;
import com.grupo2.diabetter.dto.glicemia.HorarioPostPutRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface HorarioServiceInterface {
    ResponseEntity<?> createHorario(HorarioPostPutRequestDTO dto);
    HorarioObject readHorario(UUID uuid);
    ResponseEntity<?> updateHorario(UUID uuid, HorarioPostPutRequestDTO dto);
    ResponseEntity<?> disableHorario(UUID uuid);
    ResponseEntity<?> listarHorarios();
}
