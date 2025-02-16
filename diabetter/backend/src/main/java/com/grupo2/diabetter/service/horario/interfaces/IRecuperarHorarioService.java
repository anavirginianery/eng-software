package com.grupo2.diabetter.service.horario.interfaces;

import com.grupo2.diabetter.dto.horario.HorarioObject;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IRecuperarHorarioService {
    ResponseEntity<?> recuperarHorario(UUID uuid);
}
