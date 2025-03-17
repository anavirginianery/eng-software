package com.grupo2.diabetter.service.insulin.interfaces;

import com.grupo2.diabetter.dto.insulin.InsulinResponseDTO;
import java.util.UUID;

public interface IRecuperarInsulinaService {
    InsulinResponseDTO recuperarInsulina(UUID id);
}
