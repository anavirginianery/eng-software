package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;
import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.model.Usuario;  // Assuming Usuario model is imported
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.repository.UsuarioRepository;  // Assuming UsuarioRepository is used
import com.grupo2.diabetter.service.horario.interfaces.IAtualizarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AtualizarHorarioService implements IAtualizarHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public HorarioResponseDTO updateHorario(UUID uuid, HorarioPostPutRequestDTO dto){
        Horario horario = horarioRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("Horario não encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.getId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        horario.setHorario(dto.getHorario());
        horario.setData_criacao(dto.getData_criacao());
        horario.setUsuario(horario.getUsuario());

        horarioRepository.save(horario);

        return HorarioResponseDTO.builder()
                .id(horario.getId())
                .horario(horario.getHorario())
                .data_criacao(horario.getData_criacao())
                .usuario(horario.getUsuario())
                .build();
    }
}
