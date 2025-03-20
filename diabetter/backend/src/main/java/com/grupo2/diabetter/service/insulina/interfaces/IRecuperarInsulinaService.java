package com.grupo2.diabetter.service.insulina.interfaces;

import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import java.util.UUID;

public interface IRecuperarInsulinaService {
    InsulinResponseDTO recuperarInsulina(UUID id);
}
