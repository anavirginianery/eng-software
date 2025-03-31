package com.grupo2.diabetter.dto.usuario;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grupo2.diabetter.enuns.TipoDiabetes;
import com.grupo2.diabetter.enuns.TipoInsulina;
import com.grupo2.diabetter.model.Horario;
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
    private UUID id;

    private String nome;

    @JsonProperty("data_nascimento")
    private String dataNasc;

    // private String telefone;

    private String email;

    private String genero;

    private float altura;

    private float peso;

    private TipoDiabetes tipoDiabetes;

    private TipoInsulina tipoInsulina;

    private List<String> comorbidades;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.dataNasc = usuario.getDataNasc();
        // this.telefone = usuario.getTelefone();
        this.email = usuario.getEmail();
        this.genero = usuario.getGenero().toString();
        this.altura = usuario.getAltura(); // Mapeando a altura
        this.peso = usuario.getPeso(); // Mapeando o peso
        this.tipoDiabetes = usuario.getTipoDiabetes(); // Mapeando o tipo de diabetes
        this.tipoInsulina = usuario.getTipoInsulina(); // Mapeando o tipo de insulina
        this.comorbidades = usuario.getComorbidades(); // Mapeando as comorbidades
    }
}