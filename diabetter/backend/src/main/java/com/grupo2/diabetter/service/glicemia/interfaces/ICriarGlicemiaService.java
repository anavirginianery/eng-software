package com.grupo2.diabetter.service.glicemia.interfaces;

import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;

public interface ICriarGlicemiaService {
    GlicemiaResponseDTO executar(GlicemiaPostPutRequestDto dto);
}
