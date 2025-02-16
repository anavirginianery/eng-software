package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.dto.horario.HorarioObject;
import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.ICriarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CriarHorarioService implements ICriarHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public ResponseEntity<HorarioObject> createHorario(HorarioPostPutRequestDTO dto){
        Horario horario = new Horario();
        horario.setValue(dto.getValue());
        horario.setDate(dto.getDate());
        horario.setUserId(dto.getUserId());
        horarioRepository.save(horario);
        HorarioObject horarioObject = HorarioObject.builder().value(dto.getValue())
                .date(dto.getDate()).userId(dto.getUserId()).uuid(horario.getUuid()).build();
        return ResponseEntity.ok(horarioObject);
    }

}
