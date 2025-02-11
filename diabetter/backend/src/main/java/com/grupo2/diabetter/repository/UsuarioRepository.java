package com.grupo2.diabetter.repository;

import com.grupo2.diabetter.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}