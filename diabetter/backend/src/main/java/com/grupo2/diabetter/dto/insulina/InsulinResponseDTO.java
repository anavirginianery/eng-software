package com.grupo2.diabetter.dto.insulina;

import java.time.LocalDateTime;
import java.util.UUID;

import com.grupo2.diabetter.enuns.TipoInsulina;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.model.Horario;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InsulinResponseDTO {
    private UUID insulidaId;
    private UUID horarioId;
    private TipoInsulina tipoInsulina;
    private float unidades;
    private Glicemia glicemia;
    private Horario horario;
    private LocalDateTime dataAplicacao;
}