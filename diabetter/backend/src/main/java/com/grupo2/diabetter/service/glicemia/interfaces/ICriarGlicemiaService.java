package com.grupo2.diabetter.service.glicemia.interfaces;

import com.grupo2.diabetter.dto.glicemia.GlicemiaPostPutRequestDto;
import com.grupo2.diabetter.model.Glicemia;

public interface ICriarGlicemiaService {
    Glicemia executar(GlicemiaPostPutRequestDto dto);
}
