package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.dto.horario.HorarioObject;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.IRecuperarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecuperarHorarioService implements IRecuperarHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public ResponseEntity<?> recuperarHorario(UUID uuid) {
        Horario horario = horarioRepository.findById(uuid).orElse(null);
        if (horario == null) {
            return ResponseEntity.status(404).body("Horario not found");
        } else {
            HorarioObject horarioObject = HorarioObject.builder().userId(horario.getUserId())
                    .value(horario.getValue()).date(horario.getDate()).uuid(horario.getUuid()).build();
            return ResponseEntity.ok(horarioObject);
        }
    }
}
