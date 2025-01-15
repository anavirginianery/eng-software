package com.grupo2.diabetter.service.glicemia;

import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.glicemia.interfaces.IListarGlicemiasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarGlicemiasService implements IListarGlicemiasService {

    @Autowired
    private GlicemiaRepository glicemiaRepository;

    @Override
    public List<Glicemia> executar() {
        return this.glicemiaRepository.findAll();
    }
}
