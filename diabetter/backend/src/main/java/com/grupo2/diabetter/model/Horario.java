package com.grupo2.diabetter.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    private UUID uuid;
    @Column(name = "value", nullable = false)
    private String value;
    @Column(name = "date", nullable = false)
    private String date;
}
