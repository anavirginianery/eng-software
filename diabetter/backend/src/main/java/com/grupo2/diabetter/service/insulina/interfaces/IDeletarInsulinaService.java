package com.grupo2.diabetter.service.insulina.interfaces;

import com.grupo2.diabetter.dto.insulina.InsulinDeleteResponseDTO;

import java.util.UUID;

public interface IDeletarInsulinaService {
    InsulinDeleteResponseDTO deletarInsulina(UUID id);
}