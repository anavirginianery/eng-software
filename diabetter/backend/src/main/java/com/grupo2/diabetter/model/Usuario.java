package com.grupo2.diabetter.model;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grupo2.diabetter.enuns.Genero;
import com.grupo2.diabetter.enuns.TipoDiabetes;
import com.grupo2.diabetter.enuns.TipoInsulina;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    @JsonProperty
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(name = "data_nasc", nullable = false)
    private String dataNasc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;

    @Column(nullable = false)
    private float altura;

    @Column(nullable = false)
    private float peso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDiabetes tipoDiabetes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoInsulina tipoInsulina;

    @Column(nullable = false)
    private List<String> comorbidades;

    @Column(nullable = false)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Horario> horarios_afericao;


}
