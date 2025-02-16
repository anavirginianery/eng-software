package com.grupo2.diabetter.service.horario.interfaces;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IDeletarHorarioService {
    ResponseEntity<?> deletarHorario(UUID uuid);
}
