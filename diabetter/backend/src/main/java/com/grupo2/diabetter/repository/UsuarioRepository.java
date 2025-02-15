package com.grupo2.diabetter.repository;

import com.grupo2.diabetter.model.Usuario;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
}