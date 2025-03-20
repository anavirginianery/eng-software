package com.grupo2.diabetter.dto.insulina;

import java.time.LocalDateTime;
import java.util.UUID;

import com.grupo2.diabetter.enuns.TipoInsulina;
import com.grupo2.diabetter.model.Glicemia;
import com.grupo2.diabetter.model.Horario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsulinPostPutRequestDTO {
    private TipoInsulina tipoInsulina;
    private float unidades;
    private UUID horarioId;
    private UUID glicemia;
    private LocalDateTime dataAplicacao;
}