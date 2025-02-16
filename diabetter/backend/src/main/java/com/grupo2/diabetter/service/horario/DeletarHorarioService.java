package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.IDeletarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeletarHorarioService implements IDeletarHorarioService {

    @Autowired
    private  HorarioRepository horarioRepository;

    @Override
    public ResponseEntity<?> deletarHorario(UUID uuid) {
        if (horarioRepository.existsById(uuid)) {
            horarioRepository.deleteById(uuid);
            return ResponseEntity.ok("Horario disabled successfully");
        } else {
            return ResponseEntity.status(404).body("Horario not found");
        }
    }
}
