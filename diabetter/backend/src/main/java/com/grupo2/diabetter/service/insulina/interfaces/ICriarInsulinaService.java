package com.grupo2.diabetter.service.insulina.interfaces;

import com.grupo2.diabetter.dto.insulina.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;

public interface ICriarInsulinaService {
    InsulinResponseDTO criarInsulina(InsulinPostPutRequestDTO requestDTO);
}
