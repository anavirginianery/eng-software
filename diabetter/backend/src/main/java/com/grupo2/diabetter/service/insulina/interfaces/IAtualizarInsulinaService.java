package com.grupo2.diabetter.service.insulina.interfaces;

import com.grupo2.diabetter.dto.insulina.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import java.util.UUID;

public interface IAtualizarInsulinaService {
    InsulinResponseDTO atualizarInsulina(UUID id, InsulinPostPutRequestDTO requestDTO);
}