package com.grupo2.diabetter.dto.usuario;

import java.util.List;

import com.grupo2.diabetter.enuns.Genero;

import com.grupo2.diabetter.enuns.TipoDiabetes;
import com.grupo2.diabetter.enuns.TipoInsulina;
import com.grupo2.diabetter.model.Horario;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioPostPutRequestDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "A data de nascimento não pode estar em branco")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "A data de nascimento deve estar no formato yyyy-MM-dd")
    private String dataNasc;

    @NotNull(message = "O gênero não pode ser nulo")
    private Genero genero; 

    // @NotBlank(message = "O telefone não pode estar em branco")
    // @Size(min = 10, max = 15, message = "O telefone deve ter entre 10 e 15 caracteres")
    // private String telefone;

    @NotBlank(message = "O email não pode estar em branco")
    @Email(message = "O email deve ser válido")
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    @Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
    private String password;

    private float altura;

    private float peso;

    private TipoDiabetes tipoDiabetes;

    private TipoInsulina tipoInsulina;

    private List<String> comorbidades;

    private List<Horario> horarios_afericao;
}
