package com.grupo2.diabetter.dto.glicemia;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GlicemiaPostPutRequestDto {
    @NotEmpty(message = "O campo measurement não pode ser nulo ou vazio.")
    @Email
    private float measurement;
}
