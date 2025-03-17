package com.grupo2.diabetter.service.insulin.interfaces;

import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinResponseDTO;
import java.util.UUID;

public interface IAtualizarInsulinaService {
    InsulinResponseDTO atualizarInsulina(UUID id, InsulinPostPutRequestDTO requestDTO);
}