package com.grupo2.diabetter.repository;

import com.grupo2.diabetter.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, UUID> {

    @Query("SELECT h FROM Horario h WHERE h.userId = :userId")
    List<Horario> findAllByUserId(@Param("userId") Long userId);

    List<Horario> findAllByUsuarioId(UUID usuarioId);
}
