package com.grupo2.diabetter.dto.glicemia;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grupo2.diabetter.model.Horario;
import com.grupo2.diabetter.model.Insulina;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlicemiaPostPutRequestDto {

    private UUID id;

    private float valorGlicemia;

    private UUID horario;

    private LocalDateTime createdAt;

    private UUID insulina;

    private String comentario;

}
