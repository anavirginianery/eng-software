package com.grupo2.diabetter.repository;

import com.grupo2.diabetter.model.Insulina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

import java.util.UUID;

public interface InsulinRepository extends JpaRepository<Insulina, UUID> {
    List<Insulina> findByHorario(UUID horarioId);

    List<Insulina> findByHorarioId(UUID horarioId);
}