package com.grupo2.diabetter.service.glicemia;

import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.glicemia.interfaces.IAtualizarGlicemiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AtualizarGlicemiaService implements IAtualizarGlicemiaService {

    @Autowired
    private GlicemiaRepository glicemiaRepository;

    @Override
    public Glicemia executar(UUID id, GlicemiaPostPutRequestDto dto) {
        Optional<Glicemia> glicemiaOptional = this.glicemiaRepository.findById(id);

        if (glicemiaOptional.isEmpty()) {
            throw new NotFoundException("Glicemia não encontrada");
        }

        Glicemia glicemia = glicemiaOptional.get();

        Glicemia glicemiaAtualizado = Glicemia.builder()
                .id(id)
                .measurement(dto.getMeasurement())
                .build();

        return this.glicemiaRepository.save(glicemiaAtualizado);
    }
}
