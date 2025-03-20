package com.grupo2.diabetter.service.glicemia;

import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.glicemia.interfaces.IRecuperarGlicemiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecuperarGlicemiaService implements IRecuperarGlicemiaService {

    @Autowired
    private GlicemiaRepository glicemiaRepository;

    @Override
    public GlicemiaResponseDTO executar(UUID id) {
        Glicemia glicemia = glicemiaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Glicemia não encontrada"));

        return GlicemiaResponseDTO.builder()
                .id(glicemia.getId())
                .valorGlicemia(glicemia.getValorGlicemia())
                .horario(glicemia.getHorario())
                .insulina(glicemia.getInsulina())
                .comentario(glicemia.getComentario())
                .createdAt(glicemia.getCreatedAt())
                .build();
    }
}
