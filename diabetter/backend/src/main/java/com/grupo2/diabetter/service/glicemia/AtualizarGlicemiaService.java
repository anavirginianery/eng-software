package com.grupo2.diabetter.service.glicemia;

import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;
import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.glicemia.interfaces.IAtualizarGlicemiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AtualizarGlicemiaService implements IAtualizarGlicemiaService {

    @Autowired
    private GlicemiaRepository glicemiaRepository;

    @Override
    public GlicemiaResponseDTO executar(UUID id, GlicemiaPostPutRequestDto dto) {
        Glicemia glicemia = glicemiaRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Glicemia não encontrada"));

        glicemia.setMeasurement(dto.getMeasurement());

        if (dto.getHorarioId() != null) {
            glicemia.setHorarioId(dto.getHorarioId());
        }

        Glicemia updatedGlicemia = glicemiaRepository.save(glicemia);

        return convertToDto(updatedGlicemia);
    }

    private GlicemiaResponseDTO convertToDto(Glicemia glicemia) {
        return GlicemiaResponseDTO.builder()
                .id(glicemia.getId())
                .measurement(glicemia.getMeasurement())
                .build();
    }
}
