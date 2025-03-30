package com.grupo2.diabetter.model;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grupo2.diabetter.enuns.Genero;
import com.grupo2.diabetter.enuns.TipoDiabetes;
import com.grupo2.diabetter.enuns.TipoInsulina;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
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

    /*
        Cadastro de usuário
    */

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

    /*
        Completa o perfil
    */

    @Column(name = "data_nasc", nullable = false)
    private String dataNasc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Genero genero;  // Usando o mesmo enum da classe DTO

    @Column(nullable = true)
    private float altura;

    @Column(nullable = true)
    private float peso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private TipoDiabetes tipoDiabetes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private TipoInsulina tipoInsulina;

    @Column(nullable = true)
    private List<String> comorbidades;

    @Column(nullable = true)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Horario> horarios_afericao;


}
