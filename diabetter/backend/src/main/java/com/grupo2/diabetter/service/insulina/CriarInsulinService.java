package com.grupo2.diabetter.service.insulin;

import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinResponseDTO;
import com.grupo2.diabetter.exception.CommerceException;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulin.interfaces.ICriarInsulinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriarInsulinService implements ICriarInsulinService {

    @Autowired
    private InsulinRepository insulinRepository;

    @Override
    public InsulinResponseDTO criarInsulina(InsulinPostPutRequestDTO requestDTO) {

        // Essas coisas não são verificadas dentro do DTO?

         if (requestDTO.getType() == null || requestDTO.getType().trim().isEmpty()) {
            throw new CommerceException("Tipo de insulina inválido");
        }

        if (requestDTO.getUnits() <= 0) {
            throw new CommerceException("Unidades de insulina inválidas");
        }

       
        if (requestDTO.getHorarioId() == null) {
            throw new CommerceException("Horário de insulina inválido");
        }

        Insulina insulin = Insulina.builder()
                .type(requestDTO.getType())
                .units(requestDTO.getUnits())
                .horario(requestDTO.getHorarioId())
                .build();

        Insulina insulinSalva = insulinRepository.save(insulin);

        return InsulinResponseDTO.builder()
                .uuid(insulinSalva.getUuid())
                .type(insulinSalva.getType())
                .units(insulinSalva.getUnits())
                .horarioId(insulinSalva.getHorario())
                .build();
    }
}