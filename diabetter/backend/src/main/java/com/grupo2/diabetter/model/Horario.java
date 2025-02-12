package com.grupo2.diabetter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
    private UUID uuid;
    @Column(name = "value", nullable = false)
    @JsonProperty
    private String value;
    @Column(name = "date", nullable = false)
    @JsonProperty
    private String date;
}
