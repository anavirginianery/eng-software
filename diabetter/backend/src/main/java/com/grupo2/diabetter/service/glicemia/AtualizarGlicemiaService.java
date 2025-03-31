package com.grupo2.diabetter.service.glicemia;

import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;
import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.service.glicemia.interfaces.IAtualizarGlicemiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AtualizarGlicemiaService implements IAtualizarGlicemiaService {

    @Autowired
    private GlicemiaRepository glicemiaRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Override
    public GlicemiaResponseDTO executar(UUID id, GlicemiaPostPutRequestDto dto) {
        Glicemia glicemia = glicemiaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Glicemia não encontrada"));

        glicemia.setValorGlicemia(dto.getValorGlicemia());

        if (dto.getHorario() != null) {
            Horario horario = horarioRepository.findById(dto.getHorario())
                    .orElseThrow(() -> new NotFoundException("Horario não encontrado"));
            glicemia.setHorario(horario);
        }

        if (dto.getComentario() != null) {
            glicemia.setComentario(dto.getComentario());
        }

        Glicemia updatedGlicemia = glicemiaRepository.save(glicemia);

        return GlicemiaResponseDTO.builder()
                .id(glicemia.getId())
                .valorGlicemia(glicemia.getValorGlicemia())
                .horario(glicemia.getHorario().getId())
                .insulina(glicemia.getInsulina() != null ? glicemia.getInsulina().getId() : null)
                .comentario(glicemia.getComentario())
                .createdAt(glicemia.getCreatedAt())
                .build();
    }

}
