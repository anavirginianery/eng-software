package com.grupo2.diabetter.service.glicemia.interfaces;

import com.grupo2.diabetter.model.Glicemia;

import java.util.UUID;

public interface IRecuperarGlicemiaService {
    Glicemia executar(UUID id);
}
