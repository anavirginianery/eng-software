package com.grupo2.diabetter.repository;

import com.grupo2.diabetter.model.Glicemia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GlicemiaRepository extends JpaRepository<Glicemia, UUID> {
}
