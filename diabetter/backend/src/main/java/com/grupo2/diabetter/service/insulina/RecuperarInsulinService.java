package com.grupo2.diabetter.service.insulina;

import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulina.interfaces.IRecuperarInsulinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecuperarInsulinService implements IRecuperarInsulinaService {

    @Autowired
    private InsulinRepository insulinRepository;

    @Override
    public InsulinResponseDTO recuperarInsulina(UUID id) {
        Insulina insulin = insulinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insulina não encontrada"));

        return InsulinResponseDTO.builder()
                .insulidaId(insulin.getId())
                .tipoInsulina(insulin.getTipoInsulina())
                .unidades(insulin.getUnidades())
                .horarioId(insulin.getHorario().getId())
                .glicemia(insulin.getGlicemia() != null ? insulin.getGlicemia() : null)
                .dataAplicacao(insulin.getDataAplicacao())
                .build();
    }
}
