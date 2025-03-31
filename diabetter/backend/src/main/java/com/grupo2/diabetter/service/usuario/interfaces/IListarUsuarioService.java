package com.grupo2.diabetter.service.usuario.interfaces;

import java.util.List;
import java.util.UUID;

import com.grupo2.diabetter.dto.usuario.UsuarioResponseDTO;

public interface IListarUsuarioService {
    UsuarioResponseDTO listarUsuario(UUID id);
    List<UsuarioResponseDTO> listarUsuarios();
}
