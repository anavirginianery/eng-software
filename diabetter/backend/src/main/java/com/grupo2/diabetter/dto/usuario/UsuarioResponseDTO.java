package com.grupo2.diabetter.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grupo2.diabetter.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    @JsonProperty("id_usuario")
    private Long id;

    private String nome;

    @JsonProperty("data_nascimento")
    private String dataNasc;

    private String telefone;

    private String email;

    private String genero;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.dataNasc = usuario.getDataNasc();
        this.telefone = usuario.getTelefone();
        this.email = usuario.getEmail();
        this.genero = usuario.getGenero().toString();
    }
}