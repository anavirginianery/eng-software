package com.grupo2.diabetter.dto.glicemia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HorarioPostPutRequestDTO {

    private String value;
    private String date;

}
