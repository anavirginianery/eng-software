package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;
import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.IAtualizarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AtualizarHorarioService implements IAtualizarHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public HorarioResponseDTO updateHorario(UUID uuid, HorarioPostPutRequestDTO dto){
        Optional<Horario> optionalHorario = horarioRepository.findById(uuid);
        if(optionalHorario.isPresent()){
            Horario horario = optionalHorario.get();
            horario.setValue(dto.getValue());
            horario.setUserId(dto.getUserId());
            horario.setDate(dto.getDate());
            horarioRepository.save(horario);
            HorarioResponseDTO horarioResponseDTO = HorarioResponseDTO.builder().value(dto.getValue())
                    .date(dto.getDate()).userId(dto.getUserId()).uuid(horario.getUuid()).build();
            return horarioResponseDTO;
        } else {
            throw new NotFoundException("Horario not found");
        }
    }

}
