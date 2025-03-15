package com.grupo2.diabetter.service.glicemia.interfaces;

import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;
import com.grupo2.diabetter.model.Glicemia;

import java.util.List;
import java.util.UUID;

public interface IListarGlicemiasService {
    List<GlicemiaResponseDTO> executar();
    List<GlicemiaResponseDTO> listarGlicemiaByHorario(UUID horarioId);
}
