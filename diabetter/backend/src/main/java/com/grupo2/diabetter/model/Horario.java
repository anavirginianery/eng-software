package com.grupo2.diabetter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty
    private UUID id;

    @Column(name = "usuario", nullable = false)
    @ManyToOne
    @JsonProperty
    private Usuario usuario;

    @Column(name = "horario", nullable = false)
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "O horário deve estar no formato HH:mm")
    @JsonProperty
    private String horario;

    @Column(name = "data_criacao", nullable = false)
    @JsonProperty
    private String data_criacao;


}
