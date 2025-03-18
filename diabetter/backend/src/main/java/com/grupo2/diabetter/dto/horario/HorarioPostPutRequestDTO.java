package com.grupo2.diabetter.dto.horario;

import java.util.UUID;

import com.grupo2.diabetter.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HorarioPostPutRequestDTO {

    private UUID id;

    private UUID usuario;

    private String horario;

    private String data_criacao;
}
