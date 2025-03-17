package com.grupo2.diabetter.dto.insulin;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InsulinResponseDTO {
    private UUID horarioId;
    private TipoInsulina tipoInsulina;
    private float unidades;
    private Glicemia glicemia;
    private Horario horario;
    private LocalDateTime dataAplicacao;
}