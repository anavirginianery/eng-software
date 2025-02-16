package com.grupo2.diabetter.service.horario.interfaces;

import org.springframework.http.ResponseEntity;

public interface IListarHorarioService {
    ResponseEntity<?> listarHorario(Long userId);
}
