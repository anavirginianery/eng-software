package com.grupo2.diabetter.service.glicemia;

import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.glicemia.interfaces.ICriarGlicemiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriarGlicemiaService implements ICriarGlicemiaService {

    @Autowired
    private GlicemiaRepository glicemiaRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private InsulinRepository insulinaRepository;

    @Override
    public GlicemiaResponseDTO executar(GlicemiaPostPutRequestDto dto) {
        Horario horario = horarioRepository.findById(dto.getHorario())
                .orElseThrow(() -> new IllegalArgumentException("Horario not found"));

        if (dto.getValorGlicemia() <= 0) {
            throw new IllegalArgumentException("Medição de glicemia inválida");
        }

        Insulina insulina = null;
        if (dto.getInsulina() != null) {
            insulina = insulinaRepository.findById(dto.getInsulina())
                    .orElseThrow(() -> new IllegalArgumentException("Insulina not found"));
        }

        Glicemia glicemia = Glicemia.builder()
                .valorGlicemia(dto.getValorGlicemia())
                .horario(horario)
                .insulina(insulina)
                .comentario(dto.getComentario())
                .build();

        glicemiaRepository.save(glicemia);

        GlicemiaResponseDTO glicemiaResponseDTO = GlicemiaResponseDTO.builder()
                .id(glicemia.getId())
                .valorGlicemia(glicemia.getValorGlicemia())
                .horario(glicemia.getHorario())
                .insulina(glicemia.getInsulina() != null ? glicemia.getInsulina() : null)
                .comentario(glicemia.getComentario())
                .createdAt(glicemia.getCreatedAt())
                .build();

        return glicemiaResponseDTO;
    }
}
