package com.grupo2.diabetter.repository;

import com.grupo2.diabetter.model.Usuario;

import java.util.Optional;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Usuario findByEmail(@NotBlank(message = "O email não pode estar em branco") @Email(message = "O email deve ser válido") String email);
}