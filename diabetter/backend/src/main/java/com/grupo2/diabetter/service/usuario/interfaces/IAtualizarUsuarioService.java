package com.grupo2.diabetter.service.usuario.interfaces;

import java.util.UUID;

import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.model.Usuario;

public interface IAtualizarUsuarioService {
    Usuario atualizarUsuario(UUID id, UsuarioPostPutRequestDTO dto);
}
