package com.grupo2.diabetter.service.usuario;

import com.grupo2.diabetter.dto.usuario.UsuarioLoginDTO;
import com.grupo2.diabetter.repository.UsuarioRepository;
import com.grupo2.diabetter.service.usuario.interfaces.ILoginUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.grupo2.diabetter.model.Usuario;

import java.util.Objects;

@Service
public class LoginUsuario implements ILoginUsuario {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario login(UsuarioLoginDTO usuarioLoginDTO) throws Exception {
        Usuario usuario = usuarioRepository.findByEmail(usuarioLoginDTO.getEmail());

        if (usuario == null){
            throw new Exception("Usuário não cadastrado!");
        }

        if (Objects.equals(usuarioLoginDTO.getPassword(), usuario.getSenha())) {
            return usuario;
        } else {
            throw new Exception("Senha inválida");
        }
    }

}
