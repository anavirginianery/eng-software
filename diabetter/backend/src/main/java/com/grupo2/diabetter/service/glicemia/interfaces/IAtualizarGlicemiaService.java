package com.grupo2.diabetter.service.glicemia.interfaces;

import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;

import java.util.UUID;

public interface IAtualizarGlicemiaService {
    GlicemiaResponseDTO executar(UUID id, GlicemiaPostPutRequestDto dto);
}
