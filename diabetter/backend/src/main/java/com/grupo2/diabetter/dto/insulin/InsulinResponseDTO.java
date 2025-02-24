package com.grupo2.diabetter.dto.insulin;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InsulinResponseDTO {
    private UUID uuid;
    private String type;
    private float units;
    private UUID horarioId;
}