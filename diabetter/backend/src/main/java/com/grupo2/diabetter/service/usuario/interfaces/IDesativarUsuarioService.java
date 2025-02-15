package com.grupo2.diabetter.service.usuario.interfaces;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;

public interface IDesativarUsuarioService {
    ResponseEntity<?> desativarUsuario(UUID id, UsuarioDeleteRequestDTO dto);

}
