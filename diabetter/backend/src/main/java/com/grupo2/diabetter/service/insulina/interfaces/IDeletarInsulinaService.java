package com.grupo2.diabetter.service.insulin.interfaces;

import com.grupo2.diabetter.dto.insulin.InsulinDeleteResponseDTO;

import java.util.UUID;

public interface IDeletarInsulinaService {
    InsulinDeleteResponseDTO deletarInsulina(UUID id);
}