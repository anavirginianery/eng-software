package com.grupo2.diabetter.dto.usuario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDeleteRequestDTO {

    @NotNull(message = "O ID do usuário não pode ser nulo")
    private UUID id;

    //podemos adicionar algum tipo de verificação aqui?
}