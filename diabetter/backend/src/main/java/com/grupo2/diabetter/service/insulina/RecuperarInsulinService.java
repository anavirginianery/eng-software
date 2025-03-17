package com.grupo2.diabetter.service.insulin;

import com.grupo2.diabetter.dto.insulin.InsulinResponseDTO;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulin.interfaces.IRecuperarInsulinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecuperarInsulinService implements IRecuperarInsulinService {

    @Autowired
    private InsulinRepository insulinRepository;

    @Override
    public InsulinResponseDTO recuperarInsulina(UUID id) {
        Insulina insulin = insulinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insulin não encontrada"));

        return InsulinResponseDTO.builder()
                .uuid(insulin.getUuid())
                .type(insulin.getType())
                .units(insulin.getUnits())
                .horarioId(insulin.getHorario())
                .build();
    }
}