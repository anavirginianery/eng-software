package com.grupo2.diabetter.service.insulina;

import com.grupo2.diabetter.dto.insulina.InsulinDeleteResponseDTO;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulina.interfaces.IDeletarInsulinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class DeletarInsulinService implements IDeletarInsulinaService {

    @Autowired
    private InsulinRepository insulinRepository;

    @Override
    public InsulinDeleteResponseDTO deletarInsulina(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }

        Insulina insulinaDelete = insulinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insulina não encontrada"));

        insulinRepository.deleteById(id);

        return InsulinDeleteResponseDTO.builder()
                .mensagem("Insulin deletada com sucesso")
                .build();
    }
    
}