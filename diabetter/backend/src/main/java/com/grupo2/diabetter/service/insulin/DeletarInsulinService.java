package com.grupo2.diabetter.service.insulin;

import com.grupo2.diabetter.dto.insulin.InsulinDeleteResponseDTO;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulin.interfaces.IDeletarInsulinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class DeletarInsulinService implements IDeletarInsulinService {

    @Autowired
    private InsulinRepository insulinRepository;

    @Override
    public InsulinDeleteResponseDTO deletarInsulin(UUID id) {
        if (!insulinRepository.existsById(id)) {
            throw new RuntimeException("Insulin não encontrada");
        }

        insulinRepository.deleteById(id);

        return InsulinDeleteResponseDTO.builder()
                .mensagem("Insulin deletada com sucesso")
                .build();
    }
    
}