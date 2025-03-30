package com.grupo2.diabetter.dto.insulina;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntervaloDataDTO {
    private LocalDate dataInicio;
    private LocalDate dataFim;
}
