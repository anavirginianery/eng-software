package com.grupo2.diabetter.service.usuario.interfaces;

import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioDeleteRequestDTO;
import com.grupo2.diabetter.dto.usuario.UsuarioResponseDTO;
import org.springframework.http.ResponseEntity;

public interface UsuarioServiceInterface {
    ResponseEntity<?> criarUsuario(UsuarioPostPutRequestDTO dto);
    UsuarioResponseDTO listarUsuario(Long id);
    ResponseEntity<?> atualizarUsuario(Long id, UsuarioPostPutRequestDTO dto);
    ResponseEntity<?> desativarUsuario(Long id, UsuarioDeleteRequestDTO dto);
}
