package com.grupo2.diabetter.repository;

import com.grupo2.diabetter.model.Insulina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import java.util.UUID;

public interface InsulinRepository extends JpaRepository<Insulina, UUID> {
    List<Insulina> findByHorarioId(UUID horarioId);
    List<Insulina> findByDataAplicacaoBetween(LocalDateTime start, LocalDateTime end);
}