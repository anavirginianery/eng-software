package com.grupo2.diabetter.service.insulin.interfaces;

import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinResponseDTO;

public interface ICriarInsulinService {
    InsulinResponseDTO criarInsulin(InsulinPostPutRequestDTO requestDTO);
}
