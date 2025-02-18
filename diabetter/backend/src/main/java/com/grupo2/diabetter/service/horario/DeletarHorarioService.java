package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.IDeletarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeletarHorarioService implements IDeletarHorarioService {

    @Autowired
    private  HorarioRepository horarioRepository;

    @Override
    public void deletarHorario(UUID uuid) {
        if (horarioRepository.existsById(uuid)) {
            horarioRepository.deleteById(uuid);
        } else {
            throw new NotFoundException("Horario not found");
        }
    }
}
