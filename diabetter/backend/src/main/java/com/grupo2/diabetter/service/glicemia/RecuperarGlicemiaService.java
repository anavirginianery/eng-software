package com.grupo2.diabetter.service.glicemia;

import com.grupo2.diabetter.exception.NotFoundException;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.glicemia.interfaces.IRecuperarGlicemiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecuperarGlicemiaService implements IRecuperarGlicemiaService {
    @Autowired
    private GlicemiaRepository glicemiaRepository;

    @Override
    public Glicemia executar(Long id) {
        Optional<Glicemia> glicemia = this.glicemiaRepository.findById(id);
        if (glicemia.isEmpty()) {
            throw new NotFoundException("Glicemia não encontrada");
        }
        return glicemia.get();
    }
}
