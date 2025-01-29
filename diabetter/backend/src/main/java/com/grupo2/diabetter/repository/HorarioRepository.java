package com.grupo2.diabetter.repository;

import com.grupo2.diabetter.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HorarioRepository extends JpaRepository<Horario, UUID> {

}
