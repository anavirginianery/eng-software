package com.grupo2.diabetter.service.glicemia.interfaces;

import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.model.Glicemia;

import java.util.UUID;

public interface IAtualizarGlicemiaService {
    Glicemia executar(UUID id, GlicemiaPostPutRequestDto dto);
}
