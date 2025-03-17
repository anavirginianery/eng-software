package com.grupo2.diabetter.dto.insulin;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class InsulinPostPutRequestDTO {
    private TipoInsulina tipoInsulina;
    private float unidades;
    private UUID horarioId;
    private Glicemia glicemia;
    private Horario horario;
    private LocalDateTime dataAplicacao;
}