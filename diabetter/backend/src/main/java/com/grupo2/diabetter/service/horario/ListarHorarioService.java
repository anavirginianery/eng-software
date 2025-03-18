package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.IListarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListarHorarioService implements IListarHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    public List<HorarioResponseDTO> listarHorario(UUID usuarioId) {
        List<Horario> horarios = horarioRepository.findAllByUsuarioId(usuarioId);
        return horarios.stream()
                .map(horario ->
                        HorarioResponseDTO.builder()
                                .id(horario.getId())
                                .horario(horario.getHorario())
                                .usuario(horario.getUsuario())
                                .data_criacao(horario.getData_criacao())
                                .build()
                ).collect(Collectors.toList());
    }

}
