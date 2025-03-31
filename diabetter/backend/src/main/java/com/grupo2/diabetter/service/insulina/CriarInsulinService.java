package com.grupo2.diabetter.service.insulina;

import com.grupo2.diabetter.dto.insulina.InsulinPostPutRequestDTO;
import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import com.grupo2.diabetter.exception.CommerceException;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.repository.HorarioRepository;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.insulina.interfaces.ICriarInsulinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CriarInsulinService implements ICriarInsulinaService {

    @Autowired
    private InsulinRepository insulinRepository;

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private GlicemiaRepository glicemiaRepository;

    @Override
    public InsulinResponseDTO criarInsulina(InsulinPostPutRequestDTO requestDTO) {

        // Validate tipoInsulina and unidades
        if (requestDTO.getTipoInsulina() == null) {
            throw new CommerceException("Tipo de insulina inválido");
        }

        if (requestDTO.getUnidades() <= 0) {
            throw new CommerceException("Unidades de insulina inválidas");
        }

        if (requestDTO.getHorarioId() == null) {
            throw new CommerceException("Horário de insulina inválido");
        }

        if (requestDTO.getGlicemia() == null) {
            throw new CommerceException("Glicemia inválida");
        }

        // Fetch the associated Horario and Glicemia entities
        Horario horario = horarioRepository.findById(requestDTO.getHorarioId())
                .orElseThrow(() -> new CommerceException("Horário não encontrado"));

        Glicemia glicemia = glicemiaRepository.findById(requestDTO.getGlicemia())
                .orElseThrow(() -> new CommerceException("Glicemia não encontrada"));

        Insulina insulina = Insulina.builder()
                .tipoInsulina(requestDTO.getTipoInsulina())
                .unidades(requestDTO.getUnidades())
                .horario(horario)
                .glicemia(glicemia)
                .dataAplicacao(LocalDateTime.now())
                .build();

        Insulina insulinaSalva = insulinRepository.save(insulina);

        return InsulinResponseDTO.builder()
                .insulidaId(insulinaSalva.getId())
                .tipoInsulina(insulinaSalva.getTipoInsulina())
                .unidades(insulinaSalva.getUnidades())
                .horarioId(insulinaSalva.getHorario().getId())
                .horario(insulinaSalva.getHorario().getId())
                .glicemia(insulinaSalva.getGlicemia().getId())
                .dataAplicacao(insulinaSalva.getDataAplicacao())
                .build();
    }
}
