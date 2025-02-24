package com.grupo2.diabetter.service.usuario.interfaces;

import com.grupo2.diabetter.dto.usuario.UsuarioPostPutRequestDTO;
import com.grupo2.diabetter.model.Usuario;

public interface ICriarUsuarioService {
        Usuario criarUsuario(UsuarioPostPutRequestDTO dto);
}
