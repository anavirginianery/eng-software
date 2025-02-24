package com.grupo2.diabetter.model;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.grupo2.diabetter.enuns.Genero;

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

    @Column(name = "data_nasc", nullable = false)
    private String dataNasc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;  // Usando o mesmo enum da classe DTO

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
}
