package com.grupo2.diabetter.service.insulina;

import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulina.interfaces.IListarInsulinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ListarInsulinService implements IListarInsulinaService {

    @Autowired
    private InsulinRepository insulinRepository;

    @Override
    public List<InsulinResponseDTO> listarTodasInsulinas() {
        List<Insulina> insulinas = insulinRepository.findAll();
        return insulinas.stream().map(insulina ->
                InsulinResponseDTO.builder()
                        .insulidaId(insulina.getId())
                        .tipoInsulina(insulina.getTipoInsulina())
                        .unidades(insulina.getUnidades())
                        .horarioId(insulina.getHorario().getId())
                        .glicemia(insulina.getGlicemia() != null ? insulina.getGlicemia() : null)
                        .dataAplicacao(insulina.getDataAplicacao())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<InsulinResponseDTO> listarInsulinaPorHorario(UUID horarioId) {
        List<Insulina> insulinas = insulinRepository.findByHorarioId(horarioId);
        return insulinas.stream().map(insulina ->
                InsulinResponseDTO.builder()
                        .insulidaId(insulina.getId())
                        .tipoInsulina(insulina.getTipoInsulina())
                        .unidades(insulina.getUnidades())
                        .horarioId(insulina.getHorario().getId())
                        .glicemia(insulina.getGlicemia() != null ? insulina.getGlicemia() : null)
                        .dataAplicacao(insulina.getDataAplicacao())
                        .build()
        ).collect(Collectors.toList());
    }
}
