package com.grupo2.diabetter.service.usuario.interfaces;

import com.grupo2.diabetter.dto.usuario.UsuarioLoginDTO;
import com.grupo2.diabetter.model.Usuario;

public interface ILoginUsuario {
    public Usuario login(UsuarioLoginDTO usuarioLoginDTO) throws Exception;
}
