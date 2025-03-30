package com.grupo2.diabetter.dto.usuario;

import com.grupo2.diabetter.enuns.TipoDiabetes;
import com.grupo2.diabetter.enuns.TipoInsulina;
import com.grupo2.diabetter.model.Horario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompletarPerfil {
    private float altura;

    private float peso;

    private TipoDiabetes tipoDiabetes;

    private TipoInsulina tipoInsulina;

    private List<String> comorbidades;

    private List<Horario> horarios_afericao;
}
