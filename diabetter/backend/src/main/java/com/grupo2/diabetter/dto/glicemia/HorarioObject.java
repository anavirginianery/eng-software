package com.grupo2.diabetter.dto.glicemia;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class HorarioObject {

    private UUID uuid;
    private String value;
    private String date;
}
