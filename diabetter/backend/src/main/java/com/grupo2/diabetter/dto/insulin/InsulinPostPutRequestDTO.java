package com.grupo2.diabetter.dto.insulin;

import java.util.UUID;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class InsulinPostPutRequestDTO {
    private String type;
    private float units;
    private UUID horarioId;
}