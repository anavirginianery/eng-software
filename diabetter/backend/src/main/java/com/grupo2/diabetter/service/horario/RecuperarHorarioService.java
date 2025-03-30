package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.IRecuperarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecuperarHorarioService implements IRecuperarHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public HorarioResponseDTO recuperarHorario(UUID uuid) {
        Horario horario = horarioRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Horário não encontrado"));

        HorarioResponseDTO horarioResponseDTO = HorarioResponseDTO.builder()
                .id(horario.getId())
                .horario(horario.getHorario())
                .data_criacao(horario.getData_criacao())
                .usuario(horario.getUsuario().getId())
                .build();

        return horarioResponseDTO;
    }
}
