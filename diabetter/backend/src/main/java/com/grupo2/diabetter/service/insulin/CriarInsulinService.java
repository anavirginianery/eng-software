package com.grupo2.diabetter.service.insulin;

import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinResponseDTO;
import com.grupo2.diabetter.model.Insulin;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulin.interfaces.ICriarInsulinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriarInsulinService implements ICriarInsulinService {

    @Autowired
    private InsulinRepository insulinRepository;

    @Override
    public InsulinResponseDTO criarInsulin(InsulinPostPutRequestDTO requestDTO) {
        Insulin insulin = Insulin.builder()
                .type(requestDTO.getType())
                .units(requestDTO.getUnits())
                .horario(requestDTO.getHorarioId())
                .build();

        Insulin insulinSalva = insulinRepository.save(insulin);

        return InsulinResponseDTO.builder()
                .uuid(insulinSalva.getUuid())
                .type(insulinSalva.getType())
                .units(insulinSalva.getUnits())
                .horarioId(insulinSalva.getHorario())
                .build();
    }
}