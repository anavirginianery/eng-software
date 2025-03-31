package com.grupo2.diabetter.service.glicemia;

import com.grupo2.diabetter.dto.glicemia.GlicemiaResponseDTO;
import com.grupo2.diabetter.dto.glicemia.IntervaloDataDTO;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.repository.GlicemiaRepository;
import com.grupo2.diabetter.service.glicemia.interfaces.IListarGlicemiaPorIntervalo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ListarGlicemiaPorIntervalo implements IListarGlicemiaPorIntervalo {
    @Autowired
    GlicemiaRepository glicemiaRepository;

    public Map<LocalDate, Double> listarGlicemiasPorIntervalo(IntervaloDataDTO dto) {

        LocalDateTime start = dto.getDataInicio().atStartOfDay();
        LocalDateTime end   = dto.getDataFim().atTime(23, 59, 59);

        List<Glicemia> glicemias = glicemiaRepository.findByCreatedAtBetween(start, end);

        Map<LocalDate, Double> mediaPorDia = glicemias.stream()
                .collect(Collectors.groupingBy(
                        g -> g.getCreatedAt().toLocalDate(),            // Key: LocalDate
                        Collectors.averagingDouble(Glicemia::getValorGlicemia) // Value: average
                ));

        return mediaPorDia;
    }
}
