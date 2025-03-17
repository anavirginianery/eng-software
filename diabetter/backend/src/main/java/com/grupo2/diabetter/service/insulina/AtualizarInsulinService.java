package com.grupo2.diabetter.service.insulina;
import com.grupo2.diabetter.dto.insulina.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import com.grupo2.diabetter.exception.CommerceException;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulina.interfaces.IAtualizarInsulinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AtualizarInsulinService implements IAtualizarInsulinService {

    @Autowired
    private InsulinRepository insulinRepository;

    @Override
    public InsulinResponseDTO atualizarInsulina(UUID id, InsulinPostPutRequestDTO requestDTO) {
        if (requestDTO.getType() == null || requestDTO.getType().trim().isEmpty()) {
            throw new CommerceException("Tipo de insulina inválido");
        }
        
        Insulina insulina = insulinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insulina não encontrada"));

        // Atualiza os campos com os dados do request
        insulina.setType(requestDTO.getType());
        insulina.setUnits(requestDTO.getUnits());
        insulina.setHorario(requestDTO.getHorarioId());

        // Salva a insulina atualizada
        Insulina insulinAtualizada = insulinRepository.save(insulina);

        // Converte para o DTO de resposta
        return InsulinResponseDTO.builder()
                .uuid(insulinAtualizada.getUuid())
                .type(insulinAtualizada.getType())
                .units(insulinAtualizada.getUnits())
                .horarioId(insulinAtualizada.getHorario())
                .build();
        
    }
}