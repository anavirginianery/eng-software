package com.grupo2.diabetter.service.horario;

import com.grupo2.diabetter.dto.horario.HorarioResponseDTO;
import com.grupo2.diabetter.dto.horario.HorarioPostPutRequestDTO;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.model.Usuario;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.horario.interfaces.ICriarHorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriarHorarioService implements ICriarHorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public HorarioResponseDTO createHorario(HorarioPostPutRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.getHorario() == null || dto.getHorario().trim().isEmpty()) {
            throw new IllegalArgumentException("O valor do horário não pode ser nulo ou vazio");
        }

        Horario horario = new Horario();
        horario.setId(dto.getId());
        horario.setHorario(dto.getHorario());
        horario.setData_criacao(dto.getData_criacao());
        horario.setUsuario(usuario);

        horarioRepository.save(horario);

        return HorarioResponseDTO.builder()
                .id(horario.getId())
                .horario(horario.getHorario())
                .data_criacao(horario.getData_criacao())
                .usuario(horario.getUsuario().getId())
                .build();
    }
}
