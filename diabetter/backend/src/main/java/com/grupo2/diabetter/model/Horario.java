package com.grupo2.diabetter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
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

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "horario", nullable = false)
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "O horário deve estar no formato HH:mm")
    @JsonProperty
    private String horario;

    @Column(name = "data_criacao", nullable = false)
    @JsonProperty
    private String data_criacao;
}
