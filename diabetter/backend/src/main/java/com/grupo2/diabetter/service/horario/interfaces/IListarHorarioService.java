package com.grupo2.diabetter.service.horario.interfaces;

import com.grupo2.diabetter.model.Horario;

import java.util.List;

public interface IListarHorarioService {
    List<Horario> listarHorario(Long userId);
}
