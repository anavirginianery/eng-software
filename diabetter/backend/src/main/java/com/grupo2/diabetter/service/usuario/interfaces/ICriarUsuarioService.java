package com.grupo2.diabetter.service.usuario.interfaces;

import com.grupo2.diabetter.dto.usuario.CompletarPerfil;
import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.model.Usuario;

import java.util.UUID;

public interface ICriarUsuarioService {
        Usuario criarUsuario(UsuarioPostPutRequestDTO dto);
        Usuario completarPerfil(UUID id, CompletarPerfil dto);
}
