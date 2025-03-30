package com.grupo2.diabetter.service.insulina;

import com.grupo2.diabetter.dto.insulina.InsulinResponseDTO;
import com.grupo2.diabetter.dto.insulina.IntervaloDataDTO;
import com.grupo2.diabetter.model.Insulina;
import com.grupo2.diabetter.repository.InsulinRepository;
import com.grupo2.diabetter.service.insulina.interfaces.IListarInsulinaPorIntervalo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ListarInsulinaPorIntervalo implements IListarInsulinaPorIntervalo {
    @Autowired
    private InsulinRepository insulinRepository;

    public Map<LocalDate, Double> listarInsulinasPorIntervalo(IntervaloDataDTO dto) {

        LocalDateTime start = dto.getDataInicio().atStartOfDay();
        LocalDateTime end   = dto.getDataFim().atTime(23, 59, 59);

        List<Insulina> insulinas = insulinRepository.findByDataAplicacaoBetween(start, end);

        return insulinas.stream()
                 .collect(Collectors.groupingBy(
                     i -> i.getDataAplicacao().toLocalDate(),
                     Collectors.averagingDouble(Insulina::getUnidades)
                 ));
    }
}
