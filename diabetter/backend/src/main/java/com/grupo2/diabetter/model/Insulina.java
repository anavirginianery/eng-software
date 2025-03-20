package com.grupo2.diabetter.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.UUID;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;


import com.grupo2.diabetter.enuns.TipoInsulina;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Insulina {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoInsulina tipoInsulina;

    @Column(nullable = false)
    private float unidades;

    @ManyToOne
    @JoinColumn(name = "horario_id", nullable = false)
    private Horario horario;

    @OneToOne
    @JoinColumn(name = "glicemia_id", nullable = false, unique = true)  // Cada insulina pertence a UMA glicemia
    private Glicemia glicemia;


    @CreationTimestamp
    private LocalDateTime dataAplicacao;
}