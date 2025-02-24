package com.grupo2.diabetter.service.insulin.interfaces;

import com.grupo2.diabetter.dto.insulin.InsulinDeleteResponseDTO;

import java.util.UUID;

public interface IDeletarInsulinService {
    InsulinDeleteResponseDTO deletarInsulin(UUID id);
}