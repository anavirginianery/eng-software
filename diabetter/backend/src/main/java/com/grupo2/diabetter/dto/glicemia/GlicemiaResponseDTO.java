package com.grupo2.diabetter.dto.glicemia;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grupo2.diabetter.model.Glicemia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlicemiaResponseDTO {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("nome")
    private Float measurement;


    public GlicemiaResponseDTO(Glicemia glicemia){
        this.id = glicemia.getId();
        this.measurement = glicemia.getMeasurement();
    }
}
