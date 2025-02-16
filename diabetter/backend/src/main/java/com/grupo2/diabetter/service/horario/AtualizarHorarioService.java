package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.dto.horario.HorarioObject;
import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.IAtualizarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AtualizarHorarioService implements IAtualizarHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public ResponseEntity<?> updateHorario(UUID uuid, HorarioPostPutRequestDTO dto){
        Optional<Horario> optionalHorario = horarioRepository.findById(uuid);
        if(optionalHorario.isPresent()){
            Horario horario = optionalHorario.get();
            horario.setValue(dto.getValue());
            horario.setUserId(dto.getUserId());
            horario.setDate(dto.getDate());
            horarioRepository.save(horario);
            HorarioObject horarioObject = HorarioObject.builder().value(dto.getValue())
                    .date(dto.getDate()).userId(dto.getUserId()).uuid(horario.getUuid()).build();
            return ResponseEntity.ok(horarioObject);
        } else {
            return ResponseEntity.status(404).body("Horario not found");
        }
    }

}
