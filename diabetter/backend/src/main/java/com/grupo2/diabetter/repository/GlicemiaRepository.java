package com.grupo2.diabetter.repository;

import com.grupo2.diabetter.model.Glicemia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GlicemiaRepository extends JpaRepository<Glicemia, UUID> {
    List<Glicemia> findByHorarioId(UUID horarioId);
    List<Glicemia> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
