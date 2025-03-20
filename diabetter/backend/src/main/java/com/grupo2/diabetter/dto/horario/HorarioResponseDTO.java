package com.grupo2.diabetter.dto.horario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

import com.grupo2.diabetter.model.Usuario;

@Getter
@AllArgsConstructor
@Builder
public class HorarioResponseDTO {

    private UUID id;

    private Usuario usuario;

    private String horario;

    private String data_criacao;

}
