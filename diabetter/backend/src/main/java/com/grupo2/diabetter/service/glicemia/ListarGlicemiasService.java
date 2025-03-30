package com.grupo2.diabetter.service.glicemia;

import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.glicemia.interfaces.IListarGlicemiasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListarGlicemiasService implements IListarGlicemiasService {

    @Autowired
    private GlicemiaRepository glicemiaRepository;

    @Override
    public List<GlicemiaResponseDTO> executar() {
        List<Glicemia> glicemias = glicemiaRepository.findAll();

        return glicemias.stream().map(glicemia ->
                GlicemiaResponseDTO.builder()
                        .id(glicemia.getId())
                        .valorGlicemia(glicemia.getValorGlicemia())
                        .horario(glicemia.getHorario() != null ? glicemia.getHorario().getId() : null)
                        .insulina(glicemia.getInsulina() != null ? glicemia.getInsulina().getId() : null)
                        .comentario(glicemia.getComentario())
                        .createdAt(glicemia.getCreatedAt())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<GlicemiaResponseDTO> listarGlicemiaByHorario(UUID horarioId) {
        List<Glicemia> glicemias = glicemiaRepository.findByHorarioId(horarioId);
        return glicemias.stream().map(glicemia ->
                GlicemiaResponseDTO.builder()
                        .id(glicemia.getId())
                        .valorGlicemia(glicemia.getValorGlicemia())
                        .horario(glicemia.getHorario() != null ? glicemia.getHorario().getId() : null)
                        .insulina(glicemia.getInsulina() != null ? glicemia.getInsulina().getId() : null)
                        .comentario(glicemia.getComentario())
                        .createdAt(glicemia.getCreatedAt())
                        .build()
        ).collect(Collectors.toList());
    }
}
