package com.grupo2.diabetter.service.insulin;
import com.grupo2.diabetter.dto.insulin.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulin.InsulinResponseDTO;
import com.grupo2.diabetter.model.Insulin;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulin.interfaces.IAtualizarInsulinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AtualizarInsulinService implements IAtualizarInsulinService {

    @Autowired
    private InsulinRepository insulinRepository;

    @Override
    public InsulinResponseDTO atualizarInsulin(UUID id, InsulinPostPutRequestDTO requestDTO) {
        Insulin insulin = insulinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insulin não encontrada"));

        // Atualiza os campos com os dados do request
        insulin.setType(requestDTO.getType());
        insulin.setUnits(requestDTO.getUnits());
        insulin.setHorario(requestDTO.getHorarioId());

        // Salva a insulina atualizada
        Insulin insulinAtualizada = insulinRepository.save(insulin);

        // Converte para o DTO de resposta
        return InsulinResponseDTO.builder()
                .uuid(insulinAtualizada.getUuid())
                .type(insulinAtualizada.getType())
                .units(insulinAtualizada.getUnits())
                .horarioId(insulinAtualizada.getHorario())
                .build();
    }
}