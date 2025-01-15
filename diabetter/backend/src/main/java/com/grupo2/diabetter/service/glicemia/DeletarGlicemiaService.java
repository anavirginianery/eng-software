package com.grupo2.diabetter.service.glicemia;

import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.glicemia.interfaces.IDeletarGlicemiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeletarGlicemiaService implements IDeletarGlicemiaService {

    @Autowired
    private GlicemiaRepository glicemiaRepository;

    @Override
    public void executar(Long id) {
        Optional<Glicemia> glicemia = this.glicemiaRepository.findById(id);

        if (glicemia.isEmpty()) {
            throw new NotFoundException("Glicemia não encontrada");
        }

        this.glicemiaRepository.deleteById(id);
    }
}
