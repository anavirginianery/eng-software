package com.grupo2.diabetter.dto.horario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class HorarioObject {

    private UUID uuid;
    private String value;
    private String date;
    private Long userId;

}
